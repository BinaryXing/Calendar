package com.xing.android.calendar;

import com.xing.android.calendar.model.ContinuousSelectItem;
import com.xing.android.calendar.model.DayCell;
import com.xing.android.calendar.process.DayCellHandlePolicyImp;
import com.xing.android.calendar.process.DayCellHandlePolicyInfo;
import com.xing.android.calendar.process.DayCellHandlePolicyInfo.PolicyForContinuous;
import com.xing.android.calendar.process.DayCellHandlePolicyInfo.PolicyForContinuousMulti;
import com.xing.android.calendar.process.DayCellHandlePolicyInfo.PolicyForMix;
import com.xing.android.calendar.process.DayCellHandlePolicyInfo.PolicyForMixMulti;
import com.xing.android.calendar.process.DayCellHandlePolicyInfo.PolicyForMulti;
import com.xing.android.calendar.process.DayCellHandlePolicyInfo.PolicyForSingle;
import com.xing.android.calendar.process.DayCellUserInterfaceImp;
import com.xing.android.calendar.process.DayCellUserInterfaceInfo;
import com.xing.android.calendar.util.LogUtil;
import com.xing.android.calendar.view.ICalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zhaoxx on 16/3/10.
 */
public class CalendarManager<T> implements ICalendarManager<T> {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private int mSelectMode = CalendarConstant.SELECT_MODE_SINGLE;

    private int mFirstDayOfWeek = Calendar.SUNDAY;

    private List<ICalendarView<T>> mCalendarViewList = new ArrayList<ICalendarView<T>>();

    private DayCellHandlePolicyInfo<T> mClickPolicy = new DayCellHandlePolicyInfo<T>();

    /**
     * 用于保存SELECT_MODE_SINGLE模式时的数据
     */
    private DayCell<T> mSelectedDayCell;
    /**
     * 用于保存SELECT_MODE_MULTI模式时的数据
     */
    private List<DayCell<T>> mSelectedDayCellList = new ArrayList<DayCell<T>>();
    /**
     * 用于保存SELECT_MODE_CONTINUOUS,SELECT_MODE_MIX模式时的数据
     */
    private ContinuousSelectItem<T> mSelectedContinuousItem;
    /**
     * 用于保存SELECT_MODE_CONTINUOUS_MULTI,SELECT_MODE_MIX_MULTI,模式时的数据
     */
    private List<ContinuousSelectItem<T>> mSelectedContinuousItemList = new ArrayList<ContinuousSelectItem<T>>();

    /**
     * 是否点击交互开启
     */
    private boolean isClickEnable = true;
    /**
     * 是否长按交互开启
     */
    private boolean isLongClickEnable = false;
    /**
     * 是否Touch交互开启
     */
    private boolean isTouchEnable = false;

    private DayCellUserInterfaceInfo<T> mUserInterfaceInfo;

    private ICalendarManagerListener<T> mListener;

    public CalendarManager() {
        DayCellHandlePolicyImp<T> policyImp = new DayCellHandlePolicyImp<T>();
        setSinglePolicy(policyImp.SINGLE_POLICY_1);
        setMultiPolicy(policyImp.MULTI_POLICY_1);
        setContinuousPolicy(policyImp.CONTINUOUS_POLICY_1);
        setContinuousMultiPolicy(policyImp.CONTINUOUS_MULTI_POLICY_1);
        setMixPolicy(policyImp.MIX_POLICY_1);
        setMixMultiPolicy(policyImp.MIX_MULTI_POLICY_1);
        DayCellUserInterfaceImp<T> userInterfaceImp = new DayCellUserInterfaceImp<T>();
        getDayCellUserInterfaceInfo().setClickListener(userInterfaceImp.CLICK_1);
        getDayCellUserInterfaceInfo().setLongClickListener(userInterfaceImp.LONG_CLICK_1);
    }

    @Override
    public int getSelectMode() {
        return mSelectMode;
    }

