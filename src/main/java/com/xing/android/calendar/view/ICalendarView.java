package com.xing.android.calendar.view;

import com.xing.android.calendar.ICalendarManager;
import com.xing.android.calendar.process.ICalendarViewProcessor;

/**
 * Created by zhaoxx on 16/3/14.
 */
public interface ICalendarView<T> extends ICalendarViewProcessor<T> {
    void refresh();
    void setCalendarManager(ICalendarManager<T> calendarManager);
    void setFirstDayOfWeek(int firstDayOfWeek);
    void iterator();
}