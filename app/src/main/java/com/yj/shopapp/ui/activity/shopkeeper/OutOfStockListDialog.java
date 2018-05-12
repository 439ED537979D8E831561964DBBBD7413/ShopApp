package com.yj.shopapp.ui.activity.shopkeeper;


import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.OrderDatesBean;
import com.yj.shopapp.ubeen.OrderPreview;
import com.yj.shopapp.ui.activity.adapter.OutofstockListAdpter;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.StatusBarManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class OutOfStockListDialog extends DialogFragment {
    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.Go_carlist)
    TextView GoCarlist;
    private Context mActivity;
    private List<OrderPreview.CancelBean> beanList;
    private OutofstockListAdpter adpter;
    private List<OrderDatesBean.OosdataBean> list;
    private int type = 0;

    public static OutOfStockListDialog newInstance(List<OrderPreview.CancelBean> beans) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("cancelBean", (ArrayList<? extends Parcelable>) beans);
        OutOfStockListDialog fragment = new OutOfStockListDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static OutOfStockListDialog newInstance(List<OrderDatesBean.OosdataBean> beans, int type) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("cancelBean", (ArrayList<? extends Parcelable>) beans);
        args.putInt("type", type);
        OutOfStockListDialog fragment = new OutOfStockListDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);
        mActivity = getActivity();
        if (getArguments().getParcelableArrayList("cancelBean").get(0) instanceof OrderDatesBean.OosdataBean) {
            list = getArguments().getParcelableArrayList("cancelBean");
        } else {
            beanList = getArguments().getParcelableArrayList("cancelBean");
        }
        type = getArguments().getInt("type");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StatusBarManager.getInstance().setDialogWindowStyle(getDialog().getWindow(), getResources().getColor(R.color.white));
        StatusBarManager.getInstance().setStatusBarTextColor(getDialog().getWindow(), true);
        View rootView = inflater.inflate(R.layout.outofstocklistdialog, container);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (type == 1) {
            GoCarlist.setVisibility(View.GONE);
        }
        adpter = new OutofstockListAdpter(mActivity);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        myRecyclerView.addItemDecoration(new DDecoration(mActivity, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
        myRecyclerView.setAdapter(adpter);
        if (beanList != null) {
            adpter.setList(beanList);
        } else {
            adpter.setList(list);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCanceledOnTouchOutside(true);
        Window dialogWindow = getDialog().getWindow();
        assert dialogWindow != null;
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
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

    @OnClick({R.id.Go_carlist, R.id.i_see})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Go_carlist:
                getActivity().finish();
                break;
            case R.id.i_see:
                dismiss();
                break;
            default:
                break;
        }
    }

}
