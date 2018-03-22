package com.yj.shopapp.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jm on 2016/4/28.
 */
public class DateUtils {
    /**
     * 获取当前的年数
     *
     * @return
     */
    public static int getYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    /**
     * 获取当前的月数
     *
     * @return
     */
    public static int getMonths() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前的天数
     *
     * @return
     */
    public static int getCurrentDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据 年、月 获取对应的月份 的 天数
     */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 获取当天的日期
     *
     * @return yyyy-MM-dd格式的当天的日期
     */
    public static String getNowDate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date);
    }

    /**
     * 获取小时
     */
    public static boolean getHour(long pasttime) {
        if (pasttime != 0) {
            long time = System.currentTimeMillis();
            return time - pasttime > 7200000 ? true : false;
        } else {
            return false;
        }
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString(String str) {
        try {
            if (StringHelper.isEmpty(str)) {
                return "";
            }
            long time = Long.parseLong(str);
            Date d = new Date(time);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
            return sf.format(d);
        } catch (Exception e) {
            return "";
        }
    }

    public static String timet(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    public static String timed(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy.MM.dd");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    public static String times(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    public static String timea(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString2(String str) {
        try {
            if (StringHelper.isEmpty(str)) {
                return "";
            }
            long time = Long.parseLong(str);
            Date d = new Date(time);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 hh时mm分", Locale.CHINA);
            return sf.format(d);
        } catch (Exception e) {
            return "";
        }
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString3(String str) {
        try {
            if (StringHelper.isEmpty(str)) {
                return "";
            }
            long time = Long.parseLong(str);
            Date d = new Date(time);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 hh时mm分ss秒", Locale.CHINA);
            return sf.format(d);
        } catch (Exception e) {
            return "";
        }
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString4(String str) {
        try {
            if (StringHelper.isEmpty(str)) {
                return "";
            }
            long time = Long.parseLong(str);
            Date d = new Date(time);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd     hh:mm:ss", Locale.CHINA);
            return sf.format(d);
        } catch (Exception e) {
            return "";
        }
    }

    /*时间戳转换成字符窜*/
    public static String getDateToLong(long time) {

        Date d = new Date(time * 1000);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return sf.format(d);

    }


    /*将字符串转为时间戳*/
    public static long getStringToDate(String time) {
        if (StringHelper.isEmpty(time)) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 对比两个日期大小
     * @param time1
     * @param time2
     * @return
     */
    public static int compareSize(String time1, String time2) {
        Date d1 = strToDate("yyyy-MM-dd", time1);
        Date d2 = strToDate("yyyy-MM-dd", time2);
        return d1.compareTo(d2);
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int longBetween(String smdate, String bdate) {


        Date d1 = strToDate("yyyy-MM-dd", smdate);
        Date d2 = strToDate("yyyy-MM-dd", bdate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(d2);
        long time2 = cal.getTimeInMillis();
        long between_sss = (time2 - time1);
        return Integer.parseInt(String.valueOf(between_sss));
    }

    /**
     * 字符串转成时间类型
     *
     * @return 时间类型
     */
    public static Date strToDate(String style, String mon) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        try {
            return formatter.parse(mon);
        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }

    /**
     * 字符串转成时间类型
     *
     * @return 时间类型
     */
    public static boolean ToDate(String style, String mon) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        try {
            formatter.parse(mon);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }


    public static String wellcome() {
        String str;
        DateUtils.getNowDate();
        Calendar mCalendar = Calendar.getInstance();
        int apm = mCalendar.get(Calendar.AM_PM);
        if (apm == 0) {
            str = "上午好";
        } else {
            str = "下午好";
        }
        return str;
    }

    /**
     * 获取过去或者未来 任意天内的日期数组
     *
     * @param intervals intervals天内
     * @return 日期数组
     */
    public static ArrayList<String> test(int intervals) {
        ArrayList<String> pastDaysList = new ArrayList<>();
        ArrayList<String> fetureDaysList = new ArrayList<>();
        for (int i = 0; i < intervals; i++) {
            pastDaysList.add(getPastDate(i));
            fetureDaysList.add(getFetureDate(i));
        }
        return pastDaysList;
    }

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        Log.e(null, result);
        return result;
    }

    /**
     * 获取未来 第 past 天的日期
     *
     * @param past
     * @return
     */
    public static String getFetureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        Log.e(null, result);
        return result;
    }

}
