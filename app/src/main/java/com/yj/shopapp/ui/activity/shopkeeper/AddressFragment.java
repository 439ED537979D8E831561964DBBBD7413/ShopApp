package com.yj.shopapp.ui.activity.shopkeeper;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Province;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.LiftAdpter4;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.PreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by LK on 2018/4/9.
 *
 * @author LK
 */
public class AddressFragment extends DialogFragment {

    Unbinder unbinder;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.mylistView)
    ListView mylistView;
    private Context mContext;
    private String uid;
    private String token;
    private LiftAdpter4 adpter;
    private int role;
    private List<Province> areaList1 = new ArrayList<>();
    private List<Province> provinceList = new ArrayList<>();
    private String areaId = "";
    private String name = "";
    private int index = 0;
    private onCitySelectListenter listenter;

    public AddressFragment setListenter(onCitySelectListenter listenter) {
        this.listenter = listenter;
        return this;
    }

    public static AddressFragment newInstance(int role) {

        Bundle args = new Bundle();
        args.putInt("role", role);
        AddressFragment fragment = new AddressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);
        mContext = getActivity();
        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");
        adpter = new LiftAdpter4(mContext);
        role = getArguments().getInt("role");
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCanceledOnTouchOutside(true);
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        lp.height = (int) (displayMetrics.heightPixels * 0.7);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void onResume() {
        super.onResume();
        tabLayout.addTab(tabLayout.newTab().setText("请选择"));
        tabLayout.getTabAt(tabLayout.getTabCount() - 1).select();
        tabLayout.setSelectedTabIndicatorHeight(5);
        getProvince();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addressfragment, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mylistView.setAdapter(adpter);
        mylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (index > 0) {
                    TabLayout.Tab tab = tabLayout.newTab().setText(areaList1.get(position).getArea_name());
                    tabLayout.addTab(tab, index);
                    tabLayout.getTabAt(tabLayout.getTabCount() - 1).select();
                    tabLayout.setSelectedTabIndicatorHeight(5);
                    areaId = areaList1.get(position).getId();
                    name += areaList1.get(position).getArea_name();
                } else {
                    TabLayout.Tab tab = tabLayout.newTab().setText(provinceList.get(position).getArea_name());
                    tabLayout.addTab(tab, index);
                    tabLayout.getTabAt(tabLayout.getTabCount() - 1).select();
                    tabLayout.setSelectedTabIndicatorHeight(5);
                    areaId = provinceList.get(position).getId();
                    name += provinceList.get(position).getArea_name();
                }
                index++;
                getArea();
            }
        });
    }

    /**
     * 获取省份
     */
    private void getProvince() {
        Map<String, String> params = new HashMap<>();
        params.put("putype", role + "");
        HttpHelper.getInstance().post(mContext, Contants.PortA.Getprovince, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onAfter() {
                super.onAfter();

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Province> jsonHelper = new JsonHelper<Province>(Province.class);
                    provinceList.addAll(jsonHelper.getDatas(json));
                    adpter.setList(provinceList);
                }
            }
        });

    }

    public void getArea() {
        Map<String, String> params = new HashMap<>();
        params.put("putype", role + "");
        params.put("parent_id", areaId);
        HttpHelper.getInstance().post(mContext, Contants.PortA.Getchildarea, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();

            }

            @Override
            public void onBefore() {
                super.onBefore();
                areaList1.clear();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Province> jsonHelper = new JsonHelper<Province>(Province.class);
                    if (jsonHelper.getDatas(json).size() > 0) {
                        areaList1.addAll(jsonHelper.getDatas(json));
                        adpter.setList(areaList1);
                    } else {
                        listenter.value(areaId,name);
                        dismiss();

                    }
                }
            }
        });
    }

    @Override
    public void show(FragmentManager manager, String tag) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (manager.isDestroyed())
                return;
        }
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface onCitySelectListenter {
        void value(String Cider, String address);
    }
}
