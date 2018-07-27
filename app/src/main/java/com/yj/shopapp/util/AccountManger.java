package com.yj.shopapp.util;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.shopkeeper.AuthenticationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by LK on 2018/4/1.
 *
 * @author LK
 */

public class AccountManger extends DialogFragment {


    @BindView(R.id.tips)
    TextView tips;
    Unbinder unbinder;
    private int type;
    private String phone4;
    private String[] titles = {"微信", "支付宝"};

    public static AccountManger newInstance(int type, String phone) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putString("phone", phone);
        AccountManger fragment = new AccountManger();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account_manger, container);
        getDialog().getWindow().setWindowAnimations(R.style.popuwindow_animation);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getInt("type");
        phone4 = getArguments().getString("phone");
        tips.setText(String.format("您可对尾号 %1$s 的 %2$s 进行操作", phone4, type != 0 ? titles[type - 1] : ""));
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCanceledOnTouchOutside(true);
        Window dialogWindow = getDialog().getWindow();
        assert dialogWindow != null;
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);

    }

    @OnClick({R.id.modify, R.id.cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.modify:
                Bundle bundle = new Bundle();
                bundle.putInt("type", type);
                CommonUtils.goActivity(getActivity(), AuthenticationActivity.class, bundle);
                dismiss();
                break;
            case R.id.cancel:
                dismiss();
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
