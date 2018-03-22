package com.yj.shopapp.ui.activity.upversion;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.yj.shopapp.util.PreferenceUtils;

import java.io.File;


/**
 * Created by LK on 2017/8/21.
 */

public class UpdateAppReceiver extends BroadcastReceiver {
    public UpdateAppReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
// 处理下载完成
        Cursor c = null;

        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            if (Download.downloadUpdateApkId >= 0) {
                long downloadId = Download.downloadUpdateApkId;
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);
                DownloadManager downloadManager = (DownloadManager) context
                        .getSystemService(Context.DOWNLOAD_SERVICE);
                c = downloadManager.query(query);
                if (c.moveToFirst()) {
                    int status = c.getInt(c
                            .getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_FAILED) {
                        downloadManager.remove(downloadId);

                    } else if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        //保存状态
                        PreferenceUtils.setPrefBoolean(context, "DownloadSuccess", true);
                        if (Download.downloadUpdateApkFilePath != null) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            File apkFile = new File(Download.downloadUpdateApkFilePath);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                Uri contentUri = FileProvider.getUriForFile(
                                        context, context.getPackageName() + ".fileProvider", apkFile);
                                i.setDataAndType(contentUri, "application/vnd.android.package-archive");
                            } else {
                                i.setDataAndType(Uri.fromFile(apkFile),
                                        "application/vnd.android.package-archive");
                            }
                            intent.addCategory("android.intent.category.DEFAULT");
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }

                    }
                }
                c.close();
            }
        }
    }
}
