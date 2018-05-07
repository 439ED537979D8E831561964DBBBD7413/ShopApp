package com.yj.shopapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ubeen.PieData;
import com.yj.shopapp.util.CommonUtils;

import java.util.List;
import java.util.Random;

/**
 * Created by LK on 2018/3/15.
 *
 * @author LK
 */

public class PieChart extends View {
    /**
     * 画笔
     */
    private Paint mPaint;
    private Paint mTvPaint;
    /**
     * 饼状图宽高
     */
    private int mWidth, mHeight;
    /**
     * 饼状图起始角度
     */
    private float mStartAngle = 0f;
    /**
     * 动画时间
     */
    private static final long ANIMATION_DURATION = 1000;
    /**
     * 用户数据
     */
    private List<PieData> mData;
    /**
     * 绘制方式
     */
    private int mDrawWay = COUNT;
    public static final int PART = 0;//分布绘制
    public static final int COUNT = 1;//连续绘制
    private int centerX, centerY;//中心坐标
    private int offset = 6;
    private float r = 0;
    private Random random = new Random();
    float stopX = 0;
    float stopY = 0;
    /**
     * 自定义动画
     */
    private PieChartAnimation mAnimation;
    private float sumValue = 0;//数据值的总和

    public PieChart(Context context) {
        super(context);
        init();
    }

    public PieChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);//防止抖动
        mPaint.setStyle(Paint.Style.FILL);//画笔为填充
        mTvPaint = new Paint();
        mTvPaint.setStyle(Paint.Style.FILL);
        mTvPaint.setColor(Color.GRAY);
        mTvPaint.setTextSize(CommonUtils.dip2px(getContext(), 12));
        //初始化动画
