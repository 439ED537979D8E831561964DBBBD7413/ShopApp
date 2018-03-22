package com.yj.shopapp.ui.activity.shopkeeper;

import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.RecordRedPack;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.DateUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LK on 2017/10/23.
 *
 * @author LK
 */

public class BillDetails extends BaseActivity {
    @BindView(R.id.shopname)
    TextView shopname;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.accountnumber)
    TextView accountnumber;
    @BindView(R.id.redpack_type)
    TextView redpackType;
    @BindView(R.id.change_time)
    TextView changeTime;
    @BindView(R.id.remarks)
    TextView remarks;
    private RecordRedPack redPack;
    String[] redPackType = {"超级红包", "推荐红包"};
    private String rrp = "RecordRedPack";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_billdetails;
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra(rrp)) {
            redPack = getIntent().getParcelableExtra(rrp);
        }
        settingdata();
    }

    private void settingdata() {
        shopname.setText(redPack.getShopname());
        money.setText(redPack.getReward());
        ShowLog.e(redPack.getStatus());
        status.setText(getResources().getStringArray(R.array.schedule)[Integer.parseInt(redPack.getStatus()) - 1]);
        if ("3".equals(redPack.getStatus())) {
            money.setText("+" + redPack.getReward());
        }
        accountnumber.setText(redPack.getAccountnumber());
        redpackType.setText(redPackType[Integer.parseInt(redPack.getReward_type()) - 1]);
        changeTime.setText(DateUtils.timea(redPack.getChangetime()));
        remarks.setText(redPack.getRemark() + " ");
    }




    @OnClick(R.id.rule_return)
    public void onViewClicked() {
        finish();
    }
}
