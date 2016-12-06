package com.example.notificationdemo.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

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


    // 安装Apk
    public static void installApk(Context context) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            String filePath = DownloadManagerUtils.APP_FILE_NAME;
            if (!new File(filePath).exists()) {
                Toast.makeText(context, "文件不存在!", Toast.LENGTH_SHORT).show();
                context.getSharedPreferences("DEMO_PREFERENCES", MODE_PRIVATE).edit()
                        .putInt("APK_EXIST", 0).commit();
                return;
            }
            i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android" +
                    ".package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } catch (Exception e) {
            Log.e(TAG, "安装失败");
            e.printStackTrace();
        }
    }


}
