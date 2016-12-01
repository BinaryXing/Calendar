package com.xing.android.calendar.process;

import com.xing.android.calendar.ICalendarManager;
import com.xing.android.calendar.model.ContinuousSelectItem;
import com.xing.android.calendar.model.DayCell;

import java.util.List;

/**
 * DayCell处理策略类
 * Created by zxx09506 on 2016/11/8.
 */

public class DayCellHandlePolicyInfo<T> {

    protected PolicyForSingle<T> mSinglePolicy;
    protected PolicyForMulti<T> mMultiPolicy;
    protected PolicyForContinuous<T> mContinuousPolicy;
    protected PolicyForMix<T> mMixPolicy;
    protected PolicyForContinuousMulti<T> mContinuousMultiPolicy;
    protected PolicyForMixMulti<T> mMixMultiPolicy;

    public PolicyForSingle<T> getSinglePolicy() {
        return mSinglePolicy;
    }

    public void setSinglePolicy(PolicyForSingle<T> singlePolicy) {
        this.mSinglePolicy = singlePolicy;
    }

    public PolicyForMulti<T> getMultiPolicy() {
        return mMultiPolicy;
    }

    public void setMultiPolicy(PolicyForMulti<T> multiPolicy) {
        this.mMultiPolicy = multiPolicy;
    }

    public PolicyForContinuous<T> getContinuousPolicy() {
        return mContinuousPolicy;
    }

    public void setContinuousPolicy(PolicyForContinuous<T> continuousPolicy) {
        this.mContinuousPolicy = continuousPolicy;
    }

    public PolicyForMix<T> getMixPolicy() {
        return mMixPolicy;
    }

    public void setMixPolicy(PolicyForMix<T> mixPolicy) {
        this.mMixPolicy = mixPolicy;
    }

    public PolicyForContinuousMulti<T> getContinuousMultiPolicy() {
        return mContinuousMultiPolicy;
    }

    public void setContinuousMultiPolicy(PolicyForContinuousMulti<T> continuousMultiPolicy) {
        this.mContinuousMultiPolicy = continuousMultiPolicy;
    }

    public PolicyForMixMulti<T> getMixMultiPolicy() {
        return mMixMultiPolicy;
    }

    public void setMixMultiPolicy(PolicyForMixMulti<T> mixMultiPolicy) {
        this.mMixMultiPolicy = mixMultiPolicy;
    }

    /**
     * SELECT_MODE_SINGLE的处理策略
     * @param <T>
     */
    public interface PolicyForSingle<T> {
        /**
         * SELECT_MODE_SINGLE的处理策略
         * @param calendarManager
         * @param factorCell 因子Cell（可能由Click，LongClick，Touch判定生成）
         * @param beforeSelectCell 点击之前的selectCell
         * @return 处理之后有没有刷新ICalendarView，如果返回false，ICalendarManager统一刷新所有View，建议在该方法内刷新影响到的ICalendarView
         */
        boolean handleForSingle(ICalendarManager<T> calendarManager, DayCell<T> factorCell, DayCell<T> beforeSelectCell);
    }

    /**
     * SELECT_MODE_MULTI的处理策略
     * @param <T>
     */
    public interface PolicyForMulti<T> {
        /**
         * SELECT_MODE_MULTI的处理策略
         * @param factorCell 因子Cell（可能由Click，LongClick，Touch判定生成）
         * @param beforeSelectList 点击之前的selectCellList
         * @return 处理之后有没有刷新ICalendarView，如果返回false，ICalendarManager统一刷新所有View，建议在该方法内刷新影响到的ICalendarView
         */
        boolean handleForMulti(ICalendarManager<T> calendarManager, DayCell<T> factorCell, List<DayCell<T>> beforeSelectList);
    }

    /**
     * SELECT_MODE_CONTINUOUS的处理策略
     * @param <T>
     */
    public interface PolicyForContinuous<T> {
        /**
         * SELECT_MODE_CONTINUOUS的处理策略
         * @param calendarManager
         * @param factorCell 因子Cell（可能由Click，LongClick，Touch判定生成）
         * @param beforeSelectItem 点击之前的continuousItem
         * @return 处理之后有没有刷新ICalendarView，如果返回false，ICalendarManager统一刷新所有View，建议在该方法内刷新影响到的ICalendarView
         */
        boolean handleForContinuous(ICalendarManager<T> calendarManager, DayCell<T> factorCell, ContinuousSelectItem<T> beforeSelectItem);
    }

    /**
     * SELECT_MODE_MIX的处理策略
     * @param <T>
     */
    public interface PolicyForMix<T> {
        /**
         * SELECT_MODE_MIX的处理策略
         * @param calendarManager
         * @param factorCell 因子Cell（可能由Click，LongClick，Touch判定生成）
         * @param beforeSelectItem 点击之前的continuousItem
         * @return 处理之后有没有刷新ICalendarView，如果返回false，ICalendarManager统一刷新所有View，建议在该方法内刷新影响到的ICalendarView
         */
        boolean handleForMix(ICalendarManager<T> calendarManager, DayCell<T> factorCell, ContinuousSelectItem<T> beforeSelectItem);
    }

    /**
     * SELECT_MODE_CONTINUOUS_MULTI的处理策略
     * @param <T>
     */
    public interface PolicyForContinuousMulti<T> {
        /**
         * SELECT_MODE_CONTINUOUS_MULTI的处理策略
         * @param calendarManager
         * @param factorCell 因子Cell（可能由Click，LongClick，Touch判定生成）
         * @param beforeSelectItemList 点击之前的continuousItemList
         * @return 处理之后有没有刷新ICalendarView，如果返回false，ICalendarManager统一刷新所有View，建议在该方法内刷新影响到的ICalendarView
         */
        boolean handleForContinuousMulti(ICalendarManager<T> calendarManager, DayCell<T> factorCell, List<ContinuousSelectItem<T>> beforeSelectItemList);
    }

    /**
     * SELECT_MODE_MIX_MULTI的处理策略
     * @param <T>
     */
    public interface PolicyForMixMulti<T> {
        /**
         * SELECT_MODE_MIX_MULTI的处理策略
         * @param calendarManager
         * @param factorCell 因子Cell（可能由Click，LongClick，Touch判定生成）
         * @param beforeSelectItemList 点击之前的continuousItemList
         * @return 处理之后有没有刷新ICalendarView，如果返回false，ICalendarManager统一刷新所有View，建议在该方法内刷新影响到的ICalendarView
         */
        boolean handleForMixMulti(ICalendarManager<T> calendarManager, DayCell<T> factorCell, List<ContinuousSelectItem<T>> beforeSelectItemList);
    }
}
