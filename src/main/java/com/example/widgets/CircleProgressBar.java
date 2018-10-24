package com.example.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Fan Xin <fanxin.hit@gmail.com>
 * 18/10/24  10:32
 */
public class CircleProgressBar extends View {

    //当前进度
    private int progress = 0;

    private int maxProgress = 100;
    //绘图的paint
    //背景色
    private Paint pathPaint;
    //进度填充色
    private Paint fillPaint;
    //绘制的矩形区域
    private RectF oval;

    private int[] arcColors = {0xFF02C016,
    0xFF3DF346, 0xFF40F1D5, 0xFF02C016 };
    //背景灰色
    private int pathColor = 0xFFF0EEDF;
    //边框灰色
    private int borderColor = 0xFFD2D1C4;

    private int pathWidth = 35;

    private int width;
    private int height;
    //圆的半径
    private int radius = 120;
    //梯度渲染
    private SweepGradient sweepGradient;

    private boolean reset = false;

    private EmbossMaskFilter embossMaskFilter = null;



    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public Paint getPathPaint() {
        return pathPaint;
    }

    public void setPathPaint(Paint pathPaint) {
        this.pathPaint = pathPaint;
    }

    public Paint getFillPaint() {
        return fillPaint;
    }

    public void setFillPaint(Paint fillPaint) {
        this.fillPaint = fillPaint;
    }

    public RectF getOval() {
        return oval;
    }

    public void setOval(RectF oval) {
        this.oval = oval;
    }

    public int[] getArcColors() {
        return arcColors;
    }

    public void setArcColors(int[] arcColors) {
        this.arcColors = arcColors;
    }

    public int getPathColor() {
        return pathColor;
    }

    public void setPathColor(int pathColor) {
        this.pathColor = pathColor;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public int getPathWidth() {
        return pathWidth;
    }

    public void setPathWidth(int pathWidth) {
        this.pathWidth = pathWidth;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public SweepGradient getSweepGradient() {
        return sweepGradient;
    }

    public void setSweepGradient(SweepGradient sweepGradient) {
        this.sweepGradient = sweepGradient;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
        if (reset){
            progress = 0;
            //使UI区域无效，触发刷新
            invalidate();
        }
    }

    /*
    * 构造函数
    * */
    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //初始化绘制
        pathPaint = new Paint();
        //打开反锯齿，让图像更平滑
        pathPaint.setAntiAlias(true);
        pathPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setDither(true);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);


        fillPaint = new Paint();
        //打开反锯齿，让图像更平滑
        fillPaint.setAntiAlias(true);
        fillPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setStyle(Paint.Style.STROKE);
        fillPaint.setDither(true);
        fillPaint.setStrokeJoin(Paint.Join.ROUND);

        //初始化椭圆
        oval = new RectF();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (reset){
            canvas.drawColor(0xFFFFFFFF);
            reset = false;
        }
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        radius = getMeasuredWidth()/2 - pathWidth;

        //画白色的背景

        //设置背景颜色
        pathPaint.setColor(pathColor);
        //设置画笔宽度
        pathPaint.setStrokeWidth(pathWidth);
        //pathPaint.setMaskFilter()
        //绘制背景
        canvas.drawCircle((float)(width/2),(float)(height/2),radius,pathPaint);
        pathPaint.setStrokeWidth(0.5f);
        pathPaint.setColor(borderColor);
        canvas.drawCircle((float)(width/2),(float)(height/2),(float)(radius + pathWidth/2)+0.5f,pathPaint);
        canvas.drawCircle((float)(width/2),(float)(height/2),(float)(radius - pathWidth/2)-0.5f,pathPaint);

        //画进度条
        sweepGradient = new SweepGradient((float)(width/2),(float)(height/2),arcColors,null);

        fillPaint.setShader(sweepGradient);
        fillPaint.setStrokeCap(Paint.Cap.ROUND);
        fillPaint.setStrokeWidth((float) pathWidth);
        //设置矩形区域
        oval.set((float)(width/2-radius),(float)(height/2-radius),(float)(width/2+radius),(float)(height/2+radius));

        canvas.drawArc(oval,-90.0F,
                (float)progress/(float)maxProgress*360.0F,
                 false,
                           fillPaint);
    }

    /**
    *测量方法，决定宽和高
     * MeasureSpec封装了父布局传递给子布局的布局要求，每个MeasureSpec代表了一组宽度和高度的要求
     * 通过getSize()方法拿到尺寸，通过setMeasuredDimension方法进行设置
     * getSize会分解掉参数，并提取出高度和宽度
    *@author Fan Xin <fanxin.hit@gmail.com>
    *@time
    */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        setMeasuredDimension(width,height);

    }
}
