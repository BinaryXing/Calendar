package com.xing.android.calendar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.xing.android.calendar.CalendarTool;
import com.xing.android.calendar.ICalendarManager;
import com.xing.android.calendar.model.DayCell;
import com.xing.android.calendar.model.IWeekCell;
import com.xing.android.calendar.model.MonthWeekCell;
import com.xing.android.calendar.util.LogUtil;
import com.xing.android.calendar.view.WeekView.WeekViewListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zhaoxx on 16/3/14.
 */
public class MonthListView<T> extends CalendarListView<T> {

    /**
     * WeekCell(具体到某一天)
     */
    public static final int VIEW_TYPE_NORMAL = 0;
    /**
     * 周日,周一,周二,周三,周四,周五,周六的WeekCell
     */
    public static final int VIEW_TYPE_DAY_OF_WEEK = 1;
    /**
     * 月份头部的自定义View
     */
    public static final int VIEW_TYPE_MONTH_HEADER = 2;
    /**
     * 月份底部的自定义View
     */
    public static final int VIEW_TYPE_MONTH_FOOTER = 3;

    public static final int VIEW_TYPE_COUNT = 4;

    protected List<MonthAdapterItem<T>> mMonthAdapterItemList = new ArrayList<MonthAdapterItem<T>>();

    protected boolean isShowWeekDay = true;
    protected boolean isShowMonthHeader = true;
    protected boolean isShowMonthFooter = true;

    /**
     * WeekView中的每一天的Cell和WeekHeader,WeekFooter的Adapter,必须项
     */
    protected WeekView.WeekViewListener<T> mWeekViewListener;
    /**
     * 周日,一,二,三,四,五,六的Adapter
     */
    protected WeekDayView.WeekDayListener mWeekDayListener;
    /**
     * 每月Header和Footer的Adapter
     */
    protected MonthListListener mMonthListListener;

    protected MonthAdapter<T> mAdapter;

    protected int mStartYear;
    protected int mStartMonth;
    protected int mMonthCount;

    public MonthListView(Context context) {
        super(context);
    }

