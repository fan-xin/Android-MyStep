package com.example.mystepapp;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.frame.BaseActivity;
import com.example.frame.LogWriter;
import com.example.utils.Settings;
import com.example.utils.Utiles;

import org.w3c.dom.Text;

/**
 * Created by Fan Xin <fanxin.hit@gmail.com>
 * 18/10/29  19:34
 */
public class SettingActivity extends BaseActivity {

    private ListView settingListView;
    private ImageView back;

    class ViewHolder{
        TextView title;
        TextView desc;
    }

    public class SettingListAdapter extends BaseAdapter{

        //实例化Setting对象
        private Settings settings = null;

        private String[] listTitle={"设置步长","设置体重","传感器灵敏度","传感器采样时间"};

        public SettingListAdapter(){
            settings = new Settings(SettingActivity.this);
        }



        @Override
        public int getCount() {
            return listTitle.length;
        }

        @Override
        public Object getItem(int position) {
            if ((listTitle!=null)&&(position<listTitle.length)){
                return listTitle[position];
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //加载List界面
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;

            if (convertView==null){
                //实例化一个viewHolder
                viewHolder = new ViewHolder();
                convertView = View.inflate(SettingActivity.this,
                        R.layout.item_setting,
                        null);
                viewHolder.title = (TextView) convertView.findViewById(R.id.id_title);
                viewHolder.desc = (TextView) convertView.findViewById(R.id.id_desc);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.title.setText(listTitle[position]);
            switch (position){
                case 0:{
                    final float stepLen = settings.getStepLength();
                    viewHolder.desc.setText(String.format("计算距离和消耗的热量：%s CM",stepLen));
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //设置步长
                        }
                    });

                }
                break;
                case 1:{
                    final float bodyWeight = settings.getBodyWeight();
                    viewHolder.desc.setText(String.format("通过体重计算消耗的热量：%s Kg",bodyWeight));
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //设置体重
                        }
                    });

                }
                break;
                case 2:{
                    double sensitivity  = settings.getSensitivity();
                    viewHolder.desc.setText(String.format("传感器的敏感程度：%s ",Utiles.getFormatVal(sensitivity,"#.00")));
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //设置传感器敏感度
                        }
                    });

                }
                break;
                case 3:{
                    int interval  = settings.getInterval();
                    viewHolder.desc.setText(String.format("每隔%s毫秒进行一次数据采集", Utiles.getFormatVal(interval,"#.00")));
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //设置传感器敏感度
                        }
                    });

                }
                break;
//                default:{
//                    LogWriter.d("Postion = "+position);
//                }

            }


            return convertView;
        }
    }



    @Override
    protected void onInitVariable() {

    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

        setContentView(R.layout.act_setting);
        settingListView = (ListView)findViewById(R.id.id_setting_list);
        back = (ImageView)findViewById(R.id.id_iv_setting_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.this.finish();

            }
        });

        settingListView.setAdapter(new SettingListAdapter());
        //点击设置按钮后闪退，使用一个简单布局的listview来测试页面
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(SettingActivity.this,android.R.layout.simple_list_item_1);
        //settingListView.setAdapter(adapter);
//
//        for (int i = 0; i < 20; i++) {
//            adapter.add("条目"+String.valueOf(i));
//        }

    }

    @Override
    protected void onRequestData() {

    }
}
