package com.xing.android.calendar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xing.android.calendar.CalendarTool;
import com.xing.android.calendar.ICalendarManager;
import com.xing.android.calendar.model.DayCell;
import com.xing.android.calendar.model.IWeekCell;
import com.xing.android.calendar.model.WeekCell;
import com.xing.android.calendar.util.LogUtil;
import com.xing.android.calendar.view.WeekView.WeekViewListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 周历，从制定的某一天所在的周开始展示多少周的数据
 * Created by zhaoxx on 16/3/30.
 */
public class WeekListView<T> extends CalendarListView<T> {

    /**
     * WeekCell
     */
    private static final int VIEW_TYPE_NORMAL = 0;
    /**
     * 周日,周一,周二,周三,周四,周五,周六的WeekCell
     */
    private static final int VIEW_TYPE_DAY_OF_WEEK = 1;

    private static final int VIEW_TYPE_COUNT = 2;

    protected boolean isShowWeekDay = true;

    protected WeekDayView.WeekDayListener mWeekDayListener;
    protected WeekViewListener<T> mWeekViewListener;

    protected List<WeekListAdapterItem<T>> mWeekListItemList = new ArrayList<WeekListAdapterItem<T>>();
    protected WeekListAdapter<T> mAdapter;

    protected int mStartYear;
    protected int mStartMonth;
    protected int mStartDay;

    protected int mWeekCount;

    public WeekListView(Context context) {
        super(context);
    }

