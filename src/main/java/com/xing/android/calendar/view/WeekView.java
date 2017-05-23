package com.xing.android.calendar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.xing.android.calendar.CalendarTool;
import com.xing.android.calendar.model.DayCell;
import com.xing.android.calendar.model.IWeekCell;
import com.xing.android.calendar.model.MonthWeekCell;
import com.xing.android.calendar.model.WeekCell;
import com.xing.android.calendar.model.YearWeekCell;
import com.xing.android.calendar.process.DayCellUserInterfaceInfo;
import com.xing.android.calendar.process.DayCellUserInterfaceInfo.ClickCellConvertListener;
import com.xing.android.calendar.process.DayCellUserInterfaceInfo.LongClickCellConvertListener;
import com.xing.android.calendar.process.DayCellUserInterfaceInfo.TouchCellConvertListener;
import com.xing.android.calendar.util.LogUtil;

import java.util.Arrays;


/**
 * Created by zhaoxx on 16/3/9.
 */
public class WeekView<T> extends CalendarView<T> {

    protected View[] mDayCellViewList = new View[7];
    protected OnClickListener[] mClickListenerList = new OnClickListener[7];
    protected OnLongClickListener[] mLongClickListenerList = new OnLongClickListener[7];
    protected OnTouchListener[] mTouchListenerList = new OnTouchListener[7];

    protected View mHeaderView;
    protected View mFooterView;

    protected IWeekCell<T> mWeekCell;

    protected WeekViewListener<T> mWeekViewListener;

    public WeekView(Context context) {
        super(context);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        init();
    }

