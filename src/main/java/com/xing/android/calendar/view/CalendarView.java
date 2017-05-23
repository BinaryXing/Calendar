package com.xing.android.calendar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.xing.android.calendar.CalendarConstant;
import com.xing.android.calendar.CalendarTool;
import com.xing.android.calendar.ICalendarManager;
import com.xing.android.calendar.model.ContinuousSelectItem;
import com.xing.android.calendar.model.DayCell;
import com.xing.android.calendar.process.CalendarViewProcessorImp;
import com.xing.android.calendar.process.ICalendarViewProcessor;
import com.xing.android.calendar.util.LogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * CalendarView，内部日期是连续的
 * Created by zhaoxx on 16/3/31.
 */
public abstract class CalendarView<T> extends LinearLayout implements ICalendarView<T> {

    protected final String LOG_TAG = getClass().getSimpleName();

    protected int mFirstDayOfWeek = Calendar.SUNDAY;

    //由于内部日期是连续的，所以可以通过记录开始DayCell和结束DayCell来配合mCalendarVieProcessor；
    protected DayCell<T> mStartDayCell;
    protected DayCell<T> mEndDayCell;

    protected List<DayCell<T>> mDayCellList = new ArrayList<DayCell<T>>();

    protected ICalendarViewProcessor<T> mCalendarVieProcessor;

    protected ICalendarManager<T> mCalendarManager;

    public CalendarView(Context context) {
        super(context);
        initCalendarViewProcessor();
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCalendarViewProcessor();
    }

    /**
     * 初始化，主要针对内部数据初始化
     */
    protected abstract void init();

    /**
     * 刷新View
     */
    @Override
    public abstract void refresh();

    @Override
    public void setCalendarManager(ICalendarManager<T> calendarManager, boolean refresh) {
        if(calendarManager == null) {
            LogUtil.d(LOG_TAG, "setCalendarManager:calendarManager is null");
            return;
        }
        mCalendarManager = calendarManager;
        setCalendarViewProcessor();
        if(refresh) {
            refresh();
        }
    }

    @Override
    public ICalendarManager<T> getCalendarManager() {
        return mCalendarManager;
    }

    @Override
    public boolean isAffect(DayCell<T> dayCell, DayCell<T> originDayCell) {
        initCalendarViewProcessor();
        setCalendarViewProcessor();
        if(mCalendarVieProcessor == null) {
            LogUtil.w(LOG_TAG, "isAffectDayCell:mCalendarVieProcessor is null");
            return false;
        }
        return mCalendarVieProcessor.isAffect(dayCell, originDayCell);
    }

    @Override
    public boolean isAffect(ContinuousSelectItem<T> item, ContinuousSelectItem<T> originItem) {
        initCalendarViewProcessor();
        setCalendarViewProcessor();
        if(mCalendarVieProcessor == null) {
            LogUtil.w(LOG_TAG, "isAffectContinuousSelectItem:mCalendarVieProcessor is null");
            return false;
        }
        return mCalendarVieProcessor.isAffect(item, originItem);
    }

    @Override
    public void onDayCellChanged(DayCell<T> dayCell, boolean refresh) {
        initCalendarViewProcessor();
        setCalendarViewProcessor();
        if (mCalendarManager == null) {
            LogUtil.w(LOG_TAG, "onDayCellChanged:mCalendarManager is null");
            return;
        } else if (mCalendarVieProcessor == null) {
            LogUtil.w(LOG_TAG, "onDayCellChanged:mCalendarVieProcessor is null");
            return;
        } else if (mCalendarManager.getSelectMode() != CalendarConstant.SELECT_MODE_SINGLE) {
            LogUtil.w(LOG_TAG, "onDayCellChanged:select mode not match, getSelectMode() = " + mCalendarManager.getSelectMode());
            return;
        }
        mCalendarVieProcessor.onDayCellChanged(dayCell, refresh);
        if (refresh) {
            refresh();
        }
    }

    @Override
    public void onDayCellListChanged(List<DayCell<T>> dayCellList, boolean refresh) {
        initCalendarViewProcessor();
        setCalendarViewProcessor();
        if (mCalendarManager == null) {
            LogUtil.w(LOG_TAG, "onDayCellListChanged:mCalendarManager is null");
            return;
        } else if (mCalendarVieProcessor == null) {
            LogUtil.w(LOG_TAG, "onDayCellListChanged:mCalendarVieProcessor is null");
            return;
        } else if (mCalendarManager.getSelectMode() != CalendarConstant.SELECT_MODE_MULTI) {
            LogUtil.w(LOG_TAG, "onDayCellListChanged:select mode not match, getSelectMode() = " + mCalendarManager.getSelectMode());
            return;
        }
        mCalendarVieProcessor.onDayCellListChanged(dayCellList, refresh);
        if (refresh) {
            refresh();
        }
    }