    public MonthListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MonthListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void init() {
        mStartDayCell = null;
        mEndDayCell = null;
        mDayCellList.clear();
        mMonthAdapterItemList.clear();
        if(!CalendarTool.checkValidOfMonth(mStartYear, mStartMonth)) {
            LogUtil.w(LOG_TAG, "init:invalid data,mStartYear = " + mStartYear + ",mStartMonth = " + mStartMonth);
            return;
        } else if(mMonthCount <= 0) {
            LogUtil.w(LOG_TAG, "init:invalid mMonthCount = " + mMonthCount);
            return;
        }

        for(int i = 0 ; i < mMonthCount ; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.setFirstDayOfWeek(mFirstDayOfWeek);
            calendar.set(mStartYear, mStartMonth + i - 1, 1);
            for(int j = 1; j <= calendar.getActualMaximum(Calendar.WEEK_OF_MONTH) ; j++) {
                if(j == 1) {
                    if (isShowMonthHeader) {
                        MonthAdapterItem<T> headerItem = new MonthAdapterItem<T>();
                        headerItem.viewType = VIEW_TYPE_MONTH_HEADER;
                        headerItem.year = calendar.get(Calendar.YEAR);
                        headerItem.month = calendar.get(Calendar.MONTH) + 1;
                        mMonthAdapterItemList.add(headerItem);
                    }
                    if (isShowWeekDay) {
                        MonthAdapterItem<T> dayOfWeekItem = new MonthAdapterItem<T>();
                        dayOfWeekItem.viewType = VIEW_TYPE_DAY_OF_WEEK;
                        dayOfWeekItem.year = calendar.get(Calendar.YEAR);
                        dayOfWeekItem.month = calendar.get(Calendar.MONTH) + 1;
                        mMonthAdapterItemList.add(dayOfWeekItem);
                    }
                }
                MonthAdapterItem<T> item = new MonthAdapterItem<T>();
                item.viewType = VIEW_TYPE_NORMAL;
                item.year = calendar.get(Calendar.YEAR);
                item.month = calendar.get(Calendar.MONTH) + 1;
                item.weekCell = new MonthWeekCell<T>(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, j, mFirstDayOfWeek);
                mMonthAdapterItemList.add(item);
                if(item.weekCell != null && item.weekCell.getDayCellList() != null && item.weekCell.getDayCellList().size() > 0) {
                    mDayCellList.addAll(item.weekCell.getDayCellList());
                }
                if(i == 0 && j == 1 && item != null && item.weekCell != null && item.weekCell.getDayCellList() != null && item.weekCell.getDayCellList().size() > 0) {
                    DayCell<T> cell = item.weekCell.getDayCellList().get(0);
                    if(cell != null) {
                        mStartDayCell = cell.getCopyDayCell();
                    }
                } else if(i == mMonthCount - 1 && j == calendar.getActualMaximum(Calendar.WEEK_OF_MONTH)) {
                    if(item != null && item.weekCell != null && item.weekCell.getDayCellList() != null && item.weekCell.getDayCellList().size() > 0) {
                        List<DayCell<T>> list = item.weekCell.getDayCellList();
                        DayCell<T> cell = list.get(list.size() - 1);
                        if(cell != null) {
                            mEndDayCell = cell.getCopyDayCell();
                        }
                    }
                }
                if(j == calendar.getActualMaximum(Calendar.WEEK_OF_MONTH) && isShowMonthFooter) {
                    MonthAdapterItem<T> footerItem = new MonthAdapterItem<T>();
                    footerItem.viewType = VIEW_TYPE_MONTH_FOOTER;
                    footerItem.year = calendar.get(Calendar.YEAR);
                    footerItem.month = calendar.get(Calendar.MONTH) + 1;
                    mMonthAdapterItemList.add(footerItem);
                }
            }

        }
        if(mAdapter == null) {
            mAdapter = new MonthAdapter<T>(getContext(), mMonthAdapterItemList);
            super.setAdapter(mAdapter);
        }
        mAdapter.setData(mMonthAdapterItemList);
    }

    public void setShowWeekDay(boolean value) {
        if(isShowWeekDay != value) {
            isShowWeekDay = value;
            init();
        }
    }

    public void setShowMonthHeader(boolean value) {
        if(isShowMonthHeader != value) {
            isShowMonthHeader = value;
            init();
        }
    }

    public void setShowMonthFooter(boolean value) {
        if(isShowMonthFooter != value) {
            isShowMonthFooter = value;
            init();
        }
    }

