package com.xing.android.calendar.model;

import java.util.List;

/**
 * Created by zhaoxx on 16/3/14.
 */
public interface IWeekCell<T> {
    List<DayCell<T>> getDayCellList();
    int getFirstDayOfWeek();
    void setFirstDayOfWeek(int firstDayOfWeek);
}
