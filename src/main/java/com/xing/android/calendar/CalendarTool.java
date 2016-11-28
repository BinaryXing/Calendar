package com.xing.android.calendar;

import com.xing.android.calendar.model.ContinuousSelectItem;
import com.xing.android.calendar.model.DayCell;
import com.xing.android.calendar.util.LogUtil;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by zhaoxx on 16/3/9.
 */
public class CalendarTool {
    private static final String LOG_TAG = "CalendarTool";

    public static <T>boolean isEqual(DayCell<T> firstDayCell, DayCell<T> secondDayCell) {
        if(firstDayCell == null || secondDayCell == null) {
            LogUtil.w(LOG_TAG, "isEqual:firstDayCell = " + firstDayCell + ",secondDayCell = " + secondDayCell);
            return false;
        }
        if(firstDayCell.getYear() == secondDayCell.getYear()
                && firstDayCell.getMonth() == secondDayCell.getMonth()
                && firstDayCell.getDay() == secondDayCell.getDay()) {
            return true;
        }
        return false;
    }

    public static <T>boolean isBefore(DayCell<T> firstDayCell, DayCell<T> secondDayCell) {
        if(firstDayCell == null || secondDayCell == null) {
            LogUtil.w(LOG_TAG, "isBefore:firstDayCell = " + firstDayCell + ",secondDayCell = " + secondDayCell);
            return false;
        }
        Calendar first = Calendar.getInstance();
        first.clear();
        first.set(firstDayCell.getYear(), firstDayCell.getMonth() - 1, firstDayCell.getDay());
        Calendar second = Calendar.getInstance();
        second.clear();
        second.set(secondDayCell.getYear(), secondDayCell.getMonth() - 1, secondDayCell.getDay());
        return first.before(second);
    }

    public static <T>boolean isAfter(DayCell<T> firstDayCell, DayCell<T> secondDayCell) {
        if(firstDayCell == null || secondDayCell == null) {
            LogUtil.w(LOG_TAG, "isAfter:firstDayCell = " + firstDayCell + ",secondDayCell = " + secondDayCell);
            return false;
        }
        Calendar first = Calendar.getInstance();
        first.set(firstDayCell.getYear(), firstDayCell.getMonth() - 1, firstDayCell.getDay());
        Calendar second = Calendar.getInstance();
        second.set(secondDayCell.getYear(), secondDayCell.getMonth() - 1, secondDayCell.getDay());
        return first.after(second);
    }

    public static int getValidFirstDayOfWeek(int firstDayOfWeek) {
        if(firstDayOfWeek < Calendar.SUNDAY || firstDayOfWeek > Calendar.SATURDAY) {
            firstDayOfWeek = Calendar.SUNDAY;
        }
        return firstDayOfWeek;
    }

    public static boolean checkValidOfDay(int firstDayOfWeek, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setFirstDayOfWeek(firstDayOfWeek);
        if(year < 0) {
            LogUtil.w(LOG_TAG, "checkValidOfDay:year = " + year);
            return false;
        }
        calendar.set(Calendar.YEAR, year);
        if(month - 1 < calendar.getActualMinimum(Calendar.MONTH) || month - 1 > calendar.getActualMaximum(Calendar.MONTH)) {
            LogUtil.w(LOG_TAG, "checkValidOfDay:month = " + month);
            return false;
        }
        calendar.set(Calendar.MONTH, month - 1);
        if(day < calendar.getActualMinimum(Calendar.DAY_OF_MONTH) || day > calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            LogUtil.w(LOG_TAG, "checkValidOfDay:day = " + day);
            return false;
        }
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return true;
    }

