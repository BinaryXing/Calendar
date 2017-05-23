package com.xing.android.calendar.view;

import com.xing.android.calendar.ICalendarManager;
import com.xing.android.calendar.process.ICalendarViewProcessor;

/**
 * Created by zhaoxx on 16/3/14.
 */
public interface ICalendarView<T> extends ICalendarViewProcessor<T> {
    /**
     * 刷新CalendarView
     */
    void refresh();

    /**
     * 设置CalendarManager
     * @param calendarManager
     * @param refresh 是否需要刷新View，如果有多个setXXX(Obj, refresh)连续调用,只需要最后一个refresh为true就可以来
     */
    void setCalendarManager(ICalendarManager<T> calendarManager, boolean refresh);

    /**
     * 获取CalendarManager
     * @return
     */
    ICalendarManager<T> getCalendarManager();

    /**
     * 设置第一天
     * @param firstDayOfWeek
     * @param refresh 是否需要刷新View
     */
    void setFirstDayOfWeek(int firstDayOfWeek, boolean refresh);

    /**
     * 获取第一天
     * @return
     */
    int getFirstDayOfWeek();

    /**
     * foreach该CalendarView中的每一个DayCell
     */
    void iterator();
}