package com.example.notificationdemo.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 11/2 0002.
 */
public class FileUtils {

    private static FileUtils sInstance;

    private static final String APK_NAME = "ywy.apk";

    private static class SingletonHolder {
        private static FileUtils INSTANCE = new FileUtils();
    }

    public static FileUtils getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private boolean sdCardEnable() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    private File getFileSaveDir(String type) {
        File file = Environment.getExternalStoragePublicDirectory(type);
        return file;
    }

    /**
     * @param ctx
     * @return
     */
    public File getDownloadApkFile(Context ctx) {
        String filePath;
        if (sdCardEnable()) {
            filePath = getFileSaveDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        } else {
            filePath = ctx.getCacheDir().getAbsolutePath() + File.separator + "Download";
        }
        File file = new File(filePath, APK_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * @param ctx
     * @return
     */
    public String getDownFilePath(Context ctx) {
        String filePath = null;
        if (sdCardEnable()) {
            filePath = getFileSaveDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        } else {
            filePath = ctx.getCacheDir().getAbsolutePath() + File.separator + "Download";
        }
        return filePath;
    }


}
