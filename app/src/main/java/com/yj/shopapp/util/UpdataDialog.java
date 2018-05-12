package com.yj.shopapp.util;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.Interface.DownloadProgressCallback;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.upversion.Download;

import java.util.HashMap;
import java.util.Map;

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
    private boolean isDownload;
    private boolean isDownloadComp;
    Unbinder unbinder;

    public static UpdataDialog newInstance(boolean isDownloadComp) {

        Bundle args = new Bundle();
        args.putBoolean("isDownloadComp", isDownloadComp);
        UpdataDialog fragment = new UpdataDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);
        mContext = getActivity();
        isDownloadComp = getArguments().getBoolean("isDownloadComp", false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(false);
            getDialog().setCancelable(false);
            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    if (isDownload) {
//                        return false;
//                    }
//                    AppManager.getAppManager().finishAllActivity();
                        return true;
                    }
                    return false;
                }
            });
            Window dialogWindow = getDialog().getWindow();
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
            lp.height = (int) (displayMetrics.heightPixels * 0.8f);
            dialogWindow.setAttributes(lp);
        }


        if (isDownloadComp) {
            String path = PreferenceUtils.getPrefString(mContext, "ApkPath", "");
            if (CommonUtils.fileIsExists(path)) {
                CommonUtils.installApk(mContext, path);
                dismiss();
            } else {
                cupdate(mContext);
            }
        } else {
            cupdate(mContext);
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
    }

    //是否更新新版本
    public void cupdate(final Context context) {
        //检测网络
        if (!NetUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, Contants.NetStatus.NETDISABLE, Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("app", "");
        HttpHelper.getInstance().post(context, Contants.appu, params, new OkHttpResponseHandler<String>(context) {

            @Override
            public void onAfter() {
                super.onAfter();
                //    progressDialog.dismiss();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                //     progressDialog.show();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                JSONObject jsonObject = JSONObject.parseObject(json);
                int vercode = Integer.parseInt(jsonObject.getString("version"));
                final String appurl = jsonObject.getString("appurl");
                isDownload = true;
                Download.downloadForAutoInstall(mContext.getApplicationContext(), appurl, "ShopApp.apk", "版本更新");
                Download.setCallback(callbacks);
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                Toast.makeText(context, Contants.NetStatus.NETDISABLEORNETWORKDISABLE, Toast.LENGTH_SHORT).show();
            }
        });

    }

    DownloadProgressCallback callbacks = new DownloadProgressCallback() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onProgress(int progress, int totalSize) {
            npb.setProgress(progress);
            Progressvalue.setText(progress + "%");
            if (progress == 100) {
                dismiss();
            }
        }
    };

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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

}
