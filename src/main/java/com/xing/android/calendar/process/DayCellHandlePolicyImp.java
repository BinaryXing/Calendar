package com.xing.android.calendar.process;

import com.xing.android.calendar.CalendarTool;
import com.xing.android.calendar.ICalendarManager;
import com.xing.android.calendar.model.ContinuousSelectItem;
import com.xing.android.calendar.model.DayCell;
import com.xing.android.calendar.process.DayCellHandlePolicyInfo.PolicyForContinuous;
import com.xing.android.calendar.process.DayCellHandlePolicyInfo.PolicyForContinuousMulti;
import com.xing.android.calendar.process.DayCellHandlePolicyInfo.PolicyForMix;
import com.xing.android.calendar.process.DayCellHandlePolicyInfo.PolicyForMixMulti;
import com.xing.android.calendar.process.DayCellHandlePolicyInfo.PolicyForMulti;
import com.xing.android.calendar.process.DayCellHandlePolicyInfo.PolicyForSingle;
import com.xing.android.calendar.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxx09506 on 2016/11/8.
 */

public class DayCellHandlePolicyImp<T>{

    private final String LOG_TAG = this.getClass().getSimpleName();

    /**
     * 单选策略1：可以取消已选中Cell
     */
    public final PolicyForSingle<T> SINGLE_POLICY_1 = new PolicyForSingle<T>() {
        @Override
        public boolean handleForSingle(ICalendarManager<T> calendarManager, DayCell<T> factorCell, DayCell<T> beforeSelectCell) {
            if(!checkParameterValid(calendarManager, factorCell)) {
                //参数无效，不会做任何处理，所以不需要刷新View
                return true;
            }
            DayCell<T> beforeCell = beforeSelectCell != null ? beforeSelectCell.getCopyDayCell() : null;
            DayCell<T> afterCell = null;
            if(!CalendarTool.isEqual(beforeCell, factorCell)) {
                afterCell = factorCell;
            }
            calendarManager.setSelectedDaycell(afterCell, false);
            calendarManager.refreshAffectViewList(afterCell, beforeCell);
            return true;
        }
    };

    /**
     * 单选策略2：不可以取消选中Cell
     */
    public final PolicyForSingle<T> SINGLE_POLICY_2 = new PolicyForSingle<T>() {
        @Override
        public boolean handleForSingle(ICalendarManager<T> calendarManager, DayCell<T> factorCell, DayCell<T> beforeSelectCell) {
            if(!checkParameterValid(calendarManager, factorCell)) {
                //参数无效，不会做任何处理，所以不需要刷新View
                return true;
            }
            DayCell<T> beforeCell = beforeSelectCell != null ? beforeSelectCell.getCopyDayCell() : null;
            DayCell<T> afterCell;
            if(CalendarTool.isEqual(factorCell, beforeSelectCell)) {
                afterCell = beforeCell != null ? beforeCell.getCopyDayCell() : null;
            } else {
                afterCell = factorCell.getCopyDayCell();
            }
            calendarManager.setSelectedDaycell(afterCell, false);
            calendarManager.refreshAffectViewList(afterCell, beforeCell);
            return true;
        }
    };

    /**
     * 多选策略1：可以取消任意一个
     */
    public final PolicyForMulti<T> MULTI_POLICY_1 = new PolicyForMulti<T>() {
        @Override
        public boolean handleForMulti(ICalendarManager<T> calendarManager, DayCell<T> factorCell, List<DayCell<T>> beforeSelectList) {
            if(!checkParameterValid(calendarManager, factorCell)) {
                //参数无效，不会做任何处理，所以不需要刷新View
                return true;
            }
            CalendarTool.sortDayCellList(beforeSelectList, true);
            List<DayCell<T>> afterSelectList = new ArrayList<DayCell<T>>();
            if(beforeSelectList != null) {
                afterSelectList.addAll(beforeSelectList);
            }
            DayCell<T> beforeCell = null;
            DayCell<T> afterCell = null;
            boolean isContain = false;
            for (DayCell cell : afterSelectList) {
                if (CalendarTool.isEqual(cell, factorCell)) {
                    beforeCell = cell;
                    isContain = true;
                    break;
                }
            }
            if(isContain) {
                afterSelectList.remove(beforeCell);
            } else {
                afterCell = factorCell.getCopyDayCell();
                afterSelectList.add(afterCell);
            }
            calendarManager.setSelectedDayCellList(afterSelectList, false);
            calendarManager.refreshAffectViewList(afterCell, beforeCell);
            return true;
        }
    };

