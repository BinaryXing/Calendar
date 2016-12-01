package com.xing.android.demo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xing.android.calendar.CalendarConstant;
import com.xing.android.calendar.ICalendarManager;
import com.xing.android.calendar.R;
import com.xing.android.calendar.model.DayCell;
import com.xing.android.calendar.model.IWeekCell;
import com.xing.android.calendar.model.MonthWeekCell;
import com.xing.android.calendar.model.WeekCell;
import com.xing.android.calendar.model.YearWeekCell;
import com.xing.android.calendar.util.LogUtil;
import com.xing.android.calendar.view.MonthListView;
import com.xing.android.calendar.view.WeekDayView;
import com.xing.android.calendar.view.WeekView;
import com.xing.android.calendar.view.YearListView;

import java.util.Calendar;

/**
 * Created by zxx09506 on 2016/11/16.
 */

public abstract class BaseCommonCalendarActivity extends Activity {

    protected final String LOG_TAG = this.getClass().getSimpleName();

    protected RadioGroup mSelectModeGroupView;
    protected RadioButton mSingleModeView;
    protected RadioButton mMultiModeView;
    protected RadioButton mContinuousModeView;
    protected RadioButton mMixModeView;
    protected RadioButton mContinuousMultiModeView;
    protected RadioButton mMixMultiModeView;

    protected RadioGroup mFirstDayOfWeekGroupView;
    protected RadioButton mSundayView;
    protected RadioButton mMondayView;
    protected RadioButton mTuesdayView;
    protected RadioButton mWednesdayView;
    protected RadioButton mThuresdayView;
    protected RadioButton mFridayView;
    protected RadioButton mSaturdayView;

    protected CheckBox mClickEnableView;
    protected CheckBox mLongClickEnableView;

    protected ICalendarManager<Void> mCalendarManager;

    protected final ICalendarManager.ICalendarManagerListener<Void> DEFAULT_CALENDAR_MANAGER_LISTENER = new ICalendarManager.ICalendarManagerListener<Void>() {
        @Override
        public boolean blockDayCellClick(DayCell<Void> cell) {
            return false;
        }

        @Override
        public void onPostDayCellClick(DayCell<Void> cell) {

        }

        @Override
        public void onBindData(DayCell<Void> dayCell) {

        }

        @Override
        public void onIterator(DayCell<Void> dayCell) {
            if(dayCell != null && dayCell.getDay() == 1) {
                dayCell.setDayStatus(CalendarConstant.DAY_STATUS_INVALID);
            }
        }
    };

    protected final WeekView.WeekViewListener<Void> DEFAULT_WEEK_VIEW_LISTENER = new WeekView.WeekViewListener<Void>() {
        @Override
        public View newDayCellView() {
            return LayoutInflater.from(BaseCommonCalendarActivity.this).inflate(R.layout.calendar_item_day_cell, null);
        }

        @Override
        public void bindDayCellView(View view, DayCell<Void> data) {
            TextView dayView = (TextView) view.findViewById(R.id.tv_day);
            TextView monthView = (TextView) view.findViewById(R.id.tv_month);
            dayView.setText(data.getDay() + "日");
            monthView.setText(data.getMonth() + "月");
            switch (data.getDayStatus()) {
                case CalendarConstant.DAY_STATUS_INVALID:
                    view.setBackgroundColor(getResources().getColor(R.color.calendar_cell_invalid));
                    break;
                case CalendarConstant.DAY_STATUS_UNSELECTED:
                    view.setBackgroundColor(getResources().getColor(R.color.calendar_cell_unselected));
                    break;
                case CalendarConstant.DAY_STATUS_SELECTED:
                    view.setBackgroundColor(getResources().getColor(R.color.calendar_cell_selected));
                    break;
                case CalendarConstant.DAY_STATUS_SELECTED_SECTION_START:
                    view.setBackgroundColor(getResources().getColor(R.color.calendar_cell_section_start));
                    break;
                case CalendarConstant.DAY_STATUS_SELECTED_SECTION_PASS:
                    view.setBackgroundColor(getResources().getColor(R.color.calendar_cell_section_pass));
                    break;
                case CalendarConstant.DAY_STATUS_SELECTED_SECTION_END:
                    view.setBackgroundColor(getResources().getColor(R.color.calendar_cell_section_end));
                    break;
                case CalendarConstant.DAY_STATUS_SELECTED_SECTION_START_AND_END:
                    view.setBackgroundColor(getResources().getColor(R.color.calendar_cell_send_start_and_end));
                    break;
                default:
                    view.setBackgroundColor(getResources().getColor(R.color.calendar_cell_unselected));
            }
        }

        @Override
        public View newWeekHeaderView() {
            return LayoutInflater.from(BaseCommonCalendarActivity.this).inflate(R.layout.calendar_layout_week_head, null);
        }

        @Override
        public void bindWeekHeaderView(View view, IWeekCell<Void> weekCell) {
            TextView headView = (TextView) view.findViewById(R.id.tv_week_head);
            if(weekCell instanceof WeekCell) {
                headView.setText("单周头部");
            } else if(weekCell instanceof MonthWeekCell) {
                headView.setText("" + ((MonthWeekCell) weekCell).getMonth() + "月第" + ((MonthWeekCell) weekCell).getWeek() + "周头部");
            } else if(weekCell instanceof YearWeekCell) {
                headView.setText("" + ((YearWeekCell) weekCell).getYear() + "年第" + ((YearWeekCell) weekCell).getWeek() + "周头部");
            }
        }

        @Override
        public View newWeekFooterView() {
            return LayoutInflater.from(BaseCommonCalendarActivity.this).inflate(R.layout.calendar_layout_week_foot, null);
        }

        @Override
        public void bindWeekFooterView(View view, IWeekCell<Void> weekCell) {
            TextView footView = (TextView) view.findViewById(R.id.tv_week_foot);
            if(weekCell instanceof WeekCell) {
                footView.setText("单周尾部");
            } else if(weekCell instanceof MonthWeekCell) {
                footView.setText("" + ((MonthWeekCell) weekCell).getMonth() + "月第" + ((MonthWeekCell) weekCell).getWeek() + "周尾部");
            } else if(weekCell instanceof YearWeekCell) {
                footView.setText("" + ((YearWeekCell) weekCell).getYear() + "年第" + ((YearWeekCell) weekCell).getWeek() + "周尾部");
            }
        }
    };

