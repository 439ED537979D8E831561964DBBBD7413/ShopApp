package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yj.shopapp.R;
import com.yj.shopapp.loading.ILoadView;
import com.yj.shopapp.loading.ILoadViewImpl;
import com.yj.shopapp.loading.LoadMoreClickListener;
import com.yj.shopapp.presenter.CardListRecyclerView;
import com.yj.shopapp.ubeen.ScashCoupon;
import com.yj.shopapp.ui.activity.adapter.ScashcouponUnAdapter;
import com.yj.shopapp.ui.activity.base.BaseFragment;
import com.yj.shopapp.view.headfootrecycleview.OnRecyclerViewScrollListener;
import com.yj.shopapp.view.headfootrecycleview.RecyclerViewHeaderFooterAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;



/**
 * Created by huanghao on 2016/12/3.
 */

public class CouponFragment extends BaseFragment implements CardListRecyclerView {
    @Override
    public void chooseItem(int pos, int value) {
        if (value==1) {
            for (int i = 0; i < chooseArray.size(); i++) {
                if (chooseArray.get(i) == 1) {
                    if (megsList.get(i).getBigtypeid().equals(megsList.get(pos).getBigtypeid())) {
                        showToastShort(megsList.get(pos).getBigtypename() + "现金券只能选一张");
                        nAdapter.isstop=1;
                    }
                }
            }
            if (nAdapter.isstop!=1)
            {
                chooseArray.set(pos, value);
            }

        }
        if (value==0)
        {chooseArray.set(pos, value);}

       // adapter.notifyDataSetChanged();
    }
    private ILoadView iLoadView = null;
    private View loadMoreView = null;




    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private RecyclerViewHeaderFooterAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;
    ScashcouponUnAdapter nAdapter=null;

    private boolean isRequesting = false;//标记，是否正在刷新

    private int mCurrentPage = 0;


    public List<ScashCoupon.CanuseBean > megsList = new ArrayList<>();

    public List<Integer> chooseArray = new ArrayList<>();  //0 不选择 1

    int  ostatus;
    public static CouponFragment newInstance(int content) {
        CouponFragment fragment = new CouponFragment();
        fragment.ostatus = content;
        return fragment;
    }
    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onLongItemClick(int position) {

    }

    @Override
    public void init(Bundle savedInstanceState) {
        //ButterKnife.inject(mActivity);
        for (ScashCoupon.CanuseBean canuseBean:megsList)
        {
            chooseArray.add(0);
        }






    nAdapter= new ScashcouponUnAdapter(mActivity, megsList, CouponFragment.this, chooseArray);





        nAdapter.ostatus=this.ostatus;
        layoutManager = new LinearLayoutManager(mActivity);

        adapter = new RecyclerViewHeaderFooterAdapter(layoutManager, nAdapter);

        iLoadView = new ILoadViewImpl(mActivity, new CouponFragment.mLoadMoreClickListener());

        loadMoreView = iLoadView.inflate();

        recyclerView.addOnScrollListener(new CouponFragment.MyScrollListener());


        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }


    }

    @Override
    public int getLayoutID() {
        return R.layout.sfragmentcoupon;
    }
    public class mLoadMoreClickListener implements LoadMoreClickListener {

        @Override
        public void clickLoadMoreData() {

        }
    }


    public class MyScrollListener extends OnRecyclerViewScrollListener {

        @Override
        public void onScrollUp() {

        }

        @Override
        public void onScrollDown() {

        }

        @Override
        public void onBottom() {

        }

        @Override
        public void onMoved(int distanceX, int distanceY) {

        }
    }


}
