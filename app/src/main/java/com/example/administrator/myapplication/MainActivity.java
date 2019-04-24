package com.example.administrator.myapplication;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private HistoryLayout mHistoryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] data = {"nn","2","哈哈","nn","2","哈哈","nn","2","哈哈","历史是","nn","2","哈哈","nn","2","哈哈","nn","2","哈哈","历史是","nn","2","哈哈","nn","2","哈哈","nn","2","哈哈","历史是"};
        this.mHistoryView = (HistoryLayout) findViewById(R.id.history_layout);
        mHistoryView.initCellList(data);
        mHistoryView.setBackgroundColor(Color.parseColor("#DCDCDC"));
        mHistoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int childCount = mHistoryView.getChildCount();
                for(int i = 0; i < childCount; i++) {
                    View child = mHistoryView.getChildAt(i);
                    if (child instanceof HistoryCell) {
                        ((HistoryCell) child).close(child);
                    }
                }
            }
        });
        mHistoryView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int childCount = mHistoryView.getChildCount();
                for(int i = 0; i < childCount; i++) {
                    View child = mHistoryView.getChildAt(i);
                    if (child instanceof HistoryCell) {
                        ((HistoryCell) child).activate(child);
                    }
                }
                return true;
            }
        });
    }
}
