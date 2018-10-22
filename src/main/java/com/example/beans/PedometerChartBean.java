package com.example.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *  显示图表的类
 *@author Fan Xin <fanxin.hit@gmail.com>
 *@time
 *
 */
public class PedometerChartBean implements Parcelable {

    private int[] arrayData;
    private int index;

    public PedometerChartBean(){
        index = 0;
        arrayData = new int[1440];
    }

    protected PedometerChartBean(Parcel in) {
        arrayData = in.createIntArray();
        index = in.readInt();
    }

    /*
    * 构造器
    * */
    public static final Creator<PedometerChartBean> CREATOR = new Creator<PedometerChartBean>() {
        @Override
        public PedometerChartBean createFromParcel(Parcel in) {
            return new PedometerChartBean(in);
        }

        @Override
        public PedometerChartBean[] newArray(int size) {
            return new PedometerChartBean[size];
        }
    };

    public int getIndex(){
        return index;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public int[] getArrayData() {
        return arrayData;
    }

    public void setArrayData(int[] arrayData) {
        this.arrayData = arrayData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        /*注意写入的顺序，要和读取的顺序相反，是一个堆栈*/
        dest.writeIntArray(arrayData);
        dest.writeInt(index);
    }
}
