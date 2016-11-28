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
 * Created by zhaoxx on 16/3/31.
 */
public abstract class CalendarView<T> extends LinearLayout implements ICalendarView<T> {

    protected final String LOG_TAG = getClass().getSimpleName();

    protected int mFirstDayOfWeek = Calendar.SUNDAY;
    protected ICalendarManager<T> mCalendarManager;

    protected DayCell<T> mStartDayCell;
    protected DayCell<T> mEndDayCell;

    protected List<DayCell<T>> mDayCellList = new ArrayList<DayCell<T>>();

    protected ICalendarViewProcessor<T> mCalendarVieProcessor;

    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected abstract void init();

    @Override
    public abstract void refresh();

    @Override
    public abstract void setCalendarManager(ICalendarManager<T> calendarManager);

    @Override
    public boolean isAffect(DayCell<T> dayCell, DayCell<T> originDayCell) {
        initCalendarViewHandler();
        if(mCalendarVieProcessor != null) {
            return mCalendarVieProcessor.isAffect(dayCell, originDayCell);
        }
        return false;
    }

    @Override
    public boolean isAffect(ContinuousSelectItem<T> item, ContinuousSelectItem<T> originItem) {
        initCalendarViewHandler();
        if(mCalendarVieProcessor != null) {
            return mCalendarVieProcessor.isAffect(item, originItem);
        }
        return false;
    }

    @Override
    public void onDayCellChanged(DayCell<T> dayCell, boolean refresh) {
        if(mCalendarManager == null) {
            LogUtil.w(LOG_TAG, "onDayCellChanged:mCalendarManager is null");
            return;
        } else if(mCalendarManager.getSelectMode() != CalendarConstant.SELECT_MODE_SINGLE) {
            LogUtil.w(LOG_TAG, "onDayCellChanged:select mode not match, getSelectMode() = " + mCalendarManager.getSelectMode());
            return;
        }
        initCalendarViewHandler();
        if(mCalendarVieProcessor != null) {
            mCalendarVieProcessor.onDayCellChanged(dayCell, refresh);
            if(refresh) {
                refresh();
            }
        }
    }

    @Override
    public void onDayCellListChanged(List<DayCell<T>> dayCellList, boolean refresh) {
        if(mCalendarManager == null) {
            LogUtil.w(LOG_TAG, "onDayCellListChanged:mCalendarManager is null");
            return;
        } else if(mCalendarManager.getSelectMode() != CalendarConstant.SELECT_MODE_MULTI) {
            LogUtil.w(LOG_TAG, "onDayCellListChanged:select mode not match, getSelectMode() = " + mCalendarManager.getSelectMode());
            return;
        }
        initCalendarViewHandler();
        if(mCalendarVieProcessor != null) {
            mCalendarVieProcessor.onDayCellListChanged(dayCellList, refresh);
            if(refresh) {
                refresh();
            }
        }
    }

    @Override
    public void onContinuousItemChanged(ContinuousSelectItem<T> item, boolean refresh) {
        if(mCalendarManager == null) {
            LogUtil.w(LOG_TAG, "onContinuousItemChanged:mCalendarManager is null");
            return;
        } else if(mCalendarManager.getSelectMode() != CalendarConstant.SELECT_MODE_MIX &&
                mCalendarManager.getSelectMode() != CalendarConstant.SELECT_MODE_CONTINUOUS) {
            LogUtil.w(LOG_TAG, "onContinuousItemChanged:select mode not match, getSelectMode() = " + mCalendarManager.getSelectMode());
            return;
        }
        initCalendarViewHandler();
        if(mCalendarVieProcessor != null) {
            mCalendarVieProcessor.onContinuousItemChanged(item, refresh);
            if(refresh) {
                refresh();
            }
        }
    }

    @Override
    public void onContinuousItemListChanges(List<ContinuousSelectItem<T>> continuousSelectItems, boolean refresh) {
        if(mCalendarManager == null) {
            LogUtil.w(LOG_TAG, "onContinuousItemListChanges:mCalendarManager is null");
            return;
        } else if(mCalendarManager.getSelectMode() != CalendarConstant.SELECT_MODE_MIX_MULTI &&
                mCalendarManager.getSelectMode() != CalendarConstant.SELECT_MODE_CONTINUOUS_MULTI) {
            LogUtil.w(LOG_TAG, "onContinuousItemListChanges:select mode not match, getSelectMode() = " + mCalendarManager.getSelectMode());
            return;
        }
        initCalendarViewHandler();
        if(mCalendarVieProcessor != null) {
            mCalendarVieProcessor.onContinuousItemListChanges(continuousSelectItems, refresh);
            if(refresh) {
                refresh();
            }
        }
    }

    @Override
    public void setData(List<DayCell<T>> dataList) {
        initCalendarViewHandler();
        if(mCalendarVieProcessor != null) {
            mCalendarVieProcessor.setData(dataList);
        }
    }

    @Override
    public void iterator() {
        if(mDayCellList == null || mDayCellList.size() == 0) {
            LogUtil.i(LOG_TAG, "foreach:mDayCellList is empty");
            return;
        } else if(mCalendarManager == null) {
            LogUtil.i(LOG_TAG, "foreach:mCalendarManager is empty");
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
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        firstDayOfWeek = CalendarTool.getValidFirstDayOfWeek(firstDayOfWeek);
        if(mFirstDayOfWeek != firstDayOfWeek) {
            mFirstDayOfWeek = firstDayOfWeek;
            init();
            refresh();
        }
    }

    protected void initCalendarViewHandler() {
        if(mCalendarVieProcessor == null) {
            mCalendarVieProcessor = new CalendarViewProcessorImp<T>(mStartDayCell, mEndDayCell, mDayCellList, mCalendarManager);
        }
        ((CalendarViewProcessorImp)mCalendarVieProcessor).set(mStartDayCell, mEndDayCell, mDayCellList, mCalendarManager);
    }
}
