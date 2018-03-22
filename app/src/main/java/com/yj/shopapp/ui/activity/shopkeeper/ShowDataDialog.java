package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.yj.shopapp.R;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.view.WheelView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by LK on 2017/11/23.
 *
 * @author LK
 */

public class ShowDataDialog {
    private List<String> yearlist = new ArrayList<>();
    private List<String> monthlist = new ArrayList<>();
    //private List<String> daylist = new ArrayList<>();
    private int currentYear;
    private int currentMonth;
    //private int currentDay;
    private Context mContext;
    private OnDataInterface onDataInterface;
    private int selectYear = -1, selectMonth = -1, selectSeason = -1;
    private int index = -1;
    private String[] Season = {"春", "夏", "秋", "冬"};

    public ShowDataDialog(Context mContext, OnDataInterface onDataInterface) {
        currentYear = DateUtils.getYear();
        currentMonth = DateUtils.getMonths();
        //currentDay = DateUtils.getCurrentDay();
        this.mContext = mContext;
        this.onDataInterface = onDataInterface;
    }

//    public ShowDataDialog init(Context mContext, OnDataInterface onDataInterface) {
//
//    }

    public void showDataDialog(final int type) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.wheelviewdialog, null);
        TextView titleView = (TextView) rootView.findViewById(R.id.titleView);
        WheelView wheelView = (WheelView) rootView.findViewById(R.id.wheelview);
        //wheelView.setOffset(2);
        switch (type) {
            case 0:
                setYearlist();
                titleView.setText("请选择年份");
                wheelView.setItems(yearlist);
                wheelView.setSeletion(selectYear);
                break;
            case 1:
                setMonthlist();
                titleView.setText("请选择月份");
                wheelView.setItems(monthlist);
                wheelView.setSeletion(selectMonth);
                break;
            case 2:
//                setDaylist();
//                titleView.setText("请选择天数");
//                wheelView.setItems(daylist);
//                wheelView.setSeletion(DateUtils.getCurrentDay() - 1);
                titleView.setText("请选择季度");
                wheelView.setItems(Arrays.asList(Season));
                break;
            default:
                break;
        }
        new MaterialDialog.Builder(mContext)
                .customView(rootView, false)
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        switch (type) {
                            case 0:
                                if (index != -1) {
                                    selectYear = index;
                                }
                                onDataInterface.setYead(yearlist.get(selectYear));
                                index = -1;
                                break;
                            case 1:
                                if (index != -1) {
                                    selectMonth = index;
                                }
                                onDataInterface.setMonths(yearlist.get(selectYear), monthlist.get(selectMonth));
                                index = -1;
                                break;
                            case 2:
                                //onDataInterface.setDatas(yearlist.get(selectYear), monthlist.get(selectMonth), daylist.get(selectDay));
                                onDataInterface.setQuarter(yearlist.get(selectYear), Season[selectSeason]);
                                selectSeason = -1;
                                break;
                            default:
                                break;
                        }

                        dialog.dismiss();
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                switch (type) {
                    case 0:
                        //ShowLog.e(currentYear + "");
                        if (selectYear == -1) {
                            selectYear = yearlist.size() - 1;
                        }
                        index = -1;
                        //onDataInterface.setYead(DateUtils.getYear(), true);
                        break;
//                    case 1:
//                        onDataInterface.setMonths(DateUtils.getYear(), DateUtils.getMonths(), true);
//                        break;
//                    case 2:
//                        onDataInterface.setDatas(DateUtils.getYear(), DateUtils.getMonths(), DateUtils.getCurrentDay(), true);
//                        break;
                    default:
                        break;
                }

                dialog.dismiss();
            }
        }).show();

        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {

                switch (type) {
                    case 0:
//                        if (!"".equals(item)) {
//                            selectYear = Integer.parseInt((item.substring(0, item.length() - 1)));
//                        }
                        index = selectedIndex - 1;
                        break;
                    case 1:
                        index = selectedIndex - 1;

//                        if (!"".equals(item)) {
//                            selectMonth = Integer.parseInt((item.substring(0, item.length() - 1)));
//                        }
                        break;
                    case 2:
                        //selectDay = selectedIndex;
//                        if (!"".equals(item)) {
//                            selectDay = Integer.parseInt(item);
//                        }
                        selectSeason = selectedIndex - 1;
                        break;

                    default:
                        break;
                }
            }
        });
    }


    private void setYearlist() {
        yearlist.clear();
        for (int i = currentYear - 5; i <= currentYear; i++) {
            yearlist.add(i + "年");
        }
        if (selectYear == -1) {
            selectYear = yearlist.size() - 1;
        }
        selectMonth = -1;
    }

    private void setMonthlist() {
        monthlist.clear();
        if (selectYear != -1) {
            if (Integer.parseInt(yearlist.get(selectYear).substring(0, yearlist.get(selectYear).length() - 1)) != currentYear) {
                currentMonth = 12;
            } else {
                currentMonth = 0;
            }
        }
        for (int i = 0; i < currentMonth; i++) {
            monthlist.add(i + 1 + "月");
        }
        if (selectMonth == -1) {
            selectMonth = monthlist.size() - 1;
        }
    }

//    private void setDaylist() {
//        daylist.clear();
//        currentDay = DateUtils.getDaysByYearMonth(currentYear, currentMonth);
//        for (int i = 1; i <= currentDay; i++) {
//            daylist.add(i + "");
//        }
//    }

    public interface OnDataInterface {
        void setYead(String yead);

        void setMonths(String yead, String months);

        //void setDatas(String yead, String months, String datas);

        void setQuarter(String yead, String quarter);
    }
}
