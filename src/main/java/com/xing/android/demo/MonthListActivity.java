package com.xing.android.demo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xing.android.calendar.CalendarManager;
import com.xing.android.calendar.R;
import com.xing.android.calendar.util.ToastUtil;
import com.xing.android.calendar.view.MonthListView;

/**
 * Created by zxx09506 on 2016/11/17.
 */

public class MonthListActivity extends BaseCommonCalendarActivity {

    private EditText mYearView;
    private EditText mMonthView;
    private EditText mMonthCountView;
    private Button mOkView;

    private MonthListView<Void> mMonthListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity_month_list);

        initCommon();
        mYearView = (EditText) findViewById(R.id.ev_year);
        mMonthView = (EditText) findViewById(R.id.ev_month);
        mMonthCountView = (EditText) findViewById(R.id.ev_month_count);
        mOkView = (Button) findViewById(R.id.btn_ok);

        mMonthListView = (MonthListView<Void>) findViewById(R.id.v_month_list);

        mOkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyData();
            }
        });

        mMonthListView.setListener(DEFAULT_MONTH_LIST_LISTENER, DEFAULT_WEEK_DAY_LISTENER, DEFAULT_WEEK_VIEW_LISTENER);
        mMonthListView.setShowWeekDay(true);
        mMonthListView.setShowMonthHeader(true);
        mMonthListView.setShowMonthFooter(true);

        mCalendarManager = new CalendarManager<>();
        mCalendarManager.addCalendarView(mMonthListView);
        mCalendarManager.setICalendarManagerListener(DEFAULT_CALENDAR_MANAGER_LISTENER);
    }

    private void applyData() {
        int year = 0;
        int month = 0;
        int monthCount = 0;
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
        if(!TextUtils.isEmpty(mMonthCountView.getText())) {
            try {
                monthCount = Integer.valueOf(mMonthCountView.getText().toString());
            } catch (Exception e) {
                ToastUtil.showShortToast(this, "月数格式不对，请重新输入（纯数字）");
            }
        } else {
            ToastUtil.showShortToast(this, "请输入月数（纯数字）");
        }
        mMonthListView.set(year, month, monthCount);
        mCalendarManager.iterator();
    }
}