//        mAnimation = new PieChartAnimation();
//        mAnimation.setDuration(ANIMATION_DURATION);
    }

    /**
     * 设置起始角度
     *
     * @param mStartAngle
     */
    public PieChart setmStartAngle(float mStartAngle) {
        this.mStartAngle = mStartAngle;
        invalidate();//刷新
        return this;
    }

    /**
     * 设置数据
     *
     * @param mData
     */
    public void setData(List<PieData> mData) {
        setmData(mData);
    }

    /**
     * 设置数据和绘制方式
     *
     * @param mData
     */
    public void setData(List<PieData> mData, int mDrawWay) {
        setmData(mData);
        this.mDrawWay = mDrawWay;
    }

    /**
     * 设置数据
     *
     * @param mData
     */
    private void setmData(List<PieData> mData) {
        sumValue = 0;
        this.mData = mData;
        initData(mData);
        //startAnimation(mAnimation);
        invalidate();
    }

    /**
     * 总和
     *
     * @param sumValue
     */
    public PieChart setSumValue(float sumValue) {
        this.sumValue = sumValue;
        invalidate();//刷新
        return this;
    }

    /**
     * 初始化数据
     *
     * @param mData
     */
    private void initData(List<PieData> mData) {
        if (mData == null || mData.size() == 0) {
            return;
        }
        /**
         * 计算数据总和确定颜色
         */
        for (int i = 0; i < mData.size(); i++) {
            PieData data = mData.get(i);
            sumValue += data.getValue();
            if (i > 9) {
                data.setColor(randomColor());
            } else {
                data.setColor(Contants.PieColor.COLORS[i]);
            }
        }
        /**
         * 计算百分比和角度
         */
        float currentStartAngle = mStartAngle;
        for (int i = 0; i < mData.size(); i++) {
            PieData data = mData.get(i);
            data.setCurrentStartAngle(currentStartAngle);
            //通过总和来计算百分比
            float percentage = data.getValue() / sumValue;
            //通过百分比来计算对应的角度
            float angle = percentage * 360;
            //设置用户数据
            data.setPercentage(percentage);
            data.setAngle(angle);
            currentStartAngle += angle;
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    private int randomColor() {
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return Color.rgb(r, g, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mData == null) {
            return;
        }
        centerX = (getRight() - getLeft()) / 2;
        centerY = (getBottom() - getTop()) / 2;
        //1.移动画布到中心点
        canvas.translate(centerX, centerY);
        //2.设置当前起始角度
        float currentStartAngle = mStartAngle;
        //3.确定饼图的半径
        r = (float) (Math.min(mWidth, mHeight) / 2 * 0.6);
        float r1 = r / 2;
        float r2 = r / 1.8f;

        //记录上一个的Y坐标
        float beforeY = 0;
        float currentY = 0;
        float nextY = 0;
        //4.确定饼图的矩形大小
        RectF rectF = new RectF(-r, -r, r, r);
        RectF rectF1 = new RectF(-r1, -r1, r1, r1);
        RectF rectF2 = new RectF(-r2, -r2, r2, r2);
        for (int i = 0; i < mData.size(); i++) {
            PieData data = mData.get(i);
            stopX = (float) ((r + 20) * Math.cos((2 * currentStartAngle + data.getAngle()) / 2 * Math.PI / 180));
            stopY = (float) ((r + 20) * Math.sin((2 * currentStartAngle + data.getAngle()) / 2 * Math.PI / 180));
            data.setStartX(stopX);
            data.setStartY(stopY);
            currentStartAngle += data.getAngle();
        }
        currentStartAngle = mStartAngle;
        PieData currdata;
        int size = 30;
        for (int i = 0; i < mData.size(); i++) {
            PieData data = mData.get(i);
            if (i == 0) {
//                beforeY = mData.get(mData.size() - 1).getStartY();
//                currentY = data.getStartY();
//                if (currentY + Math.abs(beforeY) < 100) {
//                    data.setStartY(100);
//                }
            } else {
                currdata = mData.get(i - 1);
                beforeY = mData.get(i - 1).getStartY();
                currentY = data.getStartY();
                if (data.getStartX() > 0 && currdata.getStartX() > 0) {
                    if (currentY - beforeY > CommonUtils.dp2px(getContext(), 35)) {
                        if (i == mData.size() - 1) {

                        } else {
                            data.setStartY(currentY - CommonUtils.dp2px(getContext(), 16));
                            data.setStartX(data.getStartX() + size);
                        }
                    } else {
                        data.setStartY(beforeY + CommonUtils.dp2px(getContext(), 16));
                        data.setStartX(data.getStartX() + size);
                    }
                } else if (data.getStartX() > 0 && currdata.getStartX() < 0) {
                    if (Math.abs(data.getStartY()) < centerY * 0.5) {
                        data.setStartY(currentY - CommonUtils.dp2px(getContext(), 16));
                    }
                    data.setStartX(data.getStartX() + size);
                }
            }
            if (data.getStartX() > 0) {
                if (data.getStartY() > 0) {
                    if (Math.abs(data.getStartY()) > centerY * 0.65) {
                        data.setStartX(data.getStartX() + CommonUtils.dp2px(getContext(), 10));
                        data.setStartY(data.getStartY() + CommonUtils.dp2px(getContext(), 15));
                    }
                } else {
                    if (Math.abs(data.getStartY()) > centerY * 0.65) {
                        data.setStartX(data.getStartX() + CommonUtils.dp2px(getContext(), 10));
                        data.setStartY(data.getStartY() - CommonUtils.dp2px(getContext(), 15));
                    }
                }
            } else {
                //x小于 y大于
                if (data.getStartY() > 0) {
                    if (Math.abs(data.getStartY()) > centerY * 0.65) {
                        data.setStartX(data.getStartX() - CommonUtils.dp2px(getContext(), 10));
                        data.setStartY(data.getStartY() + CommonUtils.dp2px(getContext(), 15));
                    }
                } else {
                    if (Math.abs(data.getStartY()) > centerY * 0.65) {
                        data.setStartX(data.getStartX() - CommonUtils.dp2px(getContext(), 10));
                        data.setStartY(data.getStartY() - CommonUtils.dp2px(getContext(), 15));
                    }
                }

            }

        }
        for (int i = 0; i < mData.size(); i++) {
            PieData data = mData.get(i);
            //5.设置颜色
            mPaint.setColor(data.getColor());
            //6.绘制饼图
            if (mDrawWay == PART) {
                canvas.drawArc(rectF, data.getCurrentStartAngle(), data.getAngle(), true, mPaint);
            } else if (mDrawWay == COUNT) {
                canvas.drawArc(rectF, currentStartAngle, data.getAngle(), true, mPaint);
                mPaint.setColor(data.getColor());
                stopX = (float) ((r + 20) * Math.cos((2 * currentStartAngle + data.getAngle()) / 2 * Math.PI / 180));
                stopY = (float) ((r + 20) * Math.sin((2 * currentStartAngle + data.getAngle()) / 2 * Math.PI / 180));
                canvas.drawCircle(stopX, stopY, 10, mPaint);
                //7.绘制下一块扇形时先将角度加上当前扇形的角度
                currentStartAngle += data.getAngle();
            }

            // 判断横线是画在左边还是右边
            int endX;
            int dx;
            float zx = 0, zy = 0;
            if (data.getStartX() > 0) {
                endX = (centerX - getPaddingRight() - 20);
            } else {
                endX = (-centerX + getPaddingLeft() + 20);
            }

            mPaint.setStrokeWidth(CommonUtils.dip2px(getContext(), 1));
            canvas.drawLine(stopX, stopY, data.getStartX(), data.getStartY(), mPaint);
            canvas.drawLine(data.getStartX(), data.getStartY(), endX, data.getStartY(), mPaint);
            dx = (int) (endX - stopX);
            //测量文字大小
            Rect rect = new Rect();
            String text = data.getText() + "  " + String.valueOf(data.getValue());
            try {
                mTvPaint.getTextBounds(text, 0, text.length(), rect);
                int w = rect.width();
                int h = rect.height();
                canvas.drawText(text, 0, text.length(), dx > 0 ? endX - offset - w : endX + offset, data.getStartY()- offset, mTvPaint);
//                String value = String.valueOf(data.getValue());
//                mTvPaint.getTextBounds(value, 0, value.length(), rect);
//                int w2 = rect.width();
//                canvas.drawText(value, 0, value.length(), dx > 0 ? endX - offset - w2 : endX + offset, data.getStartY() - offset, mTvPaint);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //绘制中心空白处
        mPaint.setColor(0x11000000);
        canvas.drawArc(rectF2, currentStartAngle, 360f, true, mPaint);
        //绘制中心阴影部分
        mPaint.setColor(Color.parseColor("#FFFFFF"));
        canvas.drawArc(rectF1, currentStartAngle, 360f, true, mPaint);
        //绘制文字
        mPaint.setColor(0xEEFF4567);
        mPaint.setTextSize(80);
    }


    /**
     * 自定义动画
     */
    public class PieChartAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                for (int i = 0; i < mData.size(); i++) {
                    PieData data = mData.get(i);
                    //通过总和来计算百分比
                    float percentage = data.getValue() / sumValue;
                    //通过百分比来计算对应的角度
                    float angle = percentage * 360;
                    //根据插入时间来计算角度
                    angle = angle * interpolatedTime;
                    data.setAngle(angle);
                }
            } else {//默认显示效果
                for (int i = 0; i < mData.size(); i++) {
                    //通过总和来计算百分比
                    PieData data = mData.get(i);
                    float percentage = data.getValue() / sumValue;
                    //通过百分比来计算对应的角度
                    float angle = percentage * 360;
                    data.setPercentage(percentage);
                    data.setAngle(angle);
                }
            }
            invalidate();
        }
    }
}
