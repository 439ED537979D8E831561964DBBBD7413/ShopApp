package com.yj.shopapp.ui.activity.wholesale;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.loading.ILoadView;
import com.yj.shopapp.presenter.BaseRecyclerView;
import com.yj.shopapp.ui.activity.adapter.WChildOrderAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.ui.activity.shopkeeper.SOrderDetailActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.view.headfootrecycleview.RecyclerViewHeaderFooterAdapter;
import com.yj.shopapp.wbeen.Worder;

import butterknife.BindView;

/**
 * Created by huanghao on 2016/11/30.
 */

public class WChilOrderActivity extends BaseActivity  implements BaseRecyclerView {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.title)
    TextView title;
    private RecyclerViewHeaderFooterAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;
    private ILoadView iLoadView = null;
    private View loadMoreView = null;
    Worder worder;
    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_childorder;
    }

    @Override
    protected void initData() {
        worder=(Worder) getIntent().getSerializableExtra("childOrder");
        title.setText(worder.getUsername()+"的子订单");
        if(worder.getChildorder()==null)
        {
            return;
        }
        WChildOrderAdapter oAdapter = new WChildOrderAdapter(mContext,worder.getChildorder(),this);

        layoutManager = new LinearLayoutManager(mContext);

        adapter = new RecyclerViewHeaderFooterAdapter(layoutManager,oAdapter);

        //iLoadView = new ILoadViewImpl(mContext, new WChilOrderActivity.mLoadMoreClickListener());

        //loadMoreView = iLoadView.inflate();

        //recyclerView.addOnScrollListener(new WOrderViewActivity.MyScrollListener());


        if(recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public void onItemClick(int position) {

        Bundle bundle = new Bundle();
        bundle.putString("oid", worder.getOid());
        bundle.putInt("isType", 0);
        CommonUtils.goActivity(mContext, SOrderDetailActivity.class, bundle, false);
    }

    @Override
    public void onLongItemClick(int position) {

    }
}
