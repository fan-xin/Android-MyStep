package com.example.service;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.example.beans.PedometerBean;

public class PedometerListener implements SensorEventListener {
    //当前步数
    private int currentSteps = 0;

    //设置当前步数
    public void setCurrentSteps(int steps){
        currentSteps=steps;
    }
    //灵敏度
    private float sensitivity = 30;
    //采样时间
    private long mLimit = 300;
    //最后保存的采样数值
    private float mLastValue;
    //缩放常数
    private float mScale = -4f;
    //采样数据的偏移值
    private float offset = 240;
    //采样时间
    private long start = 0;
    private long end = 0;
    //方向
    private float mLastDirection;
    //记录数值
    private float mLastExtremes[][] = new float[2][1];
    //最后一次的变化量
    private float mLastDiff;
    //是否匹配
    private int mLastMatch = -1;

    private PedometerBean data;

    //在数据采集时，将数据写入bean

    public PedometerListener(PedometerBean data) {
        this.data = data;

    }

    //当传感器数值发生变化的时候
    @Override
    public void onSensorChanged(SensorEvent event) {
        //获取传感器
        Sensor sensor = event.sensor;
        //同步，防止多线程
        synchronized (this){
            //判断是否是加速度传感器
            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                float sum = 0;
                //获取三个方向上的传感器数值
                for (int i = 0; i < 3; i++) {
                    //将数值放大
                    float vector = offset + event.values[i]*mScale;
                    sum += vector;
                }
                //取得传感器的平均值
                float avarage = sum/3;
                //判断方向
                float dir;
                if (avarage > mLastValue){
                    dir = 1;
                }else if (avarage < mLastValue){
                    dir = -1;
                }else {
                    dir = 0;
                }

                //如果当前的方向和上一次的方向相反
                if (dir == -mLastDirection){
                    //方向的值
                    int extType = (dir > 0?0:1);
                    //保存数值变化
                    mLastExtremes[extType][0] = mLastValue;
                    //求绝对值，保证diff是正值
                    //diff表示加速度的变化的绝对值
                    float diff = Math.abs(mLastExtremes[extType][0] - mLastExtremes[1-extType][0]);
                    //判断是否大于敏感值
                    //如果大于，则认为这次的数值是有效的
                    //在有效的情况下，继续进行筛选
                    if (diff > sensitivity){
                        //数值是否与上次的比，足够大
                        boolean isLargeAsPrevious = diff > (mLastDiff*2/3);
                        //数值是否小于上次的数值的1//3
                        boolean isPreviousLargeEnough = mLastDiff > (diff/3);
                        //判断方向
                        boolean isNotContra = (mLastMatch != 1-extType);
                        if (isLargeAsPrevious&&isNotContra&&isPreviousLargeEnough){
                            //认为这是一次有效的记录

                            //记录当前的毫秒数
                            end= System.currentTimeMillis();
                            if (end - start > mLimit){
                                currentSteps++;
                                //记录最后一次匹配的方向
                                mLastMatch = extType;
                                //重新开始计时
                                start = end;
                                mLastDiff = diff;

                                //将采集到的数据写入bean
                                if (data != null){
                                    //记录步数
                                    data.setStepCount(currentSteps);
                                    //记录时间
                                    data.setLastStepTime(System.currentTimeMillis());
                                }
                            }
                            else {
                                //时间过短，不是有效的记录
                                mLastDiff = sensitivity;
                            }
                        }
                        //条件不满足的情况
                        else{
                            mLastMatch = -1;
                            mLastDiff = sensitivity;
                        }
                    }
                }
                mLastDirection = dir;
                mLastValue = avarage;
            }
        }
    }
    //当传感器精度发生变化的时候
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public float getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(float sensitivity) {
        this.sensitivity = sensitivity;
    }

    public long getLimit() {
        return mLimit;
    }

    public void setLimit(long mLimit) {
        this.mLimit = mLimit;
    }
}
