package com.yj.shopapp.util;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.Notice;
import com.yj.shopapp.view.X5WebView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by LK on 2017/10/19.
 */

public class NoticeDialog extends DialogFragment {
    @BindView(R.id.notice_tiele)
    TextView noticeTiele;
    @BindView(R.id.hot_title)
    TextView hotTitle;
    @BindView(R.id.hot_context)
    TextView hotContext;
    @BindView(R.id.hot_time)
    TextView hotTime;
    @BindView(R.id.webView)
    X5WebView webView;
    @BindView(R.id.dialog_up)
    TextView dialogUp;
    @BindView(R.id.dialog_next)
    TextView dialogNext;
    Unbinder unbinder;
    private List<Notice> noticeLists = new ArrayList<>();
    private int notindex = 0;
    private Context mContext;

    public static NoticeDialog newInstance(List<Notice> data) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("noticelist", (ArrayList<? extends Parcelable>) data);
        NoticeDialog fragment = new NoticeDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);
        noticeLists = getArguments().getParcelableArrayList("noticelist");
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.CENTER);// 此处可以设置dialog显示的位置为居中
        WindowManager windowManager = (getActivity()).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = display.getWidth() * 9 / 10; // 设置dialog宽度
        window.setAttributes(lp);
        getDialog().setCanceledOnTouchOutside(false);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dailog_hot, container,false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDialogText();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    public void setDialogText() {
        Notice notice = noticeLists.get(notindex);
        if ("1".equals(notice.getClassify())) {

            noticeTiele.setText(notice.getType());
            hotTitle.setText(notice.getTitle());
            hotTime.setText(DateUtils.getDateToLong(notice.getAddtime()));
            hotContext.setText("\u3000\u3000" + notice.getContent());

            hotTitle.setVisibility(View.VISIBLE);
            hotContext.setVisibility(View.VISIBLE);
            hotTime.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
        } else {
            hotTitle.setVisibility(View.GONE);
            hotContext.setVisibility(View.GONE);
            hotTime.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            webView.loadUrl(notice.getUrl());
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.dialog_up, R.id.dialog_next, R.id.dialog_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dialog_sure:
                dismiss();
                break;
            case R.id.dialog_next:
                if (notindex == noticeLists.size() - 1) {
                    Toast.makeText(mContext, "没有更多了", Toast.LENGTH_SHORT).show();
                } else if (notindex < noticeLists.size() - 1) {
                    notindex++;
                    setDialogText();
                }
                break;
            case R.id.dialog_up:
                if (notindex == 0) {
                    Toast.makeText(mContext, "没有更多了", Toast.LENGTH_SHORT).show();
                } else if (notindex >= 0) {
                    notindex--;
                    setDialogText();
                }
                break;
            default:
                break;
        }
    }

    //    public NoticeDialog(Context context, int layoutResID, int[] listenedItems) {
//        super(context, R.style.dialog_custom); //dialog的样式
//        this.context = context;
//        this.layoutResID = layoutResID;
//        this.listenedItems = listenedItems;
//
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Window window = getWindow();
//        window.setGravity(Gravity.CENTER);// 此处可以设置dialog显示的位置为居中
//        window.setWindowAnimations(R.style.bottom_menu_animation); // 添加动画效果
//        setContentView(layoutResID);
//        WindowManager windowManager = ((Activity) context).getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.width = display.getWidth() * 9 / 10; // 设置dialog宽度
//        lp.y = -100;
//        getWindow().setAttributes(lp);
//        setCanceledOnTouchOutside(false);// 点击Dialog外部消失
//        //遍历控件id,添加点击事件
//        for (int id : listenedItems) {
//            findViewById(id).setOnClickListener(this);
//        }
//
//    }
//

}
