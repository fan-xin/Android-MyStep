package com.example.frame;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;

public class FrameApplication extends Application {

    private static LinkedList<Activity> activityList = new LinkedList<Activity>();

    /*获取列表*/
    public static LinkedList<Activity> getActivityList() {

        return activityList;
    }

    public static  void addToActivityList(final Activity act){
        if (act!= null){
            activityList.add(act);
        }
    }


    public static void removeFromActivityList(final Activity act){
        if (activityList != null && activityList.size()>0 && activityList.indexOf(act)!= -1){
            activityList.remove(act);
        }
    }


    /*
    * 清理所有的activity
    * */
    public static void clearActivityList(){

        for (int i = activityList.size()-1 ; i >= 0 ; i--) {

            final Activity activity = activityList.get(i);
            if (activity != null){
                activity.finish();
            }

        }
    }

    public static void exitApp(){
        try{
            clearActivityList();

        }catch (final  Exception e){
            e.printStackTrace();

        }finally {
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


    private PrefsManager prefsManager;
    private static FrameApplication instance;

    public static FrameApplication getInstance() {
        return instance;
    }

    public static void setInstance(FrameApplication instance) {
        FrameApplication.instance = instance;
    }

    private ErrorHandler errorHandler;

    public void onCreate(){
        super.onCreate();
        instance = this;
        prefsManager = new PrefsManager(this);
        //实例化一个ErrorHandler
        errorHandler = ErrorHandler.getInstance();
    }

    public PrefsManager getPrefsManager() {
        return prefsManager;
    }

    public void setPrefsManager(PrefsManager prefsManager) {
        this.prefsManager = prefsManager;
    }


}
