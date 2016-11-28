package com.xing.android.calendar.model;

/**
 * Created by zhaoxx on 16/3/29.
 */
public class ContinuousSelectItem<T> {
    public DayCell<T> mStartDayCell;
    public DayCell<T> mEndDayCell;

    public ContinuousSelectItem<T> getCopyContinuousSelectItem() {
        ContinuousSelectItem<T> item = new ContinuousSelectItem<>();
        if(mStartDayCell != null) {
            item.mStartDayCell = mStartDayCell.getCopyDayCell();
        }
        if(mEndDayCell != null) {
            item.mEndDayCell = mEndDayCell.getCopyDayCell();
        }
        return item;
    }
}
