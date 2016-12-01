package com.xing.android.calendar.process;

import android.view.MotionEvent;
import android.view.View;

import com.xing.android.calendar.CalendarConstant;
import com.xing.android.calendar.CalendarTool;
import com.xing.android.calendar.ICalendarManager;
import com.xing.android.calendar.model.ContinuousSelectItem;
import com.xing.android.calendar.model.DayCell;
import com.xing.android.calendar.process.DayCellUserInterfaceInfo.DayCellClickListener;
import com.xing.android.calendar.process.DayCellUserInterfaceInfo.DayCellLongClickListener;
import com.xing.android.calendar.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxx09506 on 2016/11/30.
 */

public class DayCellUserInterfaceImp<T> {

    private final String LOG_TAG = this.getClass().getSimpleName();


    public final DayCellClickListener<T> CLICK_1 = new DayCellClickListener<T>() {
        @Override
        public DayCell<T> onDayCellClick( View view, ICalendarManager<T> iCalendarManager, DayCell<T> cell) {
            return cell;
        }
    };

    public final DayCellLongClickListener<T> LONG_CLICK_1 = new DayCellLongClickListener<T>() {
        @Override
        public DayCell<T> onDayCellLongClick(View view, ICalendarManager<T> iCalendarManager, DayCell<T> cell) {
            if(iCalendarManager == null) {
                LogUtil.w(LOG_TAG, "onDayCellLongClick:iCalendarManager is null");
                return null;
            }
            switch (iCalendarManager.getSelectMode()) {
                case CalendarConstant.SELECT_MODE_SINGLE: {
                    if (CalendarTool.isEqual(cell, iCalendarManager.getSelectedDayCell())) {
                        return cell;
                    }
                    break;
                }
                case CalendarConstant.SELECT_MODE_MULTI: {
                    List<DayCell<T>> list = iCalendarManager.getSelectedDayCellList();
                    if (list == null) {
                        list = new ArrayList<DayCell<T>>();
                    }
                    for (DayCell dayCell : list) {
                        if (dayCell != null && CalendarTool.isEqual(cell, dayCell)) {
                            return cell;
                        }
                    }
                    break;
                }
                case CalendarConstant.SELECT_MODE_CONTINUOUS:
                case CalendarConstant.SELECT_MODE_MIX: {
                    ContinuousSelectItem<T> item = iCalendarManager.getSelectedContinuousItem();
                    if(item != null && item.mStartDayCell != null && CalendarTool.isEqual(item.mStartDayCell, cell)) {
                        return cell;
                    } else if(item != null && item.mEndDayCell != null && CalendarTool.isEqual(item.mEndDayCell, cell)) {
                        return cell;
                    }
                    break;
                }
                case CalendarConstant.SELECT_MODE_CONTINUOUS_MULTI:
                case CalendarConstant.SELECT_MODE_MIX_MULTI: {
                    List<ContinuousSelectItem<T>> list = iCalendarManager.getSelectedContinuousItemList();
                    if(list == null) {
                        list = new ArrayList<ContinuousSelectItem<T>>();
                    }
                    for(ContinuousSelectItem<T> item : list) {
                        if(item != null && item.mStartDayCell != null && CalendarTool.isEqual(item.mStartDayCell, cell)) {
                            return cell;
                        } else if(item != null && item.mEndDayCell != null && CalendarTool.isEqual(item.mEndDayCell, cell)) {
                            return cell;
                        }
                    }
                    break;
                }
            }
            return null;
        }
    };

}