    /**
     * 多选策略2：至少需要一个selectCell
     */
    public final PolicyForMulti<T> MULTI_POLICY_2 = new PolicyForMulti<T>() {
        @Override
        public boolean handleForMulti(ICalendarManager<T> calendarManager, DayCell<T> factorCell, List<DayCell<T>> beforeSelectList) {
            if(!checkParameterValid(calendarManager, factorCell)) {
                //参数无效，不会做任何处理，所以不需要刷新View
                return true;
            }
            if(beforeSelectList != null && beforeSelectList.size() == 1 && CalendarTool.isEqual(factorCell, beforeSelectList.get(0))) {
                //如果只有一个selectCell，并且和clickCell相等时，不能取消
                return true;
            }
            CalendarTool.sortDayCellList(beforeSelectList, true);
            List<DayCell<T>> afterSelectList = new ArrayList<DayCell<T>>();
            if(beforeSelectList != null) {
                afterSelectList.addAll(beforeSelectList);
            }
            DayCell<T> beforeCell = null;
            DayCell<T> afterCell = null;
            boolean isContain = false;
            for (DayCell cell : afterSelectList) {
                if (CalendarTool.isEqual(cell, factorCell)) {
                    beforeCell = cell;
                    isContain = true;
                    break;
                }
            }
            if(isContain) {
                afterSelectList.remove(beforeCell);
            } else {
                afterCell = factorCell.getCopyDayCell();
                afterSelectList.add(afterCell);
            }
            calendarManager.setSelectedDayCellList(afterSelectList, false);
            calendarManager.refreshAffectViewList(afterCell, beforeCell != null ? beforeCell.getCopyDayCell() : null);
            return true;
        }
    };

    /**
     * 连选策略1：点击起始/结束日期，可以取消；如果Item同时有开始结束日期，那么除了点击开始/结束日期，其他Cell点击无效
     */
    public final PolicyForContinuous<T> CONTINUOUS_POLICY_1 = new PolicyForContinuous<T>() {
        @Override
        public boolean handleForContinuous(ICalendarManager<T> calendarManager, DayCell<T> factorCell, ContinuousSelectItem<T> beforeSelectItem) {
            if(!checkParameterValid(calendarManager, factorCell)) {
                //参数无效，不会做任何处理，所以不需要刷新View
                return true;
            }
            ContinuousSelectItem<T> afterItem = handleContinuousItem(factorCell, beforeSelectItem, false, false);
            if(!CalendarTool.isEqual(beforeSelectItem, afterItem)) {
                calendarManager.setSelectedContinuousItem(afterItem, false);
                calendarManager.refreshAffectViewList(afterItem, beforeSelectItem);
            }
            return true;
        }
    };

    /**
     * 连选策略2：点击起始/结束日期，可以取消；如果Item同时有开始结束日期，点击除了开始/结束日期，点击其他的Cell，重新开始连选
     */
    public final PolicyForContinuous<T> CONTINUOUS_POLICY_2 = new PolicyForContinuous<T>() {
        @Override
        public boolean handleForContinuous(ICalendarManager<T> calendarManager, DayCell<T> factorCell, ContinuousSelectItem<T> beforeSelectItem) {
            if(!checkParameterValid(calendarManager, factorCell)) {
                //参数无效，不会做任何处理，所以不需要刷新View
                return true;
            }
            ContinuousSelectItem<T> afterItem = handleContinuousItem(factorCell, beforeSelectItem, false, true);
            if(!CalendarTool.isEqual(beforeSelectItem, afterItem)) {
                calendarManager.setSelectedContinuousItem(afterItem, false);
                calendarManager.refreshAffectViewList(afterItem, beforeSelectItem);
            }
            return true;
        }
    };

    /**
     * 混合选策略1：点击起始/结束日期，可以取消；如果Item同时有开始结束日期，那么除了开始/结束日期，其他Cell点击无效
     */
    public final PolicyForMix<T> MIX_POLICY_1 = new PolicyForMix<T>() {
        @Override
        public boolean handleForMix(ICalendarManager<T> calendarManager, DayCell<T> factorCell, ContinuousSelectItem<T> beforeSelectItem) {
            if(!checkParameterValid(calendarManager, factorCell)) {
                //参数无效，不会做任何处理，所以不需要刷新View
                return true;
            }
            ContinuousSelectItem<T> afterItem = handleContinuousItem(factorCell, beforeSelectItem, true, false);
            if(!CalendarTool.isEqual(beforeSelectItem, afterItem)) {
                calendarManager.setSelectedContinuousItem(afterItem, false);
                calendarManager.refreshAffectViewList(afterItem, beforeSelectItem);
            }
            return true;
        }
    };

