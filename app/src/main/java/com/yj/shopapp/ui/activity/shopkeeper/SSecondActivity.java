package com.yj.shopapp.ui.activity.shopkeeper;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.BrandGroup;
import com.yj.shopapp.ubeen.IndustryCatelist;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.SBrandAdapter;
import com.yj.shopapp.ui.activity.adapter.ScreenLvAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ezy.ui.layout.LoadingLayout;

public class SSecondActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.more)
    RelativeLayout more;
    @BindView(R.id.image2)
    ImageView image2;
    @BindView(R.id.bgView)
    View bgView;
    @BindView(R.id.loading)
    LoadingLayout loading;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.title_view)
    RelativeLayout titleView;
    private GridLayoutManager layoutManager;
    boolean isBrang;
    private IndustryCatelist industryCatelist;
    private List<BrandGroup> brandGroup = new ArrayList<>();
    private SBrandAdapter brandAdapter;
    private List<BrandGroup.ListBean> listBeans = new ArrayList<>();
    private List<IndustryCatelist.DataBean.TagGroup> groups = new ArrayList<>();
    private List<BrandGroup.ListBean> NewlistBeans = new ArrayList<>();
    private List<IndustryCatelist.DataBean.TagGroup> Newgroups = new ArrayList<>();
    private List<BrandGroup.ListBean> tabname_brand = new ArrayList<>();
    private List<IndustryCatelist.DataBean.TagGroup> tabname_classify = new ArrayList<>();
    private boolean isSereen;
    private boolean isRefresh;
    private int brandindex = 0, classifyindex = 0;
    private View rootView;
    private PopupWindow pw;
    private int currposition = 0, classifyposition = 0;
    private ScreenLvAdpter screenLvAdpter;
    private RecyclerView pwRecy;
    private String cid;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ssecond;
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

        if (getIntent().hasExtra("Name")) {
            title.setText(getIntent().getStringExtra("Name"));
        }
        if (getIntent().hasExtra("CId")) {
            cid = getIntent().getStringExtra("CId");
        }
        brandAdapter = new SBrandAdapter(mContext);
        idRightBtu.setText("按品牌");
        layoutManager = new GridLayoutManager(mContext, 4);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(brandAdapter);
        }
        brandAdapter.setOnItemClickListener(this);
        screenLvAdpter = new ScreenLvAdpter(mContext);
        screenLvAdpter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tabLayout.getTabAt(position).select();
                //这里就可以根据业务需求处理点击事件了。
                if (isBrang) {
                    currposition = position;
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(tabname_brand.get(position).getPosition(), 0);
                } else {
                    classifyposition = position;
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(tabname_classify.get(position).getPosition(), 0);
                }
                pw.dismiss();
            }
        });