    public static boolean checkValidOfWeek(int firstDayOfWeek, int year, int month, int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(firstDayOfWeek);
        if(year < 0) {
            LogUtil.w(LOG_TAG, "checkValidOfWeek:year = " + year);
            return false;
        }
        calendar.set(Calendar.YEAR, year);
        if(month - 1 < calendar.getActualMinimum(Calendar.MONTH) || month - 1 > calendar.getActualMaximum(Calendar.MONTH)) {
            LogUtil.w(LOG_TAG, "checkValidOfWeek:month = " + month);
            return false;
        }
        calendar.set(Calendar.MONTH, month - 1);
        if(week < calendar.getActualMinimum(Calendar.WEEK_OF_MONTH) || week > calendar.getActualMaximum(Calendar.WEEK_OF_MONTH)) {
            LogUtil.w(LOG_TAG, "checkValidOfWeek:week = " + week);
            return false;
        }
        calendar.set(Calendar.WEEK_OF_MONTH, week);
        return true;
    }

    public static <T>void setContinuousItemValid(ContinuousSelectItem<T> item) {
        if(item == null) {
            return;
        }
        if(item.mStartDayCell == null && item.mEndDayCell != null) {
            item.mStartDayCell = item.mEndDayCell;
            item.mEndDayCell = null;
        } else if(item.mStartDayCell != null && item.mEndDayCell != null && CalendarTool.isAfter(item.mStartDayCell, item.mEndDayCell)) {
            DayCell<T> tmpDayCell = item.mStartDayCell;
            item.mStartDayCell = item.mEndDayCell;
            item.mEndDayCell = tmpDayCell;
        }
    }

    public static boolean checkValidOfWeek(int firstDayOfWeek, int year, int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setFirstDayOfWeek(firstDayOfWeek);
        if(year < 0) {
            LogUtil.w(LOG_TAG, "checkValidOfWeek:year = " + year);
            return false;
        }
        calendar.set(Calendar.YEAR, year);
        if(week < calendar.getMinimum(Calendar.WEEK_OF_YEAR) || week > calendar.getMaximum(Calendar.WEEK_OF_YEAR)) {
            LogUtil.w(LOG_TAG, "checkValidOfWeek:week = " + week);
            return false;
        }
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
        if(calendar.get(Calendar.YEAR) > year) {
            return false;
        }
        return true;
    }

