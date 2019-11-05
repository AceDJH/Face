package com.example.face.util;

import com.example.face.NetAddress;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//按照字节流上传图片至服务器
public class UploadImage {
    public static void upload(final String url, final String uploadFileName, final byte[] uploadFile){
        new Thread(){
            OkHttpClient client = new OkHttpClient();
            RequestBody image = RequestBody.create(MediaType.parse("image/jpeg"), uploadFile);
            @Override
            public void run() {
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("key", "djh")
                        .addFormDataPart("image", uploadFileName, image)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    System.out.println("UploadImage___" + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
}
