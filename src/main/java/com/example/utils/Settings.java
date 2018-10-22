package com.example.utils;

import android.content.Context;

import com.example.frame.PrefsManager;

/*
* 保存配置
*
* */
public class Settings {

    private static final float[] SENSITIVE_ARRAY={1.97f,2.96f,4.44f,6.66f,10.0f,15.0f,22.50f,33.75f,50.62f};
    //采集数据的间隔 毫秒
    public static final int[] INTERVAL_ARRAY={100,200,300,400,500,600,700,800};

    public static final String SENSITIVITY = "sensitivity";
    public static final String INTERVAL = "interval";
    public static final String STEP_LEN = "steplen";
    public static final String BODY_WEIGHT = "bodyweight";

    private PrefsManager prefsManager;

    public Settings(Context context){
        prefsManager = new PrefsManager(context);
    }

    /*
    * 获取传感器的灵敏度
    *
    * */
    public double getSensitivity(){
        float sensitivity = prefsManager.getFloat(SENSITIVITY);
        if (sensitivity==0.0f){
            return 10.0f;
        }
        return sensitivity;
    }

    /*
    * 设置传感器的灵敏度
    *
    * */
    public void setSensitivity(float sensitivity){
        prefsManager.putFloat(SENSITIVITY,sensitivity);
    }


    public int getInterval(){
        int interval = prefsManager.getInt(INTERVAL);
        if (interval== 0){
            return 200;
        }
        return interval;
    }


    public void setInterval(int interval){
        prefsManager.putInt(INTERVAL,interval);
    }

    public float getStepLength(){
        float stepLength = prefsManager.getFloat(STEP_LEN);
        if (stepLength== 0.0f){
            return 50.0f;
        }
        return stepLength;
    }


    public void setStepLength(int stepLength){
        prefsManager.putFloat(STEP_LEN,stepLength);
    }

    public float getBodyWeight(){
        float bodyweight = prefsManager.getFloat(BODY_WEIGHT);
        if (bodyweight== 0.0f){
            return 60.0f;
        }
        return bodyweight;
    }


    public void setBodyWeight(float bodyweight){
        prefsManager.putFloat(INTERVAL,bodyweight);
    }


}
