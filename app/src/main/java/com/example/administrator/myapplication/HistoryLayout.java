package com.example.administrator.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HistoryLayout extends LinearLayout {

    private List<HistoryCell> mCellLists = null;

    public HistoryLayout(Context context) {
        super(context);
    }

    public HistoryLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HistoryLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HistoryLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void initCellList(String[] historyString) {
        mCellLists = new ArrayList<>();
        for (int i = 0; i < historyString.length; i++) {
            HistoryCell hc = new HistoryCell(this.getContext(), historyString[i]);
            mCellLists.add(hc);
            this.addView(hc);
        }
        invalidate();
    }

    //计算所有ChildView的宽度和高度 然后根据ChildView的计算结果，设置自己的宽和高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (null == mCellLists) return;
        //获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        //计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int cCount = getChildCount();
        //计算此layout的高,这里无论此layout的layout_width,layout_height设置什么，其宽永远父view一样，高与历史记录的行数有关
        int height = 0;
        //当前填充位置
        int fillCursor = 0;
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            int cWidth = childView.getMeasuredWidth();
            int cHeight = childView.getMeasuredHeight();
            LayoutParams cParams = (LayoutParams) childView.getLayoutParams();
            int tryFill = fillCursor + cParams.rightMargin + cWidth + cParams.leftMargin;
            if (tryFill > sizeWidth) {
                fillCursor = tryFill - fillCursor;
                height = height + cHeight + cParams.topMargin + cParams.bottomMargin;
            } else {
                fillCursor = tryFill;
            }
            if (i == cCount - 1)
                height = height + cHeight + cParams.topMargin + cParams.bottomMargin;
        }
        setMeasuredDimension(sizeWidth, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (null == mCellLists){
            super.onLayout(changed, l, t, r, b);
            return;
        }
        int cCount = getChildCount();
        int widthCursor = 0;
        int heightCursor = 0;
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            int cWidth = childView.getMeasuredWidth();
            int cHeight = childView.getMeasuredHeight();
            LayoutParams cParams = (LayoutParams) childView.getLayoutParams();
            int cl = 0, ct = 0, cr = 0, cb = 0;
            if (widthCursor + cWidth + cParams.leftMargin + cParams.rightMargin > getWidth()) {
                widthCursor = 0;
                heightCursor = heightCursor + cParams.topMargin + cHeight + cParams.bottomMargin;
            }
            cl = widthCursor + cParams.leftMargin;
            ct = heightCursor + cParams.topMargin;
            cr = cl + cWidth;
            cb = cHeight + ct;
            widthCursor = cr + cParams.rightMargin;
            childView.layout(cl, ct, cr, cb);
        }
    }

}

class HistoryCell extends LinearLayout {

    private int state;
    private TextView mCellText;
    private ImageView mCellImage;

    public HistoryCell(Context context, String text) {
        super(context);
        this.state = 0;
        this.mCellText = new TextView(context);
        this.mCellImage = new ImageView(context);
        mCellText.setText(text);
        mCellText.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp1.setMargins(0, 5, 0, 5);
        mCellText.setLayoutParams(lp1);
        lp2.setMargins(5, 5, 5, 5);
        lp2.gravity = Gravity.CENTER_VERTICAL;
        mCellImage.setLayoutParams(lp2);
        mCellImage.setImageAlpha(100);
        mCellImage.setImageResource(R.mipmap.delete);
        mCellImage.setVisibility(View.INVISIBLE);
        mCellImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v != null) {
                    ViewGroup parent = (ViewGroup) v.getParent();
                    if (parent!=null && parent instanceof HistoryCell) {
                        ViewGroup ancestor = (ViewGroup) parent.getParent();
                        if (ancestor != null && ancestor instanceof HistoryLayout) {
                            ancestor.removeView(parent);
                        }
                    }
                }
            }
        });
        this.addView(mCellText);
        this.addView(mCellImage);
        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                changeState(v);
                return false;
            }
        });
        LinearLayout.LayoutParams lp3 = (LayoutParams) new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp3.setMargins(5,5,5,5);
        lp3.height = 250;
        this.setLayoutParams(lp3);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.parseColor("#6A5ACD"));
        drawable.setStroke(3, Color.parseColor("#6A5ACD"),0,0);
        drawable.setCornerRadius(5);
        this.setBackground(drawable);
    }

    public void changeState(View v) {
        switch (this.state) {
            case 0:{
                activate(v);
                break;
            }
            case 1:{
                close(v);
                break;
            }
        }
    }

    public void activate(View v) {
        if (null != mCellImage)
            mCellImage.setVisibility(View.VISIBLE);
        GradientDrawable drawable = (GradientDrawable) v.getBackground();
        drawable.setColor(Color.parseColor("#FFFFFF"));
        drawable.setStroke(3, Color.parseColor("#111111"),5,1);
        v.setBackground(drawable);
        this.state = 1;
    }

    public void close(View v) {
        if (null != mCellImage)
            mCellImage.setVisibility(View.INVISIBLE);
        GradientDrawable drawable = (GradientDrawable) v.getBackground();
        drawable.setColor(Color.parseColor("#6A5ACD"));
        drawable.setStroke(3, Color.parseColor("#6A5ACD"),0,0);
        v.setBackground(drawable);
        this.state = 0;
    }
}
