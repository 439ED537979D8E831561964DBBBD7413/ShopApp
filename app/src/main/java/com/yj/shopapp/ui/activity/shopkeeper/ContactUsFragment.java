package com.yj.shopapp.ui.activity.shopkeeper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.ShopDetails;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.util.CommonUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ContactUsFragment extends NewBaseFragment {

    @BindView(R.id.CaseImag)
    ImageView CaseImag;
    @BindView(R.id.CaseName)
    TextView CaseName;
    @BindView(R.id.Address)
    TextView Address;
    private ShopDetails.DataBean bean;
    private final int REQUEST_CODE = 0x1001;

    public static ContactUsFragment newInstance(ShopDetails.DataBean bean) {
        Bundle args = new Bundle();
        args.putParcelable("deta", bean);
        ContactUsFragment fragment = new ContactUsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ShopDetails.DataBean getBean() {
        return getArguments().getParcelable("deta");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contact_us;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        bean = getBean();
    }

    @Override
    protected void initData() {
        Glide.with(mActivity).load(bean.getImgurl()).into(CaseImag);
        CaseName.setText(bean.getShopname());
        Address.setText(bean.getAddress());
    }

    @OnClick({R.id.Address, R.id.call_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Address:
                Bundle bundle = new Bundle();
                bundle.putString("storename", bean.getShopname());
                bundle.putString("address", bean.getAddress());
                CommonUtils.goActivity(mActivity, MapActivity.class, bundle);
                break;
            case R.id.call_phone:
                new MaterialDialog.Builder(mActivity).title("提示").positiveText("拨打").negativeText("取消")
                        .content(String.format("是否要拨打电话给%1$s %2$s", bean.getContacts(), bean.getTel()))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //拨打电话
                                if (Build.VERSION.SDK_INT >= 23) {
                                    //判断有没有拨打电话权限
                                    if (PermissionChecker.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        //请求拨打电话权限
                                        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);

                                    } else {
                                        callPhone();
                                    }

                                } else {
                                    callPhone();
                                }
                            }
                        }).show();
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && PermissionChecker.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            showToast("授权成功");
            callPhone();
        } else {
            showToast("授权失败");
        }
    }

    private void callPhone() {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + bean.getTel()));
            startActivity(intent);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CALL_PHONE)) {
                //已经禁止提示了
                showToast("您已禁止该权限，需要重新开启");
            } else {
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);

            }

        }

    }
}