    public WeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        init();
    }

    @Override
    protected void init() {
        mStartDayCell = null;
        mEndDayCell = null;
        mDayCellList.clear();
        if(mWeekCell == null) {
            LogUtil.i(LOG_TAG, "init:mWeekCell is null");
            return;
        }
        mWeekCell.setFirstDayOfWeek(mFirstDayOfWeek);
        if(mWeekCell.getDayCellList() != null && mWeekCell.getDayCellList().size() > 0) {
            mDayCellList.addAll(mWeekCell.getDayCellList());
            if(mDayCellList.get(0) != null) {
                mStartDayCell = mDayCellList.get(0).getCopyDayCell();
            }
            if(mDayCellList.get(mDayCellList.size() - 1) != null) {
                mEndDayCell = mDayCellList.get(mDayCellList.size() - 1).getCopyDayCell();
            }
        }
        setCalendarViewProcessor();
    }

    /**
     * 晴空Cache
     */
    protected void clearViewsCache() {
        mHeaderView = null;
        mFooterView = null;
        Arrays.fill(mDayCellViewList, null);
    }

    @Override
    public void setOrientation(int orientation) {
        if(getOrientation() != orientation) {
            switch (orientation) {
                case HORIZONTAL:
                    setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    break;
                case VERTICAL:
                    setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
                    break;
            }
            super.setOrientation(orientation);
        }
    }

    public void setWeekCell(IWeekCell<T> weekCell, boolean refresh) {
        if(mWeekCell == weekCell) {
            LogUtil.i(LOG_TAG, "setWeekCell:equal data");
        } else {
            mWeekCell = weekCell;
            mFirstDayOfWeek = mWeekCell.getFirstDayOfWeek();
            init();
        }
        if (refresh) {
            refresh();
        }
    }

    /**
     * 设置单周
     * @param year
     * @param month
     * @param day
     * @param refresh
     */
    public void setSingleWeek(int year, int month, int day, boolean refresh) {
        setSingleWeek(year, month, day, mFirstDayOfWeek, refresh);
    }

    /**
     * 设置单周
     * @param year
     * @param month
     * @param day
     * @param firstDayOfWeek
     * @param refresh
     */
    public void setSingleWeek(int year, int month, int day, int firstDayOfWeek, boolean refresh) {
        mFirstDayOfWeek = CalendarTool.getValidFirstDayOfWeek(firstDayOfWeek);
        if(mWeekCell != null && mWeekCell instanceof WeekCell) {
            ((WeekCell) mWeekCell).set(year, month, day, mFirstDayOfWeek);
        } else {
            mWeekCell = new WeekCell<T>(year, month, day, mFirstDayOfWeek);
        }
        init();
        if(refresh) {
            refresh();
        }
    }

    /**
     * 设置月周
     * @param year
     * @param month
     * @param week
     * @param refresh
     */
    public void setMonthWeek(int year, int month, int week, boolean refresh) {
        setMonthWeek(year, month, week, mFirstDayOfWeek, refresh);
    }

    /**
     * 设置月周
     * @param year
     * @param month
     * @param week
     * @param firstDayOfWeek
     * @param refresh
     */
    public void setMonthWeek(int year, int month, int week, int firstDayOfWeek, boolean refresh) {
        mFirstDayOfWeek = CalendarTool.getValidFirstDayOfWeek(firstDayOfWeek);
        if(mWeekCell != null && mWeekCell instanceof MonthWeekCell) {
            ((MonthWeekCell) mWeekCell).set(year, month, week, mFirstDayOfWeek);
        } else {
            mWeekCell = new MonthWeekCell<T>(year, month, week, mFirstDayOfWeek);
        }
        init();
        if(refresh) {
            refresh();
        }
    }

    /**
     * 设置年周
     * @param year
     * @param week
     * @param refresh
     */
    public void setYearWeek(int year, int week, boolean refresh) {
        setYearWeek(year, week, mFirstDayOfWeek, refresh);
    }

    /**
     * 设置年周
     * @param year
     * @param week
     * @param firstDayOfWeek
     * @param refresh
     */
    public void setYearWeek(int year, int week, int firstDayOfWeek, boolean refresh) {
        mFirstDayOfWeek = CalendarTool.getValidFirstDayOfWeek(firstDayOfWeek);
        if(mWeekCell != null && mWeekCell instanceof YearWeekCell) {
            ((YearWeekCell) mWeekCell).set(year, week, mFirstDayOfWeek);
        } else {
            mWeekCell = new YearWeekCell<T>(year, week, mFirstDayOfWeek);
        }
        init();
        if(refresh) {
            refresh();
        }
    }

    public void setWeekViewListener(WeekViewListener<T> listener, boolean refresh) {
        if (mWeekViewListener == listener) {
            LogUtil.i(LOG_TAG, "setWeekViewListener:equal value");
            return;
        }
        mWeekViewListener = listener;
        clearViewsCache();
        if(refresh) {
            refresh();
        }
    }

    @Override
    public void refresh() {
        removeAllViews();
        if(mWeekViewListener == null) {
            LogUtil.w(LOG_TAG, "refresh:mWeekViewListener is null");
            return;
        } else if(mWeekCell == null) {
            LogUtil.w(LOG_TAG, "refresh:mWeekCell is null");
            return;
        }
        //WeekHeaderView
        if(mHeaderView == null) {
            //newView
            mHeaderView = mWeekViewListener.newWeekHeaderView();
        }
        //addView和数据绑定
        if(mHeaderView != null) {
            addView(mHeaderView);
            mWeekViewListener.bindWeekHeaderView(mHeaderView, mWeekCell);
        }

        //DayCell
        for(int i = 0 ; i < 7 ; i++) {
            //newView
            if(mDayCellViewList[i] == null) {
                mDayCellViewList[i] = mWeekViewListener.newDayCellView();
                if (mDayCellViewList[i] != null) {
                    switch (getOrientation()) {
                        case HORIZONTAL:
                            mDayCellViewList[i].setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
                            break;
                        case VERTICAL:
                            mDayCellViewList[i].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 0, 1));
                            break;
                        default:
                            break;
                    }
                    setListener(i);
                }
            }
            //addView和数据绑定
            if(mDayCellViewList[i] != null) {
                addView(mDayCellViewList[i]);
                if(mWeekCell.getDayCellList() != null && i < mWeekCell.getDayCellList().size()) {
                    mWeekViewListener.bindDayCellView(mDayCellViewList[i], mWeekCell.getDayCellList().get(i));
                } else {
                    LogUtil.w(LOG_TAG, "refresh:has no data for i = " + i);
                }
            }
        }
        //WeekFooterView
        if(mFooterView == null) {
            //newView
            mFooterView = mWeekViewListener.newWeekFooterView();
        }
        //addView和数据绑定
        if(mFooterView != null) {
            addView(mFooterView);
            mWeekViewListener.bindWeekFooterView(mFooterView, mWeekCell);
        }
    }


    protected void setListener(final int position) {
        if(position < 0 || position >= mDayCellViewList.length) {
            LogUtil.w(LOG_TAG, "setListener:invalid view position = " + position);
            return;
        }
        final View view = mDayCellViewList[position];
        if(view == null) {
            LogUtil.w(LOG_TAG, "setListener:view is null, position = " + position);
            return;
        }

        setClickListener(position);
        setLongClickListener(position);
        setTouchListener(position);
    }

    /**
     * 对Position对应的View设置点击事件，调用处需要做边界处理，方法内不做任何保护校验
     * @param position
     */
    protected void setClickListener(final int position) {
        if(mClickListenerList[position] == null) {
            mClickListenerList[position] = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!checkParameterValid(position)) {
                        return;
                    } else if(!mCalendarManager.getDayCellUserInterfaceInfo().isClickable()) {
                        LogUtil.w(LOG_TAG, "setClickListener:mCalendarManager disable click");
                        return;
                    } else if(mCalendarManager.getDayCellUserInterfaceInfo().getClickConvertListener() == null) {
                        LogUtil.w(LOG_TAG, "setClickListener:mCalendarManager.getDayCellUserInterfaceInfo().getClickConvertListener() is null");
                        return;
                    }
                    DayCell<T> cell = mWeekCell.getDayCellList().get(position);
                    ClickCellConvertListener<T> clickListener = mCalendarManager.getDayCellUserInterfaceInfo().getClickConvertListener();
                    DayCell<T> resultCell = clickListener.convert(mDayCellViewList[position], mCalendarManager, cell);
                    if(resultCell == null) {
                        LogUtil.i(LOG_TAG, "setClickListener:filtered cell is " + cell.toString());
                        return;
                    }
                    LogUtil.i(LOG_TAG, "setClickListener.onClick:to handle cell is " + resultCell.toString() + ", origin cell is " + cell.toString());
                    mCalendarManager.onDayCellHandle(resultCell);
                }
            };
        }
        mDayCellViewList[position].setOnClickListener(mClickListenerList[position]);
    }

    /**
     * 对Position对应的View设置长按事件，调用处需要做边界处理，方法内不做任何保护校验
     * @param position
     */
    protected void setLongClickListener(final int position) {
        if(mLongClickListenerList[position] == null) {
            mLongClickListenerList[position] = new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(!checkParameterValid(position)) {
                        return false;
                    } else if(!mCalendarManager.getDayCellUserInterfaceInfo().isLongClickable()) {
                        LogUtil.w(LOG_TAG, "setLongClickListener:mCalendarManager disable longClick");
                        return false;
                    } else if(mCalendarManager.getDayCellUserInterfaceInfo().getLongClickConvertListener() == null) {
                        LogUtil.w(LOG_TAG, "mCalendarManager.getDayCellUserInterfaceInfo().getLongClickConvertListener() == null is null");
                        return false;
                    }
                    DayCell<T> cell = mWeekCell.getDayCellList().get(position);
                    LongClickCellConvertListener<T> longClickListener = mCalendarManager.getDayCellUserInterfaceInfo().getLongClickConvertListener();
                    DayCell<T> resultCell = longClickListener.convert(mDayCellViewList[position], mCalendarManager, cell);
                    if(resultCell == null) {
                        LogUtil.i(LOG_TAG, "setLongClickListener:filtered cell is " + cell.toString());
                        return false;
                    }
                    LogUtil.i(LOG_TAG, "setLongClickListener:to handle cell is " + resultCell.toString() + ", origin cell is " + cell.toString());
                    mCalendarManager.onDayCellHandle(resultCell);
                    return true;
                }
            };
        }
        mDayCellViewList[position].setOnLongClickListener(mLongClickListenerList[position]);
    }

    /**
     * 对position对应对的View设置Touch事件，调用处需要做边界处理，方法内不做任何保护校验
     * @param position
     */
    protected void setTouchListener(final int position) {
        if(mTouchListenerList[position] == null) {
            mTouchListenerList[position] = new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(!checkParameterValid(position)) {
                        return false;
                    } else if(!mCalendarManager.getDayCellUserInterfaceInfo().isTouchable()) {
                        LogUtil.w(LOG_TAG, "setTouchListener:mCalendarManager disable touch");
                        return false;
                    } else if(mCalendarManager.getDayCellUserInterfaceInfo().getTouchConvertListener() == null) {
                        LogUtil.w(LOG_TAG, "mCalendarManager.getDayCellUserInterfaceInfo().getTouchConvertListener() is null");
                        return false;
                    }
                    DayCell<T> cell = mWeekCell.getDayCellList().get(position);
                    TouchCellConvertListener<T> touchConvertListener = mCalendarManager.getDayCellUserInterfaceInfo().getTouchConvertListener();
                    DayCell<T> result = touchConvertListener.convert(mDayCellViewList[position], mCalendarManager, cell, event);
                    if(result == null) {
                        LogUtil.i(LOG_TAG, "setTouchListener:filtered cell is " + cell.toString());
                        return false;
                    }
                    LogUtil.i(LOG_TAG, "setTouchListener:to handle cell is " + result.toString() + ", origin cell is " + cell.toString());
                    mCalendarManager.onDayCellHandle(result);
                    return true;
                }
            };
        }
        mDayCellViewList[position].setOnTouchListener(mTouchListenerList[position]);
    }

    /**
     * 校验通用参数是否有效
     * @param position
     * @return
     */
    private boolean checkParameterValid(int position) {
        if(mCalendarManager == null) {
            LogUtil.w(LOG_TAG, "checkParameterValid:mCalendarManager is null");
            return false;
        } else if(mCalendarManager.getDayCellUserInterfaceInfo() == null) {
            LogUtil.w(LOG_TAG, "checkParameterValid:mCalendarManager.getDayCellUserInterfaceInfo() is null");
            return false;
        } else if(mWeekCell == null || mWeekCell.getDayCellList() == null || mWeekCell.getDayCellList().size() == 0) {
            LogUtil.w(LOG_TAG, "checkParameterValid:mWeekCell is empty");
            return false;
        } else if(position < 0 || position >= mWeekCell.getDayCellList().size()) {
            LogUtil.w(LOG_TAG, "checkParameterValid:invalid position = " + position + ", mWeekCell size = " + mWeekCell.getDayCellList().size());
            return false;
        }
        return true;
    }

    public interface WeekViewListener<T> {
        View newDayCellView();
        void bindDayCellView(View view, DayCell<T> data);
        View newWeekHeaderView();
        void bindWeekHeaderView(View view, IWeekCell<T> weekCell);
        View newWeekFooterView();
        void bindWeekFooterView(View view, IWeekCell<T> weekCell);
    }
}
