package com.example.frame;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class LogWriter {
    private static final String DEBUG_TAG="fan";
    private static boolean isDebug = true;
    /*是否将控制台的输出到文件*/
    private static boolean isWriteToLog = false;

    /*
    * 将日志输出到文件
    *
    * */
    public static void LogToFile(final String tag, final String logText){
        if (!LogWriter.isWriteToLog){
            return;
        }

        final String needWriteMessage = tag+" : "+logText;
        final String fileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/LogFile.txt";
        final File file = new File(fileName);
        //操作文件
        try{

            final FileWriter fileWriter = new FileWriter(file,true);
            final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(needWriteMessage);
            bufferedWriter.newLine();
            bufferedWriter.close();
            fileWriter.close();

        }catch (Exception e){
            e.printStackTrace();
        }


    }


    public static void debugError(final String tag,final String msg){
        //输出到控制台
        if (LogWriter.isDebug){
            Log.e(tag,msg);
        }

        //输出到文件
        if (LogWriter.isWriteToLog){
            LogWriter.LogToFile(LogWriter.DEBUG_TAG,msg);
        }
    }

    public static void d(final String msg){
        //输出到控制台
        if (LogWriter.isDebug){
            Log.d(LogWriter.DEBUG_TAG,msg);
        }

        //输出到文件
        if (LogWriter.isWriteToLog){
            LogWriter.LogToFile(LogWriter.DEBUG_TAG,msg);
        }
    }

    public static void v(final String DEBUG_TAG,final String msg){
        //输出到控制台
        if (LogWriter.isDebug){
            Log.v(LogWriter.DEBUG_TAG,msg);
        }

        //输出到文件
        if (LogWriter.isWriteToLog){
            LogWriter.LogToFile(LogWriter.DEBUG_TAG,msg);
        }
    }

    public static void w(final Object thiz){
        //输出到控制台
        if (LogWriter.isDebug){
            Log.w(LogWriter.DEBUG_TAG,thiz.getClass().getSimpleName());
        }

        //输出到文件
        if (LogWriter.isWriteToLog){
            LogWriter.LogToFile(LogWriter.DEBUG_TAG,thiz.getClass().getSimpleName());
        }
    }

    public static void v(final String msg){
        //输出到控制台
        if (LogWriter.isDebug){
            Log.v(LogWriter.DEBUG_TAG,msg);
        }

        //输出到文件
        if (LogWriter.isWriteToLog){
            LogWriter.LogToFile(LogWriter.DEBUG_TAG,msg);
        }
    }




}
