package com.xing.android.calendar.process;

import com.xing.android.calendar.ICalendarManager;
import com.xing.android.calendar.model.ContinuousSelectItem;
import com.xing.android.calendar.model.DayCell;

import java.util.List;

/**
 * Created by zxx09506 on 2016/11/8.
 */

public class DayCellClickPolicyInfo<T> {

    protected ClickPolicyForSingle<T> mSinglePolicy;
    protected ClickPolicyForMulti<T> mMultiPolicy;
    protected ClickPolicyForContinuous<T> mContinuousPolicy;
    protected ClickPolicyForMix<T> mMixPolicy;
    protected ClickPolicyForContinuousMulti<T> mContinuousMultiPolicy;
    protected ClickPolicyForMixMulti<T> mMixMultiPolicy;

    public ClickPolicyForSingle<T> getSinglePolicy() {
        return mSinglePolicy;
    }

    public void setSinglePolicy(ClickPolicyForSingle<T> singlePolicy) {
        this.mSinglePolicy = singlePolicy;
    }

    public ClickPolicyForMulti<T> getMultiPolicy() {
        return mMultiPolicy;
    }

    public void setMultiPolicy(ClickPolicyForMulti<T> multiPolicy) {
        this.mMultiPolicy = multiPolicy;
    }

    public ClickPolicyForContinuous<T> getContinuousPolicy() {
        return mContinuousPolicy;
    }

    public void setContinuousPolicy(ClickPolicyForContinuous<T> continuousPolicy) {
        this.mContinuousPolicy = continuousPolicy;
    }

    public ClickPolicyForMix<T> getMixPolicy() {
        return mMixPolicy;
    }

    public void setMixPolicy(ClickPolicyForMix<T> mixPolicy) {
        this.mMixPolicy = mixPolicy;
    }

    public ClickPolicyForContinuousMulti<T> getContinuousMultiPolicy() {
        return mContinuousMultiPolicy;
    }

    public void setContinuousMultiPolicy(ClickPolicyForContinuousMulti<T> continuousMultiPolicy) {
        this.mContinuousMultiPolicy = continuousMultiPolicy;
    }

    public ClickPolicyForMixMulti<T> getMixMultiPolicy() {
        return mMixMultiPolicy;
    }

    public void setMixMultiPolicy(ClickPolicyForMixMulti<T> mixMultiPolicy) {
        this.mMixMultiPolicy = mixMultiPolicy;
    }

    /**
     * SELECT_MODE_SINGLE的点击交互处理
     * @param <T>
     */
    public interface ClickPolicyForSingle<T> {
        /**
         * SELECT_MODE_SINGLE的点击处理
         * @param calendarManager
         * @param clickCell 点击Cell
         * @param beforeSelectCell 点击之前的selectCell
         * @return 处理之后有没有刷新ICalendarView，如果返回false，ICalendarManager统一刷新所有View，建议在该方法内刷新影响到的ICalendarView
         */
        boolean handleClickForSingle(ICalendarManager<T> calendarManager, DayCell<T> clickCell, DayCell<T> beforeSelectCell);
    }

    /**
     * SELECT_MODE_MULTI的点击交互处理
     * @param <T>
     */
    public interface ClickPolicyForMulti<T> {
        /**
         * SELECT_MODE_MULTI的点击处理
         * @param clickCell 点击Cell
         * @param beforeSelectList 点击之前的selectCellList
         * @return 处理之后有没有刷新ICalendarView，如果返回false，ICalendarManager统一刷新所有View，建议在该方法内刷新影响到的ICalendarView
         */
        boolean handleClickForMulti(ICalendarManager<T> calendarManager, DayCell<T> clickCell, List<DayCell<T>> beforeSelectList);
    }

    /**
     * SELECT_MODE_CONTINUOUS的点击交互处理
     * @param <T>
     */
    public interface ClickPolicyForContinuous<T> {
        /**
         * SELECT_MODE_CONTINUOUS的点击处理
         * @param calendarManager
         * @param clickCell 点击Cell
         * @param beforeSelectItem 点击之前的continuousItem
         * @return 处理之后有没有刷新ICalendarView，如果返回false，ICalendarManager统一刷新所有View，建议在该方法内刷新影响到的ICalendarView
         */
        boolean handleClickForContinuous(ICalendarManager<T> calendarManager, DayCell<T> clickCell, ContinuousSelectItem<T> beforeSelectItem);
    }

    /**
     * SELECT_MODE_MIX的点击交互处理
     * @param <T>
     */
    public interface ClickPolicyForMix<T> {
        /**
         * SELECT_MODE_MIX的点击处理
         * @param calendarManager
         * @param clickCell 点击Cell
         * @param beforeSelectItem 点击之前的continuousItem
         * @return 处理之后有没有刷新ICalendarView，如果返回false，ICalendarManager统一刷新所有View，建议在该方法内刷新影响到的ICalendarView
         */
        boolean handleClickForMix(ICalendarManager<T> calendarManager, DayCell<T> clickCell, ContinuousSelectItem<T> beforeSelectItem);
    }

    /**
     * SELECT_MODE_CONTINUOUS_MULTI的点击交互处理
     * @param <T>
     */
    public interface ClickPolicyForContinuousMulti<T> {
        /**
         * SELECT_MODE_CONTINUOUS_MULTI的点击处理
         * @param calendarManager
         * @param clickCell 点击Cell
         * @param beforeSelectItemList 点击之前的continuousItemList
         * @return 处理之后有没有刷新ICalendarView，如果返回false，ICalendarManager统一刷新所有View，建议在该方法内刷新影响到的ICalendarView
         */
        boolean handleClickForContinuousMulti(ICalendarManager<T> calendarManager, DayCell<T> clickCell, List<ContinuousSelectItem<T>> beforeSelectItemList);
    }

    /**
     * SELECT_MODE_MIX_MULTI的点击交互处理
     * @param <T>
     */
    public interface ClickPolicyForMixMulti<T> {
        /**
         * SELECT_MODE_MIX_MULTI的点击处理
         * @param calendarManager
         * @param clickCell 点击Cell
         * @param beforeSelectItemList 点击之前的continuousItemList
         * @return 处理之后有没有刷新ICalendarView，如果返回false，ICalendarManager统一刷新所有View，建议在该方法内刷新影响到的ICalendarView
         */
        boolean handleClickForMixMulti(ICalendarManager<T> calendarManager, DayCell<T> clickCell, List<ContinuousSelectItem<T>> beforeSelectItemList);
    }
}
