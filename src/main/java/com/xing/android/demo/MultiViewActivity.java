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
import com.xing.android.calendar.view.WeekDayView;
import com.xing.android.calendar.view.WeekListView;
import com.xing.android.calendar.view.WeekView;
import com.xing.android.calendar.view.YearListView;

/**
 * Created by zxx09506 on 2016/11/17.
 */

public class MultiViewActivity extends BaseCommonCalendarActivity {

    private EditText mYearView;
    private EditText mMonthView;
    private EditText mDayView;
    private Button mOkView;

    private WeekDayView mWeekDayView;
    private WeekView<Void> mWeekView;
    private WeekListView<Void> mWeekListView;
    private MonthListView<Void> mMonthListView;
    private YearListView<Void> mYearListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity_multi_view);

        initCommon();
        mYearView = (EditText) findViewById(R.id.ev_year);
        mMonthView = (EditText) findViewById(R.id.ev_month);
        mDayView = (EditText) findViewById(R.id.ev_day);
        mOkView = (Button) findViewById(R.id.btn_ok);

        mWeekDayView = (WeekDayView) findViewById(R.id.v_week_day);
        mWeekView = (WeekView<Void>) findViewById(R.id.v_week);
        mWeekListView = (WeekListView<Void>) findViewById(R.id.v_week_list);
        mMonthListView = (MonthListView<Void>) findViewById(R.id.v_month_list);
        mYearListView = (YearListView<Void>) findViewById(R.id.v_year_list);

        mOkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyData();
            }
        });

        mWeekDayView.setDayOfWeekCellListener(DEFAULT_WEEK_DAY_LISTENER);
        mWeekView.setWeekViewListener(DEFAULT_WEEK_VIEW_LISTENER);
        mWeekListView.setListener(DEFAULT_WEEK_DAY_LISTENER, DEFAULT_WEEK_VIEW_LISTENER);
        mWeekListView.setShowWeekDay(true);
        mMonthListView.setListener(DEFAULT_MONTH_LIST_LISTENER, DEFAULT_WEEK_DAY_LISTENER, DEFAULT_WEEK_VIEW_LISTENER);
        mMonthListView.setShowWeekDay(true);
        mMonthListView.setShowMonthHeader(true);
        mMonthListView.setShowMonthFooter(true);
        mYearListView.setListener(DEFAULT_YEAR_LIST_LISTENER, DEFAULT_WEEK_DAY_LISTENER, DEFAULT_WEEK_VIEW_LISTENER);
        mYearListView.setShowWeekDay(true);
        mYearListView.setShowYearHeader(true);
        mYearListView.setShowYearFooter(true);

        mCalendarManager = new CalendarManager<>();
        mCalendarManager.addCalendarView(mWeekView);
        mCalendarManager.addCalendarView(mWeekListView);
        mCalendarManager.addCalendarView(mMonthListView);
        mCalendarManager.addCalendarView(mYearListView);
        mCalendarManager.setICalendarManagerListener(DEFAULT_CALENDAR_MANAGER_LISTENER);
    }

    private void applyData() {
        int year = 0;
        int month = 0;
        int day = 0;
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
        mWeekView.setSingleWeek(year, month, day, true);
        mWeekListView.set(year, month, day, 1);
        mMonthListView.set(year, month, 1);
        mYearListView.set(year, 1);
        mCalendarManager.iterator();
    }

    @Override
    protected void onFirstDayOfWeekChecked(int checkedId) {
        super.onFirstDayOfWeekChecked(checkedId);
        if(mWeekDayView != null && mCalendarManager != null){
            mWeekDayView.setFirstDayOfWeek(mCalendarManager.getFirstDayOfWeek());
        }
    }
}
