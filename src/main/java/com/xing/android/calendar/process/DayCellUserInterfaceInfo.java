package com.xing.android.calendar.process;

import android.view.MotionEvent;
import android.view.View;

import com.xing.android.calendar.ICalendarManager;
import com.xing.android.calendar.model.DayCell;
import com.xing.android.calendar.util.LogUtil;

/**
 * 该类包含各种交互方式（Click，LongClick）的配制，以及对DayCell进行转换或者过滤；
 * Created by zxx09506 on 2016/11/30.
 */

public class DayCellUserInterfaceInfo<T> {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private boolean isClickable = true;
    private boolean isLongClickable = false;
    //目前不支持Touch交互
    private boolean isTouchable = false;

    private ClickCellConvertListener<T> mClickListener;
    private LongClickCellConvertListener<T> mLongClickListener;
    private TouchCellConvertListener<T> mTouchListener;

    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean value) {
        if(isClickable == value) {
            LogUtil.i(LOG_TAG, "setClickable:equal value = " + value);
            return;
        }
        isClickable = value;
    }

    public boolean isLongClickable() {
        return isLongClickable;
    }

    public void setLongClickable(boolean value) {
        if(isLongClickable == value) {
            LogUtil.i(LOG_TAG, "setLongClickable:equal value = " + value);
            return;
        }
        isLongClickable = value;
    }

    public boolean isTouchable() {
        return isTouchable;
    }

    public void setTouchable(boolean value) {
        if(isTouchable == value) {
            LogUtil.i(LOG_TAG, "setTouchable:equal value = " + value);
            return;
        }
        isTouchable = value;
    }

    public ClickCellConvertListener<T> getClickConvertListener() {
        return mClickListener;
    }

    public void setClickConvertListener(ClickCellConvertListener<T> listener) {
        if(mClickListener == listener) {
            LogUtil.i(LOG_TAG, "setClickConvertListener:equal value");
            return;
        }
        mClickListener = listener;
    }

    public LongClickCellConvertListener<T> getLongClickConvertListener() {
        return mLongClickListener;
    }

    public void setLongClickConvertListener(LongClickCellConvertListener<T> listener) {
        if(mLongClickListener == listener) {
            LogUtil.i(LOG_TAG, "setLongClickConvertListener:equal value");
            return;
        }
        mLongClickListener = listener;
    }

    public TouchCellConvertListener<T> getTouchConvertListener() {
        return mTouchListener;
    }

    /**
     * 目前内部没有定义TouchCellConvertListener实例
     * @param listener
     */
    public void setTouchConvertListener(TouchCellConvertListener<T> listener) {
        if(mTouchListener == listener) {
            LogUtil.i(LOG_TAG, "setTouchConvertListener:equal value");
            return;
        }
        mTouchListener = listener;
    }

    /**
     *
     * @param <T>
     */
    public interface ClickCellConvertListener<T> {
        DayCell<T> convert(View view, ICalendarManager<T> iCalendarManager, DayCell<T> cell);
    }

    public interface LongClickCellConvertListener<T> {
        DayCell<T> convert(View view, ICalendarManager<T> iCalendarManager, DayCell<T> cell);
    }

    public interface TouchCellConvertListener<T> {
        DayCell<T> convert(View view, ICalendarManager<T> iCalendarManager, DayCell<T> cell, MotionEvent event);
    }
}
