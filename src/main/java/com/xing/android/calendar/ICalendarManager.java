package com.xing.android.calendar;

import com.xing.android.calendar.model.ContinuousSelectItem;
import com.xing.android.calendar.model.DayCell;
import com.xing.android.calendar.process.DayCellHandlePolicyInfo.*;
import com.xing.android.calendar.process.DayCellUserInterfaceInfo;
import com.xing.android.calendar.view.ICalendarView;

import java.util.List;

/**
 * Created by zhaoxx on 16/3/10.
 */
public interface ICalendarManager<T> {

    //选择事件（暂时只支持Click）分发
    void onDayCellHandle(DayCell<T> dayCell);

    //View相关
    List<ICalendarView<T>> getCalendarViewList();
    void addCalendarView(ICalendarView calendarView);
    void addCalendarViewList(List<ICalendarView<T>> calendarViewList);
    void setCalendarViewList(List<ICalendarView<T>> calendarViewList);
    void refreshICalendarViewList();
    void refreshAffectViewList(DayCell<T> afterCell, DayCell<T> beforeCell);
    void refreshAffectViewList(ContinuousSelectItem<T> afterItem, ContinuousSelectItem<T> beforeItem);

    //Select相关
    int getSelectMode();
    void setSelectMode(int mode);
    DayCell<T> getSelectedDayCell();
    void setSelectedDaycell(DayCell<T> cell, boolean notifyView);
    List<DayCell<T>> getSelectedDayCellList();
    void setSelectedDayCellList(List<DayCell<T>> cellList, boolean notifyView);
    ContinuousSelectItem<T> getSelectedContinuousItem();
    void setSelectedContinuousItem(ContinuousSelectItem<T> item, boolean notifyView);
    List<ContinuousSelectItem<T>> getSelectedContinuousItemList();

    //日期选择处理策略相关
    void setSinglePolicy(PolicyForSingle<T> policy);
    void setSelectedContinuousItemList(List<ContinuousSelectItem<T>> itemList, boolean notifyView);
    void setMultiPolicy(PolicyForMulti<T> policy);
    void setContinuousPolicy(PolicyForContinuous<T> policy);
    void setContinuousMultiPolicy(PolicyForContinuousMulti<T> policy);
    void setMixPolicy(PolicyForMix<T> policy);
    void setMixMultiPolicy(PolicyForMixMulti<T> policy);

    //日期选择交互相关（暂时只支持Click模式）
    DayCellUserInterfaceInfo<T> getDayCellUserInterfaceInfo();

    //一周中的第一天
    int getFirstDayOfWeek();
    void setFirstDayOfWeek(int firstDayOfWeek, boolean refresh);

    void setData(List<DayCell<T>> dataList, boolean keepOld, boolean refresh);
    void onBindData(DayCell<T> dayCell);
    void iterator(boolean refresh);
    void onIterator(DayCell<T> dayCell);

    void setDayCellUserInterfaceInfo(DayCellUserInterfaceInfo<T> info);
    void setICalendarManagerListener(ICalendarManagerListener<T> listener);

    interface ICalendarManagerListener<T> {
        boolean blockDayCellClick(DayCell<T> cell);
        void onPostDayCellClick(DayCell<T> cell);
        void onBindData(DayCell<T> dayCell);
        void onIterator(DayCell<T> dayCell);
    }
}
