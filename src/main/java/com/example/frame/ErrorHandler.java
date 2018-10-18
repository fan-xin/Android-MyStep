package com.example.frame;

import android.content.Context;

/*单实例*/
public class ErrorHandler implements Thread.UncaughtExceptionHandler {
    /*未捕获的异常*/
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LogWriter.LogToFile("Error","崩溃信息"+e.getMessage());
        LogWriter.LogToFile("Error","崩溃线程名称"+t.getName()+" 线程ID："+t.getId());

        final StackTraceElement[] trace = e.getStackTrace();
        for(final StackTraceElement element: trace){
            LogWriter.debugError("Error","Line :"+element.getLineNumber()+" "+element.toString());
        }
        e.printStackTrace();
        //退出程序
        FrameApplication.exitApp();



    }

    private static ErrorHandler instance;

    public static ErrorHandler getInstance(){
        if (ErrorHandler.instance == null){
            ErrorHandler.instance = new ErrorHandler();
        }

        return ErrorHandler.instance;
    }

    private ErrorHandler(){

    }

    public void setErrorHandler(final Context ctx){
        //设置默认的未捕获异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

}
