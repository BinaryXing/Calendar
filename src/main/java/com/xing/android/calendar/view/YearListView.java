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
import com.xing.android.calendar.model.YearWeekCell;
import com.xing.android.calendar.util.LogUtil;
import com.xing.android.calendar.view.WeekView.WeekViewListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zhaoxx on 16/3/29.
 */
public class YearListView<T> extends CalendarListView<T> {

    private static final int VIEW_TYPE_NORMAL = 0;
    private static final int VIEW_TYPE_DAY_OF_WEEK = 1;
    private static final int VIEW_TYPE_YEAR_HEADER = 2;
    private static final int VIEW_TYPE_YEAR_FOOTER = 3;

    private static final int VIEW_TYPE_COUNT = 4;

    protected List<YearAdapterItem<T>> mYearAdapterItemList = new ArrayList<YearAdapterItem<T>>();

    protected boolean isShowYearHeader = true;
    protected boolean isShowYearFooter = true;
    protected boolean isShowWeekDay = true;

    protected YearListListener mYearListListener;
    protected WeekDayView.WeekDayListener mWeekDayListener;
    protected WeekView.WeekViewListener<T> mWeekViewListener;

    protected YearAdapter<T> mAdapter;

    protected int mStartYear;
    protected int mYearCount;

    public YearListView(Context context) {
        super(context);
    }

    public YearListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YearListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void init() {
        mStartDayCell = null;
        mEndDayCell = null;
        mDayCellList.clear();
        mYearAdapterItemList.clear();
        if(mStartYear <= 0) {
            LogUtil.w(LOG_TAG, "init:invalid data,mStartYear = " + mStartYear);
            return;
        } else if(mYearCount <= 0) {
            LogUtil.w(LOG_TAG, "init:invalid mYearCount = " + mYearCount);
            return;
        }

        for(int i = 0 ; i < mYearCount ; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.setFirstDayOfWeek(mFirstDayOfWeek);
            calendar.set(Calendar.YEAR, mStartYear + i);
            for(int j = 1; j <= calendar.getMaximum(Calendar.WEEK_OF_YEAR) ; j++) {
                if(j == 1) {
                    if (isShowYearHeader) {
                        YearAdapterItem<T> headerItem = new YearAdapterItem<T>();
                        headerItem.viewType = VIEW_TYPE_YEAR_HEADER;
                        headerItem.year = calendar.get(Calendar.YEAR);
                        mYearAdapterItemList.add(headerItem);
                    }
                    if (isShowWeekDay) {
                        YearAdapterItem<T> dayOfWeekItem = new YearAdapterItem<T>();
                        dayOfWeekItem.viewType = VIEW_TYPE_DAY_OF_WEEK;
                        dayOfWeekItem.year = calendar.get(Calendar.YEAR);
                        mYearAdapterItemList.add(dayOfWeekItem);
                    }
                }
                YearAdapterItem<T> item = new YearAdapterItem<T>();
                item.viewType = VIEW_TYPE_NORMAL;
                item.year = calendar.get(Calendar.YEAR);
                item.weekCell = new YearWeekCell<T>(calendar.get(Calendar.YEAR), j, mFirstDayOfWeek);
                mYearAdapterItemList.add(item);
                if(item.weekCell != null && item.weekCell.getDayCellList() != null && item.weekCell.getDayCellList().size() > 0) {
                    mDayCellList.addAll(item.weekCell.getDayCellList());
                }
                if(i == 0 && j == 1 && item != null && item.weekCell != null && item.weekCell.getDayCellList() != null && item.weekCell.getDayCellList().size() > 0) {
                    DayCell<T> cell = item.weekCell.getDayCellList().get(0);
                    if(cell != null) {
                        mStartDayCell = cell.getCopyDayCell();
                    }
                } else if(i == mYearCount - 1 && j == calendar.getActualMaximum(Calendar.WEEK_OF_YEAR)) {
                    if(item != null && item.weekCell != null && item.weekCell.getDayCellList() != null && item.weekCell.getDayCellList().size() > 0) {
                        List<DayCell<T>> list = item.weekCell.getDayCellList();
                        DayCell<T> cell = list.get(list.size() - 1);
                        if(cell != null) {
                            mEndDayCell = cell.getCopyDayCell();
                        }
                    }
                }
                if(j == calendar.getMaximum(Calendar.WEEK_OF_YEAR) && isShowYearFooter) {
                    YearAdapterItem<T> footerItem = new YearAdapterItem<T>();
                    footerItem.viewType = VIEW_TYPE_YEAR_FOOTER;
                    footerItem.year = calendar.get(Calendar.YEAR);
                    mYearAdapterItemList.add(footerItem);
                }
            }
        }
        if(mAdapter == null) {
            mAdapter = new YearAdapter<T>(getContext(), mYearAdapterItemList);
            super.setAdapter(mAdapter);
        }
        mAdapter.setData(mYearAdapterItemList);
    }

