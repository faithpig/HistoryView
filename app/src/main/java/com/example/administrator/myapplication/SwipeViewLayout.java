package com.example.administrator.myapplication;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.logging.Level;

public class SwipeViewLayout extends FrameLayout{

    private WebView webView;
    private ImageView imageView;

    //屏幕宽高
    private int sHeigth;
    private int sWidth;

    //边缘判定距离
    private double margin;

    private int currentPoint;

    private EdgeTouchType edgeTouchType = EdgeTouchType.NONE;
    private LayoutState layoutState = LayoutState.NONE;

    public SwipeViewLayout(@NonNull Context context) {
        super(context);
        myInit();
    }

    public SwipeViewLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        myInit();
    }

    public SwipeViewLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        myInit();
    }

    public SwipeViewLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        myInit();
    }

    private void myInit() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        this.sHeigth = dm.heightPixels;
        this.sWidth = dm.widthPixels;
        margin = this.sWidth * 0.035;
        webView = new WebView(this.getContext());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://xcfish.cn");
        imageView = new ImageView(this.getContext());
        LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(lp);
        imageView.setVisibility(View.INVISIBLE);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        this.addView(imageView);
        this.addView(webView);
    }


    private boolean isSwipeEdge(MotionEvent ev) {
        if (ev.getX() < margin) {
            edgeTouchType = EdgeTouchType.LEFT;
            return true;
        } else if (sWidth - ev.getX() < margin) {
            edgeTouchType = EdgeTouchType.RIGHT;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (layoutState != LayoutState.NONE || ev.getActionMasked() != MotionEvent.ACTION_DOWN )
            return super.onInterceptTouchEvent(ev);
        if (isSwipeEdge(ev)) {
            return true;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (layoutState == LayoutState.ON_ANIMATION)
            return true;
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN: {
                if (edgeTouchType == EdgeTouchType.LEFT) {
                    imageView.setImageResource(R.mipmap.web_img_left);
                    imageView.setTranslationX((float) -sWidth / 3.0f);
                } else if (edgeTouchType == EdgeTouchType.RIGHT) {
                    imageView.bringToFront();
                    imageView.setImageResource(R.mipmap.web_img_right);
                    imageView.setTranslationX((float) sWidth);
                }
                this.currentPoint = event.getPointerId(event.getActionIndex());
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (edgeTouchType == EdgeTouchType.LEFT) {
                    if (imageView.getVisibility() == View.INVISIBLE) {
                        imageView.setVisibility(View.VISIBLE);
                    }
                    webView.setTranslationX(event.getRawX());
                    imageView.setTranslationX((float) -sWidth / 3.0f + event.getRawX() / 3.0f);
                } else if (edgeTouchType == EdgeTouchType.RIGHT) {
                    if (imageView.getVisibility() == View.INVISIBLE) {
                        imageView.setVisibility(View.VISIBLE);
                    }
                    imageView.setTranslationX(event.getRawX());
                    webView.setTranslationX( -(sWidth - event.getRawX()) / 3.0f);
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:{
                if (edgeTouchType != EdgeTouchType.NONE)
                    startEndAnimation(event.getRawX());
                break;
            }
        }
        return true;
    }

    private void startEndAnimation(float x) {
        layoutState = LayoutState.ON_ANIMATION;
        int side = (x>= sWidth / 2.0f ? 1 : 0);
        AnimatorSet animatorSet = new AnimatorSet();
        Animator animator1 = null;
        Animator animator2 = null;
        if (edgeTouchType == EdgeTouchType.LEFT) {
            if (side == 0) {
                animator1 = ObjectAnimator.ofFloat(imageView,"translationX",imageView.getTranslationX(),(float) -sWidth / 3.0f);
                animator2 = ObjectAnimator.ofFloat(webView,"translationX",webView.getTranslationX(),0.0f);

            } else {
                animator1 = ObjectAnimator.ofFloat(imageView,"translationX",imageView.getTranslationX(),0.0f);
                animator2 = ObjectAnimator.ofFloat(webView,"translationX",webView.getTranslationX(),(float) sWidth);
            }
        } else if (edgeTouchType == EdgeTouchType.RIGHT){
            if (side == 0) {
                animator1 = ObjectAnimator.ofFloat(imageView,"translationX",imageView.getTranslationX(),0.0f);
                animator2 = ObjectAnimator.ofFloat(webView,"translationX",webView.getTranslationX(),(float) -sWidth / 3.0f);
            } else {
                animator1 = ObjectAnimator.ofFloat(imageView,"translationX",imageView.getTranslationX(),(float) sWidth);
                animator2 = ObjectAnimator.ofFloat(webView,"translationX",webView.getTranslationX(), 0.0f);
            }
        }
        animatorSet.playTogether(animator1, animator2);
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imageView.setTranslationX(0.0f);
                webView.setTranslationX(0.0f);
                imageView.setVisibility(View.INVISIBLE);
                webView.bringToFront();
                layoutState = LayoutState.NONE;
                edgeTouchType = EdgeTouchType.NONE;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

}

enum EdgeTouchType{
    LEFT, RIGHT, NONE
}
enum LayoutState{
    SWIPE_LEFT_TO_RIGHT, SWIPE_RIGHT_TO_LEFT, ON_ANIMATION, NONE
}