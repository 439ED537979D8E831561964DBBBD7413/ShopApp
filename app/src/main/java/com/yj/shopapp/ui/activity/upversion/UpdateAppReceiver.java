package com.yj.shopapp.ui.activity.upversion;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.yj.shopapp.R;

import java.io.File;
import java.util.Objects;


/**
 * Created by LK on 2017/8/21.
 */

public class UpdateAppReceiver extends BroadcastReceiver {
    private NotificationManager nm = null;
    private static final String TAG = "1";

    public UpdateAppReceiver() {
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        //分割线
        if (Objects.requireNonNull(intent.getAction()).equals("shopapp.update")) {
            int notifyId = 1;
            int progress = intent.getIntExtra("progress", 0);
            String title = intent.getStringExtra("title");

            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notifyDownloading(context, progress, 100, title);
                if (progress == 100) {
                    if (nm != null) {
                        nm.cancel(notifyId);
                        nm.deleteNotificationChannel("1");
                    }
                    //installApk(context, DownloadAppUtils.downloadUpdateApkFilePath);
                }
            } else {
                if (progress == 100) {
                    //installApk(context, DownloadAppUtils.downloadUpdateApkFilePath);
                }
            }
        }
    }

    /**
     * 安装apk
     */
    private void installApk(Context context, String path) {
        if (context == null || TextUtils.isEmpty(path)) {
            return;
        }
//        if (DownloadAppUtils.downloadUpdateApkFilePath != null) {
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            File apkFile = new File(DownloadAppUtils.downloadUpdateApkFilePath);
//            if (UpdateAppUtils.needFitAndroidN && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                Uri contentUri = FileProvider.getUriForFile(
//                        context, context.getPackageName() + ".fileProvider", apkFile);
//                i.setDataAndType(contentUri, "application/vnd.android.package-archive");
//            } else {
//                i.setDataAndType(Uri.fromFile(apkFile),
//                        "application/vnd.android.package-archive");
//            }
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
//        }
        File file = new File(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判读版本是否在7.0以上安卓8.0一下
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //provider authorities
            Uri contentUri = FileProvider.getUriForFile(
                    context, context.getPackageName() + ".fileProvider", file);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
        //ApkController.installSilent(apkPath);
    }

    public void notifyDownloading(Context context, long progress, long num, String file_name) {
        Notification.Builder mBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder = new Notification.Builder(context, TAG);
            NotificationChannel channel;
            channel = new NotificationChannel(TAG, file_name, NotificationManager.IMPORTANCE_LOW);
            channel.setShowBadge(true);
            nm.createNotificationChannel(channel);

        } else {
            mBuilder = new Notification.Builder(context);
        }
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setProgress((int) num, (int) progress, false);
        mBuilder.setOngoing(true);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setContentTitle(file_name);
        mBuilder.setContentText("下载中");
        nm.notify(1, mBuilder.build());
    }
}
