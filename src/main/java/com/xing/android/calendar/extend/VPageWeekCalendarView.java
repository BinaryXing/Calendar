package com.xing.android.calendar.extend;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xing.android.calendar.CalendarTool;
import com.xing.android.calendar.model.DayCell;
import com.xing.android.calendar.util.LogUtil;
import com.xing.android.calendar.view.WeekListView;

import java.util.Calendar;

/**
 * 垂直分页周历，分页是根据月份分页的
 * 该类内部不持有ICalendarManager
 * Created by zxx09506 on 2016/10/26.
 */

public class VPageWeekCalendarView<T> extends WeekListView<T> {

    private static final int PER_ROW_SCROLL_TIME = 80;

    private int mStartYear;
    private int mStartMonth;
    private int mMonthCount;

    private SparseArray<WeekEntity> mWeekEntitiyList = new SparseArray<WeekEntity>();

    private float mTouchDownY;
    private float mThresholdScrollHeight = -1f;

    private int mRowScrollTime = PER_ROW_SCROLL_TIME;

    private DayCell<T> mLastValidDayCell;

    private VertWeekCalendarListener mListener;

    public VPageWeekCalendarView(Context context) {
        super(context);
        onCreate();
    }

    public VPageWeekCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public VPageWeekCalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        onCreate();
    }

    private void onCreate() {
        super.setShowWeekDay(false);
        //屏蔽Fling
        super.setVelocityScale(0.01f);
        setDivider(null);
        setDividerHeight(0);
    }

    public void setData(int startYear, int startMonth, int monthCount) {
        setData(startYear, startMonth, monthCount, mFirstDayOfWeek);
    }

    /**
     * 设置当前月份索引
     * @param index 从0开始
     */
    public void setCurrentMonthIndex(int index) {
        int position = -1;
        int currentMonthIndex = -1;
        WeekEntity entity = null;
        for(int i = 0 ; i < mWeekEntitiyList.size() ; i++) {
            entity = mWeekEntitiyList.get(i);
            if(entity == null) {
                continue;
            }
            if(entity.currentMonthWeek == 0 && ++currentMonthIndex == index) {
                position = i;
                break;
            }
        }
        if(position >= 0) {
            setSelection(position);
            if (mListener != null) {
                mListener.onScrollToPosition(position, mWeekEntitiyList.get(position));
            }
        }

    }

    public void setData(int startYear, int startMonth, int monthCount, int firstDayOfWeek) {
        firstDayOfWeek = CalendarTool.getValidFirstDayOfWeek(firstDayOfWeek);
        if(mStartYear == startYear && mStartMonth == startMonth && mMonthCount == monthCount && mFirstDayOfWeek == firstDayOfWeek) {
            LogUtil.w(LOG_TAG, "setData:equal data,startYear = " + startYear + ",startMonth = " + startMonth + ",monthCount = " + monthCount + ",firstDayOfWeek = " + firstDayOfWeek);
            return;
        }
        mStartYear = startYear;
        mStartMonth = startMonth;
        mMonthCount = monthCount;
        mFirstDayOfWeek = firstDayOfWeek;
        initData();
    }

    public void setRowHeight(float height) {
        if(height <= 0) {
            return;
        }
        //控件高度
        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height * 6));
        //外部没有设置翻页阀值的时候，默认翻页阀值是控件高度的一半
        if(mThresholdScrollHeight < 0) {
            mThresholdScrollHeight = height * 3;
        }
    }

    public void setThresholdScrollHeight(float height) {
        if(height > 0) {
            mThresholdScrollHeight = height;
        }
    }

    public void setRowScrollTime(int millisecond) {
        mRowScrollTime = millisecond;
    }

    public void setVertWeekCalendarListener(VertWeekCalendarListener listener) {
        mListener = listener;
    }

    public DayCell<T> getLastValidDayCell() {
        return mLastValidDayCell;
    }


    @Deprecated
    @Override
    public void setVelocityScale(float scale) {
        //该方法无效，内部使用
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mTouchDownY = ev.getY();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean result = super.onTouchEvent(ev);
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                float interval = ev.getY() - mTouchDownY;
                if(interval < - mThresholdScrollHeight) {
                    //向上滑动，并且超过翻页阀值
                    scrollUp();
                } else if(interval <= 0) {
                    //向上滑动，未超过翻页阀值，回弹
                    scrollDown();
                } else if(interval <= mThresholdScrollHeight) {
                    //向下滑动，未超过翻页阀值
                    scrollUp();
                } else {
                    //向下滑动，超过翻页阀值
                    scrollDown();
                }
                break;
        }
        return result;
    }


    private void scrollUp() {
        int firstVisiblePosition = getFirstVisiblePosition();
        int Rows = 0;
        WeekEntity entity;
        for(int i = firstVisiblePosition ; i < mWeekEntitiyList.size() ; i++) {
            Rows++;
            entity = mWeekEntitiyList.get(i);
            if(entity != null && entity.currentMonthWeek == 0) {
                smoothScrollToPositionFromTop(i, 0, Rows * mRowScrollTime);
                if(mListener != null) {
                    mListener.onScrollToPosition(i, entity);
                }
                LogUtil.i(LOG_TAG, "scrollUp:scrollToPosition = " + i);
                break;
            }
        }
    }

    private void scrollDown() {
        int firstVisiblePosition = getFirstVisiblePosition();
        int Rows = 0;
        WeekEntity entity;
        for(int i = firstVisiblePosition ; i >= 0 ; i--) {
            Rows++;
            entity = mWeekEntitiyList.get(i);
            if(entity != null && entity.currentMonthWeek == 0) {
                smoothScrollToPositionFromTop(i, 0, Rows * mRowScrollTime);
                if(mListener != null) {
                    mListener.onScrollToPosition(i, entity);
                }
                LogUtil.i(LOG_TAG, "scrollDown:scrollToPosition = " + i);
                break;
            }
        }
    }

    private void initData() {
        if (mMonthCount < 1) {
            LogUtil.e(LOG_TAG, "set:initData mMonthCount = " + mMonthCount);
            return;
        }
        mWeekEntitiyList.clear();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.clear();
        startCalendar.set(mStartYear, mStartMonth - 1, 1);
        //补全第一个月的第一周
        while (startCalendar.get(Calendar.DAY_OF_WEEK) != mFirstDayOfWeek) {
            startCalendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.clear();
        endCalendar.set(Calendar.YEAR, mStartYear);
        endCalendar.set(Calendar.MONTH, mStartMonth - 1);
        endCalendar.add(Calendar.MONTH, mMonthCount - 1);
        int day = endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        endCalendar.set(Calendar.DAY_OF_MONTH, day);
        mLastValidDayCell = CalendarTool.getDayCell(endCalendar, null);
        //补全最后一个月的六周数据（没有六周的情况下，下一个月的数据补全）
        int maxWeek = endCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
        while (true) {
            endCalendar.add(Calendar.DAY_OF_MONTH, 1);
            if(endCalendar.get(Calendar.DAY_OF_WEEK) == mFirstDayOfWeek) {
                maxWeek++;
                if(maxWeek > 6) {
                    break;
                }
            }
        }
        endCalendar.add(Calendar.DAY_OF_MONTH, -1);
        int weekPosition = -1;
        int currentMonthWeek = -1;
        String monthKey = "";
        WeekEntity entity = new WeekEntity();
        while (!startCalendar.after(endCalendar)) {
            if (startCalendar.get(Calendar.DAY_OF_WEEK) == mFirstDayOfWeek) {
                weekPosition++;
                currentMonthWeek++;
                entity = new WeekEntity();
                entity.weekPosition = weekPosition;
                entity.currentMonthWeek = currentMonthWeek;
                entity.year = startCalendar.get(Calendar.YEAR);
                entity.month = startCalendar.get(Calendar.MONTH) + 1;
                mWeekEntitiyList.put(weekPosition, entity);
            }
            //这周里有下一个月的日期
            if (monthKey != null && !monthKey.equals(getMonthKey(startCalendar))) {
                currentMonthWeek = 0;
                monthKey = getMonthKey(startCalendar);
                entity.currentMonthWeek = currentMonthWeek;
                entity.year = startCalendar.get(Calendar.YEAR);
                entity.month = startCalendar.get(Calendar.MONTH) + 1;
            }
            startCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        super.set(mStartYear, mStartMonth, 1, weekPosition + 1);
        smoothScrollToPositionFromTop(0, 0, 0);
        if(mListener != null) {
            mListener.onScrollToPosition(0, mWeekEntitiyList.get(0));
        }
    }

    @Deprecated
    @Override
    public void set(int year, int month, int day, int weekCount) {
        //该方法无效，该类是以月份分页的
    }

    @Deprecated
    @Override
    public void setShowWeekDay(boolean value) {
        //该方法无效，不允许外部设置
    }

    private String getMonthKey(Calendar calendar) {
        if (calendar == null) {
            return "";
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return "" + year + month;
    }

    public static class WeekEntity {
        public int weekPosition; //从0开始
        public int currentMonthWeek; //从0开始
        public int year;
        public int month;
    }

    public interface VertWeekCalendarListener {
        void onScrollToPosition(int position, WeekEntity entity);
    }
}