    @Override
    public void setSelectMode(int mode) {
        if(mSelectMode != mode && mode >= CalendarConstant.SELECT_MODE_MIN && mode <= CalendarConstant.SELECT_MODE_MAX) {
            mSelectMode = mode;
            switch (mSelectMode) {
                case CalendarConstant.SELECT_MODE_SINGLE:
                    setSelectedDaycell(null, true);
                    break;
                case CalendarConstant.SELECT_MODE_MULTI:
                    setSelectedDayCellList(null, true);
                    break;
                case CalendarConstant.SELECT_MODE_CONTINUOUS:
                case CalendarConstant.SELECT_MODE_MIX:
                    setSelectedContinuousItem(null, true);
                    break;
                case CalendarConstant.SELECT_MODE_CONTINUOUS_MULTI:
                case CalendarConstant.SELECT_MODE_MIX_MULTI:
                    setSelectedContinuousItemList(null, true);
                    break;
                default:
                    break;
            }
        } else {
            LogUtil.w(LOG_TAG, "setSelectMode:mSelectMode = " + mSelectMode + ",mode = " + mode);
        }
    }

    @Override
    public int getFirstDayOfWeek() {
        return mFirstDayOfWeek;
    }

    @Override
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        firstDayOfWeek = CalendarTool.getValidFirstDayOfWeek(firstDayOfWeek);
        if(mFirstDayOfWeek == firstDayOfWeek) {
            LogUtil.i(LOG_TAG, "setFirstDayOfWeek:equal data,mFirstDayOfWeek = " + mFirstDayOfWeek);
            return;
        }
        mFirstDayOfWeek = firstDayOfWeek;
        for(ICalendarView<T> calendarView : mCalendarViewList) {
            if(calendarView != null) {
                calendarView.setFirstDayOfWeek(mFirstDayOfWeek);
            }
        }
        setSelectedDaycell(null, false);
        setSelectedDayCellList(null, false);
        setSelectedContinuousItem(null, false);
        setSelectedContinuousItemList(null, false);
        refreshICalendarViewList();
    }

    @Override
    public void setSinglePolicy(PolicyForSingle<T> policy) {
        if(policy == null) {
            LogUtil.w(LOG_TAG, "setSinglePolicy:policy is null");
            return;
        }
        if(mClickPolicy == null) {
            mClickPolicy = new DayCellHandlePolicyInfo<T>();
        }
        mClickPolicy.setSinglePolicy(policy);
    }

    @Override
    public void setMultiPolicy(PolicyForMulti<T> policy) {
        if(policy == null) {
            LogUtil.w(LOG_TAG, "setMultiPolicy:policy is null");
            return;
        }
        if(mClickPolicy == null) {
            mClickPolicy = new DayCellHandlePolicyInfo<T>();
        }
        mClickPolicy.setMultiPolicy(policy);
    }

    @Override
    public void setContinuousPolicy(PolicyForContinuous<T> policy) {
        if(policy == null) {
            LogUtil.w(LOG_TAG, "setContinuousPolicy:policy is null");
            return;
        }
        if(mClickPolicy == null) {
            mClickPolicy = new DayCellHandlePolicyInfo<T>();
        }
        mClickPolicy.setContinuousPolicy(policy);
    }

    @Override
    public void setContinuousMultiPolicy(PolicyForContinuousMulti<T> policy) {
        if(policy == null) {
            LogUtil.w(LOG_TAG, "setContinuousMultiPolicy:policy is null");
            return;
        }
        if(mClickPolicy == null) {
            mClickPolicy = new DayCellHandlePolicyInfo<T>();
        }
        mClickPolicy.setContinuousMultiPolicy(policy);
    }

    @Override
    public void setMixPolicy(PolicyForMix<T> policy) {
        if(policy == null) {
            LogUtil.w(LOG_TAG, "setMixPolicy:policy is null");
            return;
        }
        if(mClickPolicy == null) {
            mClickPolicy = new DayCellHandlePolicyInfo<T>();
        }
        mClickPolicy.setMixPolicy(policy);
    }

    @Override
    public void setMixMultiPolicy(PolicyForMixMulti<T> policy) {
        if(policy == null) {
            LogUtil.w(LOG_TAG, "setMixMultiPolicy:policy is null");
            return;
        }
        if(mClickPolicy == null) {
            mClickPolicy = new DayCellHandlePolicyInfo<T>();
        }
        mClickPolicy.setMixMultiPolicy(policy);
    }

    @Override
    public void setICalendarManagerListener(ICalendarManagerListener<T> listener) {
        mListener = listener;
    }

    @Override
    public List<ICalendarView<T>> getCalendarViewList() {
        if(mCalendarViewList == null) {
            mCalendarViewList = new ArrayList<ICalendarView<T>>();
        }
        return mCalendarViewList;
    }

    @Override
    public void addCalendarView(ICalendarView calendarView) {
        if(calendarView == null) {
            LogUtil.i(LOG_TAG, "addCalendarView:calendarView is null");
            return;
        }
        calendarView.setCalendarManager(this);
        calendarView.setFirstDayOfWeek(mFirstDayOfWeek);
        mCalendarViewList.add(calendarView);
    }

    @Override
    public void addCalendarViewList(List<ICalendarView<T>> calendarViewList) {
        if (calendarViewList == null || calendarViewList.size() == 0) {
            LogUtil.i(LOG_TAG, "addCalendarViewList:calendarViewList is emtpy");
            return;
        }
        for (ICalendarView calendarView : calendarViewList) {
            if (calendarView == null) {
                continue;
            }
            calendarView.setCalendarManager(this);
            calendarView.setFirstDayOfWeek(mFirstDayOfWeek);
            mCalendarViewList.add(calendarView);
        }
    }

    @Override
    public void setCalendarViewList(List<ICalendarView<T>> calendarViewList) {
        mCalendarViewList.clear();
        if (calendarViewList == null || calendarViewList.size() == 0) {
            LogUtil.i(LOG_TAG, "setCalendarViewList:calendarViewList is emtpy");
            return;
        }
        for (ICalendarView<T> calendarView : calendarViewList) {
            if(calendarView == null) {
                continue;
            }
            calendarView.setCalendarManager(this);
            calendarView.setFirstDayOfWeek(mFirstDayOfWeek);
            mCalendarViewList.add(calendarView);
        }
    }

    @Override
    public void refreshICalendarViewList() {
        if(mCalendarViewList == null || mCalendarViewList.size() == 0) {
            LogUtil.i(LOG_TAG, "refreshCalendarViewList:mCalendarViewList is empty");
            return;
        }
        for(ICalendarView<T> calendarView : mCalendarViewList) {
            if(calendarView == null) {
                continue;
            }
            refreshCalendarView(calendarView);
        }
    }

    private void refreshCalendarView(ICalendarView<T> calendarView) {
        if(calendarView == null) {
            LogUtil.w(LOG_TAG, "refreshCalendarView:calendarView is null");
            return;
        }
        switch (mSelectMode) {
            case CalendarConstant.SELECT_MODE_SINGLE:
                calendarView.onDayCellChanged(mSelectedDayCell, true);
                break;
            case CalendarConstant.SELECT_MODE_MULTI:
                calendarView.onDayCellListChanged(mSelectedDayCellList, true);
                break;
            case CalendarConstant.SELECT_MODE_CONTINUOUS:
            case CalendarConstant.SELECT_MODE_MIX:
                calendarView.onContinuousItemChanged(mSelectedContinuousItem, true);
                break;
            case CalendarConstant.SELECT_MODE_CONTINUOUS_MULTI:
            case CalendarConstant.SELECT_MODE_MIX_MULTI:
                calendarView.onContinuousItemListChanges(mSelectedContinuousItemList, true);
                break;
            default:
                LogUtil.w(LOG_TAG, "refreshCalendarView:invalid select mode, mSelectMode = " + mSelectMode);
                break;
        }
    }

    @Override
    public void refreshAffectViewList(DayCell<T> afterCell, DayCell<T> beforeCell) {
        if(mSelectMode != CalendarConstant.SELECT_MODE_SINGLE && mSelectMode != CalendarConstant.SELECT_MODE_MULTI) {
            LogUtil.w(LOG_TAG, "refreshAffectViewList:select mode not match, mSelectMode = " + mSelectMode);
            return;
        }
        List<ICalendarView<T>> refreshCalendarViewList = new ArrayList<ICalendarView<T>>();
        for(ICalendarView<T> calendarView : mCalendarViewList) {
            if(calendarView == null) {
                continue;
            }
            if(calendarView.isAffect(afterCell, beforeCell)) {
                refreshCalendarView(calendarView);
            }
        }
    }

    @Override
    public void refreshAffectViewList(ContinuousSelectItem<T> afterItem, ContinuousSelectItem<T> beforeItem) {
        if(mSelectMode != CalendarConstant.SELECT_MODE_CONTINUOUS &&
                mSelectMode != CalendarConstant.SELECT_MODE_MIX &&
                mSelectMode != CalendarConstant.SELECT_MODE_CONTINUOUS_MULTI &&
                mSelectMode != CalendarConstant.SELECT_MODE_MIX_MULTI) {
            LogUtil.w(LOG_TAG, "refreshAffectViewList:select mode not match, mSelectMode = " + mSelectMode);
            return;
        }
        for(ICalendarView<T> calendarView : mCalendarViewList) {
            if(calendarView == null) {
                continue;
            }
            if(calendarView.isAffect(afterItem, beforeItem)) {
                refreshCalendarView(calendarView);
            }
        }
    }

    @Override
    public DayCell<T> getSelectedDayCell() {
        return mSelectedDayCell;
    }

    @Override
    public void setSelectedDaycell(DayCell<T> cell, boolean notifyView) {
        if(mSelectMode != CalendarConstant.SELECT_MODE_SINGLE) {
            LogUtil.w(LOG_TAG, "setSelectedDaycell:select mode not match, mSelectMode = " + mSelectMode);
            return;
        }
        mSelectedDayCell = cell;
        if(notifyView) {
            for (ICalendarView<T> calendarView : mCalendarViewList) {
                if (calendarView == null) {
                    continue;
                }
                calendarView.onDayCellChanged(mSelectedDayCell, true);
            }
        }
    }

    @Override
    public List<DayCell<T>> getSelectedDayCellList() {
        return mSelectedDayCellList;
    }

    @Override
    public void setSelectedDayCellList(List<DayCell<T>> cellList, boolean notifyView) {
        if (mSelectMode != CalendarConstant.SELECT_MODE_MULTI) {
            LogUtil.w(LOG_TAG, "setSelectedDayCellList:mode not match, mode = " + mSelectMode);
            return;
        }

        mSelectedDayCellList.clear();
        if (cellList != null && cellList.size() > 0) {
            mSelectedDayCellList.addAll(cellList);
        }
        if(notifyView) {
            for (ICalendarView<T> calendarView : mCalendarViewList) {
                if (calendarView == null) {
                    continue;
                }
                calendarView.onDayCellListChanged(mSelectedDayCellList, true);
            }
        }
    }

    @Override
    public ContinuousSelectItem<T> getSelectedContinuousItem() {
        return mSelectedContinuousItem;
    }

    @Override
    public void setSelectedContinuousItem(ContinuousSelectItem<T> item, boolean notifyView) {
        if (mSelectMode != CalendarConstant.SELECT_MODE_CONTINUOUS && mSelectMode != CalendarConstant.SELECT_MODE_MIX) {
            LogUtil.w(LOG_TAG, "setSelectedContinuousItem:mode not match, mode = " + mSelectMode);
            return;
        }

        mSelectedContinuousItem = item == null ? null : item.getCopyContinuousSelectItem();
        if(notifyView) {
            for (ICalendarView<T> calendarView : mCalendarViewList) {
                if (calendarView == null) {
                    continue;
                }
                calendarView.onContinuousItemChanged(mSelectedContinuousItem, true);
            }
        }
    }

    @Override
    public List<ContinuousSelectItem<T>> getSelectedContinuousItemList() {
        return mSelectedContinuousItemList;
    }

    @Override
    public void setSelectedContinuousItemList(List<ContinuousSelectItem<T>> itemList, boolean notifyView) {
        if (mSelectMode != CalendarConstant.SELECT_MODE_CONTINUOUS_MULTI && mSelectMode != CalendarConstant.SELECT_MODE_MIX_MULTI) {
            LogUtil.w(LOG_TAG, "setSelectedContinuousItemList:mode not match, mode = " + mSelectMode);
            return;
        }

        mSelectedContinuousItemList.clear();
        if (itemList != null && itemList.size() > 0) {
            mSelectedContinuousItemList.addAll(itemList);
        }
        if(notifyView) {
            for (ICalendarView<T> calendarView : mCalendarViewList) {
                if (calendarView == null) {
                    continue;
                }
                calendarView.onContinuousItemListChanges(mSelectedContinuousItemList, true);
            }
        }
    }

    @Override
    public void onDayCellHandle(DayCell<T> dayCell) {
        if(dayCell == null) {
            LogUtil.w(LOG_TAG, "onDayCellHandle:dayCell is null");
            return;
        }
        if(mListener != null && mListener.blockDayCellClick(dayCell)) {
            LogUtil.i(LOG_TAG, "onDayCellHandle:block dayCell = " + dayCell.toString());
            return;
        }
        if (dayCell.getDayStatus() == CalendarConstant.DAY_STATUS_INVALID) {
            LogUtil.i(LOG_TAG, "onDayCellHandle:dayCell.getDayStatus() is invalid, dayCell = " + dayCell.toString());
            return;
        }
        switch (mSelectMode) {
            case CalendarConstant.SELECT_MODE_SINGLE:
                handleDayCellForSingle(dayCell);
                break;
            case CalendarConstant.SELECT_MODE_MULTI:
                handleDayCellForMulti(dayCell);
                break;
            case CalendarConstant.SELECT_MODE_CONTINUOUS:
                handleDayCellForContinuous(dayCell);
                break;
            case CalendarConstant.SELECT_MODE_CONTINUOUS_MULTI:
                handleDayCellForContinuousMulti(dayCell);
                break;
            case CalendarConstant.SELECT_MODE_MIX:
                handleDayCellForMix(dayCell);
                break;
            case CalendarConstant.SELECT_MODE_MIX_MULTI:
                handleDayCellForMixMulti(dayCell);
                break;
        }
        if(mListener != null) {
            mListener.onPostDayCellClick(dayCell);
        }
    }

    @Override
    public void setData(List<DayCell<T>> dataList) {
        CalendarTool.sortDayCellList(dataList, true);
        for(ICalendarView<T> calendarView : mCalendarViewList) {
            if(calendarView == null) {
                continue;
            }
            calendarView.setData(dataList);
        }
    }

    @Override
    public void onBindData(DayCell<T> dayCell) {
        if(mListener != null) {
            mListener.onBindData(dayCell);
        }
    }

    @Override
    public void iterator() {
        if(mCalendarViewList == null || mCalendarViewList.size() == 0) {
            LogUtil.i(LOG_TAG, "foreach:mCalendarViewList is empty");
            return;
        }
        for(ICalendarView<T> view : mCalendarViewList) {
            if(view == null) {
                continue;
            }
            view.iterator();
            view.refresh();
        }
    }

    @Override
    public void onIterator(DayCell<T> dayCell) {
        if(mListener != null) {
            mListener.onIterator(dayCell);
        }
    }

    /**
     * 定制交互功能还未完成，不建议使用该方法
     * @return
     */
    @Deprecated
    @Override
    public boolean isClickEnable() {
        return isClickEnable;
    }

    /**
     * 定制交互功能还未完成，不建议使用该方法
     * @param value
     */
    @Deprecated
    @Override
    public void setClickEnable(boolean value) {
        if(isClickEnable == value) {
            LogUtil.i(LOG_TAG, "setClickEnable:equal data,value = " + value);
            return;
        }
        isClickEnable = value;
        for(ICalendarView<T> iCalendarView : mCalendarViewList) {
            if(iCalendarView != null) {
                iCalendarView.refresh();
            }
        }
    }

    /**
     * 定制交互功能还未完成，不建议使用该方法
     * @return
     */
    @Deprecated
    @Override
    public boolean isLongClickEnable() {
        return isLongClickEnable;
    }

    /**
     * 定制交互功能还未完成，不建议使用该方法
     * @param value
     */
    @Deprecated
    @Override
    public void setLongClickEnable(boolean value) {
        if(isLongClickEnable == value) {
            LogUtil.i(LOG_TAG, "setLongClickEnable:equal data,value = " + value);
            return;
        }
        isLongClickEnable = value;
        for(ICalendarView<T> iCalendarView : mCalendarViewList) {
            if(iCalendarView != null) {
                iCalendarView.refresh();
            }
        }
    }

    /**
     * 定制交互功能还未完成，不建议使用该方法
     * @return
     */
    @Deprecated
    @Override
    public boolean isTouchEnable() {
        return isTouchEnable;
    }

    /**
     * 定制交互功能还未完成，不建议使用该方法
     * @param value
     */
    @Deprecated
    @Override
    public void setTouchEnable(boolean value) {
        if(isTouchEnable == value) {
            LogUtil.i(LOG_TAG, "setTouchEnable:equal data,value = " + value);
            return;
        }
        isTouchEnable = value;
        for(ICalendarView<T> iCalendarView : mCalendarViewList) {
            if(iCalendarView != null) {
                iCalendarView.refresh();
            }
        }
    }

    /**
     * 定制交互功能还未完成，不建议使用该方法
     * @return
     */
    @Deprecated
    @Override
    public DayCellUserInterfaceInfo<T> getDayCellUserInterfaceInfo() {
        if(mUserInterfaceInfo == null) {
            mUserInterfaceInfo = new DayCellUserInterfaceInfo<T>();
        }
        return mUserInterfaceInfo;
    }

    /**
     * 定制交互功能还未完成，不建议使用该方法
     * @param info
     */
    @Deprecated
    @Override
    public void setDayCellUserInterfaceInfo(DayCellUserInterfaceInfo<T> info) {
        if(info == null) {
            info = new DayCellUserInterfaceInfo<T>();
        }
        mUserInterfaceInfo = info;
    }

    protected void handleDayCellForSingle(DayCell<T> dayCell) {
        if(dayCell == null) {
            LogUtil.w(LOG_TAG, "handleDayCellForSingle:dayCell is null");
            return;
        } else if(mClickPolicy == null || mClickPolicy.getSinglePolicy() == null) {
            LogUtil.w(LOG_TAG, "handleDayCellForSingle:singlePolicy is null");
            return;
        }

        boolean hasRefreshView = mClickPolicy.getSinglePolicy().handleForSingle(this, dayCell, mSelectedDayCell);
        if(!hasRefreshView) {
            refreshICalendarViewList();
        }
    }

    protected void handleDayCellForMulti(DayCell<T> dayCell) {
        if(dayCell == null) {
            LogUtil.w(LOG_TAG, "handleDayCellForMulti:dayCell is null");
            return;
        } else if(mClickPolicy == null || mClickPolicy.getMultiPolicy() == null) {
            LogUtil.w(LOG_TAG, "handleDayCellForMulti:multiPolicy is null");
            return;
        }

        boolean hasRefreshView = mClickPolicy.getMultiPolicy().handleForMulti(this, dayCell, mSelectedDayCellList);
        if(!hasRefreshView) {
            refreshICalendarViewList();
        }
    }

    protected void handleDayCellForContinuous(DayCell<T> dayCell) {
        if(dayCell == null) {
            LogUtil.w(LOG_TAG, "handleDayCellForContinuous:dayCell is null");
            return;
        } else if(mClickPolicy == null || mClickPolicy.getContinuousPolicy() == null) {
            LogUtil.w(LOG_TAG, "handleDayCellForContinuous:continuousPolicy is null");
            return;
        }

        boolean hasRefreshView = mClickPolicy.getContinuousPolicy().handleForContinuous(this, dayCell, mSelectedContinuousItem);
        if(!hasRefreshView) {
            refreshICalendarViewList();
        }
    }

    protected void handleDayCellForContinuousMulti(DayCell<T> dayCell) {
        if(dayCell == null) {
            LogUtil.w(LOG_TAG, "handleDayCellForContinuousMulti:dayCell is null");
            return;
        } else if(mClickPolicy == null || mClickPolicy.getContinuousMultiPolicy() == null) {
            LogUtil.w(LOG_TAG, "handleDayCellForContinuousMulti:continuousMultiPolicy is null");
            return;
        }

        boolean hasRefreshView = mClickPolicy.getContinuousMultiPolicy().handleForContinuousMulti(this, dayCell, mSelectedContinuousItemList);
        if(!hasRefreshView) {
            refreshICalendarViewList();
        }
    }

    protected void handleDayCellForMix(DayCell<T> dayCell) {
        if(dayCell == null) {
            LogUtil.w(LOG_TAG, "handleDayCellForMix:dayCell is null");
            return;
        } else if(mClickPolicy == null || mClickPolicy.getMixPolicy() == null) {
            LogUtil.w(LOG_TAG, "handleDayCellForMix:mixPolicy is null");
            return;
        }

        boolean hasRefreshView = mClickPolicy.getMixPolicy().handleForMix(this, dayCell, mSelectedContinuousItem);
        if(!hasRefreshView) {
            refreshICalendarViewList();
        }
    }

    protected void handleDayCellForMixMulti(DayCell<T> dayCell) {
        if(dayCell == null) {
            LogUtil.w(LOG_TAG, "handleDayCellForMixMulti:dayCell is null");
            return;
        } else if(mClickPolicy == null || mClickPolicy.getMixMultiPolicy() == null) {
            LogUtil.w(LOG_TAG, "handleDayCellForMixMulti:mixMultiPolicy is null");
            return;
        }

        boolean hasRefreshView = mClickPolicy.getMixMultiPolicy().handleForMixMulti(this, dayCell, mSelectedContinuousItemList);
        if(!hasRefreshView) {
            refreshICalendarViewList();
        }
    }

}
