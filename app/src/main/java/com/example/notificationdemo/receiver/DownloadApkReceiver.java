package com.example.notificationdemo.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.notificationdemo.utils.DownloadManagerUtils;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by boby on 2016/12/2.
 */

public class DownloadApkReceiver extends BroadcastReceiver {

    private static final String TAG = "DownloadApkReceiver";
    private static final int APK_EXIST = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            DownloadManagerUtils.installApk(context);
            //再次打开直接安装 保存当前状态
            context.getSharedPreferences("DEMO_PREFERENCES", MODE_PRIVATE).edit()
                    .putInt("APK_EXIST", APK_EXIST).commit();

        } else if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
            //Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
        }
    }

}
