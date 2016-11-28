package com.xing.android.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xing.android.calendar.R;

/**
 * Created by zxx09506 on 2016/11/16.
 */

public class CalendarHomeDemoActivity extends Activity implements View.OnClickListener {

    private Button mSingleWeekView;
    private Button mWeekListView;
    private Button mMonthListView;
    private Button mYearListView;
    private Button mMultiView;
    private Button mVPageWeekCalendarView;
    private Button mHPageMonthCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity_home_demo);

        mSingleWeekView = (Button) findViewById(R.id.btn_single_week);
        mWeekListView = (Button) findViewById(R.id.btn_week_list);
        mMonthListView = (Button) findViewById(R.id.btn_month_list);
        mYearListView = (Button) findViewById(R.id.btn_year_list);
        mMultiView = (Button) findViewById(R.id.btn_multi_view);
        mVPageWeekCalendarView = (Button) findViewById(R.id.btn_v_page_week_calendar);
        mHPageMonthCalendarView = (Button) findViewById(R.id.btn_h_page_month_calendar);

        mSingleWeekView.setOnClickListener(this);
        mWeekListView.setOnClickListener(this);
        mMonthListView.setOnClickListener(this);
        mYearListView.setOnClickListener(this);
        mMultiView.setOnClickListener(this);
        mVPageWeekCalendarView.setOnClickListener(this);
        mHPageMonthCalendarView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        if(v == mSingleWeekView) {
            intent.setClass(this, SingleWeekActivity.class);
        } else if(v == mWeekListView) {
            intent.setClass(this, WeekListActivity.class);
        } else if(v == mMonthListView) {
            intent.setClass(this, MonthListActivity.class);
        } else if(v == mYearListView) {
            intent.setClass(this, YearListActivity.class);
        } else if(v == mMultiView) {
            intent.setClass(this, MultiViewActivity.class);
        } else if(v == mVPageWeekCalendarView) {
            intent.setClass(this, VPageWeekCalendarActivity.class);
        } else if(v == mHPageMonthCalendarView) {
            intent.setClass(this, HPageMonthCalendarActivity.class);
        }
        startActivity(intent);
    }
}
