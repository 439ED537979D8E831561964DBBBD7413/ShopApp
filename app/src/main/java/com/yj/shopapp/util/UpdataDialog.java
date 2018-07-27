package com.yj.shopapp.util;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.Interface.DownloadProgressCallback;
import com.yj.shopapp.ui.activity.upversion.DownloadAppUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by LK on 2018/2/10.
 *
 * @author LK
 */

public class UpdataDialog extends DialogFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_update_info)
    TextView tvUpdateInfo;
    @BindView(R.id.npb)
    ProgressBar npb;
    @BindView(R.id.Progressvalue)
    TextView Progressvalue;
    @BindView(R.id.progressbar_ll)
    LinearLayout progressbarLl;
    private Context mContext;
    Unbinder unbinder;
    private Activity mActivity;
    private String apkUrl;
    private String appVersion;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    public static UpdataDialog newInstance(String url, String versioon) {
        Bundle args = new Bundle();
        args.putString("apkUrl", url);
        args.putString("appVersion", versioon);
        UpdataDialog fragment = new UpdataDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);
        mContext = getActivity();
        apkUrl = getArguments().getString("apkUrl", "");
        appVersion = getArguments().getString("appVersion", "");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(false);
            getDialog().setCancelable(false);
            getDialog().setOnKeyListener((dialog, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    if (isDownload) {
//                        return false;
//                    }
//                    AppManager.getAppManager().finishAllActivity();
                    return true;
                }
                return false;
            });

            try {
                Window dialogWindow = getDialog().getWindow();
                if (dialogWindow != null) {
                    dialogWindow.setGravity(Gravity.CENTER);
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
                    lp.height = (int) (displayMetrics.heightPixels * 0.8f);
                    dialogWindow.setAttributes(lp);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.versionupdatadialog, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DownloadAppUtils.download(mActivity, apkUrl, appVersion, new DownloadProgressCallback() {
            @Override
            public void onProgress(int progress, int totalSize) {
                npb.setProgress(progress);
                Progressvalue.setText(String.format("%d%%", progress));
                if (progress == 100) {
//                    ShowLog.e("zhixingd100");
//                    //安装app
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        boolean b = mActivity.getPackageManager().canRequestPackageInstalls();
//                        if (b) {
//                            installApk(DownloadAppUtils.downloadUpdateApkFilePath);
//                        } else {
//                            //请求安装未知应用来源的权限
//                            startInstallPermissionSettingActivity(mActivity);
//                        }
//                    }
                    dismiss();
                }
            }
        });
    }

    @Override
    public void show(FragmentManager manager, String tag) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (manager.isDestroyed())
                return;
        }
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity(Activity activity) {
        Uri packageURI = Uri.parse("package:" + activity.getPackageName());
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        activity.startActivityForResult(intent, 10086);
    }

    /**
     * 安装apk
     */
    private void installApk(String path) {
        if (TextUtils.isEmpty(path)) {
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
                    mActivity, mActivity.getPackageName() + ".fileProvider", file);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        mActivity.startActivity(intent);
        //ApkController.installSilent(apkPath);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 10086) {
//            ShowLog.e("zhixingd");
//            installApk(DownloadAppUtils.downloadUpdateApkFilePath);
//        }
//    }

    @Override
    public void onDestroyView() {
        DownloadAppUtils.unRegister(mActivity);
        super.onDestroyView();
        unbinder.unbind();
    }

}
