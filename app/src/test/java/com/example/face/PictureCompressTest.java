package com.example.face;
import com.example.face.util.PictureCompressUtil;

import org.junit.Test;

import java.io.IOException;

public class PictureCompressTest {
    String imgPath = "D:\\Android_test\\Postgraduate\\Test\\Face\\Face_Python_Server\\test_baidu\\food1.jpg";
    String outPath = "D:\\Android_test\\Postgraduate\\Test\\Face\\Face_Python_Server";
    @Test
    public void testCompress() throws IOException {
        PictureCompressUtil pictureCompressUtil = new PictureCompressUtil();
        pictureCompressUtil.compressByQuality(imgPath, outPath, 512, false);
//        PictureCompressUtil.compressByQuality(imgPath, outPath, 512, false);
    }
}
