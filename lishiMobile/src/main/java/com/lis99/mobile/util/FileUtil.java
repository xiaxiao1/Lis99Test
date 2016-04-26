package com.lis99.mobile.util;

import android.content.Context;

import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yy on 16/4/20.
 */
public class FileUtil {

    public static String filePath = "";
    public static String cachePath = "";
    public static String imgPath = "";
    public static String crashPath = "";
    public static String dbImgPath = "";


    public static void setFilePath (Context context)
    {
        filePath = StorageUtils.getOwnCacheDirectory(
                context.getApplicationContext(), "lishi99").getPath();

        cachePath = filePath + "/cache";

        imgPath = filePath + "/image";

        crashPath = filePath + "/crash";

        dbImgPath = context.getFilesDir() + "/lishi99/";

        File file = new File(filePath);
        if ( !file.exists())
        {
            file.mkdirs();
        }

        file = new File(cachePath);
        if ( !file.exists())
        {
            file.mkdirs();
        }

        file = new File(imgPath);
        if ( !file.exists())
        {
            file.mkdirs();
        }

        file = new File(crashPath);
        if ( !file.exists())
        {
            file.mkdirs();
        }

        file = new File(dbImgPath);
        if ( !file.exists())
        {
            file.mkdirs();
        }

    }





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
