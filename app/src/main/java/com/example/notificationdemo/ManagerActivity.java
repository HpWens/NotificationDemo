package com.example.notificationdemo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.notificationdemo.receiver.DownloadApkReceiver;
import com.example.notificationdemo.utils.DownloadManagerUtils;

public class ManagerActivity extends PermissionActivity {

    protected String[] downloadApkPermission = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private DownloadApkReceiver downloadApkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        showUpdateDialog();

        registerReceiver();

    }


    private void registerReceiver() {
        downloadApkReceiver = new DownloadApkReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        registerReceiver(downloadApkReceiver, intentFilter);
    }


    @Override
    public void requestPermissionResult(boolean allowPermission) {
        if (allowPermission) {
            downloadApk();
        }
    }

    private void downloadApk() {
        final long id = DownloadManagerUtils.downloadApk(this, URL, "APK更新", "更新包下载中...");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                queryDownloadManager(id);
            }
        }, 1000);
    }


    /**
     * @param id
     */
    private void queryDownloadManager(long id) {
        DownloadManager mDownloadManager = (DownloadManager)
                this.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(id);
        Cursor cursor = mDownloadManager.query(query);


        if (cursor != null) {

            while (cursor.moveToNext()) {

                String bytesDownload = cursor.getString(cursor.getColumnIndex(DownloadManager
                        .COLUMN_BYTES_DOWNLOADED_SO_FAR));
                String description = cursor.getString(cursor.getColumnIndex(DownloadManager
                        .COLUMN_DESCRIPTION));
                String cid = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                String localUri = cursor.getString(cursor.getColumnIndex(DownloadManager
                        .COLUMN_LOCAL_URI));
                String mimeType = cursor.getString(cursor.getColumnIndex(DownloadManager
                        .COLUMN_MEDIA_TYPE));
                String title = cursor.getString(cursor.getColumnIndex(DownloadManager
                        .COLUMN_TITLE));
                String status = cursor.getString(cursor.getColumnIndex(DownloadManager
                        .COLUMN_STATUS));
                String totalSize = cursor.getString(cursor.getColumnIndex(DownloadManager
                        .COLUMN_TOTAL_SIZE_BYTES));

                Log.i("ManagerActivity", "bytesDownload:" + bytesDownload);
                Log.i("ManagerActivity", "description:" + description);
                Log.i("ManagerActivity", "cid:" + cid);
                Log.i("ManagerActivity", "localUri:" + localUri);
                Log.i("ManagerActivity", "mimeType:" + mimeType);
                Log.i("ManagerActivity", "title:" + title);
                Log.i("ManagerActivity", "status:" + status);
                Log.i("ManagerActivity", "totalSize:" + totalSize);

            }

            cursor.close();
        }
    }

    /**
     * 显示软件更新对话框
     */
    public void showUpdateDialog() {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("软件更新");
        builder.setMessage("有新版本,建议更新!");
        // 更新
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 显示下载对话框
                if (!mayRequestPermission(downloadApkPermission)) {
                    return;
                }
                downloadApk();
            }


        });
        // 稍后更新
        builder.setNegativeButton("稍后更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadApkReceiver != null) {
            unregisterReceiver(downloadApkReceiver);
        }
    }

}
