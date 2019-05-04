package com.example.administrator.myapplication;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.util.Random;

public class FourthActivity extends AppCompatActivity {

    private Random mRandom;
    private CircleAnimationView mCircleAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);
        mCircleAnimationView = (CircleAnimationView) findViewById(R.id.circle_view);
        mRandom = new Random();
        final Handler handler = new MyHandler(this,mCircleAnimationView, mRandom);
        new Thread(){
            @Override
            public void run() {
                for (; ; ) {
                    try {
                        this.sleep(1000);
                    } catch (Exception e) {

                    }
                    if(getMainLooper().getQueue().isIdle()) {
                        Message msg = Message.obtain();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                }
            }
        }.start();
    }

    public static class MyHandler extends Handler {

        private WeakReference<Activity> mActivity;
        private WeakReference<CircleAnimationView> circleAnimationViewWeakReference;
        private WeakReference<Random> randomWeakReferencer;

        public MyHandler (Activity activity, CircleAnimationView view, Random random) {
            mActivity = new WeakReference<Activity>(activity);
            circleAnimationViewWeakReference = new WeakReference<CircleAnimationView>(view);
            randomWeakReferencer = new WeakReference<Random>(random);
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity=mActivity.get();
            final CircleAnimationView circleView = circleAnimationViewWeakReference.get();
            final Random r = randomWeakReferencer.get();
            if(activity!=null)
            {
                if (msg.what == 1) {
                    circleView.voiceWaveCome(r.nextInt(Utils.dip2px(activity.getApplicationContext(),100)));
                    this.removeCallbacksAndMessages(null);
                }
            }
        }
    }
}
