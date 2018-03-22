package com.yj.shopapp.ui.activity.shopkeeper;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoodsDetailtwoFragment extends NewBaseFragment {
    @BindView(R.id.m_LinearLayout)
    NestedScrollView mLinearLayout;
    private WebView myWevView;

    public static GoodsDetailtwoFragment newInstance(String html) {

        Bundle args = new Bundle();
        GoodsDetailtwoFragment fragment = new GoodsDetailtwoFragment();
        args.putString("html", html);
        fragment.setArguments(args);
        return fragment;
    }

    private String getHtml() {
        return getArguments().getString("html");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods_detailtwo;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        myWevView = new WebView(getActivity());
        mLinearLayout.addView(myWevView);
        myWevView.setScrollContainer(false);
        mLinearLayout.requestDisallowInterceptTouchEvent(false);
    }

    @Override
    protected void initData() {
        myWevView.loadDataWithBaseURL(null, getHtmlData(getHtml()), "text/html", "utf-8", null);
        myWevView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String request) {
                view.loadUrl(request);
                return true;
            }
        });
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    @Override
    public void onDestroy() {
        if (myWevView != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            ViewParent parent = myWevView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(myWevView);
            }

            myWevView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            myWevView.getSettings().setJavaScriptEnabled(false);
            myWevView.clearHistory();
            myWevView.clearView();
            myWevView.removeAllViews();
            myWevView.destroy();
        }
        super.onDestroy();
    }

}