    /**
     * 在set方法之前调用
     * @param monthListListener
     * @param weekDayListener
     * @param weekViewListener
     */
    public void setListener(MonthListListener monthListListener, WeekDayView.WeekDayListener weekDayListener, WeekView.WeekViewListener<T> weekViewListener) {
        if(mMonthListListener != monthListListener || mWeekDayListener != weekDayListener || mWeekViewListener != weekViewListener) {
            mMonthListListener = monthListListener;
            mWeekDayListener = weekDayListener;
            mWeekViewListener = weekViewListener;
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void set(int year, int month, int count) {
        set(year, month, count, mFirstDayOfWeek);
    }

    public void set(int year, int month, int count, int firstDayofWeek) {
        firstDayofWeek = CalendarTool.getValidFirstDayOfWeek(firstDayofWeek);
        if(!CalendarTool.checkValidOfMonth(year, month)) {
            LogUtil.w(LOG_TAG, "set:invalid data:year = " + year + ",month = " + month);
            return;
        } else if(year == mStartYear && month == mStartMonth && count == mMonthCount && firstDayofWeek == mFirstDayOfWeek) {
            LogUtil.w(LOG_TAG, "set:equal data:year = " + year + ",month = " + month + ",count = " + count + "firstDayOfWeek = " + firstDayofWeek);
            return;
        } else if(count < 0) {
            LogUtil.w(LOG_TAG, "set:invalid count = " + count);
            return;
        }
        mStartYear = year;
        mStartMonth = month;
        mMonthCount = count;
        mFirstDayOfWeek = firstDayofWeek;
        init();
    }

    public int getStartYear() {
        return mStartYear;
    }

    public int getStartMonth() {
        return mStartMonth;
    }

    public int getMonthCount() {
        return mMonthCount;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        //不允许外部调用该方法,adapter在内部设置
    }

    @Override
    public void refresh() {
        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setCalendarManager(ICalendarManager<T> calendarManager) {
        mCalendarManager = calendarManager;
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

    @Override
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        super.setFirstDayOfWeek(firstDayOfWeek);
        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public interface MonthListListener {
        View newMonthHeaderView();
        void bindMonthHeaderView(View view, int year, int month);
        View newMonthFooterView();
        void bindMonthFooterView(View view, int year, int month);
    }

    public class MonthAdapter<T> extends BaseAdapter {

        private final String LOG_TAG = getClass().getSimpleName();

        private Context mContext;
        private List<MonthAdapterItem<T>> mDataList = new ArrayList<MonthAdapterItem<T>>();

        public MonthAdapter(Context context, List<MonthAdapterItem<T>> list) {
            mContext = context;
            if(list != null) {
                mDataList = list;
            }
        }

        public void setData(List<MonthAdapterItem<T>> list) {
            mDataList = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mDataList == null ? 0 : mDataList.size();
        }

        @Override
        public MonthAdapterItem<T> getItem(int position) {
            if(position < 0 || position >= getCount()) {
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
            MonthAdapterItem<T> item = getItem(position);
            if(item != null) {
                return item.viewType;
            }
            return super.getItemViewType(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(mWeekViewListener == null) {
                LogUtil.w(LOG_TAG, "getView:mWeekViewListener is null");
                return convertView;
            }
            MonthAdapterItem<T> item = getItem(position);
            if(item == null) {
                LogUtil.w(LOG_TAG, "getView:item is null");
                return convertView;
            }
            switch (getItemViewType(position)) {
                case VIEW_TYPE_NORMAL:
                    if(convertView == null) {
                        convertView = new WeekView<T>(mContext);
                    }
                    ((WeekView<T>) convertView).setCalendarManager((ICalendarManager<T>) mCalendarManager);
                    ((WeekView<T>)convertView).setFirstDayOfWeek(mFirstDayOfWeek);
                    ((WeekView<T>)convertView).setWeekViewListener((WeekViewListener<T>) mWeekViewListener);
                    ((WeekView<T>)convertView).setWeekCell(item.weekCell, true);
                    break;
                case VIEW_TYPE_DAY_OF_WEEK:
                    if(convertView == null) {
                        convertView = new WeekDayView(mContext);
                    }
                    ((WeekDayView)convertView).setFirstDayOfWeek(mFirstDayOfWeek);
                    ((WeekDayView)convertView).setDayOfWeekCellListener(mWeekDayListener);
                    break;
                case VIEW_TYPE_MONTH_HEADER:
                    if(mMonthListListener == null) {
                        LogUtil.w(LOG_TAG, "getView:mMonthListListener is null");
                        return convertView;
                    }
                    if(convertView == null) {
                        convertView = mMonthListListener.newMonthHeaderView();
                    }
                    mMonthListListener.bindMonthHeaderView(convertView, item.year, item.month);
                    break;
                case VIEW_TYPE_MONTH_FOOTER:
                    if(mMonthListListener == null) {
                        LogUtil.w(LOG_TAG, "getView:mMonthListListener is null");
                        return convertView;
                    }
                    if(convertView == null) {
                        convertView = mMonthListListener.newMonthFooterView();
                    }
                    mMonthListListener.bindMonthFooterView(convertView, item.year, item.month);
                    break;
                default:
                    break;
            }
            return convertView;
        }
    }

    public class MonthAdapterItem<T> {
        public int viewType;
        public int year;
        public int month;
        public IWeekCell<T> weekCell;
    }
}
