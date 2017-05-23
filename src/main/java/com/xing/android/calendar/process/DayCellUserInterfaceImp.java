package com.xing.android.calendar.process;

import android.view.MotionEvent;
import android.view.View;

import com.xing.android.calendar.CalendarConstant;
import com.xing.android.calendar.CalendarTool;
import com.xing.android.calendar.ICalendarManager;
import com.xing.android.calendar.model.ContinuousSelectItem;
import com.xing.android.calendar.model.DayCell;
import com.xing.android.calendar.process.DayCellUserInterfaceInfo.ClickCellConvertListener;
import com.xing.android.calendar.process.DayCellUserInterfaceInfo.LongClickCellConvertListener;
import com.xing.android.calendar.process.DayCellUserInterfaceInfo.TouchCellConvertListener;
import com.xing.android.calendar.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxx09506 on 2016/11/30.
 */

public class DayCellUserInterfaceImp<T> {

    private final String LOG_TAG = this.getClass().getSimpleName();

    /**
     * 不对点击Cell做任何转换
     */
    public final ClickCellConvertListener<T> CLICK_1 = new ClickCellConvertListener<T>() {
        @Override
        public DayCell<T> convert(View view, ICalendarManager<T> iCalendarManager, DayCell<T> cell) {
            return cell;
        }
    };

    /**
     * 在{@link CalendarConstant#SELECT_MODE_SINGLE}{@link CalendarConstant#SELECT_MODE_MULTI}时，如果长按Cell和选中Cell相等，则不做转换处理；
     * 在{@link CalendarConstant#SELECT_MODE_CONTINUOUS}{@link CalendarConstant#SELECT_MODE_MIX}{@link CalendarConstant#SELECT_MODE_CONTINUOUS_MULTI}
     * {@link CalendarConstant#SELECT_MODE_MIX_MULTI}时，只有长按Cell和{@link ContinuousSelectItem}的开始Cell和结束Cell相等时，才不做转换处理；
     * 其他情况下，直接过滤；
     */
    public final LongClickCellConvertListener<T> LONG_CLICK_1 = new LongClickCellConvertListener<T>() {
        @Override
        public DayCell<T> convert(View view, ICalendarManager<T> iCalendarManager, DayCell<T> cell) {
            if(iCalendarManager == null) {
                LogUtil.w(LOG_TAG, "LongClick.convert:iCalendarManager is null");
                return null;
            }
            switch (iCalendarManager.getSelectMode()) {
                case CalendarConstant.SELECT_MODE_SINGLE: {
                    if (CalendarTool.isEqual(cell, iCalendarManager.getSelectedDayCell())) {
                        return cell;
                    }
                }
                break;
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
                }
                break;
                case CalendarConstant.SELECT_MODE_CONTINUOUS:
                case CalendarConstant.SELECT_MODE_MIX: {
                    ContinuousSelectItem<T> item = iCalendarManager.getSelectedContinuousItem();
                    if(item != null && item.mStartDayCell != null && CalendarTool.isEqual(item.mStartDayCell, cell)) {
                        return cell;
                    } else if(item != null && item.mEndDayCell != null && CalendarTool.isEqual(item.mEndDayCell, cell)) {
                        return cell;
                    }
                }
                break;
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
                }
                break;
            }
            return null;
        }
    };

    //TouchCellConvert还在开发中
//    public final TouchCellConvertListener<T> TOUCH_CLICK_1 = new TouchCellConvertListener<T>() {
//
//        //Down-Move-Up一个完整的Touch事件中，在Down的时候记录，在Up的时候清空
//        private DayCell<T> mSelectedDayCell;
//        private List<DayCell<T>> mSelectedDayCellList = new ArrayList<DayCell<T>>();
//        private ContinuousSelectItem<T> mSelectedContinuousItem;
//        private List<ContinuousSelectItem<T>> mSelectedContinuousItemList = new ArrayList<ContinuousSelectItem<T>>();
//
//        @Override
//        public DayCell<T> convert(View view, ICalendarManager<T> iCalendarManager, DayCell<T> cell, MotionEvent event) {
//            if(iCalendarManager == null) {
//                LogUtil.w(LOG_TAG, "TouchClick.convert:iCalendarManager is null");
//                return null;
//            } else if(event == null) {
//                LogUtil.w(LOG_TAG, "TouchClick.convert:event is null");
//                return null;
//            } else if(cell == null) {
//                LogUtil.w(LOG_TAG, "TouchClick.convert:cell is null");
//                return null;
//            }
//            DayCell<T> resultCell = null;
//            int action = event.getAction();
//            switch (iCalendarManager.getSelectMode()) {
//                case CalendarConstant.SELECT_MODE_SINGLE:
//                    if(action == MotionEvent.ACTION_DOWN) {
//                        if(iCalendarManager.getSelectedDayCell() != null) {
//                            mSelectedDayCell = iCalendarManager.getSelectedDayCell().getCopyDayCell();
//                        }
//                    } else if(action == MotionEvent.ACTION_MOVE) {
//                        if(!CalendarTool.isEqual(mSelectedDayCell, cell)) {
//                            mSelectedDayCell = cell.getCopyDayCell();
//                        }
//                    } else if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
//                        resultCell = cell;
//                        mSelectedDayCell = null;
//                    }
//                    break;
//                case CalendarConstant.SELECT_MODE_MULTI:
//                    if(action == MotionEvent.ACTION_DOWN) {
//                        mSelectedDayCellList.add(cell);
//                    } else if(action == MotionEvent.ACTION_MOVE) {
//                        if(!mSelectedDayCellList.contains(cell)) {
//                            mSelectedDayCellList.add(cell);
//                        }
//                    } else if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
//                        if(!mSelectedDayCellList.contains(cell)) {
//                            mSelectedDayCellList.add(cell);
//                        }
//                        mSelectedDayCellList.clear();
//                    }
//                    break;
//            }
//            return null;
//        }
//    };

}
