package com.xing.android.calendar.process;

import com.xing.android.calendar.model.ContinuousSelectItem;
import com.xing.android.calendar.model.DayCell;

import java.util.List;

/**
 * ICalendarView的数据处理
 * Created by zxx09506 on 2016/11/3.
 */

public interface ICalendarViewProcessor<T> {
    /**
     * 用户操作是否影响该ICalendarView
     * @param dayCell 操作后的Cell
     * @param originDayCell 操作之前的Cell
     * @return 用户操作是否影响该ICalendarView
     */
    boolean isAffect(DayCell<T> dayCell, DayCell<T> originDayCell);

    /**
     * 用户操作是否影响该ICalendarView
     * @param item 操作后的ContinuousSelectItem
     * @param originItem 操作前的ContinuousSelectItem
     * @return 用户操作是否影响该ICalendarView
     */
    boolean isAffect(ContinuousSelectItem<T> item, ContinuousSelectItem<T> originItem);

    /**
     * 操作之后的回调
     * @param dayCell 操作之后的selectCell
     * @param refresh 是否需要刷新UI
     */
    void onDayCellChanged(DayCell<T> dayCell, boolean refresh);

    /**
     * 操作之后的回调
     * @param dayCellList 操作之后的selectCellList
     * @param refresh 是否需要刷新UI
     */
    void onDayCellListChanged(List<DayCell<T>> dayCellList, boolean refresh);

    /**
     * 操作之后的回调
     * @param item 操作之后的continuousItem
     * @param refresh 是否需要刷新UI
     */
    void onContinuousItemChanged(ContinuousSelectItem<T> item, boolean refresh);

    /**
     * 操作之后的回调
     * @param itemList 操作之后的continuousItemList
     * @param refresh 是否需要刷新
     */
    void onContinuousItemListChanges(List<ContinuousSelectItem<T>> itemList, boolean refresh);

    /**
     * 设置DayCell里的T数据
     * @param dataList
     */
    void setData(List<DayCell<T>> dataList);
}
