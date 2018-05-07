package com.yj.shopapp.ui.activity.shopkeeper;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.Interface.OnDateListenter;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.util.StatusBarManager;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by LK on 2018/4/9.
 *
 * @author LK
 */
public class SelectTimeFragmnet extends DialogFragment implements DatePicker.OnDateChangedListener {
    @BindView(R.id.exit_tv)
    TextView exitTv;
    @BindView(R.id.finish_tv)
    TextView finishTv;
    @BindView(R.id.starttimetv)
    TextView starttimetv;
    @BindView(R.id.endtimetv)
    TextView endtimetv;
    @BindView(R.id.delect_tv)
    ImageView delectTv;
    @BindView(R.id.mDatepicker)
    DatePicker mDatepicker;
    @BindView(R.id.timeSelectView)
    LinearLayout timeSelectView;
    Unbinder unbinder;
    private int year, monthOfYear, dayOfMonth;
    private TextView selectView;
    private String startTime = "";
    private String endTime = "";
    private OnDateListenter listenter;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        StatusBarManager.getInstance().setDialogWindowStyle(getDialog().getWindow(), getResources().getColor(R.color.white));
        StatusBarManager.getInstance().setStatusBarTextColor(getDialog().getWindow(), true);
        View view = inflater.inflate(R.layout.fragment_all, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCanceledOnTouchOutside(true);
        Window dialogWindow = getDialog().getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
        initDate();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        selectView = starttimetv;
        mDatepicker.init(year, monthOfYear, dayOfMonth, this);
        mDatepicker.setMaxDate(calendar.getTimeInMillis());
        selectView.setText(String.format("%d-%02d-%d", year, monthOfYear + 1, dayOfMonth));
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
        selectView.setText(String.format("%d-%02d-%d", year, monthOfYear + 1, dayOfMonth));
    }

    @OnClick({R.id.exit_tv, R.id.finish_tv, R.id.starttimetv, R.id.endtimetv, R.id.delect_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.exit_tv:
                dismiss();
                break;
            case R.id.finish_tv:
                if (!starttimetv.getText().toString().contains("开始") && !endtimetv.getText().toString().contains("结束")) {
                    int size = DateUtils.compareSize(starttimetv.getText().toString(), endtimetv.getText().toString());
                    switch (size) {
                        case 1:
                            startTime = endtimetv.getText().toString();
                            endTime = starttimetv.getText().toString();
                            break;
                        case 0:
                        default:
                            startTime = starttimetv.getText().toString();
                            endTime = endtimetv.getText().toString();
                            break;
                    }
                } else {
                    if (!starttimetv.getText().toString().contains("开始")) {
                        startTime = starttimetv.getText().toString();
                    }
                    if (!endtimetv.getText().toString().contains("结束")) {
                        startTime = endtimetv.getText().toString();
                    }
                }
                listenter.getDate(startTime, endTime);
                dismiss();
                break;
            case R.id.starttimetv:
                viewSelect(starttimetv);
                if (starttimetv.getText().toString().contains("开始")) {
                    mDatepicker.setVisibility(View.VISIBLE);
                    selectView = starttimetv;
                    selectView.setText(String.format("%d-%02d-%d", year, monthOfYear + 1, dayOfMonth));
                } else {
                    selectView = starttimetv;
                    String[] time = starttimetv.getText().toString().split("-");
                    mDatepicker.updateDate(Integer.parseInt(time[0]), Integer.parseInt(time[1]) - 1, Integer.parseInt(time[2]));
                }
                break;
            case R.id.endtimetv:
                viewSelect(endtimetv);
                if (endtimetv.getText().toString().contains("结束")) {
                    mDatepicker.setVisibility(View.VISIBLE);
                    selectView = endtimetv;
                    selectView.setText(String.format("%d-%02d-%d", year, monthOfYear + 1, dayOfMonth));
                } else {
                    selectView = endtimetv;
                    String[] time = endtimetv.getText().toString().split("-");
                    mDatepicker.updateDate(Integer.parseInt(time[0]), Integer.parseInt(time[1]) - 1, Integer.parseInt(time[2]));
                }
                break;
            case R.id.delect_tv:
                starttimetv.setText("开始日期");
                endtimetv.setText("结束日期");
                mDatepicker.setVisibility(View.INVISIBLE);
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                monthOfYear = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                selectView.setTextColor(mContext.getResources().getColor(R.color.deftvcolor));
                selectView.setBackground(mContext.getResources().getDrawable(R.drawable.tv_bottomline));
                break;
        }
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

    private void viewSelect(View selectView) {
        if (selectView.getId() == starttimetv.getId()) {
            starttimetv.setBackground(mContext.getResources().getDrawable(R.drawable.tv_bottomline_select));
            starttimetv.setTextColor(mContext.getResources().getColor(R.color.skyblue));
            endtimetv.setTextColor(mContext.getResources().getColor(R.color.deftvcolor));
            endtimetv.setBackground(mContext.getResources().getDrawable(R.drawable.tv_bottomline));
        } else {
            endtimetv.setBackground(mContext.getResources().getDrawable(R.drawable.tv_bottomline_select));
            starttimetv.setBackground(mContext.getResources().getDrawable(R.drawable.tv_bottomline));
            endtimetv.setTextColor(mContext.getResources().getColor(R.color.skyblue));
            starttimetv.setTextColor(mContext.getResources().getColor(R.color.deftvcolor));
        }
    }

    public SelectTimeFragmnet setListenter(OnDateListenter listenter) {
        this.listenter = listenter;
        return this;
    }
}
