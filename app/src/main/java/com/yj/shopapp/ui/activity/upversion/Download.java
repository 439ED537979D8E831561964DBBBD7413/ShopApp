package com.yj.shopapp.ui.activity.upversion;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.yj.shopapp.ui.activity.Interface.DownloadProgressCallback;
import com.yj.shopapp.util.PreferenceUtils;

import java.io.File;

/**
 * Created by LK on 2017/8/21.
 */

public class Download {
    public static long downloadUpdateApkId = -1;//下载更新Apk 下载任务对应的Id
    public static String downloadUpdateApkFilePath;//下载更新Apk 文件路径
    public static final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");
    private static DownloadChangeObserver downloadObserver;
    private static Context mContext;
    private static DownloadProgressCallback callback;
    private static DownloadManager downloadManager;

    public static void setCallback(DownloadProgressCallback callback) {
        Download.callback = callback;
    }

    public static void downloadForWebView(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void downloadForAutoInstall(Context context, String url, String fileName, String title) {
        mContext = context;
        if (TextUtils.isEmpty(url)) {
            return;
        }
        downloadUpdateApkId = PreferenceUtils.getPrefLong(mContext, "downloadUpdateApkId", -1);
        try {
            if (downloadUpdateApkId == -1) {
                Uri uri = Uri.parse(url);
                downloadManager = (DownloadManager) context
                        .getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                //在通知栏中显示

                request.setVisibleInDownloadsUi(true);
                request.setTitle(title);
                String filePath;
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//外部存储卡
                    filePath = Environment.getExternalStorageDirectory().getAbsolutePath();

                } else {
                    Log.e("tag", "没有SD卡");
                    return;
                }
                downloadUpdateApkFilePath = filePath + File.separator + fileName;
                // 若存在，则删除
                PreferenceUtils.setPrefString(context, "ApkPath", downloadUpdateApkFilePath);
                deleteFile(downloadUpdateApkFilePath);
                Uri fileUri = Uri.fromFile(new File(downloadUpdateApkFilePath));
                request.setDestinationUri(fileUri);
                downloadUpdateApkId = downloadManager.enqueue(request);
                PreferenceUtils.setSettingLong(mContext, "downloadUpdateApkId", downloadUpdateApkId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            downloadForWebView(context, url);
        } finally {
//            registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
        //mContext.registerReceiver(mReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        //10.采用内容观察者模式实现进度
        downloadObserver = new DownloadChangeObserver(null);
        context.getContentResolver().registerContentObserver(CONTENT_URI, true, downloadObserver);
    }

//    private static BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            checkStatus();
//        }
//    };
//
//    /**
//     * 检查下载状态
//     */
//    private void checkStatus() {
//        DownloadManager.Query query = new DownloadManager.Query();
//        query.setFilterById(downloadUpdateApkId);
//        Cursor cursor = downloadManager.query(query);
//        if (cursor.moveToFirst()) {
//            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
//            switch (status) {
//                //下载暂停
//                case DownloadManager.STATUS_PAUSED:
//                    break;
//                //下载延迟
//                case DownloadManager.STATUS_PENDING:
//                    break;
//                //正在下载
//                case DownloadManager.STATUS_RUNNING:
//                    break;
//                //下载完成
//                case DownloadManager.STATUS_SUCCESSFUL:
//                    installAPK();
//                    break;
//                //下载失败
//                case DownloadManager.STATUS_FAILED:
//                    Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
//        cursor.close();
//    }

//    /**
//     * 7.0兼容
//     */
//    private void installAPK() {
//        File apkFile =
//                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "百度.apk");
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Uri apkUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", apkFile);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
//        } else {
//            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
//        }
//        mContext.startActivity(intent);
//    }

    private static boolean deleteFile(String fileStr) {
        File file = new File(fileStr);
        return file.delete();
    }


    //用于显示下载进度
    static class DownloadChangeObserver extends ContentObserver {

        public DownloadChangeObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadUpdateApkId);
            DownloadManager dManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            final Cursor cursor = dManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                final int totalColumn = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                final int currentColumn = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                int totalSize = cursor.getInt(totalColumn);
                int currentSize = cursor.getInt(currentColumn);
                float percent = (float) currentSize / (float) totalSize;
                int progress = Math.round(percent * 100);
                callback.onProgress(progress, 100);
            }
        }


    }

}
