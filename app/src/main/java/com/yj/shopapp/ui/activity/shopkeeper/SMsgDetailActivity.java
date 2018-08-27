package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.Notice;
import com.yj.shopapp.ui.activity.adapter.NotContentAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SMsgDetailActivity extends BaseActivity {


    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.activity_msg_detail)
    LinearLayout activityMsgDetail;
    @BindView(R.id.Not_list)
    ListView NotList;
    @BindView(R.id.title_view)
    RelativeLayout titleView;
    private Context context = this;
    private List<Notice> notlist = new ArrayList<Notice>();
    private int id=0;
    private NotContentAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_msg_detail;
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
        title.setText("公告详情");
        if (getIntent().hasExtra("id")) {
            id = Integer.parseInt(getIntent().getStringExtra("id"));
        }
        if (getIntent().hasExtra("notice")) {
            notlist = getIntent().getParcelableArrayListExtra("notice");
        }
        if (adapter == null) {
            adapter = new NotContentAdapter(context);
        }
        adapter.setList(notlist);
        NotList.setAdapter(adapter);
        adapter.setDefSelect(id - 1);
    }


    @OnClick(R.id.forewadImg)
    public void onViewClicked() {
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
