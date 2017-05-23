package com.xing.android.calendar.model;

import com.xing.android.calendar.CalendarConstant;
import com.xing.android.calendar.util.LogUtil;

import java.util.Calendar;

/**
 * 某天
 * Created by zhaoxx on 16/3/9.
 */
public class DayCell<T> {

    protected final String LOG_TAG = this.getClass().getSimpleName();

    protected int mYear;
    protected int mMonth;
    protected int mDay;
    protected T mData;
    //该天的类型，具体请看CalendarConstant.DAY_TYPE_XXX
    protected int mDayType;
    //该天的选择状态
    protected int mDayStatus = CalendarConstant.DAY_STATUS_UNSELECTED;

    /**
     * 创建对象时,要确保年月日的数据有效性
     * @param year
     * @param month
     * @param day
     */
    public DayCell(int year, int month, int day) {
        set(year, month, day);
    }

    /**
     * 调用者需要确保年月日的数据有效性
     * @param year
     * @param month
     * @param day
     */
    public void set(int year, int month, int day) {
        mYear = year;
        mMonth = month;
        mDay = day;
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

    @Override
    public String toString() {
        return "" + mYear + "年" + mMonth + "月" + mDay + "日";
    }

}