    public WeekListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WeekListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        mStartDayCell = null;
        mEndDayCell = null;
        mDayCellList.clear();
        mWeekListItemList.clear();
        if (!CalendarTool.checkValidOfDay(mFirstDayOfWeek, mStartYear, mStartMonth, mStartDay)) {
            LogUtil.w(LOG_TAG, "init:invalid data,mStartYear = " + mStartYear + ",mStartMonth = " + mStartMonth + ",mStartDay = " + mStartDay);
            return;
        } else if (mWeekCount <= 0) {
            LogUtil.w(LOG_TAG, "init:invalid mWeekCount = " + mWeekCount);
            return;
        }
        WeekListAdapterItem<T> item;
        if (isShowWeekDay) {
            item = new WeekListAdapterItem<T>();
            item.viewType = VIEW_TYPE_DAY_OF_WEEK;
            mWeekListItemList.add(item);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setFirstDayOfWeek(mFirstDayOfWeek);
        calendar.set(mStartYear, mStartMonth - 1, mStartDay);
        for (int i = 0; i < mWeekCount; i++) {
            item = new WeekListAdapterItem<T>();
            item.viewType = VIEW_TYPE_NORMAL;
            item.week = i + 1;
            item.weekCell = new WeekCell<T>(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), mFirstDayOfWeek);
            mWeekListItemList.add(item);
            if (item.weekCell != null && item.weekCell.getDayCellList() != null && item.weekCell.getDayCellList().size() > 0) {
                mDayCellList.addAll(item.weekCell.getDayCellList());
                if (i == 0 && item.weekCell.getDayCellList().get(0) != null) {
                    mStartDayCell = item.weekCell.getDayCellList().get(0).getCopyDayCell();
                }
                if (i == mWeekCount - 1 && item.weekCell.getDayCellList().get(item.weekCell.getDayCellList().size() - 1) != null) {
                    mEndDayCell = item.weekCell.getDayCellList().get(item.weekCell.getDayCellList().size() - 1).getCopyDayCell();
                }
            }
            calendar.add(Calendar.DAY_OF_YEAR, 7);
        }
        setCalendarViewProcessor();
        if (mAdapter == null) {
            mAdapter = new WeekListAdapter<T>(getContext(), mWeekListItemList);
            super.setAdapter(mAdapter);
        }
    }

    public void setShowWeekDay(boolean value, boolean refresh) {
        if (isShowWeekDay == value) {
            LogUtil.i(LOG_TAG, "setShowWeekDay:equal value = " + value);
        } else {
            isShowWeekDay = value;
            init();
        }
        if(refresh) {
            refresh();
        }
    }

    /**
     * 设置Listener
     * @param weekDayListener
     * @param weekViewListener
     */
    public void setListener(WeekDayView.WeekDayListener weekDayListener, WeekView.WeekViewListener<T> weekViewListener, boolean refresh) {
        if (mWeekDayListener == weekDayListener && mWeekViewListener == weekViewListener) {
            LogUtil.i(LOG_TAG, "setListener:equal value");
        } else {
            mWeekDayListener = weekDayListener;
            mWeekViewListener = weekViewListener;
        }
        if(refresh) {
            refresh();
        }
    }

    public void set(int year, int month, int day, int weekCount, boolean refresh) {
        set(year, month, day, weekCount, mFirstDayOfWeek, refresh);
    }

    public void set(int year, int month, int day, int weekCount, int firstDayOfWeek, boolean refresh) {
        firstDayOfWeek = CalendarTool.getValidFirstDayOfWeek(firstDayOfWeek);
        if (!CalendarTool.checkValidOfDay(firstDayOfWeek, year, month, day)) {
            LogUtil.w(LOG_TAG, "set:invalid data:year = " + year + ",month = " + month + ",day = " + day);
        } else if(year == mStartYear && month == mStartMonth && day == mStartDay && weekCount == mWeekCount && firstDayOfWeek == mFirstDayOfWeek) {
            LogUtil.w(LOG_TAG, "set:equal data:year = " + year + ",month = " + month + ",day = " + day + ",weekCount = " + weekCount + ",firstDayOfWeek = " + firstDayOfWeek);
        } else if (weekCount < 0) {
            LogUtil.w(LOG_TAG, "set:invalid weekCount = " + weekCount);
        } else {
            mStartYear = year;
            mStartMonth = month;
            mStartDay = day;
            mWeekCount = weekCount;
            mFirstDayOfWeek = firstDayOfWeek;
            init();
        }
        if(refresh) {
            refresh();
        }
    }

    public int getWeekCount() {
        return mWeekCount;
    }

    public int getStartDay() {
        return mStartDay;
    }

    public int getStartMonth() {
        return mStartMonth;
    }

    public int getStartYear() {
        return mStartYear;
    }

    @Override
    public void refresh() {
        if (mAdapter != null) {
            mAdapter.setData(mWeekListItemList);
            mAdapter.notifyDataSetChanged();
        }
    }

    public class WeekListAdapter<T> extends BaseAdapter {

        private final String LOG_TAG = getClass().getSimpleName();

        private Context mContext;
        private List<WeekListAdapterItem<T>> mDataList = new ArrayList<WeekListAdapterItem<T>>();

        public WeekListAdapter(Context context, List<WeekListAdapterItem<T>> list) {
            mContext = context;
            if (list != null) {
                mDataList = list;
            }
        }

        public void setData(List<WeekListAdapterItem<T>> list) {
            mDataList = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mDataList == null ? 0 : mDataList.size();
        }

        @Override
        public WeekListAdapterItem<T> getItem(int position) {
            if (position < 0 || position >= getCount()) {
                return null;
            }
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE_COUNT;
        }

        @Override
        public int getItemViewType(int position) {
            WeekListAdapterItem<T> item = getItem(position);
            if (item != null) {
                return item.viewType;
            }
            return super.getItemViewType(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (mWeekViewListener == null) {
                LogUtil.w(LOG_TAG, "getView:mWeekViewListener is null");
                return convertView;
            }
            WeekListAdapterItem<T> item = getItem(position);
            if (item == null) {
                LogUtil.w(LOG_TAG, "getView:item is null");
                return convertView;
            }
            switch (getItemViewType(position)) {
                case VIEW_TYPE_NORMAL:
                    if (convertView == null) {
                        convertView = new WeekView<T>(mContext);
                    }
                    ((WeekView<T>) convertView).setCalendarManager((ICalendarManager<T>) mCalendarManager, false);
                    ((WeekView<T>) convertView).setFirstDayOfWeek(mFirstDayOfWeek, false);
                    ((WeekView<T>) convertView).setWeekViewListener((WeekViewListener<T>) mWeekViewListener, false);
                    ((WeekView<T>) convertView).setWeekCell(item.weekCell, true);
                    break;
                case VIEW_TYPE_DAY_OF_WEEK:
                    if (convertView == null) {
                        convertView = new WeekDayView(mContext);
                    }
                    ((WeekDayView) convertView).setFirstDayOfWeek(mFirstDayOfWeek, false);
                    ((WeekDayView) convertView).setDayOfWeekCellListener(mWeekDayListener, true);
                    break;
                default:
                    break;
            }
            return convertView;
        }
    }

    public class WeekListAdapterItem<T> {
        public int viewType;
        public int week;
        public IWeekCell<T> weekCell;
    }
}
