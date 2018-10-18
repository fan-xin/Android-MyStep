// IPedometerService.aidl
package com.example.service;

// Declare any non-default types here with import statements

interface IPedometerService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

        //开始计步
        void startCount();
        //结束计步
        void stopCount();
        //重置计步器
        void resetCount();
        //获取计步数
        int getSetpsCount();
        //获取消耗的卡路里
        double getCarlorie();
        //获取距离
        double getDistance();
        //数据保存
        void saveData();
        //设置传感器的灵敏度
        void setSensitivity(double sensitivity);
        //获取传感器的灵敏度
        double getSensitivity();
        //设置采样时间间隔
        void setInterval(int interval);
        //获取采样事件
        int getInterval();
        //获取开始时间戳
        long getStartTimeStamp();
        //设置服务运行状态
        int getServiceStatus();
}
