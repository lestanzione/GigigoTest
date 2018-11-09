package br.com.stanzione.gigigotest.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy' - 'HH'h'mm");

    public static String formatDate(Date date){
        if(null == date){
            return "";
        }
        else{
            return sdf.format(date);
        }
    }

}
