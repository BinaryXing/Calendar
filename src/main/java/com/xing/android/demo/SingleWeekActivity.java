package com.xing.android.demo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xing.android.calendar.CalendarManager;
import com.xing.android.calendar.R;
import com.xing.android.calendar.util.ToastUtil;
import com.xing.android.calendar.view.WeekDayView;
import com.xing.android.calendar.view.WeekView;

/**
 * Created by zxx09506 on 2016/11/16.
 */

public class SingleWeekActivity extends BaseCommonCalendarActivity {

    private EditText mSingleWeekYearView;
    private EditText mSingleWeekMonthView;
    private EditText mSingleWeekDayView;
    private Button mSingleWeekOkView;

    private EditText mMonthWeekYearView;
    private EditText mMonthWeekMonthView;
    private EditText mMonthWeekWeekView;
    private Button mMonthWeekOkView;

    private EditText mYearWeekYearView;
    private EditText mYearWeekWeekView;
    private Button mYearWeekOkView;

    private WeekDayView mWeekDayView;
    private WeekView<Void> mWeekView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity_single_week);

        initCommon();
        mSingleWeekYearView = (EditText) findViewById(R.id.ev_single_week_year);
        mSingleWeekMonthView = (EditText) findViewById(R.id.ev_single_week_month);
        mSingleWeekDayView = (EditText) findViewById(R.id.ev_single_week_day);
        mSingleWeekOkView = (Button) findViewById(R.id.btn_single_week_ok);

        mMonthWeekYearView = (EditText) findViewById(R.id.ev_month_week_year);
        mMonthWeekMonthView = (EditText) findViewById(R.id.ev_month_week_month);
        mMonthWeekWeekView = (EditText) findViewById(R.id.ev_month_week_week);
        mMonthWeekOkView = (Button) findViewById(R.id.btn_month_week_ok);

        mYearWeekYearView = (EditText) findViewById(R.id.ev_year_week_year);
        mYearWeekWeekView = (EditText) findViewById(R.id.ev_year_week_week);
        mYearWeekOkView = (Button) findViewById(R.id.btn_year_week_ok);

        mWeekView = (WeekView<Void>) findViewById(R.id.v_week);
        mWeekDayView = (WeekDayView) findViewById(R.id.v_week_day);

        mSingleWeekOkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applySingleWeekData();
            }
        });
        mMonthWeekOkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyMonthWeekData();
            }
        });
        mYearWeekOkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyYearWeekData();
            }
        });

        mWeekDayView.setDayOfWeekCellListener(DEFAULT_WEEK_DAY_LISTENER, true);
        mWeekView.setWeekViewListener(DEFAULT_WEEK_VIEW_LISTENER, true);

        mCalendarManager = new CalendarManager();
        mCalendarManager.addCalendarView(mWeekView);
        mCalendarManager.setICalendarManagerListener(DEFAULT_CALENDAR_MANAGER_LISTENER);
    }

    private void applySingleWeekData() {
        int year = 0;
        int month = 0;
        int day = 0;
        if(!TextUtils.isEmpty(mSingleWeekYearView.getText())) {
            try {
                year = Integer.valueOf(mSingleWeekYearView.getText().toString());
            } catch (Exception e) {
                ToastUtil.showShortToast(this, "年份格式不对，请重新输入（纯数字）");
            }
        } else {
            ToastUtil.showShortToast(this, "请输入年份（纯数字）");
        }
        if(!TextUtils.isEmpty(mSingleWeekMonthView.getText())) {
            try {
                month = Integer.valueOf(mSingleWeekMonthView.getText().toString());
            } catch (Exception e) {
                ToastUtil.showShortToast(this, "月份格式不对，请重新输入（纯数字）");
            }
        } else {
            ToastUtil.showShortToast(this, "请输入月份（纯数字）");
        }
        if(!TextUtils.isEmpty(mSingleWeekDayView.getText())) {
            try {
                day = Integer.valueOf(mSingleWeekDayView.getText().toString());
            } catch (Exception e) {
                ToastUtil.showShortToast(this, "日期格式不对，请重新输入（纯数字）");
            }
        } else {
            ToastUtil.showShortToast(this, "请输入日期（纯数字）");
        }
        mWeekView.setSingleWeek(year, month, day, true);
        mCalendarManager.iterator(true);
    }

    private void applyMonthWeekData() {
        int year = 0;
        int month = 0;
        int week = 0;
        if(!TextUtils.isEmpty(mMonthWeekYearView.getText())) {
            try {
                year = Integer.valueOf(mMonthWeekYearView.getText().toString());
            } catch (Exception e) {
                ToastUtil.showShortToast(this, "年份格式不对，请重新输入（纯数字）");
            }
        } else {
            ToastUtil.showShortToast(this, "请输入年份（纯数字）");
        }
        if(!TextUtils.isEmpty(mMonthWeekMonthView.getText())) {
            try {
                month = Integer.valueOf(mMonthWeekMonthView.getText().toString());
            } catch (Exception e) {
                ToastUtil.showShortToast(this, "月份格式不对，请重新输入（纯数字）");
            }
        } else {
            ToastUtil.showShortToast(this, "请输入月份（纯数字）");
        }
        if(!TextUtils.isEmpty(mMonthWeekWeekView.getText())) {
            try {
                week = Integer.valueOf(mMonthWeekWeekView.getText().toString());
            } catch (Exception e) {
                ToastUtil.showShortToast(this, "第几周格式不对，请重新输入（纯数字）");
            }
        } else {
            ToastUtil.showShortToast(this, "请输入第几周（纯数字）");
        }
        mWeekView.setMonthWeek(year, month, week, true);
        mCalendarManager.iterator(true);
    }

    private void applyYearWeekData() {
        int year = 0;
        int week = 0;
        if(!TextUtils.isEmpty(mYearWeekYearView.getText())) {
            try {
                year = Integer.valueOf(mYearWeekYearView.getText().toString());
            } catch (Exception e) {
                ToastUtil.showShortToast(this, "年份格式不对，请重新输入（纯数字）");
            }
        } else {
            ToastUtil.showShortToast(this, "请输入年份（纯数字）");
        }
        if(!TextUtils.isEmpty(mYearWeekWeekView.getText())) {
            try {
                week = Integer.valueOf(mYearWeekWeekView.getText().toString());
            } catch (Exception e) {
                ToastUtil.showShortToast(this, "第几周格式不对，请重新输入（纯数字）");
            }
        } else {
            ToastUtil.showShortToast(this, "请输入第几周（纯数字）");
        }
        mWeekView.setYearWeek(year, week, true);
        mCalendarManager.iterator(true);
    }

    @Override
    protected void onFirstDayOfWeekChecked(int checkedId) {
        super.onFirstDayOfWeekChecked(checkedId);
        if(mWeekDayView != null && mCalendarManager != null){
            mWeekDayView.setFirstDayOfWeek(mCalendarManager.getFirstDayOfWeek(), true);
        }
    }
}
