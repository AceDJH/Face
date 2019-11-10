package com.example.face;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ThingActivity extends AppCompatActivity {
    private String keyword;
    private String root;
    private String baike;
    private String imageUrl;
    private ImageView ivBaike;
    private TextView tvKeyword;
    private TextView tvRoot;
    private TextView tvDescription;
    private TextView tvBaike;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing);
        ivBaike = findViewById(R.id.iv_activity_thing_image);
        tvKeyword = findViewById(R.id.tv_activity_thing_keyword);
        tvRoot = findViewById(R.id.tv_activity_thing_root);
        tvBaike = findViewById(R.id.tv_activity_thing_baike);
        tvDescription = findViewById(R.id.tv_activity_thing_description);

        Intent intent = getIntent();
        keyword = intent.getStringExtra("keyword");
        root = intent.getStringExtra("root");
        baike = intent.getStringExtra("baike");
        imageUrl = intent.getStringExtra("imageUrl");

        tvKeyword.setText(keyword);
//        tvRoot.setText(root);
//        if (baike.length() == 0){
//            tvDescription.setVisibility(View.GONE);
//        }
        tvBaike.setText(baike);

        StrictMode.setThreadPolicy(new
                StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(
                new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        try {
            ivBaike.setImageBitmap(getBitmap(imageUrl));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmap(String path) throws IOException {

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200){
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }
}
