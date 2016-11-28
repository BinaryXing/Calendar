package com.xing.android.calendar.extend;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xing.android.calendar.CalendarManager;
import com.xing.android.calendar.CalendarTool;
import com.xing.android.calendar.ICalendarManager;
import com.xing.android.calendar.R;
import com.xing.android.calendar.util.LogUtil;
import com.xing.android.calendar.view.MonthListView;
import com.xing.android.calendar.view.WeekDayView;
import com.xing.android.calendar.view.WeekView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 水平分页月历
 * 该类内部持有ICalendarManager
 * 该控件需要使用者设置高度，计算方式为：WeekCell高度 * 6 + WeekDay高度 + MonthHead高度 + MonthFoot高度；
 * WeekCell高度取DayCell/WeekCellHead/WeekCellFoot中最大者；WeekDay高度取WeekDayCell/WeekDayHead/WeekDayFoot最大者
 * Created by zhaoxx on 16/5/20.
 */
public class HPageMonthCalendarView<T> extends LinearLayout {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private ViewPager mPagerView;

    private List<MonthListView<T>> mMonthList = new ArrayList<MonthListView<T>>();
    private CalendarPagerAdapter mAdapter;

    private int mStartYear;
    private int mStartMonth;
    private int mMonthCount;
    private int mFirstDayOfWeek = Calendar.SUNDAY;

    private ICalendarManager mCalendarManager;

    private MonthListView.MonthListListener mMonthListListener;
    private WeekDayView.WeekDayListener mWeekDayListener;
    private WeekView.WeekViewListener<T> mWeekViewListener;

    private boolean isShowWeekDay = false;
    private boolean isShowMonthHeader = false;
    private boolean isShowMonthFooter = false;

    private PageMonthCalendarListener mListener;

    public HPageMonthCalendarView(Context context) {
        super(context);
        init();
    }

    public HPageMonthCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.calendar_layout_horizontal_calendar, this, true);
        mPagerView = (ViewPager) findViewById(R.id.v_pager);

        mCalendarManager = new CalendarManager();
    }

    public void setListener(MonthListView.MonthListListener monthListListener, WeekDayView.WeekDayListener weekDayListener, WeekView.WeekViewListener<T> weekViewListener) {
        if(mMonthListListener != monthListListener || mWeekDayListener != weekDayListener || mWeekViewListener != weekViewListener) {
            mMonthListListener = monthListListener;
            mWeekDayListener = weekDayListener;
            mWeekViewListener = weekViewListener;
            for(MonthListView<T> view : mMonthList) {
                if(view == null) {
                    continue;
                }
                view.setListener(mMonthListListener, mWeekDayListener, mWeekViewListener);
            }
            if(mListener != null) {
                mListener.onCalendarHeightChange(mPagerView, isShowWeekDay, isShowMonthHeader, isShowMonthFooter);
            }
        }
    }

    public void setShowWeekDay(boolean value) {
        if(isShowWeekDay = value) {
            LogUtil.i(LOG_TAG, "setShowWeekDay:equal data, value = " + value);
            return;
        }
        isShowWeekDay = value;
        for(MonthListView<T> monthListView : mMonthList) {
            if(monthListView != null) {
                monthListView.setShowWeekDay(isShowWeekDay);
            }
        }
        if(mListener != null) {
            mListener.onCalendarHeightChange(mPagerView, isShowWeekDay, isShowMonthHeader, isShowMonthFooter);
        }
    }

    public void setShowMonthHeader(boolean value) {
        if(isShowMonthHeader == value) {
            LogUtil.i(LOG_TAG, "setShowMonthHeader:equal data, value = " + value);
            return;
        }
        isShowMonthHeader = value;
        for(MonthListView<T> monthListView : mMonthList) {
            if(monthListView != null) {
                monthListView.setShowMonthHeader(isShowMonthHeader);
            }
        }
        if(mListener != null) {
            mListener.onCalendarHeightChange(mPagerView, isShowWeekDay, isShowMonthHeader, isShowMonthFooter);
        }
    }

    public void setShowMonthFooter(boolean value) {
        if(isShowMonthFooter == value) {
            LogUtil.i(LOG_TAG, "setShowMonthFooter:equal data, value = " + value);
        }
        isShowMonthFooter = value;
        for(MonthListView<T> monthListView : mMonthList) {
            if(monthListView != null) {
                monthListView.setShowMonthFooter(isShowMonthFooter);
            }
        }
        if(mListener != null) {
            mListener.onCalendarHeightChange(mPagerView, isShowWeekDay, isShowMonthHeader, isShowMonthFooter);
        }
    }

    public ICalendarManager<T> getCalendarManager() {
        return mCalendarManager;
    }

    public ViewPager getViewPager() {
        return mPagerView;
    }

    public void set(int startYear, int startMonth, int monthCount) {
        set(startYear, startMonth, monthCount, mFirstDayOfWeek);
    }

    public void set(int startYear, int startMonth, int monthCount, int firstDayofWeek) {
        firstDayofWeek = CalendarTool.getValidFirstDayOfWeek(firstDayofWeek);
        if(mStartYear == startYear && mStartMonth == startMonth && mMonthCount == monthCount && mFirstDayOfWeek == firstDayofWeek) {
            LogUtil.w(LOG_TAG, "set:equal data,startYear = " + startYear + ",startMonth = " + startMonth +
                ",monthCount = " + monthCount + ",firstDayofWeek = " + firstDayofWeek);
            mPagerView.setCurrentItem(0);
            return;
        }
        mStartYear = startYear;
        mStartMonth = startMonth;
        mMonthCount = monthCount;
        mFirstDayOfWeek = firstDayofWeek;
        mMonthList.clear();
        for(int i = 0 ; i < monthCount ; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(startYear, startMonth - 1, 1);
            calendar.add(Calendar.MONTH, i);
            mMonthList.add(createMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1));
        }
        mCalendarManager.setCalendarViewList(mMonthList);
        mAdapter = new CalendarPagerAdapter();
        mPagerView.setAdapter(mAdapter);
        mPagerView.setCurrentItem(0);
        mCalendarManager.iterator();
        if(mListener != null) {
            mListener.onCalendarHeightChange(mPagerView, isShowWeekDay, isShowMonthHeader, isShowMonthFooter);
        }
    }

    private MonthListView<T> createMonth(int year, int month) {
        MonthListView<T> monthListView = new MonthListView<T>(getContext());
        monthListView.setDivider(null);
        monthListView.setDividerHeight(0);
        monthListView.setShowWeekDay(isShowWeekDay);
        monthListView.setShowMonthHeader(isShowMonthHeader);
        monthListView.setShowMonthFooter(isShowMonthFooter);
        monthListView.setListener(mMonthListListener, mWeekDayListener, mWeekViewListener);
        monthListView.set(year, month, 1, mFirstDayOfWeek);
        return monthListView;
    }

    public interface PageMonthCalendarListener {
        void onCalendarHeightChange(ViewPager viewPager, boolean showWeekDay, boolean showMonthHead, boolean showMonthFoot);
    }

    public class CalendarPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            MonthListView<T> monthListView = mMonthList.get(position);
            container.addView(monthListView);
            return monthListView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mMonthList.get(position));
        }

        @Override
        public int getCount() {
            return mMonthList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }
}
