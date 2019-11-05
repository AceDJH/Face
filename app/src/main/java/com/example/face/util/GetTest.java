package com.example.face.util;

import com.example.face.NetAddress;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//用来测试get连接
public class GetTest {
    public static void getTest(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final OkHttpClient client = new OkHttpClient();
                final Request request = new Request.Builder()
                        .get()
                        .url(url)
                        .build();
                // Log.d(testHttp, "Thread___");
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        System.out.println("succeed");
                        //Log.d(testHttp, "succeed___" + response.headers().toString());
                        System.out.println(response.body().string());
                    }else{
                        System.out.println("failed");
                        //Log.d(testHttp, "failed___");
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
