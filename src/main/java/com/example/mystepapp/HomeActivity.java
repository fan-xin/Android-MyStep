package com.example.mystepapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.beans.PedometerChartBean;
import com.example.frame.BaseActivity;
import com.example.frame.LogWriter;
import com.example.service.IPedometerService;
import com.example.service.PedometerService;
import com.example.utils.Utiles;
import com.example.widgets.CircleProgressBar;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.example.utils.Utiles.getFormatVal;
import static com.example.utils.Utiles.isServiceRunning;

public class HomeActivity extends BaseActivity {
    private CircleProgressBar progressBar;

    private TextView textCalorie, textTime, textDistance;
    private TextView stepCount;
    private Button reset;
    private Button start;

    private BarChart dataChart;

    private IPedometerService remoteService;
    //保存服务的状态
    private int status = -1;
    private static final int STATUS_NOT_RUNNING = 0;
    private static final int STATUS_RUNNING = 1;


    private boolean isRunning = false;
    //是否更新数据
    private boolean isChartUpdate = false;

    private static final int MESSAGE_UPDATE_STEP_COUNT = 1000;
    private static final int MESSAGE_UPDATE_CHART_DATA = 2000;
    //判断正常人一秒中不会走超过五步，所以每个２００毫秒去获取一次数据
    private static final int GET_DATA_TIME = 200;

    private static final long GET_CHART_DATA_TIME = 60000L;
    //使用一分钟的时间来刷新
    private PedometerChartBean pedometerChartBean;

    private boolean bindService = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onInitVariable() {
        //不显示系统标题栏
        super.setHedeSysTitle(true);

    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);

        //初始化组件

        //获取和设置进度条
        progressBar = findViewById(R.id.circleProgressBar);
        progressBar.setProgress(5000);
        progressBar.setMaxProgress(10000);


        textCalorie = findViewById(R.id.id_tx_calorie);
        textDistance = findViewById(R.id.id_tx_distance);
        textTime = findViewById(R.id.id_tx_time);

        stepCount = findViewById(R.id.stepCount);

        reset = findViewById(R.id.reset);
        start = findViewById(R.id.start);

        dataChart = findViewById(R.id.dataChart);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动服务
                try {
                    status = remoteService.getServiceStatus();

                } catch (RemoteException e) {
                    LogWriter.d(e.toString());
                }
                //如果是正在运行使，按下按钮，则停止记步，并且改变按钮的显示
                if (status == STATUS_RUNNING&& remoteService!= null){
                    try {
                        remoteService.stopCount();
                        start.setText("启动");
                        isRunning = false;
                        isChartUpdate = false;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }else if (status == STATUS_NOT_RUNNING&& remoteService!= null){
                    try {
                        remoteService.startCount();
//                        start.setText("停止");
//                        isRunning = true;
//                        isChartUpdate = true;
                        startStepCount();
                    } catch (RemoteException e) {
                        LogWriter.d(e.toString());
                    }
                }
            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }

