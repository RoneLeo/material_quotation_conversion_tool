package com.chiyun.material_quotation_conversion_tool.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by wazto on 2019/4/2.
 */
public class DateUtils {
    public static Date getNewDate(Date old, Integer month) {
        if (old == null || old.before(new Date()))
            old = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(old);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }
}
