package com.xing.android.calendar.process;

import com.xing.android.calendar.CalendarTool;
import com.xing.android.calendar.ICalendarManager;
import com.xing.android.calendar.model.ContinuousSelectItem;
import com.xing.android.calendar.model.DayCell;
import com.xing.android.calendar.process.DayCellClickPolicyInfo.ClickPolicyForContinuous;
import com.xing.android.calendar.process.DayCellClickPolicyInfo.ClickPolicyForContinuousMulti;
import com.xing.android.calendar.process.DayCellClickPolicyInfo.ClickPolicyForMix;
import com.xing.android.calendar.process.DayCellClickPolicyInfo.ClickPolicyForMixMulti;
import com.xing.android.calendar.process.DayCellClickPolicyInfo.ClickPolicyForMulti;
import com.xing.android.calendar.process.DayCellClickPolicyInfo.ClickPolicyForSingle;
import com.xing.android.calendar.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxx09506 on 2016/11/8.
 */

public class DayCellClickPolicyImp<T>{

    private final String LOG_TAG = this.getClass().getSimpleName();

    /**
     * 单选策略1：可以取消已选中Cell
     */
    public final ClickPolicyForSingle<T> SINGLE_POLICY_1 = new ClickPolicyForSingle<T>() {
        @Override
        public boolean handleClickForSingle(ICalendarManager<T> calendarManager, DayCell<T> clickCell, DayCell<T> beforeSelectCell) {
            if(!checkParamterValid(calendarManager, clickCell)) {
                //参数无效，不会做任何处理，所以不需要刷新View
                return true;
            }
            DayCell<T> beforeCell = beforeSelectCell;
            DayCell<T> afterCell;
            if(CalendarTool.isEqual(beforeSelectCell, clickCell)) {
                afterCell = null;
            } else {
                afterCell = clickCell;
            }
            calendarManager.setSelectedDaycell(afterCell, false);
            calendarManager.refreshAffectViewList(afterCell, beforeCell);
            return true;
        }
    };

    /**
     * 单选策略2：不可以取消选中Cell
     */
    public final ClickPolicyForSingle<T> SINGLE_POLICY_2 = new ClickPolicyForSingle<T>() {
        @Override
        public boolean handleClickForSingle (ICalendarManager<T> calendarManager, DayCell<T> clickCell, DayCell<T> beforeSelectCell) {
            if(!checkParamterValid(calendarManager, clickCell)) {
                //参数无效，不会做任何处理，所以不需要刷新View
                return true;
            }
            DayCell<T> beforeCell = clickCell;
            DayCell<T> afterCell;
            if(CalendarTool.isEqual(clickCell, beforeSelectCell)) {
                LogUtil.i(LOG_TAG, "SINGLE_POLICY_2.handleClickForSingle:equal cell");
                afterCell = beforeCell;
            } else {
                afterCell = clickCell;
            }
            calendarManager.setSelectedDaycell(afterCell, false);
            calendarManager.refreshAffectViewList(afterCell, beforeCell);
            return true;
        }
    };

