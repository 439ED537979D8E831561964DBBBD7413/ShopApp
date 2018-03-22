package com.yj.shopapp.ui.activity.shopkeeper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.EventMassg;
import com.yj.shopapp.ubeen.Industry;
import com.yj.shopapp.ui.activity.adapter.NewGoodRecyAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.DDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ScreenActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.newgoodrecy)
    RecyclerView newgoodrecy;
    private NewGoodRecyAdpter adpter;
    private List<Industry> mdatas = new ArrayList<>();


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EventBus.getDefault().post(new EventMassg(mdatas.get(position).getId()));
        finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_screen;
    }

    @Override
    protected void initData() {
        title.setText("选择分类");
        if (getIntent().hasExtra("industrylist")) {
            mdatas = getIntent().getParcelableArrayListExtra("industrylist");
        }
        adpter = new NewGoodRecyAdpter(mContext, mdatas);
        newgoodrecy.setLayoutManager(new GridLayoutManager(mContext, 2));
        newgoodrecy.addItemDecoration(new DDecoration(mContext));
        newgoodrecy.setAdapter(adpter);
        adpter.setOnItemClickListener(this);
    }
}
