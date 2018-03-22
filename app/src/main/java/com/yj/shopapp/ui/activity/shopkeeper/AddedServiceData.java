package com.yj.shopapp.ui.activity.shopkeeper;

import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ubeen.ServiceOrder;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.DateUtils;

import butterknife.BindView;

public class AddedServiceData extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.orderNumber)
    TextView orderNumber;
    @BindView(R.id.order_time)
    TextView orderTime;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.storename)
    TextView storename;
    @BindView(R.id.Contacts)
    TextView Contacts;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.stroename)
    TextView stroename;
    @BindView(R.id.shopimag)
    SimpleDraweeView shopimag;
    @BindView(R.id.shopname)
    TextView shopname;
    @BindView(R.id.spesc)
    TextView spesc;
    @BindView(R.id.addtime)
    TextView addtime;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.allprice)
    TextView allprice;
    @BindView(R.id.Total_amount)
    TextView TotalAmount;
    private ServiceOrder order;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_added_service_data;
    }

    @Override
    protected void initData() {
        title.setText("增值服务订单详情");
        if (getIntent().hasExtra("data")) {
            order = getIntent().getParcelableExtra("data");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
        orderNumber.setText("订单号：" + order.getOrder());
        orderTime.setText(DateUtils.timet(order.getAddtime()));
        storename.setText(order.getShopname());
        Contacts.setText(order.getContacts());
        phone.setText(order.getMobile());
        stroename.setText(order.getShop() + "-" + order.getClassX());
        Glide.with(mContext).load(order.getImgurl()).into(shopimag);
        shopname.setText(order.getName());
        spesc.setText("规格：" + order.getSpecs());
        addtime.setText(DateUtils.timet(order.getAddtime()));
        price.setText(order.getUnitprice());
        num.setText("x" + order.getNum());
        allprice.setText(order.getMoney());
        TotalAmount.setText(String.format("共%1$s%2$s   合计：%3$s", order.getNum(), order.getUnit(), order.getMoney()));
        status.setText(Contants.OrderDrawerw[Integer.parseInt(order.getStatus())]);
        address.setText(order.getAddress());
    }


}
