package com.xing.android.calendar;

import android.support.annotation.NonNull;

import com.xing.android.calendar.model.ContinuousSelectItem;
import com.xing.android.calendar.model.DayCell;
import com.xing.android.calendar.util.LogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 日历选择框架里涉及的一些静态方法
 * Created by zhaoxx on 16/3/9.
 */
public class CalendarTool {

    private static final String LOG_TAG = "CalendarTool";

    /**
     * 判断两个DayCell是否是同一天；
     * 对参数不做有效校验，比如两个2012-13-59的DayCell，返回true；
     * 不做DayCell中的Data比较；
     * @param firstDayCell 非空
     * @param secondDayCell 非空
     * @return 如果有Cell为null，返回false
     */
    public static boolean isEqual(DayCell firstDayCell, DayCell secondDayCell) {
        if(firstDayCell == null || secondDayCell == null) {
            return false;
        }
        return firstDayCell.getYear() == secondDayCell.getYear()
                && firstDayCell.getMonth() == secondDayCell.getMonth()
                && firstDayCell.getDay() == secondDayCell.getDay();
    }

    /**
     * 判断firstDayCell是否在secondDayCell的前面；
     * 不做DayCell中的Data比较；
     * @param firstDayCell 非空
     * @param secondDayCell 非空
     * @return 如果有Cell为null，返回false
     */
    public static boolean isBefore(DayCell firstDayCell, DayCell secondDayCell) {
        if(firstDayCell == null || secondDayCell == null) {
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

    /**
     * 判断firstDayCell是否在secondDayCell的后面；
     * 不做DayCell中的Data比较；
     * @param firstDayCell 非空
     * @param secondDayCell 非空
     * @return 如果有Cell为null，返回false
     */
    public static boolean isAfter(DayCell firstDayCell, DayCell secondDayCell) {
        if(firstDayCell == null || secondDayCell == null) {
            return false;
        }
        Calendar first = Calendar.getInstance();
        first.set(firstDayCell.getYear(), firstDayCell.getMonth() - 1, firstDayCell.getDay());
        Calendar second = Calendar.getInstance();
        second.set(secondDayCell.getYear(), secondDayCell.getMonth() - 1, secondDayCell.getDay());
        return first.after(second);
    }

    /**
     * 获取有效的一周的第一天，如果firstDayOfWeek无效，则默认周日
     * @param firstDayOfWeek
     * @return
     */
    public static int getValidFirstDayOfWeek(int firstDayOfWeek) {
        if(firstDayOfWeek < Calendar.SUNDAY || firstDayOfWeek > Calendar.SATURDAY) {
            firstDayOfWeek = Calendar.SUNDAY;
        }
        return firstDayOfWeek;
    }

    /**
     * 获取一周的最后一天
     * @param firstDayOfWeek
     * @return
     */
    public static int getLastDayOfWeek(int firstDayOfWeek) {
        firstDayOfWeek = getValidFirstDayOfWeek(firstDayOfWeek);
        return (firstDayOfWeek + 5) % 7 + 1;
    }

    /**
     * 校验某一天是否有效
     * @param firstDayOfWeek
     * @param year
     * @param month
     * @param day
     * @return
     */
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

    /**
     * 校验某年某月某周是否有效
     * @param firstDayOfWeek
     * @param year
     * @param month
     * @param week
     * @return
     */
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

    /**
     * 校验某年某周是否有效
     * @param firstDayOfWeek
     * @param year
     * @param week
     * @return
     */
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

    /**
     * 校验某年某月是否有效
     * @param year
     * @param month
     * @return
     */
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

    /**
     * ContinuousSelectItem设置成合理数据
     * @param item
     */
    public static void setContinuousItemValid(ContinuousSelectItem item) {
        if(item == null) {
            return;
        }
        if(item.mStartDayCell == null && item.mEndDayCell != null) {
            //如果mStartDayCell为空，mEndDayCell不为空，则互换
            item.mStartDayCell = item.mEndDayCell;
            item.mEndDayCell = null;
        } else if(item.mStartDayCell != null && item.mEndDayCell != null && CalendarTool.isAfter(item.mStartDayCell, item.mEndDayCell)) {
            //如果mStartDayCell，mEndDayCell都不为空，如果mStartDayCell大于mEndDayCell，互换
            DayCell tmpDayCell = item.mStartDayCell;
            item.mStartDayCell = item.mEndDayCell;
            item.mEndDayCell = tmpDayCell;
        }
    }

    /**
     * 返回DayCell对应的Calendar
     * @param dayCell
     * @return
     */
    public static Calendar getCalendar(DayCell dayCell) {
        if(dayCell == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(dayCell.getYear(), dayCell.getMonth() - 1, dayCell.getDay());
        return calendar;
    }

    /**
     * 返回DayCell对应的Date
     * @param dayCell
     * @return
     */
    public static Date getDate(DayCell dayCell) {
        Calendar calendar = getCalendar(dayCell);
        if(calendar == null) {
            return null;
        }
        return calendar.getTime();
    }

    /**
     * 返回Calendar对应的DayCell
     * @param calendar
     * @param t
     * @param <T>
     * @return
     */
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
     * @return
     */
    public static int getInclusiveIntervalDays(DayCell firstDayCell, DayCell secondDayCell) {
        if(firstDayCell == null || secondDayCell == null) {
            return -1;
        }
        if(isAfter(firstDayCell, secondDayCell)) {
            DayCell dayCell = firstDayCell;
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
     * @param asc 是否升序
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
                DayCell<T> leftCell = lhs.mStartDayCell;
                if(lhs.mEndDayCell != null) {
                    leftCell = lhs.mEndDayCell;
                }
                DayCell<T> rightCell = rhs.mStartDayCell;

                if(asc) {
                    return isBefore(leftCell, rightCell) ? -1 : 1;
                } else {
                    return isBefore(leftCell, rightCell) ? 1 : -1;
                }
            }
        });
    }

    /**
     * 是否两个ContinuousSelectItem有交叉
     * @param leftItem
     * @param rightItem
     * @return
     */
    public static boolean isContinuousItemCross(ContinuousSelectItem leftItem, ContinuousSelectItem rightItem) {
        setContinuousItemValid(leftItem);
        setContinuousItemValid(rightItem);
        if (leftItem == null || (leftItem.mStartDayCell == null && leftItem.mEndDayCell == null) ||
                rightItem == null || (rightItem.mStartDayCell == null && rightItem.mEndDayCell == null)) {
            LogUtil.i(LOG_TAG, "isContinuousItemCross:empty item,leftItem = " + leftItem + ", rightItem = " + rightItem);
            return false;
        }
        ContinuousSelectItem localLeftItem = leftItem.getCopyContinuousSelectItem();
        ContinuousSelectItem localRightItem = rightItem.getCopyContinuousSelectItem();
        //如果只有开始日期，没有结束日期，可以Item当作开始和结束日期一样
        if (localLeftItem.mEndDayCell == null) {
            localLeftItem.mEndDayCell = localLeftItem.mStartDayCell;
        }
        //如果只有开始日期，没有结束日期，可以Item当作开始和结束日期一样
        if (localRightItem.mEndDayCell == null) {
            localRightItem.mEndDayCell = localRightItem.mStartDayCell;
        }
        if (!(isBefore(localLeftItem.mEndDayCell, localRightItem.mStartDayCell) || (isAfter(localLeftItem.mStartDayCell, localRightItem.mEndDayCell)))) {
            return true;
        }
        return false;
    }

    /**
     * 判断两个ContinuousSelectItem是否相等；
     * 不做有效校验；
     * 不做Data比较；
     * @param leftItem
     * @param rightItem
     * @return
     */
    public static boolean isEqual(ContinuousSelectItem leftItem, ContinuousSelectItem rightItem) {
        setContinuousItemValid(leftItem);
        setContinuousItemValid(rightItem);
        if(leftItem == null || (leftItem.mStartDayCell == null && leftItem.mEndDayCell == null) ||
                rightItem == null || (rightItem.mStartDayCell == null && rightItem.mEndDayCell == null)) {
            LogUtil.i(LOG_TAG, "isEqual:empty item,leftItem = " + leftItem + ", rightItem = " + rightItem);
            return false;
        } else if(leftItem.mStartDayCell != null && leftItem.mEndDayCell == null
                && rightItem.mStartDayCell != null && rightItem.mEndDayCell == null
                && isEqual(leftItem.mStartDayCell, rightItem.mStartDayCell)) {
            return true;
        } else if(leftItem.mStartDayCell != null && leftItem.mEndDayCell != null
                && rightItem.mStartDayCell != null && rightItem.mEndDayCell != null
                && isEqual(leftItem.mStartDayCell, rightItem.mStartDayCell)
                && isEqual(leftItem.mEndDayCell, rightItem.mEndDayCell)) {
            return true;
        }
        return false;
    }

    /**
     * 判断leftItem是否在rightItem的前面；
     * 不做有效校验；
     * 不做Data比较；
     * @param leftItem
     * @param rightItem
     * @return
     */
    public static boolean isBefore(ContinuousSelectItem leftItem, ContinuousSelectItem rightItem) {
        setContinuousItemValid(leftItem);
        setContinuousItemValid(rightItem);
        if (leftItem == null || (leftItem.mStartDayCell == null && leftItem.mEndDayCell == null) ||
                rightItem == null || (rightItem.mStartDayCell == null && rightItem.mEndDayCell == null)) {
            LogUtil.i(LOG_TAG, "isBefore:empty item,leftItem = " + leftItem + ", rightItem = " + rightItem);
            return false;
        }
        DayCell leftCell = leftItem.mStartDayCell;
        if (leftItem.mEndDayCell != null) {
            leftCell = leftItem.mEndDayCell;
        }
        DayCell rightCell = rightItem.mStartDayCell;
        return isBefore(leftCell, rightCell);
    }

    /**
     * 判断leftItem是否在rightItem的后面；
     * 不做有效校验；
     * 不做Data比较；
     * @param leftItem
     * @param rightItem
     * @return
     */
    public static boolean isAfter(ContinuousSelectItem leftItem, ContinuousSelectItem rightItem) {
        setContinuousItemValid(leftItem);
        setContinuousItemValid(rightItem);
        if(leftItem == null || (leftItem.mStartDayCell == null && leftItem.mEndDayCell == null) ||
                rightItem == null || (rightItem.mStartDayCell == null && rightItem.mEndDayCell == null)) {
            LogUtil.i(LOG_TAG, "isAfter:empty item,leftItem = " + leftItem + ", rightItem = " + rightItem);
            return false;
        } else {
            DayCell leftCell = leftItem.mStartDayCell;
            DayCell rightCell;
            if(rightItem.mEndDayCell != null) {
                rightCell = rightItem.mEndDayCell;
            } else {
                rightCell = rightItem.mStartDayCell;
            }
            return isAfter(leftCell, rightCell);
        }
    }

    /**
     * 判断DayCell是否在ContinuousSelectItem的前面
     * @param leftCell
     * @param rightItem
     * @return
     */
    public static boolean isBefore(DayCell leftCell, ContinuousSelectItem rightItem) {
        if(leftCell == null) {
            LogUtil.i(LOG_TAG, "isBefore:leftCell is null");
            return false;
        } else if(rightItem == null || (rightItem.mStartDayCell == null && rightItem.mEndDayCell == null)) {
            LogUtil.i(LOG_TAG, "isBefore:rightItem is null");
            return false;
        }
        setContinuousItemValid(rightItem);
        DayCell rightCell = rightItem.mStartDayCell;
        return isBefore(leftCell, rightCell);
    }

    /**
     * 判断DayCell是否在ContinuousSelectItem的后面
     * @param leftCell
     * @param rightItem
     * @return
     */
    public static boolean isAfter(DayCell leftCell, ContinuousSelectItem rightItem) {
        if(leftCell == null) {
            LogUtil.i(LOG_TAG, "isAfter:leftCell is null");
            return false;
        } else if(rightItem == null || (rightItem.mStartDayCell == null && rightItem.mEndDayCell == null)) {
            LogUtil.i(LOG_TAG, "isAfter:rightItem is null");
            return false;
        }
        setContinuousItemValid(rightItem);
        DayCell rightCell = rightItem.mStartDayCell;
        if(rightItem.mEndDayCell != null) {
            rightCell = rightItem.mEndDayCell;
        }
        return isAfter(leftCell, rightCell);
    }

    /**
     * 判断ContinuousSelectItem是否在DayCell的前面
     * @param leftItem
     * @param rightCell
     * @return
     */
    public static boolean isBefore(ContinuousSelectItem leftItem, DayCell rightCell) {
        if(rightCell == null) {
            LogUtil.i(LOG_TAG, "isBefore:rightCell is null");
            return false;
        } else if(leftItem == null || (leftItem.mStartDayCell == null && leftItem.mEndDayCell == null)) {
            LogUtil.i(LOG_TAG, "isBefore:leftItem is null");
            return false;
        }
        setContinuousItemValid(leftItem);
        DayCell leftCell = leftItem.mStartDayCell;
        if(leftItem.mEndDayCell != null) {
            leftCell = leftItem.mEndDayCell;
        }
        return isBefore(leftCell, rightCell);
    }

    /**
     * 判断ContinuousSelectItem是否在DayCell的后面
     * @param leftItem
     * @param rightCell
     * @return
     */
    public static boolean isAfter(ContinuousSelectItem leftItem, DayCell rightCell) {
        if(rightCell == null) {
            LogUtil.i(LOG_TAG, "isAfter:rightCell is null");
            return false;
        } else if(leftItem == null || (leftItem.mStartDayCell == null && leftItem.mEndDayCell == null)) {
            LogUtil.i(LOG_TAG, "isAfter:leftItem is null");
            return false;
        }
        setContinuousItemValid(leftItem);
        DayCell leftCell = leftItem.mStartDayCell;
        return isAfter(leftCell, rightCell);
    }

    /**
     * cell是否属于item（包括开始/结束日期）
     * @param cell
     * @param item
     * @return
     */
    public static boolean isInClusive(DayCell cell, ContinuousSelectItem item) {
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

    /**
     * cell是否属于item（不包括开始／结束日期）
     * @param cell
     * @param item
     * @return
     */
    public static boolean isExClusive(DayCell cell, ContinuousSelectItem item) {
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

    /**
     * 清除Null的DayCell
     * @param list
     */
    public static void clearNullDayCell(List<DayCell> list) {
        if(list == null || list.size() == 0) {
            LogUtil.i(LOG_TAG, "clearNullDayCell:list is empty");
            return;
        }
        DayCell cell;
        for(int i = list.size() - 1 ; i >= 0 ; i--) {
            cell = list.get(i);
            if(cell == null) {
                list.remove(i);
            }
        }
    }

    /**
     * 清除Null的ContinuousSelectItem
     * @param list
     */
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

    public static <T>List<DayCell<T>> getCopyDayCellList(List<DayCell<T>> list) {
        List<DayCell<T>> result = new ArrayList<DayCell<T>>();
        if(list == null) {
            return result;
        }
        for(DayCell<T> cell : list) {
            if(cell == null) {
                continue;
            }
            result.add(cell.getCopyDayCell());
        }
        return result;
    }

    public static <T>List<ContinuousSelectItem<T>> getCopyContinuousItemList(List<ContinuousSelectItem<T>> list) {
        List<ContinuousSelectItem<T>> result = new ArrayList<ContinuousSelectItem<T>>();
        if(list == null) {
            return result;
        }
        for(ContinuousSelectItem<T> item : list) {
            if(item == null) {
                continue;
            }
            result.add(item.getCopyContinuousSelectItem());
        }
        return result;
    }

}
