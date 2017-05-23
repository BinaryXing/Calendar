package com.xing.android.calendar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

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
 * CalendarListView，内部日期是连续的
 * Created by zhaoxx on 16/3/30.
 */
public abstract class CalendarListView<T> extends ListView implements ICalendarView<T> {

    protected final String LOG_TAG = getClass().getSimpleName();

    protected int mFirstDayOfWeek = Calendar.SUNDAY;

    //内部日期是连续的，需要记录开始DayCell和结束DayCell来配合mCalendarViewProcessor
    protected DayCell<T> mStartDayCell;
    protected DayCell<T> mEndDayCell;
    
    protected List<DayCell<T>> mDayCellList = new ArrayList<DayCell<T>>();

    protected ICalendarViewProcessor<T> mCalendarViewProcessor;

    protected ICalendarManager<T> mCalendarManager;

    public CalendarListView(Context context) {
        super(context);
        initCalendarViewProcessor();
    }

    public CalendarListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCalendarViewProcessor();
    }

    public CalendarListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initCalendarViewProcessor();
    }

    /**
     * 初始化，主要针对数据进行初始化
     */
    protected abstract void init();

    @Override
    public abstract void refresh();

    @Override
    public void setCalendarManager(ICalendarManager<T> calendarManager, boolean refresh) {
        if(calendarManager == null) {
            LogUtil.d(LOG_TAG, "setCalendarManager:calendarManager is null");
        } else {
            mCalendarManager = calendarManager;
            setCalendarViewProcessor();
        }
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
        if(mCalendarViewProcessor == null) {
            LogUtil.w(LOG_TAG, "isAffectDayCell:mCalendarViewProcessor is null");
            return false;
        }
        return mCalendarViewProcessor.isAffect(dayCell, originDayCell);
    }

    @Override
    public boolean isAffect(ContinuousSelectItem<T> item, ContinuousSelectItem<T> originItem) {
        initCalendarViewProcessor();
        setCalendarViewProcessor();
        if(mCalendarViewProcessor == null) {
            LogUtil.w(LOG_TAG, "isAffectContinuousSelectItem:mCalendarViewProcessor is null");
            return false;
        }
        return mCalendarViewProcessor.isAffect(item, originItem);
    }

    @Override
    public void onDayCellChanged(DayCell<T> dayCell, boolean refresh) {
        initCalendarViewProcessor();
        setCalendarViewProcessor();
        if (mCalendarManager == null) {
            LogUtil.w(LOG_TAG, "onDayCellChanged:mCalendarManager is null");
            return;
        } else if (mCalendarViewProcessor == null) {
            LogUtil.w(LOG_TAG, "onDayCellChanged:mCalendarViewProcessor is null");
            return;
        } else if (mCalendarManager.getSelectMode() != CalendarConstant.SELECT_MODE_SINGLE) {
            LogUtil.w(LOG_TAG, "onDayCellChanged:select mode not match, getSelectMode() = " + mCalendarManager.getSelectMode());
            return;
        }
        mCalendarViewProcessor.onDayCellChanged(dayCell, refresh);
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
        } else if (mCalendarViewProcessor == null) {
            LogUtil.w(LOG_TAG, "mCalendarViewProcessor is null");
            return;
        } else if (mCalendarManager.getSelectMode() != CalendarConstant.SELECT_MODE_MULTI) {
            LogUtil.w(LOG_TAG, "onDayCellListChanged:select mode not match, getSelectMode() = " + mCalendarManager.getSelectMode());
            return;
        }
        mCalendarViewProcessor.onDayCellListChanged(dayCellList, refresh);
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
        } else if (mCalendarViewProcessor == null) {
            LogUtil.w(LOG_TAG, "mCalendarViewProcessor is null");
            return;
        } else if (mCalendarManager.getSelectMode() != CalendarConstant.SELECT_MODE_MIX &&
                mCalendarManager.getSelectMode() != CalendarConstant.SELECT_MODE_CONTINUOUS) {
            LogUtil.w(LOG_TAG, "onContinuousItemChanged:select mode not match, getSelectMode() = " + mCalendarManager.getSelectMode());
            return;
        }
        mCalendarViewProcessor.onContinuousItemChanged(item, refresh);
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
        } else if (mCalendarViewProcessor == null) {
            LogUtil.w(LOG_TAG, "mCalendarViewProcessor is null");
            return;
        } else if (mCalendarManager.getSelectMode() != CalendarConstant.SELECT_MODE_MIX_MULTI &&
                mCalendarManager.getSelectMode() != CalendarConstant.SELECT_MODE_CONTINUOUS_MULTI) {
            LogUtil.w(LOG_TAG, "onContinuousItemListChanges:select mode not match, getSelectMode() = " + mCalendarManager.getSelectMode());
            return;
        }
        mCalendarViewProcessor.onContinuousItemListChanges(continuousSelectItems, refresh);
        if (refresh) {
            refresh();
        }
    }

    @Override
    public void setData(List<DayCell<T>> dataList, boolean keepOld, boolean refresh) {
        initCalendarViewProcessor();
        setCalendarViewProcessor();
        if(mCalendarViewProcessor == null) {
            LogUtil.w(LOG_TAG, "setData:mCalendarViewProcessor is null");
            return;
        }
        mCalendarViewProcessor.setData(dataList, keepOld, refresh);
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
        if(mCalendarViewProcessor == null) {
            mCalendarViewProcessor = new CalendarViewProcessorImp<T>(mStartDayCell, mEndDayCell, mDayCellList, mCalendarManager);
            setCalendarViewProcessor();
        }
    }

    /**
     * 更新CalendarViewProcessor的数据，相关数据有变化时，需要调用该方法
     */
    protected void setCalendarViewProcessor() {
        if(mCalendarViewProcessor != null && mCalendarViewProcessor instanceof CalendarViewProcessorImp) {
            ((CalendarViewProcessorImp<T>) mCalendarViewProcessor).set(mStartDayCell, mEndDayCell, mDayCellList, mCalendarManager);
        }
    }
}