    public static boolean checkValidOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        if(year < 0) {
            LogUtil.w(LOG_TAG, "checkValidOfMonth:year = " + year);
            return false;
        }
        calendar.set(Calendar.YEAR, year);
        if(month - 1 < calendar.getActualMinimum(Calendar.MONTH) || month - 1 > calendar.getActualMaximum(Calendar.MONTH)) {
            LogUtil.w(LOG_TAG, "checkValidOfMonth:month = " + month);
            return false;
        }
        calendar.set(Calendar.MONTH, month - 1);
        return true;
    }

    public static <T>Calendar getCalendar(DayCell<T> dayCell) {
        if(dayCell == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(dayCell.getYear(), dayCell.getMonth() - 1, dayCell.getDay());
        return calendar;
    }

    public static <T>Date getDate(DayCell<T> dayCell) {
        Calendar calendar = getCalendar(dayCell);
        if(calendar == null) {
            return null;
        }
        return calendar.getTime();
    }

    public static <T> DayCell<T> getDayCell(Calendar calendar, T t) {
        if(calendar == null) {
            return null;
        }
        DayCell<T> dayCell = new DayCell<T>(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        dayCell.setData(t);
        return dayCell;
    }

    /**
     * 间隔天数,开始日期和结束日期也包括在内
     * @param firstDayCell
     * @param secondDayCell
     * @param <T>
     * @return
     */
    public static <T>int getInclusiveIntervalDays(DayCell<T> firstDayCell, DayCell<T> secondDayCell) {
        if(firstDayCell == null || secondDayCell == null) {
            return -1;
        }
        if(isAfter(firstDayCell, secondDayCell)) {
            DayCell<T> dayCell = firstDayCell;
            firstDayCell = secondDayCell;
            secondDayCell = dayCell;
        }
        Calendar firstCalendar = getCalendar(firstDayCell);
        Calendar secondCalendar = getCalendar(secondDayCell);
        return (int) ((secondCalendar.getTimeInMillis() - firstCalendar.getTimeInMillis()) / (24 * 60 * 60 * 1000) + 1);
    }

    /**
     * 对DayCell列表进行排序
     * @param list 数据List
     * @param asc 是否升序
     * @param <T>
     */
    public static <T>void sortDayCellList(List<DayCell<T>> list, final boolean asc) {
        if(list == null || list.size() == 0) {
            LogUtil.i(LOG_TAG, "sortDayCellList:list is empty");
            return;
        }
        Collections.sort(list, new Comparator<DayCell<T>>() {
            @Override
            public int compare(DayCell<T> lhs, DayCell<T> rhs) {
                if(lhs == null || rhs == null || isEqual(lhs, rhs)) {
                    return 0;
                }
                if(asc) {
                    return isBefore(lhs, rhs) ? -1 : 1;
                } else {
                    return isBefore(lhs, rhs) ? 1 : -1;
                }
            }
        });
    }

    /**
     * 对ContinuousItem列表进行排序，ContinuousItem不能有交叉
     * @param list 数据List
     * @param asc
     * @param <T>
     */
    public static <T>void sortContinuousItemList(List<ContinuousSelectItem<T>> list, final boolean asc) {
        if(list == null || list.size() == 0) {
            LogUtil.i(LOG_TAG, "sortContinuousItemList:list is empty");
            return;
        }
        Collections.sort(list, new Comparator<ContinuousSelectItem<T>>() {
            @Override
            public int compare(ContinuousSelectItem<T> lhs, ContinuousSelectItem<T> rhs) {
                if(lhs == null || (lhs.mStartDayCell == null && lhs.mEndDayCell == null) ||
                        rhs == null || (rhs.mStartDayCell == null && rhs.mEndDayCell == null)) {
                    return 0;
                }
                setContinuousItemValid(lhs);
                setContinuousItemValid(rhs);
                DayCell<T> leftCell = null;
                DayCell<T> rightCell = null;
                if(lhs.mEndDayCell != null) {
                    leftCell = lhs.mEndDayCell;
                } else if(lhs.mStartDayCell != null) {
                    leftCell = lhs.mStartDayCell;
                }
                if(rhs.mStartDayCell != null) {
                    rightCell = rhs.mStartDayCell;
                } else if(rhs.mEndDayCell != null) {
                    rightCell = rhs.mEndDayCell;
                }
                if(asc) {
                    return isBefore(leftCell, rightCell) ? -1 : 1;
                } else {
                    return isBefore(leftCell, rightCell) ? 1 : -1;
                }
            }
        });
    }

    public static <T>boolean isContinuousItemCross(ContinuousSelectItem<T> leftItem, ContinuousSelectItem<T> rightItem) {
        setContinuousItemValid(leftItem);
        setContinuousItemValid(rightItem);
        if(leftItem == null || (leftItem.mStartDayCell == null && leftItem.mEndDayCell == null) ||
                rightItem == null || (rightItem.mStartDayCell == null && rightItem.mEndDayCell == null)) {
            LogUtil.i(LOG_TAG, "isContinuousItemCross:empty item,leftItem = " + leftItem + ", rightItem = " + rightItem);
            return false;
        } else {
            ContinuousSelectItem<T> localLeftItem = leftItem.getCopyContinuousSelectItem();
            ContinuousSelectItem<T> localRightItem = rightItem.getCopyContinuousSelectItem();
            //如果只有开始日期，没有结束日期，可以Item当作开始和结束日期一样
            if(localLeftItem.mEndDayCell == null) {
                localLeftItem.mEndDayCell = localLeftItem.mStartDayCell;
            }
            //如果只有开始日期，没有结束日期，可以Item当作开始和结束日期一样
            if(localRightItem.mEndDayCell == null) {
                localRightItem.mEndDayCell = localRightItem.mStartDayCell;
            }
            if(!(isBefore(localLeftItem.mEndDayCell, localRightItem.mStartDayCell) || (isAfter(localLeftItem.mStartDayCell, localRightItem.mEndDayCell)))) {
                return true;
            }
        }
        return false;
    }

    public static <T>boolean isEqual(ContinuousSelectItem<T> leftItem, ContinuousSelectItem<T> rightItem) {
        setContinuousItemValid(leftItem);
        setContinuousItemValid(rightItem);
        if(leftItem == null || (leftItem.mStartDayCell == null && leftItem.mEndDayCell == null) ||
                rightItem == null || (rightItem.mStartDayCell == null && rightItem.mEndDayCell == null)) {
            LogUtil.i(LOG_TAG, "isEqual:empty item,leftItem = " + leftItem + ", rightItem = " + rightItem);
            return false;
        } else if(leftItem.mStartDayCell != null && leftItem.mEndDayCell == null &&
                rightItem.mStartDayCell != null && rightItem.mEndDayCell == null &&
                isEqual(leftItem.mStartDayCell, rightItem.mStartDayCell)) {
            return true;
        } else if(leftItem.mStartDayCell != null && leftItem.mEndDayCell != null &&
                rightItem.mStartDayCell != null && rightItem.mEndDayCell != null &&
                isEqual(leftItem.mStartDayCell, rightItem.mStartDayCell) && isEqual(leftItem.mEndDayCell, rightItem.mEndDayCell)) {
            return true;
        }
        return false;
    }


    public static <T>boolean isBefore(ContinuousSelectItem<T> leftItem, ContinuousSelectItem<T> rightItem) {
        setContinuousItemValid(leftItem);
        setContinuousItemValid(rightItem);
        if(leftItem == null || (leftItem.mStartDayCell == null && leftItem.mEndDayCell == null) ||
                rightItem == null || (rightItem.mStartDayCell == null && rightItem.mEndDayCell == null)) {
            LogUtil.i(LOG_TAG, "isBefore:empty item,leftItem = " + leftItem + ", rightItem = " + rightItem);
            return false;
        } else {
            DayCell<T> leftCell;
            if(leftItem.mEndDayCell != null) {
                leftCell = leftItem.mEndDayCell;
            } else {
                leftCell = leftItem.mStartDayCell;
            }
            DayCell<T> rightCell = rightItem.mStartDayCell;
            return isBefore(leftCell, rightCell);
        }
    }

    public static <T>boolean isAfter(ContinuousSelectItem<T> leftItem, ContinuousSelectItem<T> rightItem) {
        setContinuousItemValid(leftItem);
        setContinuousItemValid(rightItem);
        if(leftItem == null || (leftItem.mStartDayCell == null && leftItem.mEndDayCell == null) ||
                rightItem == null || (rightItem.mStartDayCell == null && rightItem.mEndDayCell == null)) {
            LogUtil.i(LOG_TAG, "isAfter:empty item,leftItem = " + leftItem + ", rightItem = " + rightItem);
            return false;
        } else {
            DayCell<T> leftCell = leftItem.mStartDayCell;
            DayCell<T> rightCell;
            if(rightItem.mEndDayCell != null) {
                rightCell = rightItem.mEndDayCell;
            } else {
                rightCell = rightItem.mStartDayCell;
            }
            return isAfter(leftCell, rightCell);
        }
    }

    public static <T>boolean isBefore(DayCell<T> leftCell, ContinuousSelectItem<T> rightItem) {
        if(leftCell == null) {
            LogUtil.i(LOG_TAG, "isBefore:leftCell is null");
            return false;
        } else if(rightItem == null || (rightItem.mStartDayCell == null && rightItem.mEndDayCell == null)) {
            LogUtil.i(LOG_TAG, "isBefore:rightItem is null");
            return false;
        }
        setContinuousItemValid(rightItem);
        DayCell<T> rightCell = rightItem.mStartDayCell;
        return isBefore(leftCell, rightCell);
    }

    public static <T>boolean isAfter(DayCell<T> leftCell, ContinuousSelectItem<T> rightItem) {
        if(leftCell == null) {
            LogUtil.i(LOG_TAG, "isAfter:leftCell is null");
            return false;
        } else if(rightItem == null || (rightItem.mStartDayCell == null && rightItem.mEndDayCell == null)) {
            LogUtil.i(LOG_TAG, "isAfter:rightItem is null");
            return false;
        }
        setContinuousItemValid(rightItem);
        DayCell<T> rightCell;
        if(rightItem.mEndDayCell != null) {
            rightCell = rightItem.mEndDayCell;
        } else {
            rightCell = rightItem.mStartDayCell;
        }
        return isAfter(leftCell, rightCell);
    }

    public static <T>boolean isBefore(ContinuousSelectItem<T> leftItem, DayCell<T> rightCell) {
        if(rightCell == null) {
            LogUtil.i(LOG_TAG, "isBefore:rightCell is null");
            return false;
        } else if(leftItem == null || (leftItem.mStartDayCell == null && leftItem.mEndDayCell == null)) {
            LogUtil.i(LOG_TAG, "isBefore:leftItem is null");
            return false;
        }
        setContinuousItemValid(leftItem);
        DayCell<T> leftCell;
        if(leftItem.mEndDayCell != null) {
            leftCell = leftItem.mEndDayCell;
        } else {
            leftCell = leftItem.mStartDayCell;
        }
        return isBefore(leftCell, rightCell);
    }

    public static <T>boolean isAfter(ContinuousSelectItem<T> leftItem, DayCell<T> rightCell) {
        if(rightCell == null) {
            LogUtil.i(LOG_TAG, "isAfter:rightCell is null");
            return false;
        } else if(leftItem == null || (leftItem.mStartDayCell == null && leftItem.mEndDayCell == null)) {
            LogUtil.i(LOG_TAG, "isAfter:leftItem is null");
            return false;
        }
        setContinuousItemValid(leftItem);
        DayCell<T> leftCell = leftItem.mStartDayCell;
        return isAfter(leftCell, rightCell);
    }

    /**
     * cell是否属于item（包括开始/结束日期）
     * @param cell
     * @param item
     * @param <T>
     * @return
     */
    public static <T>boolean isInClusive(DayCell<T> cell, ContinuousSelectItem<T> item) {
        if(cell == null) {
            LogUtil.i(LOG_TAG, "isInClusive:cell is null");
            return false;
        } else if(item == null || (item.mStartDayCell == null && item.mEndDayCell == null)) {
            LogUtil.i(LOG_TAG, "isInClusive:item is null");
            return false;
        }
        setContinuousItemValid(item);
        if(item.mStartDayCell != null && item.mEndDayCell == null) {
            return isEqual(cell, item.mStartDayCell);
        } else {
            return !(isBefore(cell, item.mStartDayCell) || isAfter(cell, item.mEndDayCell));
        }
    }

    public static <T>boolean isExClusive(DayCell<T> cell, ContinuousSelectItem<T> item) {
        if(cell == null) {
            LogUtil.i(LOG_TAG, "isExClusive:cell is null");
            return false;
        } else if(item == null || (item.mStartDayCell == null && item.mEndDayCell == null)) {
            LogUtil.i(LOG_TAG, "isExClusive:item is null");
            return false;
        }
        setContinuousItemValid(item);
        return isAfter(cell, item.mStartDayCell) && isBefore(cell, item.mEndDayCell);
    }

    public static <T>void clearNullDayCell(List<DayCell<T>> list) {
        if(list == null || list.size() == 0) {
            LogUtil.i(LOG_TAG, "clearNullDayCell:list is empty");
            return;
        }
        DayCell<T> cell;
        for(int i = list.size() - 1 ; i >= 0 ; i--) {
            cell = list.get(i);
            if(cell == null) {
                list.remove(i);
            }
        }
    }

    public static <T>void  clearNullContinuousItem(List<ContinuousSelectItem<T>> list) {
        if(list == null || list.size() == 0) {
            LogUtil.i(LOG_TAG, "clearNullContinuousItem:list is empty");
            return;
        }
        ContinuousSelectItem<T> item;
        for(int i = list.size() - 1 ; i >= 0 ; i--) {
            item = list.get(i);
            if(item == null || (item.mStartDayCell == null && item.mEndDayCell == null)) {
                list.remove(i);
            }
        }
    }

    public static int getLastDayOfWeek(int firstDayOfWeek) {
        firstDayOfWeek = getValidFirstDayOfWeek(firstDayOfWeek);
        return (firstDayOfWeek + 5) % 7 + 1;
    }

}
