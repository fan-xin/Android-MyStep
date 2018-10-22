package com.example.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utiles {
    public static long getTimestampByDay(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date();
        String dateStr = simpleDateFormat.format(d);
        try{
            Date date = simpleDateFormat.parse(dateStr);
            return date.getTime();

        }catch (ParseException e){
            e.printStackTrace();
        }
        return 0L;
    }

    public static double getCalorieBySteps(int stepCount){
        //步长
        int stepLen = 50;
        //体重
        int bodyWeight = 70;
        //走路 走路热量(kcal)=体重(kg)*距离(公里)*0.0708
        double METRIC_WALKING_FACTOR = 0.708;
        //跑步 跑步热量（kcal）=体重（kg）* 距离(公里)*1.02784823
        double METRIC_RUNNING_FACTOR = 1.02784823;

        double calories = (bodyWeight*METRIC_WALKING_FACTOR)*stepLen*stepCount/100000.0;

        return calories;
    }

    public static double getDistanceVal(int stepCount){
        //步长
        int stepLen = 50;
        double distance = (stepCount*(long)(stepLen))/100000.0f;
        return distance;
    }


}