    protected final WeekDayView.WeekDayListener DEFAULT_WEEK_DAY_LISTENER = new WeekDayView.WeekDayListener() {
        @Override
        public View newDayOfWeekCellView() {
            return LayoutInflater.from(BaseCommonCalendarActivity.this).inflate(R.layout.calendar_layout_week_day_cell, null);
        }

        @Override
        public void bindDayOfWeekCellView(View view, int dayOfWeek) {
            TextView weekDayView = (TextView) view.findViewById(R.id.tv_week_day);
            switch (dayOfWeek) {
                case Calendar.SUNDAY:
                    weekDayView.setText("周日");
                    break;
                case Calendar.MONDAY:
                    weekDayView.setText("周一");
                    break;
                case Calendar.TUESDAY:
                    weekDayView.setText("周二");
                    break;
                case Calendar.WEDNESDAY:
                    weekDayView.setText("周三");
                    break;
                case Calendar.THURSDAY:
                    weekDayView.setText("周四");
                    break;
                case Calendar.FRIDAY:
                    weekDayView.setText("周五");
                    break;
                case Calendar.SATURDAY:
                    weekDayView.setText("周六");
                    break;
            }
        }

        @Override
        public View newDayOfWeekHeaderView() {
            return LayoutInflater.from(BaseCommonCalendarActivity.this).inflate(R.layout.calendar_layout_week_day_head, null);
        }

        @Override
        public void bindDayOfWeekHeaderView(View view) {
            TextView headView = (TextView) view.findViewById(R.id.tv_week_day_head);
            headView.setText("WeekDay头部");
        }

        @Override
        public View newDayOfWeekFooterView() {
            return LayoutInflater.from(BaseCommonCalendarActivity.this).inflate(R.layout.calendar_layout_week_day_foot, null);
        }

        @Override
        public void bindDayOfWeekFooterView(View view) {
            TextView footView = (TextView) view.findViewById(R.id.tv_week_day_foot);
            footView.setText("WeekDay尾部");
        }
    };

    protected final MonthListView.MonthListListener DEFAULT_MONTH_LIST_LISTENER = new MonthListView.MonthListListener() {
        @Override
        public View newMonthHeaderView() {
            return LayoutInflater.from(BaseCommonCalendarActivity.this).inflate(R.layout.calendar_layout_month_head, null);
        }

        @Override
        public void bindMonthHeaderView(View view, int year, int month) {
            TextView monthHeadView = (TextView) view.findViewById(R.id.tv_month_head);
            monthHeadView.setText("" + year + "年" + month + "月头部");
        }

        @Override
        public View newMonthFooterView() {
            return LayoutInflater.from(BaseCommonCalendarActivity.this).inflate(R.layout.calendar_layout_month_foot, null);
        }

        @Override
        public void bindMonthFooterView(View view, int year, int month) {
            TextView monthFootView = (TextView) view.findViewById(R.id.tv_month_foot);
            monthFootView.setText("" + year + "年" + month + "月尾部");
        }
    };

    protected YearListView.YearListListener DEFAULT_YEAR_LIST_LISTENER = new YearListView.YearListListener() {
        @Override
        public View newYearHeaderView() {
            return LayoutInflater.from(BaseCommonCalendarActivity.this).inflate(R.layout.calendar_layout_year_head, null);
        }

        @Override
        public void bindYearHeaderView(View view, int year) {
            TextView yearHeadView = (TextView) view.findViewById(R.id.tv_year_head);
            yearHeadView.setText("" + year + "年头部");
        }

        @Override
        public View newYearFooterView() {
            return LayoutInflater.from(BaseCommonCalendarActivity.this).inflate(R.layout.calendar_layout_year_foot, null);
        }

        @Override
        public void bindYearFooterView(View view, int year) {
            TextView yearfootView = (TextView) view.findViewById(R.id.tv_year_foot);
            yearfootView.setText("" + year + "年尾部");
        }
    };

