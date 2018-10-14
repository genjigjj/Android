package com.gjj.avgle.utils;

import java.text.SimpleDateFormat;

public class DateUtils {

    /**
     * 返回unix时间戳 (1970年至今的秒数)
     *
     * @return
     */

    public static long getUnixStamp() {

        return System.currentTimeMillis() / 1000;

    }


    /**
     * 时间戳转化为时间格式
     *
     * @param timeStamp
     * @return
     */

    public static String timeStampToStr(long timeStamp) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String date = sdf.format(timeStamp * 1000);

        return date;

    }

    /**
     * 得到日期   yyyy-MM-dd
     *
     * @param timeStamp  时间戳
     * @return
     */

    public static String formatDate(long timeStamp) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String date = sdf.format(timeStamp * 1000);

        return date;

    }

    /**
     * 得到时间  HH:mm:ss
     *
     * @param timeStamp   时间戳
     * @return
     */

    public static String getTime(long timeStamp) {

        String time = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String date = sdf.format(timeStamp * 1000);

        String[] split = date.split("\\s");

        if (split.length > 1) {

            time = split[1];

        }

        return time;

    }

    /**
     * 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
     *
     * @param timeStamp
     * @return
     */

    public static String convertTimeToFormat(long timeStamp) {

        long curTime = System.currentTimeMillis() / (long) 1000;

        long time = curTime - timeStamp;

        if (time < 60 && time >= 0) {

            return "刚刚";

        } else if (time >= 60 && time < 3600) {

            return time / 60 + "分钟前";

        } else if (time >= 3600 && time < 3600 * 24) {

            return time / 3600 + "小时前";

        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {

            return time / 3600 / 24 + "天前";

        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {

            return time / 3600 / 24 / 30 + "个月前";

        } else {

            return time / 3600 / 24 / 30 / 12 + "年前";

        }

    }

    /**
     * 将一个时间戳转换成提示性时间字符串，(多少分钟)
     *
     * @param timeStamp
     * @return
     */

    public static String timeStampToFormat(long timeStamp) {

        long curTime = System.currentTimeMillis() / (long) 1000;

        long time = curTime - timeStamp;

        return time / 60 + "";

    }

}
