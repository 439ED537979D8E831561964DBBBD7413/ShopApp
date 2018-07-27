package com.yj.shopapp.dialog;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.StatusBarManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by LK on 2018/5/22.
 * 封装dialogfragment
 *
 * @author LK
 */
public abstract class BaseV4DialogFragment extends DialogFragment {
    protected Context mActivity;
    protected String uid;
    protected String token;
    Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        uid = PreferenceUtils.getPrefString(mActivity, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mActivity, Contants.Preference.TOKEN, "");
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        StatusBarManager.getInstance().setDialogWindowStyle(getDialog().getWindow(), getResources().getColor(R.color.color_4c4c4c));
        View rootView = inflater.inflate(getLayoutId(), container);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    /**
     * 该抽象方法就是 onCreateView中需要的layoutID * @return
     */
    protected abstract int getLayoutId();

    /**
     * 执行数据的加载
     */
    protected abstract void initData();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCanceledOnTouchOutside(true);
        Window dialogWindow = getDialog().getWindow();
        assert dialogWindow != null;
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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

    protected void showKeyBoard(EditText valuet) {
        new Handler().postDelayed(() -> {
            InputMethodManager inManager = (InputMethodManager) valuet.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inManager != null;
            inManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }, 300);
    }
    protected void hideImm(EditText valuet) {
        InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(valuet.getWindowToken(), 0);
    }
}
