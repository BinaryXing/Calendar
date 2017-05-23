package com.xing.android.demo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xing.android.calendar.CalendarManager;
import com.xing.android.calendar.R;
import com.xing.android.calendar.util.ToastUtil;
import com.xing.android.calendar.view.WeekListView;

/**
 * Created by zxx09506 on 2016/11/17.
 */

public class WeekListActivity extends BaseCommonCalendarActivity {

    private EditText mYearView;
    private EditText mMonthView;
    private EditText mDayView;
    private EditText mWeekCountView;
    private Button mOkView;

    private WeekListView<Void> mWeekListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity_week_list);

        initCommon();
        mYearView = (EditText) findViewById(R.id.ev_year);
        mMonthView = (EditText) findViewById(R.id.ev_month);
        mDayView = (EditText) findViewById(R.id.ev_day);
        mWeekCountView = (EditText) findViewById(R.id.ev_week_count);
        mOkView = (Button) findViewById(R.id.btn_ok);

        mWeekListView = (WeekListView<Void>) findViewById(R.id.v_week_list);

        mOkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyData();
            }
        });

        mWeekListView.setListener(DEFAULT_WEEK_DAY_LISTENER, DEFAULT_WEEK_VIEW_LISTENER, false);
        mWeekListView.setShowWeekDay(true, true);

        mCalendarManager = new CalendarManager();
        mCalendarManager.addCalendarView(mWeekListView);
        mCalendarManager.setICalendarManagerListener(DEFAULT_CALENDAR_MANAGER_LISTENER);
    }

    private void applyData() {
        int year = 0;
        int month = 0;
        int day = 0;
        int weekCount = 0;
        if(!TextUtils.isEmpty(mYearView.getText())) {
            try {
                year = Integer.valueOf(mYearView.getText().toString());
            } catch (Exception e) {
                ToastUtil.showShortToast(this, "年份格式不对，请重新输入（纯数字）");
            }
        } else {
            ToastUtil.showShortToast(this, "请输入年份（纯数字）");
        }
        if(!TextUtils.isEmpty(mMonthView.getText())) {
            try {
                month = Integer.valueOf(mMonthView.getText().toString());
            } catch (Exception e) {
                ToastUtil.showShortToast(this, "月份格式不对，请重新输入（纯数字）");
            }
        } else {
            ToastUtil.showShortToast(this, "请输入月份（纯数字）");
        }
        if(!TextUtils.isEmpty(mDayView.getText())) {
            try {
                day = Integer.valueOf(mDayView.getText().toString());
            } catch (Exception e) {
                ToastUtil.showShortToast(this, "日期格式不对，请重新输入（纯数字）");
            }
        } else {
            ToastUtil.showShortToast(this, "请输入日期（纯数字）");
        }
        if(!TextUtils.isEmpty(mWeekCountView.getText())) {
            try {
                weekCount = Integer.valueOf(mWeekCountView.getText().toString());
            } catch (Exception e) {
                ToastUtil.showShortToast(this, "周数格式不对，请重新输入（纯数字）");
            }
        } else {
            ToastUtil.showShortToast(this, "请输入周数（纯数字）");
        }
        mWeekListView.set(year, month, day, weekCount, true);
        mCalendarManager.iterator(true);
    }
}
