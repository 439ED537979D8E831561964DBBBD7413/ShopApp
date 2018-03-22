package com.yj.shopapp.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.Interface.OnDateListenter;
import com.yj.shopapp.ui.activity.ShowLog;

import java.util.Calendar;

/**
 * Created by LK on 2018/3/14.
 *
 * @author LK
 */

public class Allpopupwindow extends PopupWindow implements View.OnClickListener, DatePicker.OnDateChangedListener {
    private View rootView;
    private TextView exit_tv, finish_tv, starttimetv, endtimetv, delect_tv;
    private DatePicker datePicker;
    private TextView selectView;
    private int year, monthOfYear, dayOfMonth;
    private OnDateListenter listenter;
    private String startTime = "";
    private String endTime = "";
    private Context mContext;

    public Allpopupwindow(Context context) {
        super(context);
        mContext = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        setWidth(metrics.widthPixels);
        setHeight(metrics.heightPixels);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.timeselect_animation);
        rootView = LayoutInflater.from(context).inflate(R.layout.fragment_all, null);
        setContentView(rootView);
        /**
         * 设置可以获取集点
         */
        setFocusable(true);

        /**
         * 设置点击外边可以消失
         */
        setOutsideTouchable(true);

        /**
         *设置可以触摸
         */
        setTouchable(true);

        //设置点击外部可以消失
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /**
                 * 判断是不是点击了外部
                 */
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    return true;
                }
                //不是点击外部
                return false;
            }
        });
        init();
        initDate();
    }

    private void init() {
        exit_tv = (TextView) rootView.findViewById(R.id.exit_tv);
        exit_tv.setOnClickListener(this);
        finish_tv = (TextView) rootView.findViewById(R.id.finish_tv);
        finish_tv.setOnClickListener(this);
        starttimetv = (TextView) rootView.findViewById(R.id.starttimetv);
        starttimetv.setOnClickListener(this);
        endtimetv = (TextView) rootView.findViewById(R.id.endtimetv);
        endtimetv.setOnClickListener(this);
        delect_tv = (TextView) rootView.findViewById(R.id.delect_tv);
        delect_tv.setOnClickListener(this);
        datePicker = (DatePicker) rootView.findViewById(R.id.mDatepicker);
    }

    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        selectView = starttimetv;
        datePicker.init(year, monthOfYear, dayOfMonth, this);
        datePicker.setMaxDate(calendar.getTimeInMillis());
        selectView.setText(String.format("%d-%02d-%d", year, monthOfYear + 1, dayOfMonth));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit_tv:
                dismiss();
                break;
            case R.id.finish_tv:
                if (!starttimetv.getText().toString().contains("开始") && !endtimetv.getText().toString().contains("结束")) {
                    int size = DateUtils.compareSize(starttimetv.getText().toString(), endtimetv.getText().toString());
                    ShowLog.e(size + "");
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
                    datePicker.setVisibility(View.VISIBLE);
                    selectView = starttimetv;
                    selectView.setText(String.format("%d-%02d-%d", year, monthOfYear + 1, dayOfMonth));
                } else {
                    selectView = starttimetv;
                    String[] time = starttimetv.getText().toString().split("-");
                    datePicker.updateDate(Integer.parseInt(time[0]), Integer.parseInt(time[1]) - 1, Integer.parseInt(time[2]));
                }
                break;
            case R.id.endtimetv:
                viewSelect(endtimetv);
                if (endtimetv.getText().toString().contains("结束")) {
                    datePicker.setVisibility(View.VISIBLE);
                    selectView = endtimetv;
                    selectView.setText(String.format("%d-%02d-%d", year, monthOfYear + 1, dayOfMonth));
                } else {
                    selectView = endtimetv;
                    String[] time = endtimetv.getText().toString().split("-");
                    datePicker.updateDate(Integer.parseInt(time[0]), Integer.parseInt(time[1]) - 1, Integer.parseInt(time[2]));
                }
                break;
            case R.id.delect_tv:
                starttimetv.setText("开始日期");
                endtimetv.setText("结束日期");
                datePicker.setVisibility(View.INVISIBLE);
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                monthOfYear = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                selectView.setTextColor(mContext.getResources().getColor(R.color.deftvcolor));
                selectView.setBackground(mContext.getResources().getDrawable(R.drawable.tv_bottomline));
                break;
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

    public Allpopupwindow setListenter(OnDateListenter listenter) {
        this.listenter = listenter;
        return this;
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
        selectView.setText(String.format("%d-%02d-%d", year, monthOfYear + 1, dayOfMonth));
    }
}
