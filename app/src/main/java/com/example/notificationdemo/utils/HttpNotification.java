package com.example.notificationdemo.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.notificationdemo.R;

/**
 * Created by boby on 2016/12/3.
 */

public class HttpNotification {

    private static final String NOTIFICATION_TAG = "HttpNotification";

    private static Context mContext;

    private NotificationCompat.Builder mBuilder;

    private static class SingletonHolder {
        private static HttpNotification INSTANCE = new HttpNotification();
    }

    public static HttpNotification getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        return HttpNotification.SingletonHolder.INSTANCE;
    }


    private HttpNotification() {
    }

    private NotificationCompat.Builder buildNotification() {
        final Resources res = mContext.getResources();

        // This image is used as the notification's large icon (thumbnail).
        // TODO: Remove this if your notification has no relevant thumbnail.
        final Bitmap picture = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher);

        return new NotificationCompat.Builder(mContext).
                setContentTitle("更新包下载中...")
                .setTicker("准备下载...")
                .setProgress(100, 0, false)
                .setContentText(String.format(mContext.getResources()
                        .getString(R.string.apk_progress), 0) + "%")
                .setLargeIcon(picture)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false);
    }

    public void showProgressNotification(int progress) {
        if (mBuilder == null) {
            mBuilder = buildNotification();
        }
        Notification notification = mBuilder.setProgress(100, progress, false)
                .setContentText(String.format(mContext.getResources().getString(R.string
                        .apk_progress), progress) + "%")
                .build();
        notify(mContext, notification);
    }

    public void removeProgressNotification() {
        cancel(mContext);
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        notify(NOTIFICATION_TAG, context, notification);
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final String tag, final Context context, final Notification
            notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(tag, 0, notification);
        } else {
            nm.notify(tag.hashCode(), notification);
        }
    }


    /**
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void cancel(final Context context) {
        cancel(NOTIFICATION_TAG, context);
    }


    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void cancel(final String tag, final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(tag, 0);
        } else {
            nm.cancel(tag.hashCode());
        }
    }

}
