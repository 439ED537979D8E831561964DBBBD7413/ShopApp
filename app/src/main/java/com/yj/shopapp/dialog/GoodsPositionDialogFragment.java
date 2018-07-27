package com.yj.shopapp.dialog;

import android.support.design.widget.TabLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.adapter.LiftAdpter4;

import butterknife.BindView;

/**
 * Created by LK on 2018/5/25.
 *
 * @author LK
 */
public class GoodsPositionDialogFragment extends BaseDialogFragment {
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.mylistView)
    ListView mylistView;
    private LiftAdpter4 adpter;
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
    protected int getLayoutId() {
        return R.layout.goods_position_dialogfragment;
    }

    @Override
    protected void initData() {
        tabLayout.addTab(tabLayout.newTab().setText("请选择"));
        tabLayout.getTabAt(tabLayout.getTabCount() - 1).select();
        tabLayout.setSelectedTabIndicatorHeight(5);
    }
}