    @Override
    protected void onRequestData() {
        //检查服务是否运行
        //服务没有运行，则启动服务，如果服务已经运行，则直接绑定服务
        Intent serviceIntent = new Intent(this, PedometerService.class);
        if (!isServiceRunning(this, PedometerService.class.getName())){
            //服务没有运行，启动服务
//            serviceIntent = new Intent(this, PedometerService.class);
            startService(serviceIntent);

        }else {
            //服务运行
//            serviceIntent = new Intent(this, PedometerService.class);
            serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        }
        //绑定服务操作
        bindService = bindService(serviceIntent,serviceConnection,BIND_AUTO_CREATE);
        //初始化一些对应的状态，按钮文字
        //判断绑定是否成功
//        if (bindService&&remoteService!= null){
//            status = remoteService.getServiceStatus();
//
//        }
        if (bindService&&remoteService!= null){
            try {
                status = remoteService.getServiceStatus();
                if (status == PedometerService.STATUS_NOT_RUN){
                    start.setText("启动");

                }else if (status == PedometerService.STATUS_RUNNING){
                    start.setText("停止");
                    isRunning = true;
                    isChartUpdate = true;

                    //启动记步的线程
                    new Thread(new StepRunnable()).start();
                    new Thread(new ChartRunnable()).start();
                }
            }catch (RemoteException e){
                LogWriter.d(e.toString());
            }
        }else {
            start.setText("启动");
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        //服务连接以后，会执行的函数
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            remoteService = IPedometerService.Stub.asInterface(service);
            try {
                //获取服务的状态
                status = remoteService.getServiceStatus();
                if (status == STATUS_RUNNING){
                    startStepCount();

                }else {
                    start.setText("启动");
                }

            } catch (RemoteException e) {
//                e.printStackTrace();
                LogWriter.d(e.toString());
            }
        }

        //服务断开以后，会执行的函数
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void startStepCount() throws RemoteException {
        start.setText("停止");
        //隔一段时间，获取数据，刷新UI
        //启动线程来实现
        isRunning = true;
        isChartUpdate = true;

        pedometerChartBean = remoteService.getChartData();
        updateChart(pedometerChartBean);

        //启动记步的线程
        new Thread(new StepRunnable()).start();
        new Thread(new ChartRunnable()).start();
    }


    private class StepRunnable implements Runnable{
        @Override
        public void run() {
            while (isRunning){
                try {
                    //获取当前的运行状态
                    status = remoteService.getServiceStatus();
                    if (status== STATUS_RUNNING){
                        //清空消息，保证下次发送消息的唯一性
                        handler.removeMessages(MESSAGE_UPDATE_STEP_COUNT);
                        //发送消息，让Handler去更新数据
                        handler.sendEmptyMessage(MESSAGE_UPDATE_STEP_COUNT);
                        Thread.sleep(GET_DATA_TIME);
                    }
                } catch (RemoteException e) {
                    LogWriter.d(e.toString());
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    LogWriter.d(e.toString());
                }
            }
        }
    }

    private class ChartRunnable implements Runnable{
        @Override
        public void run() {
            while (isChartUpdate){
                try {
                    pedometerChartBean = remoteService.getChartData();
                    handler.removeMessages(MESSAGE_UPDATE_CHART_DATA);
                    handler.sendEmptyMessage(MESSAGE_UPDATE_CHART_DATA);
                    //等待一分钟以后再更新
                    Thread.sleep(GET_CHART_DATA_TIME);

                } catch (RemoteException e) {
                    LogWriter.d(e.toString());
                } catch (InterruptedException e) {
                    LogWriter.d(e.toString());
                }
            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_UPDATE_STEP_COUNT:
                {
                    //更新记步数据
                    updateStepCount();
                }
                break;
                case MESSAGE_UPDATE_CHART_DATA:
                {
                    if (pedometerChartBean != null){
                        //调用函数，更新图表
                        updateChart(pedometerChartBean);
                    }
                }
                break;
                default:
                    LogWriter.d("Default = "+ msg.what);

            }
            super.handleMessage(msg);

        }
    };

    //填充数据
    public void updateChart(PedometerChartBean bean){
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        if (bean!= null){
            for (int i = 0; i < bean.getIndex(); i++) {
                xVals.add(String.valueOf(i)+"分");
                int valY = bean.getArrayData()[i];
                yVals.add(new BarEntry(valY,i));
            }
            textTime.setText(String.valueOf(bean.getIndex()+"分"));
            BarDataSet set1 = new BarDataSet(yVals,"所走的步数");
            set1.setBarBorderWidth(2f);
            ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
            dataSets.add(set1);
            //对于同一组ｘ，可以有多个ｙ，在这个例子中，只有一个数据集
            BarData data = new BarData(xVals,dataSets);
            data.setValueTextSize(10f);
            dataChart.setData(data);
            //刷新
            dataChart.invalidate();
        }
    }

    //更新记步
    public void updateStepCount(){
        if (remoteService != null){
            int stepCountVal = 0;
            double calorieVal = 0;
            double distanceVal = 0;
            //从服务获取需要的数据
            try {

                stepCountVal = remoteService.getSetpsCount();
                calorieVal = remoteService.getCarlorie();
                distanceVal = remoteService.getDistance();

            } catch (RemoteException e) {
                LogWriter.d(e.toString());
            }
            //更新数据到UI
            stepCount.setText(String.valueOf(stepCountVal)+"步");
            textCalorie.setText(getFormatVal(calorieVal)+"卡");
            textDistance.setText(Utiles.getFormatVal(distanceVal)+"米");
            //设置进度
            progressBar.setProgress(stepCountVal);
        }
    }

    //解绑,但服务并没有＼停止，会继续在后台进行记步
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bindService){
            bindService = false;
            isRunning = false;
            isChartUpdate = false;
            unbindService(serviceConnection);
        }


    }
}
