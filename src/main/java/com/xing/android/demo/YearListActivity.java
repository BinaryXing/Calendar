package com.xing.android.demo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xing.android.calendar.CalendarManager;
import com.xing.android.calendar.R;
import com.xing.android.calendar.util.ToastUtil;
import com.xing.android.calendar.view.YearListView;

/**
 * Created by zxx09506 on 2016/11/17.
 */

public class YearListActivity extends BaseCommonCalendarActivity {

    private EditText mYearView;
    private EditText mYearCountView;
    private Button mOkView;

    private YearListView<Void> mYearListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity_year_list);

        initCommon();
        mYearView = (EditText) findViewById(R.id.ev_year);
        mYearCountView = (EditText) findViewById(R.id.ev_year_count);
        mOkView = (Button) findViewById(R.id.btn_ok);

        mYearListView = (YearListView<Void>) findViewById(R.id.v_year_list);

        mOkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyData();
            }
        });

        mYearListView.setListener(DEFAULT_YEAR_LIST_LISTENER, DEFAULT_WEEK_DAY_LISTENER, DEFAULT_WEEK_VIEW_LISTENER);
        mYearListView.setShowWeekDay(true);
        mYearListView.setShowYearHeader(true);
        mYearListView.setShowYearFooter(true);

        mCalendarManager = new CalendarManager<>();
        mCalendarManager.addCalendarView(mYearListView);
        mCalendarManager.setICalendarManagerListener(DEFAULT_CALENDAR_MANAGER_LISTENER);
    }

    private void applyData() {
        int year = 0;
        int yearCount = 0;
        if(!TextUtils.isEmpty(mYearView.getText())) {
            try {
                year = Integer.valueOf(mYearView.getText().toString());
            } catch (Exception e) {
                ToastUtil.showShortToast(this, "年份格式不对，请重新输入（纯数字）");
            }
        } else {
            ToastUtil.showShortToast(this, "请输入年份（纯数字）");
        }
        if(!TextUtils.isEmpty(mYearCountView.getText())) {
            try {
                yearCount = Integer.valueOf(mYearCountView.getText().toString());
            } catch (Exception e) {
                ToastUtil.showShortToast(this, "年数格式不对，请重新输入（纯数字）");
            }
        } else {
            ToastUtil.showShortToast(this, "请输入年数（纯数字）");
        }
        mYearListView.set(year, yearCount);
        mCalendarManager.iterator();
    }
}
