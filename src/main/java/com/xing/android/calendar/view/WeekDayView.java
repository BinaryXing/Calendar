package com.xing.android.calendar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.xing.android.calendar.CalendarTool;
import com.xing.android.calendar.util.LogUtil;

import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by zhaoxx on 16/3/14.
 */
public class WeekDayView extends LinearLayout {

    protected final String LOG_TAG = this.getClass().getSimpleName();

    protected View mHeaderView;
    protected View mFooterView;

    protected int mFirstDayOfWeek = Calendar.SUNDAY;
    protected View[] mDayOfWeekViewList = new View[7];

    protected WeekDayListener mWeekDayListener;

    public WeekDayView(Context context) {
        super(context);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        init();
    }

    public WeekDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        init();
    }

    protected void init() {
        removeAllViews();
        if(mWeekDayListener == null) {
            LogUtil.w(LOG_TAG, "init:mWeekDayListener is null");
            return;
        }
        //HeaderView
        if(mHeaderView == null) {
            mHeaderView = mWeekDayListener.newDayOfWeekHeaderView();
        }
        if(mHeaderView != null) {
            addView(mHeaderView);
            mWeekDayListener.bindDayOfWeekHeaderView(mHeaderView);
        }

        //DayView
        for(int i = 0 ; i < 7 ; i++) {
            if(mDayOfWeekViewList[i] == null) {
                mDayOfWeekViewList[i] = mWeekDayListener.newDayOfWeekCellView();
                if(mDayOfWeekViewList[i] != null) {
                    switch (getOrientation()) {
                        case HORIZONTAL:
                            mDayOfWeekViewList[i].setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                            break;
                        case VERTICAL:
                            mDayOfWeekViewList[i].setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1));
                            break;
                        default:
                            break;
                    }
                }
            }
            addView(mDayOfWeekViewList[i]);
            mWeekDayListener.bindDayOfWeekCellView(mDayOfWeekViewList[i], (mFirstDayOfWeek - 1 + i) % 7 + 1);
        }

        //FooterView
        if(mFooterView == null) {
            mFooterView = mWeekDayListener.newDayOfWeekFooterView();
        }
        if(mFooterView != null) {
            addView(mFooterView);
            mWeekDayListener.bindDayOfWeekFooterView(mFooterView);
        }
    }

    @Override
    public void setOrientation(int orientation) {
        if(getOrientation() != orientation) {
            switch (orientation) {
                case HORIZONTAL:
                    setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    break;
                case VERTICAL:
                    setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
                    break;
            }
            super.setOrientation(orientation);
        }
    }

    protected void clearViewsCache() {
        mHeaderView = null;
        mFooterView = null;
        Arrays.fill(mDayOfWeekViewList, null);
    }

    public void setDayOfWeekCellListener(WeekDayListener listener, boolean refresh) {
        if (mWeekDayListener == listener) {
            LogUtil.i(LOG_TAG, "setDayOfWeekCellListener:equal value");
            return;
        }
        mWeekDayListener = listener;
        clearViewsCache();
        if (refresh) {
            init();
        }
    }

    public int getFirstDayOfWeek() {
        return mFirstDayOfWeek;
    }

    public void setFirstDayOfWeek(int firstDayOfWeek, boolean refresh) {
        firstDayOfWeek = CalendarTool.getValidFirstDayOfWeek(firstDayOfWeek);
        if (mFirstDayOfWeek == firstDayOfWeek) {
            LogUtil.i(LOG_TAG, "setFirstDayOfWeek:equal value = " + mFirstDayOfWeek);
            return;
        }
        mFirstDayOfWeek = firstDayOfWeek;
        if(refresh) {
            init();
        }
    }

    public interface WeekDayListener {
        View newDayOfWeekCellView();
        void bindDayOfWeekCellView(View view, int dayOfWeek);
        View newDayOfWeekHeaderView();
        void bindDayOfWeekHeaderView(View view);
        View newDayOfWeekFooterView();
        void bindDayOfWeekFooterView(View view);
    }
}
