package com.example.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.beans.PedometerBean;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    //数据库的版本
    private static final int VERSION = 1;
    //数据库的表名
    public static final String TABLE_NAME="pedometer";

    public static final String DB_NAME = "PedometerDB";

    //数据库的列名
    public static final String[] COLUMNS = {
        "id",
        "stepCount",
        "calorie",
        "distance",
        "pace",
        "startTime",
        "lastStepTime",
        "day"
    };

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);


    }

    public DBHelper(Context context, String name) {
        this(context, name, VERSION);


    }

    public DBHelper(Context context, String name, int version) {
        this(context, name,null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                "(id integer PRIMARY KEY AUTOINCREMENT DEFAULT NULL, "+
                "stepCount integer, "+
                "calorie Double, "+
                "distance Double DEFAULT null,"+
                "pace integer, "+
                "speed double, "+
                "startTime Timestamp DEFAULT NUll, "+
                "lastStepTime Timestamp DEFAULT NULL, "+
                "day Timestamp DEFAULT NULL)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //将数据写入数据库
    public void writeDatabase(PedometerBean data){
        //获取一个可以写入的数据库
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //写入数据
        values.put("stepCount", data.getStepCount());
        values.put("calorie", data.getCalorie());
        values.put("distance", data.getDistance());
        values.put("pace", data.getPace());
        values.put("speed", data.getSpeed());
        values.put("startTime", data.getStartTime());
        values.put("lastStepTime", data.getLastStepTime());
        values.put("createTime", data.getDay());

        db.insert(DBHelper.TABLE_NAME, null,values);
        db.close();
    }

    /*
    * 根据天为单位获取记步的基本数据
    *
    * */
    public PedometerBean getByDayTime(long dayTime){
        Cursor cursor = null;
        PedometerBean bean = new PedometerBean();
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery("select * from "+DBHelper.TABLE_NAME+" where day = "+String.valueOf(dayTime),null);

        if (cursor!= null && cursor.getCount()>0){
            if (cursor.moveToNext()){

                //获取到数值
                int id = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMNS[0]));
                int stepCount = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMNS[1]));
                double calorie = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMNS[2]));
                double distance = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMNS[3]));
                int pace = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMNS[4]));
                double speed = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMNS[5]));
                long startTime = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMNS[6]));
                long lastStepTime = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMNS[7]));
                long cTime = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMNS[8]));

                //设置bean，填充数值
                bean.setId(id);
                bean.setStepCount(stepCount);
                bean.setCalorie(calorie);
                bean.setDistance(distance);
                bean.setPace(pace);
                bean.setSpeed(speed);
                bean.setStartTime(startTime);
                bean.setLastStepTime(lastStepTime);
                bean.setDay(cTime);
                return bean;
            }
        }

        cursor.close();
        db.close();

        //如果没有获取到数据，则返回没有数据的bean
        return bean;

    }

    /*
    * 获取全部数据，并且分页
    *
    * */
    public ArrayList<PedometerBean> getFromDatabase(int offVal){
        int pageSize = 20;
//        int offVal = 0;
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME,null,null,null,null,null,
                "day desc limit "+String.valueOf(pageSize)+ " offset "+ String.valueOf(offVal),null);

        ArrayList<PedometerBean> dataList = new ArrayList<PedometerBean>();

        if (cursor!= null && cursor.getCount()>0){
            while (cursor.moveToNext()){
                PedometerBean bean = new PedometerBean();
                //获取到数值
                int id = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMNS[0]));
                int stepCount = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMNS[1]));
                double calorie = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMNS[2]));
                double distance = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMNS[3]));
                int pace = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMNS[4]));
                double speed = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMNS[5]));
                long startTime = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMNS[6]));
                long lastStepTime = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMNS[7]));
                long cTime = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMNS[8]));

                //设置bean，填充数值
                bean.setId(id);
                bean.setStepCount(stepCount);
                bean.setCalorie(calorie);
                bean.setDistance(distance);
                bean.setPace(pace);
                bean.setSpeed(speed);
                bean.setStartTime(startTime);
                bean.setLastStepTime(lastStepTime);
                bean.setDay(cTime);

                dataList.add(bean);

            }
        }
        cursor.close();
        db.close();

        return dataList;
    }


    /*
    * 更新数据库
    *
    * */
    public void updateToDatabase(ContentValues values, long dayTime){
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_NAME,values,"day = ?",new String[]{String.valueOf(dayTime)});
        db.close();
    }


}
