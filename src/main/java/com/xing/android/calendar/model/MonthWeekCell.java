package com.xing.android.calendar.model;

import com.xing.android.calendar.CalendarConstant;
import com.xing.android.calendar.CalendarTool;
import com.xing.android.calendar.util.LogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by zhaoxx on 16/3/11.
 */
public class MonthWeekCell<T> implements IWeekCell<T> {

    protected final String LOG_TAG = this.getClass().getSimpleName();

    protected int mYear;
    protected int mMonth;
    protected int mWeek;

    protected int mFirstDayOfWeek = Calendar.SUNDAY;

    protected List<DayCell<T>> mDayCellList = new ArrayList<DayCell<T>>();

    public MonthWeekCell(int year, int month, int week) {
        set(year, month, week);
    }

    public MonthWeekCell(int year, int month, int week, int firstDayOfWeek) {
        set(year, month, week, firstDayOfWeek);
    }

    protected void init() {
        mDayCellList.clear();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.clear();
        startCalendar.setFirstDayOfWeek(mFirstDayOfWeek);
        startCalendar.set(Calendar.YEAR, mYear);
        startCalendar.set(Calendar.MONTH, mMonth - 1);
        startCalendar.set(Calendar.WEEK_OF_MONTH, mWeek);
        startCalendar.set(Calendar.DAY_OF_WEEK, mFirstDayOfWeek);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.clear();
        endCalendar.setFirstDayOfWeek(mFirstDayOfWeek);
        endCalendar.set(Calendar.YEAR, mYear);
        endCalendar.set(Calendar.MONTH, mMonth - 1);
        endCalendar.set(Calendar.WEEK_OF_MONTH, mWeek);
        endCalendar.set(Calendar.DAY_OF_WEEK, CalendarTool.getLastDayOfWeek(mFirstDayOfWeek));
        while (!startCalendar.after(endCalendar)) {
            DayCell<T> cell = new DayCell<T>(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH) + 1, startCalendar.get(Calendar.DATE));
            cell.setDayType(CalendarConstant.DAY_TYPE_CURRENT_MONTH);
            if(cell.getYear() < mYear) {
                cell.setDayType(CalendarConstant.DAY_TYPE_LAST_MONTH);
            } else if(cell.getMonth() < mMonth) {
                cell.setDayType(CalendarConstant.DAY_TYPE_LAST_MONTH);
            } else if(cell.getYear() > mYear) {
                cell.setDayType(CalendarConstant.DAY_TYPE_NEXT_MONTH);
            } else if(cell.getMonth() > mMonth) {
                cell.setDayType(CalendarConstant.DAY_TYPE_NEXT_MONTH);
            }
            mDayCellList.add(cell);
            startCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    public void set(int year, int month, int week) {
        set(year, month, week, mFirstDayOfWeek);
    }

    public void set(int year, int month, int week, int firstDayOfWeek) {
        if (mYear == year && mMonth == month && mWeek == week && mFirstDayOfWeek == firstDayOfWeek) {
            LogUtil.i(LOG_TAG, "set:equal data");
            return;
        }
        firstDayOfWeek = CalendarTool.getValidFirstDayOfWeek(firstDayOfWeek);
        if (CalendarTool.checkValidOfWeek(firstDayOfWeek, year, month, week)) {
            mFirstDayOfWeek = firstDayOfWeek;
            mYear = year;
            mMonth = month;
            mWeek = week;
            init();
        } else {
            LogUtil.w(LOG_TAG, "set:invalid data,year = " + year + ",month = " + month + ",week = " + week);
        }
    }

    @Override
    public List<DayCell<T>> getDayCellList() {
        return mDayCellList;
    }

    @Override
    public int getFirstDayOfWeek() {
        return mFirstDayOfWeek;
    }

    @Override
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        firstDayOfWeek = CalendarTool.getValidFirstDayOfWeek(firstDayOfWeek);
        if(mFirstDayOfWeek != firstDayOfWeek) {
            set(mYear, mMonth, mWeek, firstDayOfWeek);
        }
    }

    public int getYear() {
        return mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getWeek() {
        return mWeek;
    }

}