    /**
     * 多选策略1：可以取消任意一个
     */
    public final ClickPolicyForMulti<T> MULTI_POLICY_1 = new ClickPolicyForMulti<T>() {
        @Override
        public boolean handleClickForMulti(ICalendarManager<T> calendarManager, DayCell<T> clickCell, List<DayCell<T>> beforeSelectList) {
            if(!checkParamterValid(calendarManager, clickCell)) {
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
                if (CalendarTool.isEqual(cell, clickCell)) {
                    beforeCell = cell;
                    isContain = true;
                    break;
                }
            }
            if(isContain) {
                afterSelectList.remove(beforeCell);
            } else {
                afterCell = clickCell.getCopyDayCell();
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
    public final ClickPolicyForMulti<T> MULTI_POLICY_2 = new ClickPolicyForMulti<T>() {
        @Override
        public boolean handleClickForMulti(ICalendarManager<T> calendarManager, DayCell<T> clickCell, List<DayCell<T>> beforeSelectList) {
            if(!checkParamterValid(calendarManager, clickCell)) {
                //参数无效，不会做任何处理，所以不需要刷新View
                return true;
            }
            if(beforeSelectList == null) {
                beforeSelectList = new ArrayList<DayCell<T>>();
            }
            if(beforeSelectList.size() == 1 && CalendarTool.isEqual(clickCell, beforeSelectList.get(0))) {
                //如果只有一个selectCell，并且和clickCell相等时，不能取消
                return true;
            }
            CalendarTool.sortDayCellList(beforeSelectList, true);
            DayCell<T> beforeCell = null;
            DayCell<T> afterCell = null;
            boolean isContain = false;
            for (DayCell cell : beforeSelectList) {
                if (CalendarTool.isEqual(cell, clickCell)) {
                    beforeCell = cell;
                    isContain = true;
                    break;
                }
            }
            if(isContain) {
                beforeSelectList.remove(beforeCell);
            } else {
                afterCell = clickCell.getCopyDayCell();
                beforeSelectList.add(afterCell);
            }
            calendarManager.setSelectedDayCellList(beforeSelectList, false);
            calendarManager.refreshAffectViewList(afterCell, beforeCell);
            return true;
        }
    };

    /**
     * 连选策略1：点击起始/结束日期，可以取消；如果Item同时有开始结束日期，那么除了点击开始/结束日期，其他Cell点击无效
     */
    public final ClickPolicyForContinuous<T> CONTINUOUS_POLICY_1 = new ClickPolicyForContinuous<T>() {
        @Override
        public boolean handleClickForContinuous(ICalendarManager<T> calendarManager, DayCell<T> clickCell, ContinuousSelectItem<T> beforeSelectItem) {
            if(!checkParamterValid(calendarManager, clickCell)) {
                //参数无效，不会做任何处理，所以不需要刷新View
                return true;
            }
            ContinuousSelectItem<T> afterItem = handleContinuousItem(clickCell, beforeSelectItem, false, false);
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
    public final ClickPolicyForContinuous<T> CONTINUOUS_POLICY_2 = new ClickPolicyForContinuous<T>() {
        @Override
        public boolean handleClickForContinuous(ICalendarManager<T> calendarManager, DayCell<T> clickCell, ContinuousSelectItem<T> beforeSelectItem) {
            if(!checkParamterValid(calendarManager, clickCell)) {
                //参数无效，不会做任何处理，所以不需要刷新View
                return true;
            }
            ContinuousSelectItem<T> afterItem = handleContinuousItem(clickCell, beforeSelectItem, false, true);
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
    public final ClickPolicyForMix<T> MIX_POLICY_1 = new ClickPolicyForMix<T>() {
        @Override
        public boolean handleClickForMix(ICalendarManager<T> calendarManager, DayCell<T> clickCell, ContinuousSelectItem<T> beforeSelectItem) {
            if(!checkParamterValid(calendarManager, clickCell)) {
                //参数无效，不会做任何处理，所以不需要刷新View
                return true;
            }
            ContinuousSelectItem<T> afterItem = handleContinuousItem(clickCell, beforeSelectItem, true, false);
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
    public final ClickPolicyForMix<T> MIX_POLICY_2 = new ClickPolicyForMix<T>() {
        @Override
        public boolean handleClickForMix(ICalendarManager<T> calendarManager, DayCell<T> clickCell, ContinuousSelectItem<T> beforeSelectItem) {
            if(!checkParamterValid(calendarManager, clickCell)) {
                //参数无效，不会做任何处理，所以不需要刷新View
                return true;
            }
            ContinuousSelectItem<T> afterItem = handleContinuousItem(clickCell, beforeSelectItem, true, true);
            if(!CalendarTool.isEqual(beforeSelectItem, afterItem)) {
                calendarManager.setSelectedContinuousItem(afterItem, false);
                calendarManager.refreshAffectViewList(afterItem, beforeSelectItem);
            }
            return true;
        }
    };

    /**
     * 多个连选策略1：
     */
    public final ClickPolicyForContinuousMulti<T> CONTINUOUS_MULTI_POLICY_1 = new ClickPolicyForContinuousMulti<T>() {
        @Override
        public boolean handleClickForContinuousMulti(ICalendarManager<T> calendarManager, DayCell<T> clickCell, List<ContinuousSelectItem<T>> beforeSelectItemList) {
            if(!checkParamterValid(calendarManager, clickCell)) {
                //参数无效，不会做任何处理，所以不需要刷新View
                return true;
            }
            CalendarTool.sortContinuousItemList(beforeSelectItemList, true);
            CalendarTool.clearNullContinuousItem(beforeSelectItemList);
            List<ContinuousSelectItem<T>> afterSelectItemList = new ArrayList<ContinuousSelectItem<T>>();
            if(beforeSelectItemList != null) {
                afterSelectItemList.addAll(beforeSelectItemList);
            }

            ContinuousSelectItem<T> lastItem;
            ContinuousSelectItem<T> currentItem = null;

            int index = -1;
            ContinuousSelectItem<T> affectItem = null;
            while (index + 1 < afterSelectItemList.size()) {
                index++;
                lastItem = currentItem;
                currentItem = afterSelectItemList.get(index);
                CalendarTool.setContinuousItemValid(currentItem);
                if(currentItem == null || (currentItem.mStartDayCell == null && currentItem.mEndDayCell == null)) {
                    continue;
                }
                if(index == 0 && CalendarTool.isBefore(clickCell, currentItem)) {
                    if(currentItem.mStartDayCell != null && currentItem.mEndDayCell == null) {
                        affectItem = currentItem;
                    } else {
                        affectItem = null;
                    }
                    break;
                } else if(index == afterSelectItemList.size() - 1 && CalendarTool.isAfter(clickCell, currentItem)) {
                    if(currentItem.mStartDayCell != null && currentItem.mEndDayCell == null) {
                        affectItem = currentItem;
                    } else {
                        affectItem = null;
                    }
                    break;
                } else if(CalendarTool.isInClusive(clickCell, lastItem)) {
                    affectItem = lastItem;
                    break;
                } else if(CalendarTool.isInClusive(clickCell, currentItem)) {
                    affectItem = currentItem;
                    break;
                } else if(CalendarTool.isAfter(clickCell, lastItem) && CalendarTool.isBefore(clickCell, currentItem)) {
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
            ContinuousSelectItem<T> afterItem = handleContinuousItem(clickCell, affectItem, false, false);
            if(affectItem != null) {
                afterSelectItemList.remove(affectItem);
            }
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

    public final ClickPolicyForMixMulti<T> MIX_MULTI_POLICY_1 = new ClickPolicyForMixMulti<T>() {
        @Override
        public boolean handleClickForMixMulti(ICalendarManager<T> calendarManager, DayCell<T> clickCell, List<ContinuousSelectItem<T>> beforeSelectItemList) {
            if(!checkParamterValid(calendarManager, clickCell)) {
                //参数无效，不会做任何处理，所以不需要刷新View
                return true;
            }
            CalendarTool.sortContinuousItemList(beforeSelectItemList, true);
            CalendarTool.clearNullContinuousItem(beforeSelectItemList);
            List<ContinuousSelectItem<T>> afterSelectItemList = new ArrayList<ContinuousSelectItem<T>>();
            if(beforeSelectItemList != null) {
                afterSelectItemList.addAll(beforeSelectItemList);
            }
            ContinuousSelectItem<T> lastItem;
            ContinuousSelectItem<T> currentItem = null;

            int index = -1;
            ContinuousSelectItem<T> affectItem = null;
            while (index + 1 < afterSelectItemList.size()) {
                index++;
                lastItem = currentItem;
                currentItem = afterSelectItemList.get(index);
                CalendarTool.setContinuousItemValid(currentItem);
                if(currentItem == null || (currentItem.mStartDayCell == null && currentItem.mEndDayCell == null)) {
                    continue;
                }
                if(index == 0 && CalendarTool.isBefore(clickCell, currentItem)) {
                    if(currentItem.mStartDayCell != null && currentItem.mEndDayCell == null) {
                        affectItem = currentItem;
                    } else {
                        affectItem = null;
                    }
                    break;
                } else if(index == afterSelectItemList.size() - 1 && CalendarTool.isAfter(clickCell, currentItem)) {
                    if(currentItem.mStartDayCell != null && currentItem.mEndDayCell == null) {
                        affectItem = currentItem;
                    } else {
                        affectItem = null;
                    }
                    break;
                } else if(CalendarTool.isInClusive(clickCell, lastItem)) {
                    affectItem = lastItem;
                    break;
                } else if(CalendarTool.isInClusive(clickCell, currentItem)) {
                    affectItem = currentItem;
                    break;
                } else if(CalendarTool.isAfter(clickCell, lastItem) && CalendarTool.isBefore(clickCell, currentItem)) {
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
            ContinuousSelectItem<T> afterItem = handleContinuousItem(clickCell, affectItem, true, false);
            if(affectItem != null) {
                afterSelectItemList.remove(affectItem);
            }
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

    /**
     * SELECT_MODE_CONTINUOUS/SELECT_MODE_CONTINUOUS_MULTI/SELECT_MODE_MIX/SELECT_MODE_MIX_MULTI的点击处理
     * @param clickCell 点击Cell
     * @param beforeItem 点击之前的Item
     * @param isMix 是混合模式（包括多个混合）
     * @param canCancel beforeItem的开始和结束日期都不为空时，且点击Cell不等于开始/结束日期时，是否重置Item，重新选择
     * @return 处理后的Item
     */
    private ContinuousSelectItem<T> handleContinuousItem(DayCell<T> clickCell, ContinuousSelectItem<T> beforeItem, boolean isMix, boolean canCancel) {
        if(clickCell == null) {
            LogUtil.i(LOG_TAG, "handleContinuousItem:clickCell is null");
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
            afterItem.mStartDayCell = clickCell.getCopyDayCell();
        } else if(beforeItem.mStartDayCell != null && beforeItem.mEndDayCell == null) {
            if(CalendarTool.isEqual(clickCell, beforeItem.mStartDayCell) && !isMix) {
                afterItem = null;
            } else {
                afterItem.mEndDayCell = clickCell.getCopyDayCell();
            }
        } else {
            if(CalendarTool.isEqual(beforeItem.mStartDayCell, beforeItem.mEndDayCell) &&
                    CalendarTool.isEqual(beforeItem.mStartDayCell, clickCell)) {
                afterItem = null;
            } else if(CalendarTool.isEqual(beforeItem.mStartDayCell, clickCell)) {
                afterItem.mStartDayCell = null;
            } else if(CalendarTool.isEqual(beforeItem.mEndDayCell, clickCell)) {
                afterItem.mEndDayCell = null;
            } else {
                if(canCancel) {
                    afterItem.mStartDayCell = clickCell.getCopyDayCell();
                    afterItem.mEndDayCell = null;
                }
            }
        }
        CalendarTool.setContinuousItemValid(afterItem);
        return afterItem;
    }

    private boolean checkParamterValid(ICalendarManager<T> calendarManager, DayCell<T> clickCell) {
        if(calendarManager == null) {
            LogUtil.w(LOG_TAG, "SINGLE_POLICY_2.handleClickForSingle:calendarManager is null");
            //没有任何改变，不需要刷新View
            return false;
        } else if(clickCell == null) {
            LogUtil.w(LOG_TAG, "SINGLE_POLICY_2.handleClickForSingle:clickCell is null");
            //点击Cell为null，没有任何改变，不需要刷新View
            return false;
        }
        return true;
    }

}
