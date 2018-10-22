package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.beans.PedometerChartBean;
import com.example.db.DBHelper;
import com.example.frame.FrameApplication;
import com.example.utils.ACache;
import com.example.utils.Settings;
import com.example.utils.Utiles;
import com.example.beans.PedometerBean;

import static com.example.db.DBHelper.DB_NAME;

//继承service
public class PedometerService extends Service {

    private SensorManager sensorManager;
    private PedometerBean pedometerBean;
    //传感器监听
    private PedometerListener pedometerListener;

    //设置状态
    public static  final int STATUS_NOT_RUN = 0;
    public static  final int STATUS_RUNNING = 1;
    private int runStatus = STATUS_NOT_RUN;

    private Settings settings;

    private PedometerChartBean pedometerChartBean;

    private static Handler handler = new Handler();
    //一分钟
    private static final long SAVE_CHART_TIME = 60000L;

    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            if (runStatus == STATUS_RUNNING){
                if (handler != null&&pedometerChartBean!=null){
                    handler.removeCallbacks(timeRunnable);
                    updateChartData();//更新数据
                    handler.postDelayed(timeRunnable,SAVE_CHART_TIME);
                }
            }

        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        pedometerBean = new PedometerBean();
        pedometerListener = new PedometerListener(pedometerBean);
        //构造图表类型的数据
        pedometerChartBean = new PedometerChartBean();
        settings = new Settings(this);

    }

    //更新记步器的图表数据
    private void updateChartData(){
        if (pedometerChartBean.getIndex() <1440-1 ){
            pedometerChartBean.setIndex(pedometerChartBean.getIndex()+1);
            pedometerChartBean.getArrayData()[pedometerChartBean.getIndex()] = pedometerBean.getStepCount();

        }
    }

    /**
     * 将对象保存
     *
     * */
    private void saveChartData(){
        String jsonStr = Utiles.objToJson(pedometerChartBean);
        ACache.get(FrameApplication.getInstance()).put("JsonChartData",jsonStr);


    }




    private IPedometerService.Stub iPedometerService =  new IPedometerService.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        /*
        * 开始记步的功能
        * */
        @Override
        public void startCount() throws RemoteException {
            if (sensorManager != null&&pedometerListener!= null){
                Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sensorManager.registerListener(pedometerListener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
                //设置开始记步的时间
                pedometerBean.setStartTime(System.currentTimeMillis());
                //解析数据，记录是哪一天的数据
                pedometerBean.setDay(Utiles.getTimestampByDay());
                runStatus = STATUS_RUNNING;

                handler.postDelayed(timeRunnable,SAVE_CHART_TIME);//开始触发数据刷新
            }


        }

        /*
        * 停止记步的功能
        *
        * */
        @Override
        public void stopCount() throws RemoteException {
            //判断监听器是否正常
            if (sensorManager!=null && pedometerListener!=null){
                Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sensorManager.unregisterListener(pedometerListener,sensor);
                runStatus=STATUS_NOT_RUN;
                handler.removeCallbacks(timeRunnable);//停止刷新

            }

        }

        @Override
        public void resetCount() throws RemoteException {
            if (pedometerBean!=null){
                pedometerBean.reset();
                saveData();
            }
            if (pedometerListener!=null){
                pedometerListener.setCurrentSteps(0);
            }

        }

        @Override
        public int getSetpsCount() throws RemoteException {
            if (pedometerBean!=null){
                return pedometerBean.getStepCount();
            }
            return 0;
        }

        @Override
        public double getCarlorie() throws RemoteException {
            if (pedometerBean != null){
                return Utiles.getCalorieBySteps(pedometerBean.getStepCount());
            }

            return 0;
        }

        @Override
        public double getDistance() throws RemoteException {
//            if (pedometerBean!=null){
//                return Utiles.getDistanceVal(pedometerBean.getStepCount());
//            }
            return getDistanceVal();
        }

        public double getDistanceVal() {
            if (pedometerBean!=null){
                return Utiles.getDistanceVal(pedometerBean.getStepCount());
            }
            return 0;
        }


        /*
        * 每次保存数据的时候，都会新起一个线程，进行计算，并且保存到数据库
        *
        * */
        @Override
        public void saveData() throws RemoteException {
            if (pedometerBean!=null){
                //使用另外的线程进行读写操作
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DBHelper dbHelper = new DBHelper(PedometerService.this,DB_NAME);
                        //设置距离
                        pedometerBean.setDistance(getDistanceVal());
                        //设置热量消耗
                        pedometerBean.setCalorie(Utiles.getCalorieBySteps(pedometerBean.getStepCount()));

                        long time = (pedometerBean.getLastStepTime() - pedometerBean.getStartTime())/1000;

                        if (time == 0){
                            //设置多少步/分钟
                            pedometerBean.setPace(0);
                            pedometerBean.setSpeed(0);
                        }else {
                            int pace = Math.round(60*pedometerBean.getStepCount()/time);
                            pedometerBean.setPace(pace);
                            long speed = Math.round((pedometerBean.getDistance()/1000)/(time/60*60));
                            pedometerBean.setSpeed(speed);
                        }
                        dbHelper.writeDatabase(pedometerBean);
                    }
                }).start();
            }
        }

        @Override
        public void setSensitivity(double sensitivity) throws RemoteException {
            if (settings!=null){
                settings.setSensitivity((float) sensitivity);
            }
        }

        @Override
        public double getSensitivity() throws RemoteException {
            if (settings!= null){
                return settings.getSensitivity();
            }
            return 0;
        }

        @Override
        public void setInterval(int interval) throws RemoteException {
            if (settings!=null){
                settings.setInterval(interval);
            }
        }

        @Override
        public int getInterval() throws RemoteException {
            if (settings!=null){
                return settings.getInterval();
            }
            return 0;
        }

        @Override
        public long getStartTimeStamp() throws RemoteException {
            if (pedometerBean!=null){
                return pedometerBean.getStartTime();
            }

            return 0;
        }

        @Override
        public int getServiceStatus() throws RemoteException {
            return runStatus;
        }

        @Override
        public PedometerChartBean getChartData() throws RemoteException {
            return null;
        }

    };

    @Override
    public IBinder onBind(Intent intent) {
        return iPedometerService;
    }
}
