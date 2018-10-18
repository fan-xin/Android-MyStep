package com.example.mystepapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.frame.BaseActivity;

public class WelcomeActivity extends BaseActivity {

    private Handler handler;
    private Runnable jumpRunnable;
    public static final int DELAY_MILLIS = 2000;


    @Override
    protected void onInitVariable() {

        handler = new Handler();
        jumpRunnable = new Runnable() {
            @Override
            public void run() {
                //跳转到Home页面
                Intent intent = new Intent();
                //设置跳转页面
                intent.setClass(WelcomeActivity.this, HomeActivity.class);
                //开启home页面
                startActivity(intent);
                //结束当前页面
                WelcomeActivity.this.finish();
            }
        };

        super.setHedeSysTitle(true);
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

        //加载布局
        setContentView(R.layout.activity_welcome);

    }

    @Override
    protected void onRequestData() {
        handler.postDelayed(jumpRunnable, DELAY_MILLIS);

    }
}