    public void set(int year, int count) {
        set(year, count, mFirstDayOfWeek);
    }

    public void set(int year, int count, int firstDayOfWeek) {
        firstDayOfWeek = CalendarTool.getValidFirstDayOfWeek(firstDayOfWeek);
        if(year <= 0) {
            LogUtil.w(LOG_TAG, "set:invalid data:year = " + year);
            return;
        } else if(year == mStartYear && count == mYearCount && firstDayOfWeek == mFirstDayOfWeek) {
            LogUtil.w(LOG_TAG, "set:equal data:year = " + year + ",count = " + count + ",firstDayOfWeek = " + firstDayOfWeek);
            return;
        } else if(count < 0) {
            LogUtil.w(LOG_TAG, "set:invalid count = " + count);
            return;
        }
        mStartYear = year;
        mYearCount = count;
        mFirstDayOfWeek = firstDayOfWeek;
        init();
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        //不允许外部调用该方法,adapter在内部设置
    }


    public void setShowYearHeader(boolean value) {
        if(isShowYearHeader != value) {
            isShowYearHeader = value;
            init();
        }
    }

    public void setShowYearFooter(boolean value) {
        if(isShowYearFooter != value) {
            isShowYearFooter = value;
            init();
        }
    }

    public void setShowWeekDay(boolean value) {
        if(isShowWeekDay != value) {
            isShowWeekDay = value;
            init();
        }
    }

    /**
     * 在set之前调用
     * @param yearListListener
     * @param weekDayListener
     * @param weekViewListener
     */
    public void setListener(YearListListener yearListListener, WeekDayView.WeekDayListener weekDayListener, WeekView.WeekViewListener<T> weekViewListener) {
        if(mYearListListener != yearListListener || mWeekDayListener != weekDayListener || mWeekViewListener != weekViewListener) {
            mYearListListener = yearListListener;
            mWeekDayListener = weekDayListener;
            mWeekViewListener = weekViewListener;
            if(mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
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

    public interface YearListListener {
        View newYearHeaderView();
        void bindYearHeaderView(View view, int year);
        View newYearFooterView();
        void bindYearFooterView(View view, int year);
    }

    public class YearAdapter<T> extends BaseAdapter {

        private final String LOG_TAG = getClass().getSimpleName();

        private Context mContext;
        private List<YearAdapterItem<T>> mDataList = new ArrayList<YearAdapterItem<T>>();

        public YearAdapter(Context context, List<YearAdapterItem<T>> list) {
            mContext = context;
            if(list != null) {
                mDataList = list;
            }
        }

        public void setData(List<YearAdapterItem<T>> list) {
            mDataList = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mDataList == null ? 0 : mDataList.size();
        }

        @Override
        public YearAdapterItem<T> getItem(int position) {
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
            YearAdapterItem<T> item = getItem(position);
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
            YearAdapterItem<T> item = getItem(position);
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
                case VIEW_TYPE_YEAR_HEADER:
                    if(mYearListListener == null) {
                        LogUtil.w(LOG_TAG, "getView:mYearListListener is null");
                        return convertView;
                    }
                    if(convertView == null) {
                        convertView = mYearListListener.newYearHeaderView();
                    }
                    mYearListListener.bindYearHeaderView(convertView, item.year);
                    break;
                case VIEW_TYPE_YEAR_FOOTER:
                    if(mYearListListener == null) {
                        LogUtil.w(LOG_TAG, "getView:mYearListListener is null");
                        return convertView;
                    }
                    if(convertView == null) {
                        convertView = mYearListListener.newYearFooterView();
                    }
                    mYearListListener.bindYearFooterView(convertView, item.year);
                    break;
                default:
                    break;
            }
            return convertView;
        }
    }

    public class YearAdapterItem<T> {
        public int viewType;
        public int year;
        public IWeekCell<T> weekCell;
    }
}
