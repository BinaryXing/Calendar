package com.xing.android.calendar;

/**
 * 包含了日历选择框架中涉及的一些常量定义
 * Created by zhaoxx on 16/3/9.
 */
public class CalendarConstant {

    //以下是几种选择模式
    /**
     * 无选择模式，可以只是当作普通的日历控件，而非日历选择控件；
     * 在该模式下，所有Cell的状态都是@link DAY_STATUS_INVALID
     */
    public static final int SELECT_MODE_NONE = 0;
    /**
     * 单选模式
     */
    public static final int SELECT_MODE_SINGLE = 1;
    /**
     * 多选模式
     */
    public static final int SELECT_MODE_MULTI = 2;
    /**
     * 连选模式,结束日期必须大于起始日期
     */
    public static final int SELECT_MODE_CONTINUOUS = 3;
    /**
     * 多个连选模式,结束日期必须大于起始日期，不允许交叉
     */
    public static final int SELECT_MODE_CONTINUOUS_MULTI = 4;
    /**
     * 混合模式,起始日期和结束日期相等时,判定为单选
     */
    public static final int SELECT_MODE_MIX = 5;
    /**
     * 多个混合模式,起始日期和结束日期相等时,判定为单选，不允许交叉
     */
    public static final int SELECT_MODE_MIX_MULTI = 6;

    public static final int SELECT_MODE_MIN = SELECT_MODE_NONE;
    public static final int SELECT_MODE_MAX = SELECT_MODE_MIX_MULTI;

    //以下是Day的几种类型
    /**
     * 属于当前年份（用于YearListView）
     */
    public static final int DAY_TYPE_CURRENT_YEAR = 1;
    /**
     * 属于当前月份（用于MonthListView）
     */
    public static final int DAY_TYPE_CURRENT_MONTH = 2;
    /**
     * 属于当前周（用于WeekListView和WeekView）
     */
    public static final int DAY_TYPE_CURRENT_WEEK = 3;
    /**
     * 属于去年（用于YearListView）
     */
    public static final int DAY_TYPE_LAST_YEAR = -1;
    /**
     * 属于明年（用于YearListView）
     */
    public static final int DAY_TYPE_NEXT_YEAR = -2;
    /**
     * 属于前一个月（用于MonthListView）
     */
    public static final int DAY_TYPE_LAST_MONTH = -3;
    /**
     * 属于后一个月（用于MonthListView）
     */
    public static final int DAY_TYPE_NEXT_MONTH = -4;


    //以下是Day的几种状态，相对于选择的状态
    /**
     * 无效状态（不可选择），在SELECT_MODE_NONE时，所有的状态都是DAY_STATUS_INVALID
     */
    public static final int DAY_STATUS_INVALID = 1;
    /**
     * 未选中状态
     */
    public static final int DAY_STATUS_UNSELECTED = 2;
    /**
     * 单点模式,选中状态
     */
    public static final int DAY_STATUS_SELECTED = 3;
    /**
     * 连选模式,起始选中状态
     */
    public static final int DAY_STATUS_SELECTED_SECTION_START = 4;
    /**
     * 连选模式,中间选中状态
     */
    public static final int DAY_STATUS_SELECTED_SECTION_PASS = 5;
    /**
     * 连选模式,结束选中状态
     */
    public static final int DAY_STATUS_SELECTED_SECTION_END = 6;
    /**
     * 混合模式,开始日期和结束日期重合
     */
    public static final int DAY_STATUS_SELECTED_SECTION_START_AND_END = 7;
}
