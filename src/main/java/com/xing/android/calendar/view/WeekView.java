package com.xing.android.calendar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.xing.android.calendar.CalendarTool;
import com.xing.android.calendar.ICalendarManager;
import com.xing.android.calendar.model.DayCell;
import com.xing.android.calendar.model.IWeekCell;
import com.xing.android.calendar.model.MonthWeekCell;
import com.xing.android.calendar.model.WeekCell;
import com.xing.android.calendar.model.YearWeekCell;
import com.xing.android.calendar.util.LogUtil;

import java.util.Arrays;
import java.util.List;


/**
 * Created by zhaoxx on 16/3/9.
 */
public class WeekView<T> extends CalendarView<T> {

    protected View[] mDayCellViewList = new View[7];

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
            mStartDayCell = null;
            mEndDayCell = null;
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
    }

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
        if(mWeekCell != weekCell) {
            mWeekCell = weekCell;
            mFirstDayOfWeek = mWeekCell.getFirstDayOfWeek();
            init();
        }
        if (refresh) {
            refresh();
        }
    }

    public void setSingleWeek(int year, int month, int day, boolean refresh) {
        setSingleWeek(year, month, day, mFirstDayOfWeek, refresh);
    }

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

    public void setMonthWeek(int year, int month, int week, boolean refresh) {
        setMonthWeek(year, month, week, mFirstDayOfWeek, refresh);
    }

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

    public void setYearWeek(int year, int week, boolean refresh) {
        setYearWeek(year, week, mFirstDayOfWeek, refresh);
    }

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

    public void setWeekViewListener(WeekViewListener<T> listener) {
        if(mWeekViewListener != listener) {
            mWeekViewListener = listener;
            clearViewsCache();
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
                    final int finalI = i;
                    mDayCellViewList[finalI].setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mCalendarManager == null) {
                                LogUtil.w(LOG_TAG, "refresh.onClick:mCalendarManager is null");
                                return;
                            } else if (mWeekCell == null || mWeekCell.getDayCellList() == null || finalI >= mWeekCell.getDayCellList().size()) {
                                LogUtil.i(LOG_TAG, "refresh.onClick:dayCell is empty,finalI = " + finalI);
                                return;
                            }
                            mCalendarManager.onDayCellClick(mWeekCell.getDayCellList().get(finalI));
                        }
                    });
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

    @Override
    public void setCalendarManager(ICalendarManager<T> calendarManager) {
        if(calendarManager == null) {
            return;
        }
        mCalendarManager = calendarManager;
    }

    @Override
    public void setData(List<DayCell<T>> dataList) {
        super.setData(dataList);
        refresh();
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
