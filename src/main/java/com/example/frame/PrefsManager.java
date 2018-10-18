package com.example.frame;

import android.content.Context;

public class PrefsManager {

    private final Context mContext;
    private static final String PERFERNCE_NAME="fan_step";

    public PrefsManager(final Context context) {
        this.mContext = context;

    }

    /*清除存储文件内容*/
    public void clear(){
        //对SharedPreference中保存的内容进行清理
        //编辑，清空，提交修改
        mContext.getSharedPreferences(PrefsManager.PERFERNCE_NAME,Context.MODE_PRIVATE)
                .edit().clear().commit();
    }

    //检查文件是否存在
    public boolean contains(){
        return mContext.getSharedPreferences(PrefsManager.PERFERNCE_NAME,Context.MODE_PRIVATE)
                .contains(PrefsManager.PERFERNCE_NAME);
    }

    /*获取key的值*/
    public boolean getBoolean(final String key){
        return this.mContext.getSharedPreferences(PrefsManager.PERFERNCE_NAME,Context.MODE_PRIVATE)
                .getBoolean(key, false);
    }

    /*获取配置文件的方法*/
    public Boolean getBooleanDefaultTrue(final String key){
        return this.mContext.getSharedPreferences(PrefsManager.PERFERNCE_NAME,Context.MODE_PRIVATE)
                .getBoolean(key,true);
    }

    /*获取key的值*/
    public int getInt(final String key){
        return this.mContext.getSharedPreferences(PrefsManager.PERFERNCE_NAME,Context.MODE_PRIVATE)
                .getInt(key, 0);
    }

    /*获取key的值*/
    public long getLong(final String key){
        return this.mContext.getSharedPreferences(PrefsManager.PERFERNCE_NAME,Context.MODE_PRIVATE)
                .getLong(key, 0L);
    }


    /*获取key的值*/
    public float getFloat(final String key){
        return this.mContext.getSharedPreferences(PrefsManager.PERFERNCE_NAME,Context.MODE_PRIVATE)
                .getFloat(key, 0.0f);
    }


    /*获取key的值*/
    public String getString(final String key){
        return this.mContext.getSharedPreferences(PrefsManager.PERFERNCE_NAME,Context.MODE_PRIVATE)
                .getString(key, "");
    }

    /*put操作*/
    public boolean putInt(final String key, final int value){
        return mContext.getSharedPreferences(PrefsManager.PERFERNCE_NAME,Context.MODE_PRIVATE)
                .edit().putInt(key,value).commit();
    }

    /*put操作*/
    public boolean putString(final String key, final String value){
        return mContext.getSharedPreferences(PrefsManager.PERFERNCE_NAME,Context.MODE_PRIVATE)
                .edit().putString(key,value).commit();
    }


    /*put操作*/
    public boolean putLong(final String key, final long value){
        return mContext.getSharedPreferences(PrefsManager.PERFERNCE_NAME,Context.MODE_PRIVATE)
                .edit().putLong(key,value).commit();
    }


    /*put操作*/
    public boolean putFloat(final String key, final float value){
        return mContext.getSharedPreferences(PrefsManager.PERFERNCE_NAME,Context.MODE_PRIVATE)
                .edit().putFloat(key,value).commit();
    }

    /*put操作*/
    public boolean putBoolean(final String key, final  boolean value){
        return mContext.getSharedPreferences(PrefsManager.PERFERNCE_NAME,Context.MODE_PRIVATE)
                .edit().putBoolean(key,value).commit();
    }

}