    /**
     * 混合选策略2：点击起始/结束日期，可以取消；如果Item同时有开始结束日期，点击除了开始/结束日期，点击其他的Cell，重新开始连选
     */
    public final PolicyForMix<T> MIX_POLICY_2 = new PolicyForMix<T>() {
        @Override
        public boolean handleForMix(ICalendarManager<T> calendarManager, DayCell<T> factorCell, ContinuousSelectItem<T> beforeSelectItem) {
            if(!checkParameterValid(calendarManager, factorCell)) {
                //参数无效，不会做任何处理，所以不需要刷新View
                return true;
            }
            ContinuousSelectItem<T> afterItem = handleContinuousItem(factorCell, beforeSelectItem, true, true);
            if(!CalendarTool.isEqual(beforeSelectItem, afterItem)) {
                calendarManager.setSelectedContinuousItem(afterItem, false);
                calendarManager.refreshAffectViewList(afterItem, beforeSelectItem);
            }
            return true;
        }
    };

    /**
     * 多个连选策略1：factorCell优先和前面的半开ContinuousItem相结合
     */
    public final PolicyForContinuousMulti<T> CONTINUOUS_MULTI_POLICY_1 = new PolicyForContinuousMulti<T>() {
        @Override
        public boolean handleForContinuousMulti(ICalendarManager<T> calendarManager, DayCell<T> factorCell, List<ContinuousSelectItem<T>> beforeSelectItemList) {
            if(!checkParameterValid(calendarManager, factorCell)) {
                //参数无效，不会做任何处理，所以不需要刷新View
                return true;
            }
            CalendarTool.sortContinuousItemList(beforeSelectItemList, true);
            CalendarTool.clearNullContinuousItem(beforeSelectItemList);
            List<ContinuousSelectItem<T>> afterSelectItemList = new ArrayList<ContinuousSelectItem<T>>();
            if(beforeSelectItemList != null) {
                afterSelectItemList.addAll(beforeSelectItemList);
            }

            ContinuousSelectItem<T> affectItem = getAffectContinuousItem(factorCell, afterSelectItemList);

            if(affectItem != null) {
                afterSelectItemList.remove(affectItem);
            }

            ContinuousSelectItem<T> afterItem = handleContinuousItem(factorCell, affectItem, false, false);

            if(afterItem != null) {
                afterSelectItemList.add(afterItem);
            }

            if(!CalendarTool.isEqual(affectItem, afterItem)) {
                calendarManager.setSelectedContinuousItemList(afterSelectItemList, false);
                calendarManager.refreshAffectViewList(afterItem, affectItem);
            }
            return false;
        }
    };

    /**
     * 多个混合选策略1：factorCell优先和前面的半开ContinuousItem相结合
     */
    public final PolicyForMixMulti<T> MIX_MULTI_POLICY_1 = new PolicyForMixMulti<T>() {
        @Override
        public boolean handleForMixMulti(ICalendarManager<T> calendarManager, DayCell<T> factorCell, List<ContinuousSelectItem<T>> beforeSelectItemList) {
            if(!checkParameterValid(calendarManager, factorCell)) {
                //参数无效，不会做任何处理，所以不需要刷新View
                return true;
            }
            CalendarTool.sortContinuousItemList(beforeSelectItemList, true);
            CalendarTool.clearNullContinuousItem(beforeSelectItemList);
            List<ContinuousSelectItem<T>> afterSelectItemList = new ArrayList<ContinuousSelectItem<T>>();
            if(beforeSelectItemList != null) {
                afterSelectItemList.addAll(beforeSelectItemList);
            }

            ContinuousSelectItem<T> affectItem = getAffectContinuousItem(factorCell, afterSelectItemList);

            if(affectItem != null) {
                afterSelectItemList.remove(affectItem);
            }

            ContinuousSelectItem<T> afterItem = handleContinuousItem(factorCell, affectItem, true, false);

            if(afterItem != null) {
                afterSelectItemList.add(afterItem);
            }

            if(!CalendarTool.isEqual(affectItem, afterItem)) {
                calendarManager.setSelectedContinuousItemList(afterSelectItemList, false);
                calendarManager.refreshAffectViewList(afterItem, affectItem);
            }
            return true;
        }
    };

