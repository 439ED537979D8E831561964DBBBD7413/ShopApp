package com.yj.shopapp.ui.activity.wholesale;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.adapter.SWhomeAdapter;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.wbeen.ClassList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassPagerFragment extends NewBaseFragment {
    @BindView(R.id.classi_gv)
    RecyclerView classiGv;
    private SWhomeAdapter adapter;
    private List<ClassList.ListBean> listBeans = new ArrayList<>();

    public static ClassPagerFragment newInstance(List<ClassList.ListBean> listBeans) {
        Bundle args = new Bundle();
        ClassPagerFragment fragment = new ClassPagerFragment();
        args.putParcelableArrayList("listbeans", (ArrayList<? extends Parcelable>) listBeans);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_class_pager;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        assert getArguments() != null;
        listBeans = getArguments().getParcelableArrayList("listbeans");
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 4);
        adapter = new SWhomeAdapter(mActivity, listBeans);
        if (classiGv != null) {
            classiGv.setLayoutManager(layoutManager);
            classiGv.setNestedScrollingEnabled(false);
            classiGv.setAdapter(adapter);

        }
    }

    @Override
    protected void initData() {
        adapter.setOnItemClickListener((a, b, c, d) -> {
            Bundle bundle = new Bundle();
            bundle.putString("bigtypeid", listBeans.get(c).getId());
            bundle.putString("bigtypeName", listBeans.get(c).getName());
            CommonUtils.goActivity(mActivity, WGoodsActivity.class, bundle);
        });
    }

}
