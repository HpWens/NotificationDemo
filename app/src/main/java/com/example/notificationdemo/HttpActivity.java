package com.example.notificationdemo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.example.notificationdemo.net.CallBack;
import com.example.notificationdemo.net.RetrofitClient;
import com.example.notificationdemo.utils.FileUtils;
import com.example.notificationdemo.utils.HttpNotification;
import com.example.notificationdemo.view.CircleProgressView;

import java.io.File;

/**
 * Created by boby on 2016/12/3.
 */

public class HttpActivity extends PermissionActivity {

    private static final String DOWN_URL = "web/api.do?qt=8051&id=723";

    private CircleProgressView mCircleProgressView;

    private HttpNotification mHttpNotification;

    protected String[] downloadApkPermission = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);
        mCircleProgressView = (CircleProgressView) findViewById(R.id.circle_progress);
        mHttpNotification = HttpNotification.getInstance(getApplicationContext());
    }

    public void update(View v) {
        showUpdateDialog();
    }

    private void downloadApk() {

        RetrofitClient.getInstance(this).createBaseApi()
                .download(DOWN_URL, new CallBack() {
                    @Override
                    public void onError(Throwable e) {
                        Log.e("HttpActivity", "onError--------2222" + e.getMessage());
                        mHttpNotification.removeProgressNotification();
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        mHttpNotification.showProgressNotification(0);
                    }

                    @Override
                    public void onSucess(String path, String name, long fileSize) {
                        mHttpNotification.removeProgressNotification();
                        installApk(HttpActivity.this);
                    }

                    @Override
                    public void onProgress(int progress) {
                        super.onProgress(progress);
                        mCircleProgressView.setProgress(progress);
                        mHttpNotification.showProgressNotification(progress);
                    }
                });

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
    public void requestPermissionResult(boolean allowPermission) {
        if (allowPermission) {
            downloadApk();
        }
    }

    // 安装Apk
    private void installApk(Context context) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            String filePath = FileUtils.getInstance().getDownFilePath(context) + File.separator +
                    "app.apk";
            i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android" +
                    ".package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } catch (Exception e) {
            Log.e("HttpActivity", "installApk--------安装失败");
            e.printStackTrace();
        }
    }
}