    private ContinuousSelectItem<T> getAffectContinuousItem(DayCell<T> factorCell, List<ContinuousSelectItem<T>> list) {
        if(list == null || list.size() == 0) {
            LogUtil.w(LOG_TAG, "getAffectContinuousItem:list is empty");
            return null;
        }
        ContinuousSelectItem<T> lastItem;
        ContinuousSelectItem<T> currentItem = null;
        int index = -1;
        ContinuousSelectItem<T> affectItem = null;
        while (index + 1 < list.size()) {
            index++;
            lastItem = currentItem;
            currentItem = list.get(index);
            CalendarTool.setContinuousItemValid(currentItem);
            if(currentItem == null || (currentItem.mStartDayCell == null && currentItem.mEndDayCell == null)) {
                continue;
            }
            if(index == 0 && CalendarTool.isBefore(factorCell, currentItem)) {
                if(currentItem.mStartDayCell != null && currentItem.mEndDayCell == null) {
                    affectItem = currentItem;
                } else {
                    affectItem = null;
                }
                break;
            } else if(index == list.size() - 1 && CalendarTool.isAfter(factorCell, currentItem)) {
                if(currentItem.mStartDayCell != null && currentItem.mEndDayCell == null) {
                    affectItem = currentItem;
                } else {
                    affectItem = null;
                }
                break;
            } else if(CalendarTool.isInClusive(factorCell, lastItem)) {
                affectItem = lastItem;
                break;
            } else if(CalendarTool.isInClusive(factorCell, currentItem)) {
                affectItem = currentItem;
                break;
            } else if(CalendarTool.isAfter(factorCell, lastItem) && CalendarTool.isBefore(factorCell, currentItem)) {
                if(lastItem.mStartDayCell != null && lastItem.mEndDayCell == null) {
                    affectItem = lastItem;
                } else if(currentItem.mStartDayCell != null && currentItem.mEndDayCell == null) {
                    affectItem = currentItem;
                } else {
                    affectItem = null;
                }
                break;
            }
        }
        return affectItem;
    }

    /**
     * SELECT_MODE_CONTINUOUS/SELECT_MODE_CONTINUOUS_MULTI/SELECT_MODE_MIX/SELECT_MODE_MIX_MULTI的点击处理
     * @param factorCell 点击Cell
     * @param beforeItem 点击之前的Item
     * @param isMix 是混合模式（包括多个混合）
     * @param canCancel beforeItem的开始和结束日期都不为空时，且点击Cell不等于开始/结束日期时，是否重置Item，重新选择
     * @return 处理后的Item
     */
    private ContinuousSelectItem<T> handleContinuousItem(DayCell<T> factorCell, ContinuousSelectItem<T> beforeItem, boolean isMix, boolean canCancel) {
        if(factorCell == null) {
            LogUtil.i(LOG_TAG, "handleContinuousItem:factorCell is null");
            return null;
        }
        CalendarTool.setContinuousItemValid(beforeItem);
        ContinuousSelectItem<T> afterItem;
        if(beforeItem != null) {
            afterItem = beforeItem.getCopyContinuousSelectItem();
        } else {
            afterItem = new ContinuousSelectItem<T>();
        }
        if(beforeItem == null || (beforeItem.mStartDayCell == null && beforeItem.mEndDayCell == null)) {
            afterItem.mStartDayCell = factorCell.getCopyDayCell();
        } else if(beforeItem.mStartDayCell != null && beforeItem.mEndDayCell == null) {
            if(CalendarTool.isEqual(factorCell, beforeItem.mStartDayCell) && !isMix) {
                afterItem = null;
            } else {
                afterItem.mEndDayCell = factorCell.getCopyDayCell();
            }
        } else {
            if(CalendarTool.isEqual(beforeItem.mStartDayCell, beforeItem.mEndDayCell) &&
                    CalendarTool.isEqual(beforeItem.mStartDayCell, factorCell)) {
                afterItem = null;
            } else if(CalendarTool.isEqual(beforeItem.mStartDayCell, factorCell)) {
                afterItem.mStartDayCell = null;
            } else if(CalendarTool.isEqual(beforeItem.mEndDayCell, factorCell)) {
                afterItem.mEndDayCell = null;
            } else {
                if(canCancel) {
                    afterItem.mStartDayCell = factorCell.getCopyDayCell();
                    afterItem.mEndDayCell = null;
                }
            }
        }
        CalendarTool.setContinuousItemValid(afterItem);
        return afterItem;
    }

    private boolean checkParameterValid(ICalendarManager<T> calendarManager, DayCell<T> factorCell) {
        if(calendarManager == null) {
            LogUtil.w(LOG_TAG, "checkParameterValid:calendarManager is null");
            return false;
        } else if(factorCell == null) {
            LogUtil.w(LOG_TAG, "checkParameterValid:factorCell is null");
            return false;
        }
        return true;
    }

}
