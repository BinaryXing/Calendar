package com.xing.android.calendar.process;

import android.view.MotionEvent;
import android.view.View;

import com.xing.android.calendar.ICalendarManager;
import com.xing.android.calendar.model.DayCell;

/**
 * 该类里包含各种交互方式（Click，LongClick）下，判定DayCell是否需要处理
 * Created by zxx09506 on 2016/11/30.
 */

public class DayCellUserInterfaceInfo<T> {

    protected DayCellClickListener<T> mClickListener;
    protected DayCellLongClickListener<T> mLongClickListener;

    public DayCellClickListener<T> getClickListener() {
        return mClickListener;
    }

    public void setClickListener(DayCellClickListener<T> listener) {
        mClickListener = listener;
    }

    public DayCellLongClickListener<T> getLongClickListener() {
        return mLongClickListener;
    }

    public void setLongClickListener(DayCellLongClickListener<T> listener) {
        mLongClickListener = listener;
    }

    /**
     *
     * @param <T>
     */
    public interface DayCellClickListener<T> {
        DayCell<T> onDayCellClick(View view, ICalendarManager<T> iCalendarManager, DayCell<T> cell);
    }

    public interface DayCellLongClickListener<T> {
        DayCell<T> onDayCellLongClick( View view, ICalendarManager<T> iCalendarManager, DayCell<T> cell);
    }
}