//        valueEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!"".equals(s.toString())) {
//                    if (isBrang) {
//                        for (BrandGroup.ListBean bean : listBeans) {
//                            if (!bean.isSort()) {
//                                if (bean.getName().contains(s.toString())) {
//                                    NewlistBeans.add(bean);
//                                }
//                            } else {
//                                NewlistBeans.add(bean);
//                            }
//                        }
//                        List<BrandGroup.ListBean> list = new ArrayList<>();
//                        for (int i = 0; i < NewlistBeans.size(); i++) {
//                            if (i == NewlistBeans.size() - 1) {
//                                if (NewlistBeans.get(i).isSort()) {
//                                    continue;
//                                } else {
//                                    list.add(NewlistBeans.get(i));
//                                    continue;
//                                }
//                            }
//                            if (NewlistBeans.get(i).isSort() && !NewlistBeans.get(i + 1).isSort()
//                                    || !NewlistBeans.get(i).isSort() && NewlistBeans.get(i + 1).isSort()
//                                    || !NewlistBeans.get(i).isSort() && !NewlistBeans.get(i + 1).isSort()) {
//                                list.add(NewlistBeans.get(i));
//                            }
//                        }
//                        NewlistBeans = list;
//                        brandAdapter.setList(NewlistBeans);
//                    } else {
//                        for (IndustryCatelist.DataBean.TagGroup group : groups) {
//                            if (!group.isSort()) {
//                                if (group.getName().contains(s.toString())) {
//                                    Newgroups.add(group);
//                                }
//                            } else {
//                                Newgroups.add(group);
//                            }
//                        }
//                        List<IndustryCatelist.DataBean.TagGroup> list = new ArrayList<>();
//                        for (int i = 0; i < Newgroups.size(); i++) {
//                            if (i == Newgroups.size() - 1) {
//                                if (Newgroups.get(i).isSort()) {
//                                    continue;
//                                } else {
//                                    list.add(Newgroups.get(i));
//                                    continue;
//                                }
//                            }
//                            if (Newgroups.get(i).isSort() && !Newgroups.get(i + 1).isSort()
//                                    || !Newgroups.get(i).isSort() && Newgroups.get(i + 1).isSort()
//                                    || !Newgroups.get(i).isSort() && !Newgroups.get(i + 1).isSort()) {
//                                list.add(Newgroups.get(i));
//                            }
//                        }
//                        Newgroups = list;
//                        brandAdapter.setList(Newgroups);
//                    }
//                    isSereen = true;
//                } else {
//                    isSereen = false;
//                    if (isBrang) {
//                        brandAdapter.setList(listBeans);
//                    } else {
//                        brandAdapter.setList(groups);
//                    }
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    if (firstItemPosition == -1) return;
                    try {
                        if (isBrang) {
                            if (listBeans.size() > 0) {
                                //                            if (listBeans.get(firstItemPosition).isSort()) {
                                if (!tabLayout.getTabAt(listBeans.get(firstItemPosition).getIndex()).isSelected()) {
                                    tabLayout.getTabAt(listBeans.get(firstItemPosition).getIndex()).select();
                                    currposition = listBeans.get(firstItemPosition).getIndex();
                                    //    }
                                }
                            }
                        } else {
                            if (groups.size() > 0) {
                                //   if (groups.get(firstItemPosition).isSort()) {
                                if (!tabLayout.getTabAt(groups.get(firstItemPosition).getIndex()).isSelected()) {
                                    tabLayout.getTabAt(groups.get(firstItemPosition).getIndex()).select();
                                    classifyposition = groups.get(firstItemPosition).getIndex();

                                }
                                // }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        initPw();
        if (isNetWork(mContext)) {
            industry();
            getBrand();
        }
    }

    private void initPw() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.searchandclassify, null);
        pwRecy = rootView.findViewById(R.id.my_RecyclerView);
        pwRecy.setLayoutManager(new GridLayoutManager(mContext, 4));
        pwRecy.setAdapter(screenLvAdpter);
        pw = new PopupWindow(rootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        pw.setTouchable(true);
    }

    /**
     * 显示弹出窗
     */
    private void showPW() {
        image2.setRotation(180);
        pw.showAsDropDown(more);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                image2.setRotation(0);
                bgView.setVisibility(View.GONE);
            }
        });
        if (isBrang) {
            screenLvAdpter.setDef(currposition);
            screenLvAdpter.setList(tabname_brand);
        } else {
            screenLvAdpter.setDef(classifyposition);
            screenLvAdpter.setList(tabname_classify);
        }
        bgView.setVisibility(View.VISIBLE);
    }

    private void setTablayoutclick() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab == null) return;
            //这里使用到反射，拿到Tab对象后获取Class
            Class c = tab.getClass();
            try {
                //Filed “字段、属性”的意思,c.getDeclaredField 获取私有属性。
                //"mView"是Tab的私有属性名称(可查看TabLayout源码),类型是 TabView,TabLayout私有内部类。
                Field field = c.getDeclaredField("mView");
                //值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查。值为 false 则指示反射的对象应该实施 Java 语言访问检查。
                //如果不这样会报如下错误
                // java.lang.IllegalAccessException:
                //Class com.test.accessible.Main
                //can not access
                //a member of class com.test.accessible.AccessibleTest
                //with modifiers "private"
                field.setAccessible(true);
                final View view = (View) field.get(tab);
                if (view == null) return;
                view.setTag(i);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) view.getTag();
                        currposition = position;
                        classifyposition = position;
                        //这里就可以根据业务需求处理点击事件了。
                        if (isBrang) {
                            ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(tabname_brand.get(position).getPosition(), 0);
                        } else {
                            ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(tabname_classify.get(position).getPosition(), 0);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void industry() {
        if (isRefresh) return;
        groups.clear();
        tabname_classify.clear();
        classifyindex = 0;
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("cid", cid);
        HttpHelper.getInstance().post(mContext, Contants.PortU.INDUSTRY_CATELIST, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onAfter() {
                super.onAfter();
                Mosaic();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JSONObject object = JSONObject.parseObject(json);
                    if (object.getInteger("status") == 1) {
                        industryCatelist = object.toJavaObject(IndustryCatelist.class);
                        for (int i = 0; i < industryCatelist.getData().size(); i++) {
                            IndustryCatelist.DataBean bean = industryCatelist.getData().get(i);
                            tabname_classify.add(new IndustryCatelist.DataBean.TagGroup(bean.getName(), classifyindex));
                            groups.add(new IndustryCatelist.DataBean.TagGroup(bean.getName(), i, true));
                            for (IndustryCatelist.DataBean.TagGroup t : bean.getList()) {
                                t.setIndex(i);
                                groups.add(t);
                            }
                            classifyindex += bean.getList().size() + 1;
                        }

                    } else {
                        showToastShort(object.getString("info"));
                    }

                }
            }

        });
    }

    @OnClick({R.id.id_right_btu, R.id.more})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.id_right_btu:
                isBrang = !isBrang;
                idRightBtu.setText(isBrang ? "按分类" : "按品牌");
                Mosaic();
                break;
            case R.id.more:
                showPW();
                break;
            default:
                break;
        }
    }

    public void getBrand() {
        if (isRefresh) return;
        listBeans.clear();
        tabname_brand.clear();
        brandindex = 0;
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("cid", getIntent().getStringExtra("CId"));
        HttpHelper.getInstance().post(mContext, Contants.PortU.BRANDGROUP, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (loading != null) {
                    loading.showContent();
                }
                //Mosaic();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRefresh = true;
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    if (!"".equals(json)) {
                        brandGroup = JSONArray.parseArray(json, BrandGroup.class);
                        for (int i = 0; i < brandGroup.size(); i++) {
                            BrandGroup group = brandGroup.get(i);
                            tabname_brand.add(new BrandGroup.ListBean(group.getName(), brandindex));
                            listBeans.add(new BrandGroup.ListBean(group.getName(), i, true));
                            for (BrandGroup.ListBean b : group.getList()) {
                                b.setIndex(i);
                                listBeans.add(b);
                            }
                            brandindex += group.getList().size() + 1;
                        }
                    }
                }
            }
        });
    }

    private void Mosaic() {
        if (tabLayout != null) {
            tabLayout.removeAllTabs();
            if (isBrang) {
                for (BrandGroup.ListBean s : tabname_brand) {
                    tabLayout.addTab(tabLayout.newTab().setText(s.getName()));
                }
                brandAdapter.setList(listBeans);
            } else {
                for (IndustryCatelist.DataBean.TagGroup s : tabname_classify) {
                    tabLayout.addTab(tabLayout.newTab().setText(s.getName()));
                }
                brandAdapter.setList(groups);
            }
            addEmptyView();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // tabLayout.setScrollPosition(0, 0, false);
                    tabLayout.getTabAt(0).select();
                }
            }, 30);
            ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(0, 0);
            currposition = 0;
            classifyposition = 0;
            isRefresh = false;
            reflex(tabLayout, 0);
            setTablayoutclick();
        }
    }

    private void addEmptyView() {
        new Handler().postDelayed(() -> {
            try {
                int itemHeight = recyclerView.getLayoutManager().getChildAt(1).getHeight();
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                        , recyclerView.getHeight() - itemHeight);
                View view = new View(mContext);
                view.setLayoutParams(layoutParams);
                brandAdapter.setFoootView(view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 200);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isBrang) {
            Bundle bundle = new Bundle();
            if (isSereen) {
                if (NewlistBeans.get(position).getIs_open() == 0) {
                    showToastShort(NewlistBeans.get(position).getInfo());
                    return;
                }
                if (NewlistBeans.get(position).isSort()) {
                    return;
                }
                bundle.putString("bid", NewlistBeans.get(position).getId());
                bundle.putString("typeName", NewlistBeans.get(position).getName());
                bundle.putString("gid", NewlistBeans.get(position).getGid());
            } else {
                if (listBeans.get(position).getIs_open() == 0) {
                    showToastShort(listBeans.get(position).getInfo());
                    return;
                }
                if (listBeans.get(position).isSort()) {
                    return;
                }
                bundle.putString("bid", listBeans.get(position).getId());
                bundle.putString("typeName", listBeans.get(position).getName());
                bundle.putString("gid", listBeans.get(position).getGid());
            }
            bundle.putString("cid", cid);
            CommonUtils.goActivity(mContext, SGoodsActivity.class, bundle);

        } else {

            Bundle bundle = new Bundle();
            if (isSereen) {
                if (Newgroups.get(position).isSort()) {
                    return;
                }
                bundle.putString("typeid", Newgroups.get(position).getId());
                bundle.putString("typeName", Newgroups.get(position).getName());
            } else {
                if (groups.get(position).isSort()) {
                    return;
                }
                bundle.putString("typeid", groups.get(position).getId());
                bundle.putString("typeName", groups.get(position).getName());
            }
            CommonUtils.goActivity(mContext, SGoodsActivity.class, bundle);
        }
    }

//    @Override
//    public void onChildViewClickListener(View view, int position) {
//        pw.dismiss();
//        currposition = position;
//        if (isBrang) {
//            ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(tabname_brand.get(position).getPosition(), 0);
//        } else {
//            ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(tabname_classify.get(position).getPosition(), 0);
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pw = null;
        rootView = null;
        pwRecy = null;
    }

}
