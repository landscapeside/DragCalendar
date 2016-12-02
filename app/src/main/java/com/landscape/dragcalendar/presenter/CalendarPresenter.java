package com.landscape.dragcalendar.presenter;

import android.text.TextUtils;

import com.landscape.dragcalendar.DragCalendarLayout;
import com.landscape.dragcalendar.utils.DateUtils;
import com.landscape.dragcalendar.view.MonthView;
import com.landscape.dragcalendar.view.WeekView;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by landscape on 2016/12/2.
 */

public class CalendarPresenter {
    private static CalendarPresenter instance;
    private ViewPackage viewPackage;
    private String selectTime;

    public class ViewPackage{
        private DragCalendarLayout dragCalendarLayout;
        private MonthView monthView;
        private WeekView weekView;
    }

    public ViewPackage viewPackage() {
        if (viewPackage == null) {
            viewPackage = new ViewPackage();
        }
        return viewPackage;
    }

    public void registerDragCalendarLayout(DragCalendarLayout dragCalendarLayout) {
        viewPackage.dragCalendarLayout = dragCalendarLayout;
    }

    public void registerMonthView(MonthView monthView) {
        viewPackage.monthView = monthView;
    }

    public void registerWeekView(WeekView weekView) {
        viewPackage.weekView = weekView;
    }

    private CalendarPresenter() {
        init();
    }

    public static CalendarPresenter instance() {
        if (instance == null) {
            instance = new CalendarPresenter();
        }
        return instance;
    }

    public void init() {
        //选中今天
        Calendar today = new GregorianCalendar();
        today.setTimeInMillis(System.currentTimeMillis());
        selectTime = DateUtils.getTagTimeStr(today);

    }


    public String getSelectTime() {
        return selectTime;
    }

    public void setSelectTime(String selectTime) {
        if (TextUtils.isEmpty(selectTime)) {
            throw new IllegalArgumentException("selectTime can not be empty");
        }
        if (!this.selectTime.equals(selectTime)) {
            this.selectTime = selectTime;
            // TODO: 2016/12/2 refresh UI


        }
    }

    public void backToday() {

    }
}
