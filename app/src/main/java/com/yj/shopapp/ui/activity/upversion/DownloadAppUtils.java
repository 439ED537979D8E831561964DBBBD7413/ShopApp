package com.yj.shopapp.ui.activity.upversion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadLargeFileListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.yj.shopapp.ui.activity.Interface.DownloadProgressCallback;
import com.yj.shopapp.util.PreferenceUtils;

import java.io.File;

/**
 * Created by LK on 2018/7/17.
 *
 * @author LK
 */
public class DownloadAppUtils {
    private static final String TAG = DownloadAppUtils.class.getSimpleName();
    public static long downloadUpdateApkId = -1;//下载更新Apk 下载任务对应的Id
    public static String downloadUpdateApkFilePath;//下载更新Apk 文件路径
    private static UpdateAppReceiver receiver = null;


    /**
     * 通过浏览器下载APK包
     *
     * @param context
     * @param url
     */
    public static void downloadForWebView(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static void download(final Context context, String url, final String serverVersionName, DownloadProgressCallback callback) {

        String packageName = context.getPackageName();
        String filePath = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//外部存储卡
            filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            Log.i(TAG, "没有SD卡");
            return;
        }
        receiver = new UpdateAppReceiver();
        context.registerReceiver(receiver, new IntentFilter("shopapp.update"));
        String apkLocalPath = filePath + File.separator + packageName + "_" + serverVersionName + ".apk";
        PreferenceUtils.setPrefString(context, "apkPath", apkLocalPath);
        downloadUpdateApkFilePath = apkLocalPath;
        FileDownloader.setup(context);

        FileDownloader.getImpl().create(url)
                .setPath(apkLocalPath)
                .setListener(new FileDownloadLargeFileListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, long soFarBytes, long totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, long soFarBytes, long totalBytes) {
                        send(context, (int) (soFarBytes * 100.0 / totalBytes), serverVersionName);
                        callback.onProgress((int) (soFarBytes * 100.0 / totalBytes), 100);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, long soFarBytes, long totalBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        send(context, 100, serverVersionName);
                        callback.onProgress(100, 100);
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Toast.makeText(context, "下载出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();
    }


    private static void send(Context context, int progress, String serverVersionName) {
        Intent intent = new Intent();
        intent.setAction("shopapp.update");
        intent.putExtra("progress", progress);
        intent.putExtra("title", serverVersionName);
        context.sendBroadcast(intent);
    }

    public static void unRegister(Activity mActivity) {
        try {
            mActivity.unregisterReceiver(receiver);
            receiver = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
