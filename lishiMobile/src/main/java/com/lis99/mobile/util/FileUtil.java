package com.lis99.mobile.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yy on 16/4/20.
 */
public class FileUtil {

    /**
     *      获取文件的byte[]
     * @param fileName
     * @return
     */
    public static String readFileByBytes(String fileName) {
        String result = "";
        File file = new File(fileName);
        InputStream in = null;
        try {
            // 一次读一个字节
            in = new FileInputStream(file);
            int tempbyte;
            byte[] b = new byte[in.available()];
            while ((tempbyte = in.read(b)) != -1) {
//                System.out.write(tempbyte);
            }
            in.close();


            result = Base64.encode(b);

            return result;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
