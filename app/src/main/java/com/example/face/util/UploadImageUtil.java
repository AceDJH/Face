package com.example.face.util;
import android.util.Log;

import com.example.face.bean.GeneralRecognitionBean;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
//按照字节流上传图片至服务器
public class UploadImageUtil {
    public static void upload(final String url, final String uploadFileName, final byte[] uploadFile, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody image = RequestBody.create(MediaType.parse("image/jpeg"), uploadFile);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", uploadFileName, image)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
