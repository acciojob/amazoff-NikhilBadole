package com.driver;

import java.util.Arrays;
import java.util.List;

public class TimeUtil {
    public static int convertStringToInt(String deliveryTime){
        List<String> list = Arrays.asList(deliveryTime.split(":"));
        int HH = Integer.parseInt(list.get(0));
        int MM = Integer.parseInt(list.get(1));
        return HH*60 + MM;
    }

    public static String convertIntToString(int deliveryTime){
        int hh = deliveryTime/60;
        int mm = deliveryTime%60;
        String HH = String.valueOf(hh);
        String MM = String.valueOf(mm);

        if(HH.length() == 1){
            HH = "0" + HH;
        }

        if(MM.length() == 1){
            MM = "0" + MM;
        }
        return (HH + ":" + MM);
    }
}
