package com.example.mystepapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.frame.BaseActivity;

public class HomeActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void onInitVariable() {
        //不显示系统标题栏
        super.setHedeSysTitle(true);

    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected void onRequestData() {

    }
}
