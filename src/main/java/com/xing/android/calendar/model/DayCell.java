package com.xing.android.calendar.model;

import com.xing.android.calendar.CalendarConstant;
import com.xing.android.calendar.util.LogUtil;

import java.util.Calendar;

/**
 * 具体某一天
 * Created by zhaoxx on 16/3/9.
 */
public class DayCell<T> {

    protected final String LOG_TAG = this.getClass().getSimpleName();

    protected int mYear;
    protected int mMonth;
    protected int mDay;
    protected int mWeekDay;
    protected T mData;
    protected int mDayType;
    protected int mDayStatus = CalendarConstant.DAY_STATUS_UNSELECTED;

    public DayCell(int year, int month, int day) {
        mYear = year;
        mMonth = month;
        mDay = day;
        setWeekDay();
    }

    public void set(int year, int month, int day) {
        if(mYear == year && mMonth == month && mDay == day) {
            LogUtil.i(LOG_TAG, "set:equal data");
            return;
        }
        mYear = year;
        mMonth = month;
        mDay = day;
        setWeekDay();
    }

    public int getYear() {
        return mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getDay() {
        return mDay;
    }

    public int getWeekDay() {
        return mWeekDay;
    }

    public T getData() {
        return mData;
    }

    public void setData(T data) {
        mData = data;
    }

    public int getDayType() {
        return mDayType;
    }

    public void setDayType(int dayType) {
        mDayType = dayType;
    }

    public int getDayStatus() {
        return mDayStatus;
    }

    public void setDayStatus(int status) {
        mDayStatus = status;
    }

    public DayCell<T> getCopyDayCell() {
        DayCell<T> result = new DayCell<T>(mYear, mMonth, mDay);
        result.setData(mData);
        result.setDayType(mDayType);
        result.setDayStatus(mDayStatus);
        return result;
    }

    private void setWeekDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(mYear, mMonth - 1, mDay);
        mWeekDay = calendar.get(Calendar.DAY_OF_WEEK);
    }

    @Override
    public String toString() {
        return "" + mYear + "年" + mMonth + "月" + mDay + "日";
    }
}
