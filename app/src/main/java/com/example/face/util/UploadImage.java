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

//按照字节流上传图片至服务器,目前问题是本地调用超时
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
//                    解析json
                    String jsonData = response.body().string();

//                    若是成功
                    System.out.println("ResponseBody___" + jsonData);

                    Gson gson = new Gson();
                    GeneralRecognitionBean generalRecognitionBean = gson.fromJson(jsonData, GeneralRecognitionBean.class);
//                    取得的GSON___: GeneralRecognitionBean{log_id=1249435923600825512, resultBean=null, result_num=5}
//                    由此可见没有得到resultBean
                    Log.d("取得的GSON___", generalRecognitionBean.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
}
