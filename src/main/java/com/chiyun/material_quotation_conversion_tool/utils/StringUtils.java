package com.chiyun.material_quotation_conversion_tool.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StringUtils {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 判断目标字符串是否为空
     *
     * @param target
     * @return
     */
    public static Boolean isEmpty(String target) {
        if (target == null) {
            return true;
        }
        if (target.trim().length() <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断目标字符串是否不为空
     *
     * @param target
     * @return
     */
    public static Boolean isNotEmpty(String target) {
        if (target == null) {
            return false;
        }
        if (target.trim().length() <= 0) {
            return false;
        }
        return true;
    }

    /**
     * 分裂字符串
     *
     * @param splitStr 需要分裂的字符串
     * @param spilt    此字符分裂
     * @return String[]
     */
    public static String[] splitString(String splitStr, String spilt) {
        if (isEmpty(splitStr) || isEmpty(spilt)) {
            return null;
        }
        String[] strA = splitStr.split(spilt);
        return strA;
    }

    /**
     * 分裂字符串
     *
     * @param splitStr 需要分裂的字符串
     * @param spilt    此字符分裂
     * @return Integer[]
     */
    public static Integer[] splitIntger(String splitStr, String spilt) {
        if (isEmpty(splitStr) || isEmpty(spilt)) {
            return null;
        }
        String[] strA = splitStr.split(spilt);
        Integer[] strB = new Integer[strA.length];
        for (int i = 0; i < strA.length; i++) {
            strB[i] = Integer.parseInt(strA[i]);
        }
        return strB;
    }

    /**
     * 获取给定时间前几小时的时间
     *
     * @param date 时间段的结束时间
     * @param hour 小时
     * @return String
     */
    public static String getbeginString(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);//date 换成已经已知的Date对象
        cal.add(Calendar.HOUR_OF_DAY, hour);// before  hour
        return format.format(cal.getTime());
    }

    /**
     * 获取给定时间前几小时的时间
     *
     * @param date 时间段的结束时间
     * @param hour 小时
     * @return Date
     */
    public static Date getbeginDate(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);//date 换成已经已知的Date对象
        cal.add(Calendar.HOUR_OF_DAY, hour);// before  hour
        return cal.getTime();
    }


    public static String getstationdata(String data) {
        Integer sss;
        try {
            sss = Integer.valueOf(data);
        } catch (Exception e) {
            sss = 0;
        }
        return sss + "";
    }

    public static Integer getIntegerdata(String data) {
        int sss;
        double sssdd;
        try {
            sssdd = Double.valueOf(data);
            sss = (int) sssdd;
        } catch (Exception e) {
            sss = 0;
        }
        return sss;
    }

    public static Integer getIntegerdata(Integer data) {
        int sss;
        try {
            sss = data;
        } catch (Exception e) {
            sss = 0;
        }
        return sss;
    }

    public static Double getDoubledata(String data) {
        double sss;
        try {
            sss = Double.valueOf(data);
        } catch (Exception e) {
            sss = Double.valueOf(0);
        }
        return sss;
    }

    public static Double getDoubledata(Double data) {
        Integer sss;
        try {
            sss = Integer.valueOf(String.valueOf(data));
        } catch (Exception e) {
            sss = 0;
        }
        return Double.valueOf(sss);
    }

    public static boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
                return false;
            }
        }
        return true;
    }

    public static double getdouble(double d, int scal) {
        return new BigDecimal(d).setScale(scal, BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    public static String getnamelike(String str) {
        if (isEmpty(str))
            return "%%";
        return "%" + str + "%";
    }
}
