package com.yj.shopapp.ui.activity.wholesale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.StatusBarUtils;
import com.yj.shopapp.util.StringHelper;
import com.yj.shopapp.wbeen.Power;

import butterknife.BindView;
import butterknife.OnClick;

public class WPriceEditActivity extends BaseActivity {
    public static final int BACK_TO = 88;
    public static int EDIT_CODE = 33;
    Context mContext = this;
    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.cost_price)
    EditText costPrice;
    @BindView(R.id.ed_minnum)
    EditText edMinnum;
    @BindView(R.id.ed_maxnum)
    EditText edMaxnum;
    @BindView(R.id.minimum_inventory)
    EditText minimumInventory;
    @BindView(R.id.wholesale_price)
    EditText wholesalePrice;
    @BindView(R.id.inventory)
    EditText inventory;
    @BindView(R.id.maximum_inventory)
    EditText maximumInventory;
    @BindView(R.id.login_btn)
    CardView loginBtn;
    @BindView(R.id.activity_wprice_edit)
    LinearLayout activityWpriceEdit;
    @BindView(R.id.stopunm)
    EditText stopunm;
    @BindView(R.id.title_view)
    RelativeLayout titleView;
    @BindView(R.id.retail_price)
    EditText retailPrice;
    private Power power;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wprice_edit;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.from(this)
                .setActionbarView(titleView)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
    }

    @Override
    protected void initData() {
       // InputFilter[] filters = new InputFilter[]{new CashierInputFilter()};
        title.setText("修改库存");
       // wholesalePrice.setFilters(filters);
        //costPrice.setFilters(filters);
//        costPrice.setFocusable(true);
//        costPrice.setFocusableInTouchMode(true);
//        costPrice.requestFocus();
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        if (getIntent().hasExtra("costPrice")) {
            costPrice.setText(getIntent().getStringExtra("costPrice"));
            costPrice.setSelection(costPrice.getText().length());
            wholesalePrice.setText(getIntent().getStringExtra("wholesalePrice"));
            inventory.setText(getIntent().getStringExtra("inventory"));
            maximumInventory.setText(getIntent().getStringExtra("max"));
            minimumInventory.setText(getIntent().getStringExtra("min"));
            edMinnum.setText(getIntent().getStringExtra("minnum"));
            edMaxnum.setText(getIntent().getStringExtra("maxnum"));
            stopunm.setText(getIntent().getStringExtra("stopnum"));
            retailPrice.setText(getIntent().getStringExtra("vipprice"));
        }
        if (getIntent().hasExtra("power")) {
            power = getIntent().getParcelableExtra("power");
            setEditFocus();
        }
    }

    private void setEditFocus() {
        if (power==null)return;
        if (power.getMinitemsum() == 0) {
            maximumInventory.setFocusable(false);
            maximumInventory.setTextColor(getResources().getColor(R.color.color_999999));
        }
        if (power.getMaxitemsum() == 0) {
            minimumInventory.setFocusable(false);
            minimumInventory.setTextColor(getResources().getColor(R.color.color_999999));
        }
        if (power.getItemsum() == 0) {
            inventory.setFocusable(false);
            inventory.setTextColor(getResources().getColor(R.color.color_999999));
        }
        if (power.getMinnum() == 0) {
            edMinnum.setFocusable(false);
            edMinnum.setTextColor(getResources().getColor(R.color.color_999999));
        }
        if (power.getMaxnum() == 0) {
            edMaxnum.setFocusable(false);
            edMaxnum.setTextColor(getResources().getColor(R.color.color_999999));
        }
        if (power.getStopitemsum() == 0) {
            stopunm.setFocusable(false);
            stopunm.setTextColor(getResources().getColor(R.color.color_999999));
        }
        if (power.getCostprice() == 0) {
            costPrice.setFocusable(false);
            costPrice.setTextColor(getResources().getColor(R.color.color_999999));
        }
        if (power.getPrice() == 0) {
            wholesalePrice.setFocusable(false);
            wholesalePrice.setTextColor(getResources().getColor(R.color.color_999999));
        }
        if (power.getVipprice()==0){
            retailPrice.setFocusable(false);
            retailPrice.setTextColor(getResources().getColor(R.color.color_999999));
        }
    }

    @OnClick(R.id.login_btn)
    public void onViewClicked() {
        String cost;
        cost = costPrice.getText().toString();
        String wholesale = wholesalePrice.getText().toString();
        String inv = inventory.getText().toString();
        String max = maximumInventory.getText().toString();
        String min = minimumInventory.getText().toString();
        String minnum = edMinnum.getText().toString();
        String maxnum = edMaxnum.getText().toString();
        Log.e("my_tag", minnum + maxnum);
        if (!CheckNum(minnum, maxnum)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(WPriceEditActivity.this);
            builder.setTitle("提示");
            builder.setMessage("最大购买量要大于最小购买量");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
            return;
        }
//        if (StringHelper.isEmpty(cost)) {
//            showToastShort("成本价不能为空！");
//            return;
//        }
        if (StringHelper.isEmpty(wholesale)) {
            showToastShort("批发价不能为空！");
            return;
        }
        if (StringHelper.isEmpty(inv)) {
            showToastShort("库存不能为空！");
            return;
        }
        if (StringHelper.isEmpty(max)) {
            showToastShort("最大库存不能为空！");
            return;
        }
        if (StringHelper.isEmpty(min)) {
            showToastShort("最小库存不能为空！");
            return;
        }

        int d2 = Integer.valueOf(min);
        int d1 = Integer.valueOf(max);
        if (d1 <= d2) {
            showToastShort("最大库存必须大于最小库存");
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("costPrice", cost); //成本价
        bundle.putString("wholesalePrice", wholesale);//批发价
        bundle.putString("inventory", inv); //库存
        bundle.putString("max", max); //最大库存
        bundle.putString("min", min); //最小库存
        bundle.putString("minnum", minnum);//最小购买量
        bundle.putString("maxnum", maxnum);//最大购买量
        bundle.putString("stopnum", stopunm.getText().toString());
        bundle.putString("vipprice", retailPrice.getText().toString());
        CommonUtils.goResult(mContext, bundle, BACK_TO);
    }

    private boolean CheckNum(String arg1, String arg2) {
        int var1 = 0, var2 = 0;
        if (!arg1.equals("")) {
            var1 = Integer.parseInt(arg1);
        }
        if (!arg2.equals("")) {
            var2 = Integer.parseInt(arg2);
        }
        return var1 == 0 || var2 == 0 || var2 > var1;
    }
}
