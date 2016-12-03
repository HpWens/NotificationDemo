package com.example.notificationdemo.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

import java.io.File;

/**
 * Created by boby on 2016/12/2.
 */

public class DownloadManagerUtils {

    public final static String SAVE_APP_NAME = "app.apk";
    public final static String SAVE_APP_LOCATION = "/download";


    public final static String APP_FILE_NAME = "/sdcard" + SAVE_APP_LOCATION + File.separator +
            SAVE_APP_NAME;


    /**
     * 下载Apk, 并设置Apk地址,
     * 默认位置: /storage/sdcard0/Download
     *
     * @param context     上下文
     * @param downLoadUrl 下载地址
     * @param description 通知描述
     * @param name        通知名称
     */
    @SuppressWarnings("unused")
    public static long downloadApk(Context context,
                                   String downLoadUrl,
                                   String name,
                                   String description) {

        DownloadManager.Request request;

        if (!isDownloadManagerAvailable()) {
            return 0;
        }

        String appUrl = downLoadUrl;
        if (appUrl == null || appUrl.isEmpty()) {
            return 0;
        }
        appUrl = appUrl.trim(); // 去掉首尾空格
        if (!appUrl.startsWith("http")) {
            appUrl = "http://" + appUrl; // 添加Http信息
        }

        try {
           request = new DownloadManager.Request(Uri.parse(appUrl));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

        request.setTitle(name);
        request.setDescription(description);

        //在通知栏显示下载进度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request
                    .VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setDestinationInExternalPublicDir(SAVE_APP_LOCATION, SAVE_APP_NAME);

        Context appContext = context.getApplicationContext();
        DownloadManager manager = (DownloadManager)
                appContext.getSystemService(Context.DOWNLOAD_SERVICE);
        //进入下载队列
        return manager.enqueue(request);

    }

    /**
     * @param context
     * @param ids
     */
    public static void removeEnqueue(Context context, long... ids) {
        DownloadManager manager = (DownloadManager)
                context.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        for (long id : ids) {
            if (id != 0) {
                manager.remove(id);
            }
        }
    }

    // 最小版本号大于9
    private static boolean isDownloadManagerAvailable() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }


}
