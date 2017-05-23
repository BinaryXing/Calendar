package com.xing.android.calendar.model;

import com.xing.android.calendar.CalendarConstant;
import com.xing.android.calendar.CalendarTool;
import com.xing.android.calendar.util.LogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 根据某年某周生成该周的数据，需要配合{@link #mFirstDayOfWeek}
 * Created by zhaoxx on 16/3/14.
 */
public class YearWeekCell<T> implements IWeekCell<T> {

    protected final String LOG_TAG = this.getClass().getSimpleName();

    protected int mYear;
    protected int mWeek;

    protected int mFirstDayOfWeek = Calendar.SUNDAY;

    protected List<DayCell<T>> mDayCellList = new ArrayList<DayCell<T>>();

    /**
     * 创建对象时，确保年周的有效性（需要配合{@link #mFirstDayOfWeek}）
     * @param year
     * @param week
     */
    public YearWeekCell(int year, int week) {
        set(year, week);
    }

    /**
     * 创建对象时，确保年周的有效性（需要配合{@link #mFirstDayOfWeek}）
     * @param year
     * @param week
     * @param firstDayOfWeek
     */
    public YearWeekCell(int year, int week, int firstDayOfWeek) {
        set(year, week, firstDayOfWeek);
    }

    protected void init() {
        mDayCellList.clear();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.clear();
        startCalendar.setFirstDayOfWeek(mFirstDayOfWeek);
        startCalendar.set(Calendar.YEAR, mYear);
        startCalendar.set(Calendar.WEEK_OF_YEAR, mWeek);
        startCalendar.set(Calendar.DAY_OF_WEEK, mFirstDayOfWeek);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.clear();
        endCalendar.setFirstDayOfWeek(mFirstDayOfWeek);
        endCalendar.set(Calendar.YEAR, mYear);
        endCalendar.set(Calendar.WEEK_OF_YEAR, mWeek);
        endCalendar.set(Calendar.DAY_OF_WEEK, CalendarTool.getLastDayOfWeek(mFirstDayOfWeek));
        while (!startCalendar.after(endCalendar)) {
            DayCell<T> cell = new DayCell<T>(startCalendar.get(Calendar.YEAR), (startCalendar.get(Calendar.MONTH) + 1), startCalendar.get(Calendar.DATE));
            cell.setDayType(CalendarConstant.DAY_TYPE_CURRENT_YEAR);
            if(cell.getYear() < mYear) {
                cell.setDayType(CalendarConstant.DAY_TYPE_LAST_YEAR);
            } else if(cell.getYear() > mYear) {
                cell.setDayType(CalendarConstant.DAY_TYPE_NEXT_YEAR);
            }
            mDayCellList.add(cell);
            startCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    /**
     * 调用者，确保年周的有效性（需要配合{@link #mFirstDayOfWeek}）
     * @param year
     * @param week
     */
    public void set(int year, int week) {
        set(year, week, mFirstDayOfWeek);
    }

    /**
     * 调用者，确保年周的有效性（需要配合firstDayOfWeek）
     * @param year
     * @param week
     * @param firstDayOfWeek
     */
    public void set(int year, int week, int firstDayOfWeek) {
        firstDayOfWeek = CalendarTool.getValidFirstDayOfWeek(firstDayOfWeek);
        if (mYear == year && mWeek == week && mFirstDayOfWeek == firstDayOfWeek) {
            LogUtil.i(LOG_TAG, "set:equal data");
            return;
        }
        if (CalendarTool.checkValidOfWeek(firstDayOfWeek, year, week)) {
            mFirstDayOfWeek = firstDayOfWeek;
            mYear = year;
            mWeek = week;
            init();
        } else {
            LogUtil.w(LOG_TAG, "set:invalid data,year = " + year + ",week = " + week);
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
            set(mYear, mWeek, firstDayOfWeek);
        }
    }

    public int getYear() {
        return mYear;
    }

    public int getWeek() {
        return mWeek;
    }
}
