package com.xing.android.calendar.process;

import com.xing.android.calendar.CalendarConstant;
import com.xing.android.calendar.CalendarTool;
import com.xing.android.calendar.ICalendarManager;
import com.xing.android.calendar.model.ContinuousSelectItem;
import com.xing.android.calendar.model.DayCell;
import com.xing.android.calendar.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * CalendarView 数据处理，日历控件都是连续日期的数据处理可以用该类，内部持有{@link #mStartDayCell}和{@link #mEndDayCell}；
 * 比如{@link #isAffect(DayCell, DayCell)}是通过{@link #mStartDayCell}和{@link #mEndDayCell}计算的；
 * Created by zxx09506 on 2016/11/3.
 */

public class CalendarViewProcessorImp<T> implements ICalendarViewProcessor<T> {

    protected final String LOG_TAG = this.getClass().getSimpleName();

    protected ICalendarManager<T> mCalendarManager;

    protected DayCell<T> mStartDayCell;
    protected DayCell<T> mEndDayCell;

    protected List<DayCell<T>> mDayCellList = new ArrayList<DayCell<T>>();

    public CalendarViewProcessorImp(DayCell<T> startDayCell, DayCell<T> endDayCell, List<DayCell<T>> list, ICalendarManager<T> calendarManager) {
        set(startDayCell, endDayCell, list, calendarManager);
    }

    public void set(DayCell<T> startDayCell, DayCell<T> endDayCell, List<DayCell<T>> list, ICalendarManager<T> calendarManager) {
        if(startDayCell == null) {
            LogUtil.w(LOG_TAG, "set:invalid data,startDayCell is null");
            return;
        } else if(endDayCell == null) {
            LogUtil.w(LOG_TAG, "set:invalid data,endDayCell is null");
            return;
        } else if(list == null || list.size() == 0) {
            LogUtil.w(LOG_TAG, "set:invalid data,list is empty");
            return;
        } else if(calendarManager == null) {
            LogUtil.w(LOG_TAG, "set:invalid data,calendarManager is null");
            return;
        }
        mStartDayCell = startDayCell;
        mEndDayCell = endDayCell;
        mDayCellList = list;
        mCalendarManager = calendarManager;
    }

    @Override
    public boolean isAffect(DayCell<T> dayCell, DayCell<T> originDayCell) {
        if(isAffectSingleItem(dayCell)) {
            return true;
        } else if(isAffectSingleItem(originDayCell)) {
            //判断originDayCell是因为SELECT_MODE_SINGLE时，dayCell和originDayCell互斥，可能只有一个对当前View有影响
            return true;
        }
        return false;
    }

    @Override
    public boolean isAffect(ContinuousSelectItem<T> item, ContinuousSelectItem<T> originItem) {
        if(isAffectContinuousItem(item)) {
            return true;
        } else if(isAffectContinuousItem(originItem)){
            return true;
        }
        return false;
    }

    @Override
    public void onDayCellChanged(DayCell<T> dayCell, boolean refresh) {
        if (mCalendarManager == null) {
            LogUtil.w(LOG_TAG, "onDayCellChanged:mCalendarManager is null");
            return;
        } else if(mDayCellList == null || mDayCellList.size() == 0) {
            LogUtil.w(LOG_TAG, "onDayCellChanged:mDayCellList is empty");
            return;
        }
        switch (mCalendarManager.getSelectMode()) {
            case CalendarConstant.SELECT_MODE_SINGLE:
                for (DayCell<T> cell : mDayCellList) {
                    if (cell == null || cell.getDayStatus() == CalendarConstant.DAY_STATUS_INVALID) {
                        continue;
                    }
                    if (CalendarTool.isEqual(dayCell, cell)) {
                        cell.setDayStatus(CalendarConstant.DAY_STATUS_SELECTED);
                    } else {
                        cell.setDayStatus(CalendarConstant.DAY_STATUS_UNSELECTED);
                    }
                }
                break;
            default:
                LogUtil.w(LOG_TAG, "onDayCellChanged:select mode not match, getSelectMode() = " + mCalendarManager.getSelectMode());
                break;
        }
    }

    @Override
    public void onDayCellListChanged(List<DayCell<T>> dayCellList, boolean refresh) {
        CalendarTool.sortDayCellList(dayCellList, true);
        if (mCalendarManager == null) {
            LogUtil.w(LOG_TAG, "onDayCellListChanged:mCalendarManager is null");
            return;
        } else if(mDayCellList == null || mDayCellList.size() == 0) {
            LogUtil.w(LOG_TAG, "onDayCellListChanged:mDayCellList is empty");
            return;
        }
        switch (mCalendarManager.getSelectMode()) {
            case CalendarConstant.SELECT_MODE_MULTI:
                int index = -1;
                DayCell<T> selectCell = null;
                for(DayCell<T> cell : mDayCellList) {
                    if (cell == null || cell.getDayStatus() == CalendarConstant.DAY_STATUS_INVALID) {
                        continue;
                    }
                    //selectCell == null || CalendarTool.isBefore(selectCell, cell)时，取下一个selectCell
                    while (dayCellList != null && (index + 1) >= 0 && (index + 1) < dayCellList.size() &&
                            (selectCell == null || CalendarTool.isBefore(selectCell, cell))) {
                        index++;
                        selectCell = dayCellList.get(index);
                    }
                    if(CalendarTool.isEqual(selectCell, cell)) {
                        cell.setDayStatus(CalendarConstant.DAY_STATUS_SELECTED);
                    } else {
                        cell.setDayStatus(CalendarConstant.DAY_STATUS_UNSELECTED);
                    }
                }
                break;
            default:
                LogUtil.w(LOG_TAG, "onDayCellListChanged:select mode not match,mCalendarManager.getSelectMode() = " + mCalendarManager.getSelectMode());
                break;
        }
    }

    @Override
    public void onContinuousItemChanged(ContinuousSelectItem<T> item, boolean refresh) {
        if (mCalendarManager == null) {
            LogUtil.w(LOG_TAG, "onContinuousItemChanged:mCalendarManager is null");
            return;
        } else if(mDayCellList == null || mDayCellList.size() == 0) {
            LogUtil.w(LOG_TAG, "onContinuousItemChanged:mDayCellList is empty");
        }
        switch (mCalendarManager.getSelectMode()) {
            case CalendarConstant.SELECT_MODE_CONTINUOUS:
            case CalendarConstant.SELECT_MODE_MIX:
                CalendarTool.setContinuousItemValid(item);
                for (DayCell<T> cell : mDayCellList) {
                    if(cell == null || cell.getDayStatus() == CalendarConstant.DAY_STATUS_INVALID) {
                        continue;
                    }
                    setDayStatusForContinuousItem(cell, item);
                }
                break;
            default:
                LogUtil.w(LOG_TAG, "onContinuousItemChanged:select mode not match,mCalendarManager.getSelectMode() = " + mCalendarManager.getSelectMode());
                break;
        }
    }

    @Override
    public void onContinuousItemListChanges(List<ContinuousSelectItem<T>> continuousSelectItems, boolean refresh) {
        CalendarTool.sortContinuousItemList(continuousSelectItems, true);
        if (mCalendarManager == null) {
            LogUtil.w(LOG_TAG, "onContinuousItemListChanges:mCalendarManager is null");
            return;
        } else if(mDayCellList == null || mDayCellList.size() == 0) {
            LogUtil.w(LOG_TAG, "onContinuousItemListChanges:mDayCellList is emtpy");
            return;
        }
        switch (mCalendarManager.getSelectMode()) {
            case CalendarConstant.SELECT_MODE_CONTINUOUS_MULTI:
            case CalendarConstant.SELECT_MODE_MIX_MULTI:
                int index = -1;
                DayCell<T> thresholdCell = null;
                ContinuousSelectItem<T> selectItem = null;
                for(DayCell<T> cell : mDayCellList) {
                    if(cell == null || cell.getDayStatus() == CalendarConstant.DAY_STATUS_INVALID) {
                        continue;
                    }
                    //selectItem == null || CalendarTool.isBefore(selectCell, cell)时，selectItem取下一个
                    while (continuousSelectItems != null && (index + 1) >= 0 && (index + 1) < continuousSelectItems.size() &&
                            (thresholdCell == null || CalendarTool.isBefore(thresholdCell, cell))) {
                        index++;
                        selectItem = continuousSelectItems.get(index);
                        CalendarTool.setContinuousItemValid(selectItem);
                        if(selectItem != null && selectItem.mEndDayCell != null) {
                            thresholdCell = selectItem.mEndDayCell;
                        } else if(selectItem != null && selectItem.mStartDayCell != null) {
                            thresholdCell = selectItem.mStartDayCell;
                        } else {
                            thresholdCell = null;
                        }
                    }
                    setDayStatusForContinuousItem(cell, selectItem);
                }
                break;
            default:
                LogUtil.w(LOG_TAG, "onContinuousItemListChanges:select mode not match,mCalendarManager.getSelectMode() = " + mCalendarManager.getSelectMode());
                break;
        }
    }

    @Override
    public void setData(List<DayCell<T>> dataList, boolean keepOld, boolean refresh) {
        if(mDayCellList == null || mDayCellList.size() == 0) {
            LogUtil.w(LOG_TAG, "setData:mDayCellList is empty");
            return;
        }
        CalendarTool.sortDayCellList(dataList, true);
        int index = -1;
        DayCell<T> currentCell = null;
        for(DayCell<T> cell : mDayCellList) {
            if(cell == null) {
                continue;
            }
            while (dataList != null && 0 <= index + 1 && index + 1 < dataList.size() &&
                    (currentCell == null || CalendarTool.isBefore(currentCell, cell))) {
                index++;
                currentCell = dataList.get(index);
            }
            if(CalendarTool.isEqual(cell, currentCell)) {
                cell.setData(currentCell.getData());
            } else if(!keepOld) {
                cell.setData(null);
            }
            if(mCalendarManager != null) {
                mCalendarManager.onBindData(cell);
            }
        }
    }

    private boolean isAffectSingleItem(DayCell<T> cell) {
        if(mStartDayCell == null || mEndDayCell == null) {
            LogUtil.w(LOG_TAG, "isAffectSingleItem:empty data,mStartDayCell = " + mStartDayCell + ", mEndDayCell = " + mEndDayCell);
            return false;
        }
        if(cell != null && !CalendarTool.isBefore(cell, mStartDayCell) && !CalendarTool.isAfter(cell, mEndDayCell)) {
            return true;
        }
        return false;
    }

    private boolean isAffectContinuousItem(ContinuousSelectItem<T> item) {
        if(mStartDayCell == null || mEndDayCell == null) {
            LogUtil.w(LOG_TAG, "isAffectContinuousItem:empty data,mStartDayCell = " + mStartDayCell + ", mEndDayCell = " + mEndDayCell);
        }
        CalendarTool.setContinuousItemValid(item);
        if(item != null && item.mStartDayCell != null && item.mEndDayCell == null) {
            if(!CalendarTool.isBefore(item.mStartDayCell, mStartDayCell) && !CalendarTool.isAfter(item.mStartDayCell, mEndDayCell)) {
                return true;
            }
        } else if(item != null && item.mStartDayCell != null && item.mEndDayCell != null) {
            if(!CalendarTool.isBefore(item.mEndDayCell, mStartDayCell) && !CalendarTool.isAfter(item.mStartDayCell, mEndDayCell)) {
                return true;
            }
        }
        return false;
    }

    private void setDayStatusForContinuousItem(DayCell<T> cell, ContinuousSelectItem<T> item) {
        if(cell == null) {
            LogUtil.w(LOG_TAG, "setDayStatusForContinuousItem:cell is null");
            return;
        } else if(cell.getDayStatus() == CalendarConstant.DAY_STATUS_INVALID) {
            LogUtil.w(LOG_TAG, "setDayStatusForContinuousItem:cell status is DAY_STATUS_INVALID");
            return;
        }
        if (item == null || (item.mStartDayCell == null && item.mEndDayCell == null)) {
            cell.setDayStatus(CalendarConstant.DAY_STATUS_UNSELECTED);
        } else if (item.mStartDayCell != null && item.mEndDayCell == null) {
            if (CalendarTool.isEqual(cell, item.mStartDayCell)) {
                cell.setDayStatus(CalendarConstant.DAY_STATUS_SELECTED);
            } else {
                cell.setDayStatus(CalendarConstant.DAY_STATUS_UNSELECTED);
            }
        } else if (CalendarTool.isEqual(item.mStartDayCell, item.mEndDayCell)) {
            if (CalendarTool.isEqual(cell, item.mStartDayCell)) {
                cell.setDayStatus(CalendarConstant.DAY_STATUS_SELECTED_SECTION_START_AND_END);
            } else {
                cell.setDayStatus(CalendarConstant.DAY_STATUS_UNSELECTED);
            }
        } else {
            if (CalendarTool.isEqual(cell, item.mStartDayCell)) {
                cell.setDayStatus(CalendarConstant.DAY_STATUS_SELECTED_SECTION_START);
            } else if (CalendarTool.isAfter(cell, item.mStartDayCell) && CalendarTool.isBefore(cell, item.mEndDayCell)) {
                cell.setDayStatus(CalendarConstant.DAY_STATUS_SELECTED_SECTION_PASS);
            } else if (CalendarTool.isEqual(cell, item.mEndDayCell)) {
                cell.setDayStatus(CalendarConstant.DAY_STATUS_SELECTED_SECTION_END);
            } else {
                cell.setDayStatus(CalendarConstant.DAY_STATUS_UNSELECTED);
            }
        }
    }
}
