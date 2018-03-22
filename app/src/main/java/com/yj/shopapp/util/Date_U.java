package com.yj.shopapp.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Date_U {



    /**
     * @param time 毫秒值
     * @return 时:分:秒 (03:12:34)
     */
    public static String getHours(long time) {
                long second = time / 1000;
                long hour = second / 60 / 60;
                long minute = (second - hour * 60 * 60) / 60;
                long sec = (second - hour * 60 * 60) - minute * 60;

                String rHour = "";
                String rMin = "";
                String rSs = "";
                // 时
                if (hour < 10) {
                        rHour = "0" + hour;
                } else {
                        rHour = hour + "";
                }
                // 分
                if (minute < 10) {
                        rMin = "0" + minute;
                } else {
                        rMin = minute + "";
                }
                // 秒
                if (sec < 10) {
                        rSs = "0" + sec;
                } else {
                        rSs = sec + "";
                }

                // return hour + "小时" + minute + "分钟" + sec + "秒";
                return rHour + ":" + rMin + ":" + rSs;

        }

    /**
     *
     * @param time 毫秒值
     * @return 倒计时数组 {03，12，34}
     */
    public static String[] getHour(long time) {
                long second = time / 1000;
                long hour = second / 60 / 60;
                long minute = (second - hour * 60 * 60) / 60;
                long sec = (second - hour * 60 * 60) - minute * 60;

                String rHour = "";
                String rMin = "";
                String rSs = "";
                // 时
                if (hour < 10) {
                        rHour = "0" + hour;
                } else {
                        rHour = hour + "";
                }
                // 分
                if (minute < 10) {
                        rMin = "0" + minute;
                } else {
                        rMin = minute + "";
                }
                // 秒
                if (sec < 10) {
                        rSs = "0" + sec;
                } else {
                        rSs = sec + "";
                }

                String[] timelis = new String[]{rHour,rMin,rSs};
                return timelis;

        }


        /**
         * 掉此方法输入所要转换的时间输入例如（"2014年06月14日16时09分00秒"）返回时间戳
         *
         * @param time
         * @return
         */
        public String data(String time) {
                SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒",
                        Locale.CHINA);
                Date date;
                String times = null;
                try {
                        date = sdr.parse(time);
                        long l = date.getTime();
                        String stf = String.valueOf(l);
                        times = stf.substring(0, 10);
                        Log.d("--444444---", times);
                } catch (ParseException e) {
                        e.printStackTrace();
                }
                return times;
        }

        /**
         * 掉此方法输入所要转换的时间输入例如（"2014-06-14-16-09-00"）返回时间戳
         *
         * @param time
         * @return
         */
        public String dataOne(String time) {
                SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",
                        Locale.CHINA);
                Date date;
                String times = null;
                try {
                        date = sdr.parse(time);
                        long l = date.getTime();
                        String stf = String.valueOf(l);
                        times = stf.substring(0, 10);
                        Log.d("--444444---", times);
                } catch (ParseException e) {
                        e.printStackTrace();
                }
                return times;
        }

        public static String getTimestamp(String time, String type) {
                SimpleDateFormat sdr = new SimpleDateFormat(type, Locale.CHINA);
                Date date;
                String times = null;
                try {
                        date = sdr.parse(time);
                        long l = date.getTime();
                        String stf = String.valueOf(l);
                        times = stf.substring(0, 10);
                        Log.d("--444444---", times);
                } catch (ParseException e) {
                        e.printStackTrace();
                }
                return times;
        }

        /**
         * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分00秒"）
         *
         * @param time
         * @return
         */
        public String times(String time) {
                SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                @SuppressWarnings("unused")
                long lcc = Long.valueOf(time);
                //int i = Integer.parseInt(time);
                String times = sdr.format(new Date(lcc * 1000L));
                return times;

        }

        /**
         * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分"）
         *
         * @param time
         * @return
         */
        public static String timet(long time) {
                SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String times = sdr.format(new Date(time));
                return times;

        }

        // 调用此方法输入所要转换的时间戳例如（1402733340）输出（"2014年06月14日16时09分00秒"）
        public static String times(long timeStamp) {
                SimpleDateFormat sdr = new SimpleDateFormat("MM月dd日  #  HH:mm");
                return sdr.format(new Date(timeStamp)).replaceAll("#",
                        getWeek(timeStamp));

        }

        private static String getWeek(long timeStamp) {
                int mydate = 0;
                String week = null;
                Calendar cd = Calendar.getInstance();
                cd.setTime(new Date(timeStamp));
                mydate = cd.get(Calendar.DAY_OF_WEEK);
                // 获取指定日期转换成星期几
                if (mydate == 1) {
                        week = "周日";
                } else if (mydate == 2) {
                        week = "周一";
                } else if (mydate == 3) {
                        week = "周二";
                } else if (mydate == 4) {
                        week = "周三";
                } else if (mydate == 5) {
                        week = "周四";
                } else if (mydate == 6) {
                        week = "周五";
                } else if (mydate == 7) {
                        week = "周六";
                }
                return week;
        }

        // 并用分割符把时间分成时间数组

        /**
         * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14-16-09-00"）
         *
         * @param time
         * @return
         */
        public String timesOne(String time) {
                SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                @SuppressWarnings("unused")
                long lcc = Long.valueOf(time);
                int i = Integer.parseInt(time);
                String times = sdr.format(new Date(i * 1000L));
                return times;

        }

        /**
         * 并用分割符把时间分成时间数组
         *
         * @param time
         * @return
         */
        public static String[] timestamp(String time) {
                SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
                @SuppressWarnings("unused")
                long lcc = Long.valueOf(time);
                int i = Integer.parseInt(time);
                String times = sdr.format(new Date(i * 1000L));
                String[] fenge = times.split("[年月日时分秒]");
                return fenge;
        }

        /**
         * 根据传递的类型格式化时间
         *
         * @param str
         * @param type 例如：yy-MM-dd
         * @return
         */
        public static String getDateTimeByMillisecond(String str, String type) {

                Date date = new Date(Long.valueOf(str));

                SimpleDateFormat format = new SimpleDateFormat(type);

                String time = format.format(date);

                return time;
        }

        /**
         * 分割符把时间分成时间数组
         *
         * @param time
         * @return
         */
        public String[] division(String time) {

                String[] fenge = time.split("[年月日时分秒]");

                return fenge;

        }

        /**
         * 输入时间戳变星期
         *
         * @param time
         * @return
         */
        public static String changeweek(String time) {
                SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
                long lcc = Long.valueOf(time);
                int i = Integer.parseInt(time);
                String times = sdr.format(new Date(i * 1000L));
                Date date = null;
                int mydate = 0;
                String week = null;
                try {
                        date = sdr.parse(times);
                        Calendar cd = Calendar.getInstance();
                        cd.setTime(date);
                        mydate = cd.get(Calendar.DAY_OF_WEEK);
                        // 获取指定日期转换成星期几
                } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                if (mydate == 1) {
                        week = "星期日";
                } else if (mydate == 2) {
                        week = "星期一";
                } else if (mydate == 3) {
                        week = "星期二";
                } else if (mydate == 4) {
                        week = "星期三";
                } else if (mydate == 5) {
                        week = "星期四";
                } else if (mydate == 6) {
                        week = "星期五";
                } else if (mydate == 7) {
                        week = "星期六";
                }
                return week;

        }

        /**
         * 获取日期和星期　例如：２０１４－１１－１３　１１:００　星期一
         * @param time
         * @param type
         * @return
         */
        public static String getDateAndWeek(String time, String type) {
                return getDateTimeByMillisecond(time + "000", type) + "  "
                        + changeweekOne(time);
        }

        /**
         * 输入时间戳变星期
         *
         * @param time
         * @return
         */
        public static String changeweekOne(String time) {
                SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                long lcc = Long.valueOf(time);
                int i = Integer.parseInt(time);
                String times = sdr.format(new Date(i * 1000L));
                Date date = null;
                int mydate = 0;
                String week = null;
                try {
                        date = sdr.parse(times);
                        Calendar cd = Calendar.getInstance();
                        cd.setTime(date);
                        mydate = cd.get(Calendar.DAY_OF_WEEK);
                        // 获取指定日期转换成星期几
                } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                if (mydate == 1) {
                        week = "星期日";
                } else if (mydate == 2) {
                        week = "星期一";
                } else if (mydate == 3) {
                        week = "星期二";
                } else if (mydate == 4) {
                        week = "星期三";
                } else if (mydate == 5) {
                        week = "星期四";
                } else if (mydate == 6) {
                        week = "星期五";
                } else if (mydate == 7) {
                        week = "星期六";
                }
                return week;

        }

        /**
         * 获取当前时间
         *
         * @return
         */
        public static String getCurrentTime() {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分");
                return sdf.format(new Date());
        }

        /**
         * 输入日期如（2014年06月14日16时09分00秒）返回（星期数）
         *
         * @param time
         * @return
         */
        public String week(String time) {
                Date date = null;
                SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
                int mydate = 0;
                String week = null;
                try {
                        date = sdr.parse(time);
                        Calendar cd = Calendar.getInstance();
                        cd.setTime(date);
                        mydate = cd.get(Calendar.DAY_OF_WEEK);
                        // 获取指定日期转换成星期几
                } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                if (mydate == 1) {
                        week = "星期日";
                } else if (mydate == 2) {
                        week = "星期一";
                } else if (mydate == 3) {
                        week = "星期二";
                } else if (mydate == 4) {
                        week = "星期三";
                } else if (mydate == 5) {
                        week = "星期四";
                } else if (mydate == 6) {
                        week = "星期五";
                } else if (mydate == 7) {
                        week = "星期六";
                }
                return week;
        }

        /**
         * 输入日期如（2014-06-14-16-09-00）返回（星期数）
         *
         * @param time
         * @return
         */
        public String weekOne(String time) {
                Date date = null;
                SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                int mydate = 0;
                String week = null;
                try {
                        date = sdr.parse(time);
                        Calendar cd = Calendar.getInstance();
                        cd.setTime(date);
                        mydate = cd.get(Calendar.DAY_OF_WEEK);
                        // 获取指定日期转换成星期几
                } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                if (mydate == 1) {
                        week = "星期日";
                } else if (mydate == 2) {
                        week = "星期一";
                } else if (mydate == 3) {
                        week = "星期二";
                } else if (mydate == 4) {
                        week = "星期三";
                } else if (mydate == 5) {
                        week = "星期四";
                } else if (mydate == 6) {
                        week = "星期五";
                } else if (mydate == 7) {
                        week = "星期六";
                }
                return week;
        }
}