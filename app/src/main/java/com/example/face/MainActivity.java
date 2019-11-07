package com.example.face;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.face.util.PictureCompressUtil;
import com.example.face.util.UploadImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private boolean isPhoto = false;
    private boolean isAlbum = false;
    private String fileName; //图片名，并非路径
    private byte[] fileBuf;//图片字节流
    private final double compressRatio = 0.1;//图片缩放比例0-1
    private ImageView ivPicture;
    private Uri imageUri;
    private Button btnTakePhoto;
    private Button btnFromAlbum;
    private Button btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTakePhoto = findViewById(R.id.btn_photo);
        btnFromAlbum = findViewById(R.id.btn_choose_from_album);
        btnUpload = findViewById(R.id.btn_upload);
        ivPicture = findViewById(R.id.iv_picture);
        btnTakePhoto.setOnClickListener(this);
        btnFromAlbum.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_photo:
                isPhoto = true;
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
//                    与manifest中的配置相关
                    imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.face.fileprovider", outputImage);
                    Log.d("点击照相后图片的uri___", String.valueOf(imageUri));
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
                break;
            case R.id.btn_choose_from_album:
                isAlbum = true;
                select();
                break;
            case R.id.btn_upload:
                if (isPhoto || isAlbum){
                    UploadImage.upload(NetAddress.uploadUrl, fileName, fileBuf);
                }else{
                    Toast.makeText(this, "请先拍照或使用相册", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        ivPicture.setImageBitmap(bitmap);
                        fileName = makePictureName();
//                        Bitmap compressBitmap = PictureCompressUtil.ratio(bitmap, pixelW, pixelH);
                        Bitmap compressBitmap = PictureCompressUtil.ratio(bitmap, bitmap.getWidth() * compressRatio, bitmap.getHeight() * compressRatio);
                        fileBuf = PictureCompressUtil.Bitmap2Bytes(compressBitmap);
                        Log.d("点击照相后图片的图片名___", fileName);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                handleSelect(data);
                break;
            default:
                break;
        }
    }

    //从相册中选择照片
    public void select() {
        String[] permissions=new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        //进行sdcard的读写请求
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,permissions, CHOOSE_PHOTO);
        }
        else{
            openGallery(); //打开相册，进行选择
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CHOOSE_PHOTO:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openGallery();
                }
                else{
                    Toast.makeText(this,"读相册的操作被拒绝",Toast.LENGTH_LONG).show();
                }
        }
    }

    //打开相册,进行照片的选择
    private void openGallery(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    //选择后照片的读取工作
    private void handleSelect(Intent intent){
        imageUri = intent.getData();
        try {
            fileName = makePictureName();
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//            Log.d("bitmap___压缩前", String.valueOf(bitmap.getByteCount()/1024));
//            Bitmap compressBitmap = PictureCompressUtil.ratio(bitmap, pixelW, pixelH);
            Bitmap compressBitmap = PictureCompressUtil.ratio(bitmap, bitmap.getWidth() * compressRatio, bitmap.getHeight() * compressRatio);
//            Log.d("bitmap___压缩后", String.valueOf(compressBitmap.getByteCount()/1024));
            fileBuf = PictureCompressUtil.Bitmap2Bytes(compressBitmap);
            Glide.with(this).load(imageUri)
                    .fitCenter()
                    .into(ivPicture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //给图片命名
    private String makePictureName(){
//        时间格式化格式
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyyMMddHHmmssSSS");
//        获取当前时间并作为时间戳给文件夹命名
        String timeStamp=simpleDateFormat.format(new Date());
//        通过时间戳给照片命名
        return timeStamp + ".jpg";
    }
}