    protected void initCommon() {
        mSelectModeGroupView = (RadioGroup) findViewById(R.id.rg_select_mode);
        mSingleModeView = (RadioButton) findViewById(R.id.rb_single);
        mMultiModeView = (RadioButton) findViewById(R.id.rb_multi);
        mContinuousModeView = (RadioButton) findViewById(R.id.rb_continuous);
        mMixModeView = (RadioButton) findViewById(R.id.rb_mix);
        mContinuousMultiModeView = (RadioButton) findViewById(R.id.rb_continuous_multi);
        mMixMultiModeView = (RadioButton) findViewById(R.id.rb_mix_multi);

        mFirstDayOfWeekGroupView = (RadioGroup) findViewById(R.id.rg_first_day_of_week);
        mSundayView = (RadioButton) findViewById(R.id.rb_sunday);
        mMondayView = (RadioButton) findViewById(R.id.rb_monday);
        mTuesdayView = (RadioButton) findViewById(R.id.rb_tuesday);
        mWednesdayView = (RadioButton) findViewById(R.id.rb_wednesday);
        mThuresdayView = (RadioButton) findViewById(R.id.rb_thursday);
        mFridayView = (RadioButton) findViewById(R.id.rb_friday);
        mSaturdayView = (RadioButton) findViewById(R.id.rb_saturday);

        mClickEnableView = (CheckBox) findViewById(R.id.cb_click_enable);
        mLongClickEnableView = (CheckBox) findViewById(R.id.cb_long_click_enable);

        if(mSelectModeGroupView != null) {
            mSelectModeGroupView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    onSelectModeChecked(checkedId);
                }
            });
        }
        if(mSingleModeView != null) {
            mSingleModeView.setChecked(true);
        }

        if(mFirstDayOfWeekGroupView != null) {
            mFirstDayOfWeekGroupView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    onFirstDayOfWeekChecked(checkedId);
                }
            });
        }
        if(mSundayView != null) {
            mSundayView.setChecked(true);
        }

        mClickEnableView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(mCalendarManager != null) {
                    mCalendarManager.setClickEnable(b);
                }
            }
        });
        mClickEnableView.setChecked(true);
        mLongClickEnableView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(mCalendarManager != null) {
                    mCalendarManager.setLongClickEnable(b);
                }
            }
        });
        mLongClickEnableView.setChecked(false);
    }

    protected void onSelectModeChecked(int checkedId) {
        if(mCalendarManager == null) {
            LogUtil.w(LOG_TAG, "onSelectModeChecked:mCalendarManager is null");
            return;
        }
        int mode = CalendarConstant.SELECT_MODE_SINGLE;
        if(checkedId == R.id.rb_single) {
            mode = CalendarConstant.SELECT_MODE_SINGLE;
        } else if(checkedId == R.id.rb_multi) {
            mode = CalendarConstant.SELECT_MODE_MULTI;
        } else if(checkedId == R.id.rb_continuous) {
            mode = CalendarConstant.SELECT_MODE_CONTINUOUS;
        } else if(checkedId == R.id.rb_mix) {
            mode = CalendarConstant.SELECT_MODE_MIX;
        } else if(checkedId == R.id.rb_continuous_multi) {
            mode = CalendarConstant.SELECT_MODE_CONTINUOUS_MULTI;
        } else if(checkedId == R.id.rb_mix_multi) {
            mode = CalendarConstant.SELECT_MODE_MIX_MULTI;
        }
        mCalendarManager.setSelectMode(mode);
    }

    protected void onFirstDayOfWeekChecked(int checkedId) {
        if(mCalendarManager == null) {
            LogUtil.w(LOG_TAG, "onFirstDayOfWeekChecked:mCalendarManager is null");
            return;
        }
        int firstDayOfWeek = Calendar.SUNDAY;
        if(checkedId == R.id.rb_sunday) {
            firstDayOfWeek = Calendar.SUNDAY;
        } else if(checkedId == R.id.rb_monday) {
            firstDayOfWeek = Calendar.MONDAY;
        } else if(checkedId == R.id.rb_tuesday) {
            firstDayOfWeek = Calendar.TUESDAY;
        } else if(checkedId == R.id.rb_wednesday) {
            firstDayOfWeek = Calendar.WEDNESDAY;
        } else if(checkedId == R.id.rb_thursday) {
            firstDayOfWeek = Calendar.THURSDAY;
        } else if(checkedId == R.id.rb_friday) {
            firstDayOfWeek = Calendar.FRIDAY;
        } else if(checkedId == R.id.rb_saturday) {
            firstDayOfWeek = Calendar.SATURDAY;
        }
        mCalendarManager.setFirstDayOfWeek(firstDayOfWeek);
    }
}