    @Override
    public void onContinuousItemChanged(ContinuousSelectItem<T> item, boolean refresh) {
        initCalendarViewProcessor();
        setCalendarViewProcessor();
        if (mCalendarManager == null) {
            LogUtil.w(LOG_TAG, "onContinuousItemChanged:mCalendarManager is null");
            return;
        } else if (mCalendarVieProcessor == null) {
            LogUtil.w(LOG_TAG, "onContinuousItemChanged:mCalendarVieProcessor is null");
            return;
        } else if (mCalendarManager.getSelectMode() != CalendarConstant.SELECT_MODE_MIX &&
                mCalendarManager.getSelectMode() != CalendarConstant.SELECT_MODE_CONTINUOUS) {
            LogUtil.w(LOG_TAG, "onContinuousItemChanged:select mode not match, getSelectMode() = " + mCalendarManager.getSelectMode());
            return;
        }
        mCalendarVieProcessor.onContinuousItemChanged(item, refresh);
        if (refresh) {
            refresh();
        }
    }

    @Override
    public void onContinuousItemListChanges(List<ContinuousSelectItem<T>> continuousSelectItems, boolean refresh) {
        initCalendarViewProcessor();
        setCalendarViewProcessor();
        if (mCalendarManager == null) {
            LogUtil.w(LOG_TAG, "onContinuousItemListChanges:mCalendarManager is null");
            return;
        } else if (mCalendarVieProcessor == null) {
            LogUtil.w(LOG_TAG, "onContinuousItemListChanges:mCalendarVieProcessor is null");
            return;
        } else if (mCalendarManager.getSelectMode() != CalendarConstant.SELECT_MODE_MIX_MULTI &&
                mCalendarManager.getSelectMode() != CalendarConstant.SELECT_MODE_CONTINUOUS_MULTI) {
            LogUtil.w(LOG_TAG, "onContinuousItemListChanges:select mode not match, getSelectMode() = " + mCalendarManager.getSelectMode());
            return;
        }
        mCalendarVieProcessor.onContinuousItemListChanges(continuousSelectItems, refresh);
        if (refresh) {
            refresh();
        }
    }

    @Override
    public void setData(List<DayCell<T>> dataList, boolean keepOld, boolean refresh) {
        initCalendarViewProcessor();
        setCalendarViewProcessor();
        if(mCalendarVieProcessor == null) {
            LogUtil.w(LOG_TAG, "setData:mCalendarVieProcessor is null");
            return;
        }
        mCalendarVieProcessor.setData(dataList, keepOld, refresh);
        if(refresh) {
            refresh();
        }
    }

    @Override
    public void iterator() {
        if(mDayCellList == null || mDayCellList.size() == 0) {
            LogUtil.i(LOG_TAG, "iterator:mDayCellList is empty");
            return;
        } else if(mCalendarManager == null) {
            LogUtil.i(LOG_TAG, "iterator:mCalendarManager is empty");
            return;
        }
        for(DayCell<T> dayCell : mDayCellList) {
            if(dayCell == null) {
                continue;
            }
            mCalendarManager.onIterator(dayCell);
        }
    }

    @Override
    public void setFirstDayOfWeek(int firstDayOfWeek, boolean refresh) {
        firstDayOfWeek = CalendarTool.getValidFirstDayOfWeek(firstDayOfWeek);
        if (mFirstDayOfWeek == firstDayOfWeek) {
            LogUtil.i(LOG_TAG, "setFirstDayOfWeek:equal value, mFirstDayOfWeek = " + mFirstDayOfWeek + ", firstDayOfWeek = " + firstDayOfWeek);
            return;
        }
        mFirstDayOfWeek = firstDayOfWeek;
        init();
        setCalendarViewProcessor();
        if(refresh) {
            refresh();
        }
    }

    @Override
    public int getFirstDayOfWeek() {
        return mFirstDayOfWeek;
    }

    /**
     * 初始化CalendarViewProcessor
     */
    protected void initCalendarViewProcessor() {
        if(mCalendarVieProcessor == null) {
            mCalendarVieProcessor = new CalendarViewProcessorImp<T>(mStartDayCell, mEndDayCell, mDayCellList, mCalendarManager);
            setCalendarViewProcessor();
        }
    }

    /**
     * 更新CalendarViewProcessor的数据，相关数据有变化时，需要调用该方法
     */
    protected void setCalendarViewProcessor() {
        if(mCalendarVieProcessor != null && mCalendarVieProcessor instanceof CalendarViewProcessorImp) {
            ((CalendarViewProcessorImp<T>) mCalendarVieProcessor).set(mStartDayCell, mEndDayCell, mDayCellList, mCalendarManager);
        }
    }
}
