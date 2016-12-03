package com.example.notificationdemo.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.notificationdemo.utils.DownloadManagerUtils;

/**
 * Created by boby on 2016/12/2.
 */

public class DownloadApkReceiver extends BroadcastReceiver {
    private static final String TAG = "DownloadApkReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

            installApk(context);

        } else if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
            //Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();

        }
    }

    // 安装Apk
    private void installApk(Context context) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            String filePath = DownloadManagerUtils.APP_FILE_NAME;
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
