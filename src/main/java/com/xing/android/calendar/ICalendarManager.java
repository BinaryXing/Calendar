package com.xing.android.calendar;

import com.xing.android.calendar.model.ContinuousSelectItem;
import com.xing.android.calendar.model.DayCell;
import com.xing.android.calendar.process.DayCellClickPolicyInfo;
import com.xing.android.calendar.process.DayCellClickPolicyInfo.*;
import com.xing.android.calendar.view.ICalendarView;

import java.util.List;

/**
 * Created by zhaoxx on 16/3/10.
 */
public interface ICalendarManager<T> {
    int getSelectMode();
    void setSelectMode(int mode);
    int getFirstDayOfWeek();
    void setFirstDayOfWeek(int firstDayOfWeek);
    void setSinglePolicy(ClickPolicyForSingle<T> policy);
    void setMultiPolicy(ClickPolicyForMulti<T> policy);
    void setContinuousPolicy(ClickPolicyForContinuous<T> policy);
    void setContinuousMultiPolicy(ClickPolicyForContinuousMulti<T> policy);
    void setMixPolicy(ClickPolicyForMix<T> policy);
    void setMixMultiPolicy(ClickPolicyForMixMulti<T> policy);
    List<ICalendarView<T>> getCalendarViewList();
    void addCalendarView(ICalendarView calendarView);
    void addCalendarViewList(List<ICalendarView<T>> calendarViewList);
    void setCalendarViewList(List<ICalendarView<T>> calendarViewList);
    void refreshICalendarViewList();
    void refreshAffectViewList(DayCell<T> afterCell, DayCell<T> beforeCell);
    void refreshAffectViewList(ContinuousSelectItem<T> afterItem, ContinuousSelectItem<T> beforeItem);
    DayCell<T> getSelectedDayCell();
    void setSelectedDaycell(DayCell<T> cell, boolean notifyView);
    List<DayCell<T>> getSelectedDayCellList();
    void setSelectedDayCellList(List<DayCell<T>> cellList, boolean notifyView);
    ContinuousSelectItem<T> getSelectedContinuousItem();
    void setSelectedContinuousItem(ContinuousSelectItem<T> item, boolean notifyView);
    List<ContinuousSelectItem<T>> getSelectedContinuousItemList();
    void setSelectedContinuousItemList(List<ContinuousSelectItem<T>> itemList, boolean notifyView);
    void setData(List<DayCell<T>> dataList);
    void onDayCellClick(DayCell<T> dayCell);
    void onBindData(DayCell<T> dayCell);
    void iterator();
    void onIterator(DayCell<T> dayCell);
    void setICalendarManagerListener(ICalendarManagerListener<T> listener);

    interface ICalendarManagerListener<T> {
        boolean blockDayCellClick(DayCell<T> cell);
        void onPostDayCellClick(DayCell<T> cell);
        void onBindData(DayCell<T> dayCell);
        void onIterator(DayCell<T> dayCell);
    }
}
