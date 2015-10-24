package com.org.photodemo.util;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 计算农历节日、公历节日
 *
 * @author CLEVO
 * @modify jidongdong 1015/01/23
 * <p/>
 * 添加关于日期的相关计算方法
 */
public class DateUtil {
    public static String getFestival(long timeInMills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMills);
        LunarDay lunar = new LunarDay(calendar);
        SolarDay solar = new SolarDay(calendar);
        String temp = "";
        temp += (lunar.lunarFestival().trim().length() != 0) ? lunar.lunarFestival().trim() : "";
        if ((temp.length() != 0) && (solar.solarFestival().trim().length() != 0)) {
            temp += " ";
        }
        temp += (solar.solarFestival().trim().length() != 0) ? solar.solarFestival().trim() : "";
        return temp;
    }

    /**
     * 两个时间之间的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int getDays(String date1, String date2) {
        return getDaysUnAbs(date1, date2, true);
    }

    /**
     * 计算两个日期相差的天数，是否取绝对值
     *
     * @param date1
     * @param date2
     * @param isabs
     * @return
     */
    public static int getDaysUnAbs(String date1, String date2, boolean isabs) {
        if (TextUtils.isEmpty(date1) || TextUtils.isEmpty(date2))
            return 0;
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        java.util.Date mydate = null;
        try {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int day = (int) ((date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000));
        return isabs ? Math.abs(day) : day;
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    /**
     * 得到现在时间
     *
     * @return 字符串 yyyyMMdd HHmmss
     */
    public static String getStringToday() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 毫秒数
     *
     * @param time
     * @return
     */
    public static String getDate(long time) {
//        System.setProperty("user.timezone", "Asia/Shanghai");
//        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
//        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(time * 1000L));
    }

    /**
     * 将long时间转化成固定格式
     *
     * @param time
     * @param format
     * @return
     */
    public static String getDate(long time, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = new Date(time);
        return formatter.format(date);
    }

    /**
     * 获取xxxx-xx-xx的日
     *
     * @param d
     * @return
     */
    public static int getDay(Date d) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        return now.get(Calendar.DAY_OF_MONTH);
    }

    public static int getDay(long time) {
        Date date = new Date(time);
        return getDay(date);
    }


    /**
     * 获取月份，1-12月
     *
     * @param d
     * @return
     */
    public static int getMonth(Date d) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        return now.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取19xx,20xx形式的年
     *
     * @param d
     * @return
     */
    public static int getYear(Date d) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        return now.get(Calendar.YEAR);
    }

    public static int getYear(long time) {
        Date date = new Date(time);
        return getYear(date);
    }

    /**
     * 获取时期对象
     *
     * @param strDate
     * @return
     */
    public static Date getDate(String strDate) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(strDate);
            return date;
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 获取星期
     *
     * @param d
     * @return
     */
    public static String getWeekSting(Date d) {
        String weeks[] = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        return weeks[now.get(Calendar.DAY_OF_WEEK) - 1];
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDateShortAfterDays(int days) {
        Date currentTime = new Date();
        Date newDate = new Date(currentTime.getTime() + days * 24 * 60 * 60 * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(newDate);
        return dateString;
    }


    public static int compareDate(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                //System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                //System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

}
