package com.xing.android.calendar.model;

import com.xing.android.calendar.CalendarConstant;
import com.xing.android.calendar.CalendarTool;
import com.xing.android.calendar.util.LogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 根据指定的某一天，生成该天所在的一周数据，需要配合{@link #mFirstDayOfWeek}
 * Created by zhaoxx on 16/3/11.
 */
public class WeekCell<T> implements IWeekCell<T> {

    protected final String LOG_TAG = this.getClass().getSimpleName();

    protected int mYear;
    protected int mMonth;
    protected int mDay;
    protected int mFirstDayOfWeek = Calendar.SUNDAY;

    protected List<DayCell<T>> mDayCellList = new ArrayList<DayCell<T>>();

    /**
     * 创建对象时，需要确保年月日的有效性(需要配合{@link #mFirstDayOfWeek})
     * @param year
     * @param month
     * @param day
     */
    public WeekCell(int year, int month, int day) {
        set(year, month, day);
    }

    /**
     * 创建对象时，需要确保年月日的有效性(需要配合firstDayOfWeek)
     * @param year
     * @param month
     * @param day
     * @param firstDayOfWeek
     */
    public WeekCell(int year, int month, int day, int firstDayOfWeek) {
        set(year, month, day, firstDayOfWeek);
    }

    protected void init() {
        mDayCellList.clear();
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setFirstDayOfWeek(mFirstDayOfWeek);
        calendar.set(mYear, mMonth - 1, mDay);
        while (calendar.get(Calendar.DAY_OF_WEEK) != mFirstDayOfWeek) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        do {
            DayCell<T> cell = new DayCell<T>(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1), calendar.get(Calendar.DATE));
            cell.setDayType(CalendarConstant.DAY_TYPE_CURRENT_WEEK);
            mDayCellList.add(cell);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        } while (calendar.get(Calendar.DAY_OF_WEEK) != mFirstDayOfWeek);
    }

    /**
     * 调用时，需要确保年月日的有效性(需要配合{@link #mFirstDayOfWeek})
     * @param year
     * @param month
     * @param day
     */
    public void set(int year, int month, int day) {
        set(year, month, day, mFirstDayOfWeek);
    }

    /**
     * 调用时，需要确保年月日的有效性(需要配合{@link #mFirstDayOfWeek})
     * @param year
     * @param month
     * @param day
     * @param firstDayOfWeek
     */
    public void set(int year, int month, int day, int firstDayOfWeek) {
        firstDayOfWeek = CalendarTool.getValidFirstDayOfWeek(firstDayOfWeek);
        if(mYear == year && mMonth == month && day == mDay && mFirstDayOfWeek == firstDayOfWeek) {
            LogUtil.i(LOG_TAG, "set:equal data");
            return;
        }
        if(CalendarTool.checkValidOfDay(firstDayOfWeek, year, month, day)) {
            mFirstDayOfWeek = firstDayOfWeek;
            mYear = year;
            mMonth = month;
            mDay = day;
            init();
        } else {
            LogUtil.w(LOG_TAG, "set:invalid data,year = " + year + ",month = " + month + ",day = " + day);
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
            set(mYear, mMonth, mDay, firstDayOfWeek);
        }
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

}
