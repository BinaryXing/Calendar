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
 * Created by zhaoxx on 16/3/30.
 */
public class WeekListView<T> extends CalendarListView<T> {

    private static final int VIEW_TYPE_NORMAL = 0;
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
        if (isShowWeekDay) {
            WeekListAdapterItem<T> item = new WeekListAdapterItem<T>();
            item.viewType = VIEW_TYPE_DAY_OF_WEEK;
            mWeekListItemList.add(item);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setFirstDayOfWeek(mFirstDayOfWeek);
        calendar.set(mStartYear, mStartMonth - 1, mStartDay);
        for (int i = 0; i < mWeekCount; i++) {
            WeekListAdapterItem<T> item = new WeekListAdapterItem<T>();
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
        if (mAdapter == null) {
            mAdapter = new WeekListAdapter<T>(getContext(), null);
            super.setAdapter(mAdapter);
        }
        mAdapter.setData(mWeekListItemList);

    }

    public void setShowWeekDay(boolean value) {
        if (isShowWeekDay != value) {
            isShowWeekDay = value;
            init();
            if (mAdapter != null) {
                mAdapter.setData(mWeekListItemList);
            }
        }
    }

    /**
     * 这是Listener
     *
     * @param weekDayListener
     * @param weekViewListener
     */
    public void setListener(WeekDayView.WeekDayListener weekDayListener, WeekView.WeekViewListener<T> weekViewListener) {
        if (mWeekDayListener != weekDayListener || mWeekViewListener != weekViewListener) {
            mWeekDayListener = weekDayListener;
            mWeekViewListener = weekViewListener;
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void set(int year, int month, int day, int weekCount) {
        set(year, month, day, weekCount, mFirstDayOfWeek);
    }

    public void set(int year, int month, int day, int weekCount, int firstDayOfWeek) {
        firstDayOfWeek = CalendarTool.getValidFirstDayOfWeek(firstDayOfWeek);
        if (!CalendarTool.checkValidOfDay(firstDayOfWeek, year, month, day)) {
            LogUtil.w(LOG_TAG, "set:invalid data:year = " + year + ",month = " + month + ",day = " + day);
            return;
        } else if(year == mStartYear && month == mStartMonth && day == mStartDay && weekCount == mWeekCount && firstDayOfWeek == mFirstDayOfWeek) {
            LogUtil.w(LOG_TAG, "set:equal data:year = " + year + ",month = " + month + ",day = " + day + ",weekCount = " + weekCount + ",firstDayOfWeek = " + firstDayOfWeek);
            return;
        } else if (weekCount < 0) {
            LogUtil.w(LOG_TAG, "set:invalid weekCount = " + weekCount);
            return;
        }
        mStartYear = year;
        mStartMonth = month;
        mStartDay = day;
        mWeekCount = weekCount;
        mFirstDayOfWeek = firstDayOfWeek;
        init();
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
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setCalendarManager(ICalendarManager<T> calendarManager) {
        mCalendarManager = calendarManager;
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        super.setFirstDayOfWeek(firstDayOfWeek);
        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setData(List<DayCell<T>> dataList) {
        super.setData(dataList);
        if(mAdapter != null) {
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
                    ((WeekView<T>) convertView).setCalendarManager((ICalendarManager<T>) mCalendarManager);
                    ((WeekView<T>) convertView).setFirstDayOfWeek(mFirstDayOfWeek);
                    ((WeekView<T>) convertView).setWeekViewListener((WeekViewListener<T>) mWeekViewListener);
                    ((WeekView<T>) convertView).setWeekCell(item.weekCell, true);
                    break;
                case VIEW_TYPE_DAY_OF_WEEK:
                    if (convertView == null) {
                        convertView = new WeekDayView(mContext);
                    }
                    ((WeekDayView) convertView).setFirstDayOfWeek(mFirstDayOfWeek);
                    ((WeekDayView) convertView).setDayOfWeekCellListener(mWeekDayListener);
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
