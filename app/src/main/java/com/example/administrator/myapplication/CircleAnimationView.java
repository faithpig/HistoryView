package com.example.administrator.myapplication;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

public class CircleAnimationView extends View {

    private Paint mPaintOuterCircle;
    private Paint mPaintInnerCircle;
    private int mOuterRadius;
    private int mInnerRadius;
    private int mNextOuterRadius;
    private int mNextInnerRadius;
    private final int sDefaultOuterRadius = Utils.dip2px(this.getContext(),50);
    private final int sDefaultInnerRadius = Utils.dip2px(this.getContext(),47);
    private final int sMinOuterRadius = Utils.dip2px(this.getContext(),15);
    private final int sMinInnerRadius = Utils.dip2px(this.getContext(),12);
    private ValueAnimator animator1;
    private ValueAnimator animator2;

    public CircleAnimationView(Context context) {
        super(context);
        initCircle();
    }

    public CircleAnimationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initCircle();
    }

    public CircleAnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCircle();
    }

    public CircleAnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initCircle();
    }

    public void initCircle() {
        mPaintInnerCircle = new Paint();
        mPaintOuterCircle = new Paint();
        mPaintOuterCircle.setColor(Color.BLUE);
        mPaintInnerCircle.setColor(Color.WHITE);
        mPaintInnerCircle.setStyle(Paint.Style.FILL);//画笔属性是实心圆
        mPaintOuterCircle.setStyle(Paint.Style.FILL);
        mInnerRadius = sDefaultInnerRadius;
        mOuterRadius = sDefaultOuterRadius;
    }

    public void voiceWaveCome(int voicePower) {
        mNextInnerRadius = mInnerRadius - voicePower;
        mNextOuterRadius = mOuterRadius - (int)(0.8*voicePower);
        if (mNextInnerRadius < sMinInnerRadius)
            mNextInnerRadius = sMinInnerRadius;
        if (mNextOuterRadius < sMinOuterRadius)
            mNextOuterRadius = sMinOuterRadius;
        if (null != animator1) {
            animator1.cancel();
            animator1.removeAllUpdateListeners();
            animator1 = null;
        }
        if (null != animator2) {
            animator2.cancel();
            animator2.removeAllUpdateListeners();
            animator2 = null;
        }
        startAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mOuterRadius, mPaintOuterCircle);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mInnerRadius, mPaintInnerCircle);
    }

    public void startAnimation() {
        animator1 = ValueAnimator.ofInt(mInnerRadius, mNextInnerRadius);
        animator2 = ValueAnimator.ofInt(mOuterRadius, mNextOuterRadius);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                mInnerRadius = value;
                invalidate();
            }
        });
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                mOuterRadius = value;
                invalidate();
            }
        });
        animator1.setDuration(500);
        animator2.setDuration(500);
        animator1.setInterpolator(new LinearInterpolator());
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator1.setRepeatMode(ValueAnimator.REVERSE);
        animator2.setRepeatMode(ValueAnimator.REVERSE);
        animator1.setRepeatCount(1);
        animator2.setRepeatCount(1);
        animator1.start();
        animator2.start();
    }
}
