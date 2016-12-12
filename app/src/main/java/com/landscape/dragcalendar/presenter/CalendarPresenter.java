package com.landscape.dragcalendar.presenter;

import android.text.TextUtils;

import com.landscape.dragcalendar.CalendarBar;
import com.landscape.dragcalendar.DragCalendarLayout;
import com.landscape.dragcalendar.bean.CalendarDotVO;
import com.landscape.dragcalendar.utils.DateUtils;
import com.landscape.dragcalendar.view.MonthView;
import com.landscape.dragcalendar.view.WeekView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by landscape on 2016/12/2.
 */

public class CalendarPresenter {
    private static CalendarPresenter instance;
    private ViewPackage viewPackage;
    private String selectTime, todayTime;
    private CalendarDotVO calendarDotVO = null;

    public class ViewPackage {
        private DragCalendarLayout dragCalendarLayout;
        private MonthView monthView;
        private WeekView weekView;
        private CalendarBar calendarBar;
    }

    public ViewPackage viewPackage() {
        if (viewPackage == null) {
            viewPackage = new ViewPackage();
        }
        return viewPackage;
    }

    public void registerDragCalendarLayout(DragCalendarLayout dragCalendarLayout) {
        viewPackage().dragCalendarLayout = dragCalendarLayout;
    }

    public void registerMonthView(MonthView monthView) {
        viewPackage().monthView = monthView;
    }

    public void registerWeekView(WeekView weekView) {
        viewPackage().weekView = weekView;
    }

    public void registerCalendarBar(CalendarBar calendarBar) {
        viewPackage().calendarBar = calendarBar;
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
        todayTime = selectTime;
    }

    public void loadCalendarDotVO(CalendarDotVO calendarDotVO) {
        if (calendarDotVO == null) {
            throw new IllegalArgumentException("Dot Data must not be null");
        }
        this.calendarDotVO = calendarDotVO;
    }

    public <T> void parseData(List<T> sources) {
        if (calendarDotVO == null) {
            throw new IllegalArgumentException("Dot Data must not be null");
        }
        calendarDotVO.parseData(sources);
        viewPackage().dragCalendarLayout.focusCalendar();
    }

    public CalendarDotVO getCalendarDotVO() {
        if (calendarDotVO == null) {
            calendarDotVO = new CalendarDotVO() {
                @Override
                protected CalendarDotItem parseVO(Object bean) {
                    return null;
                }
            };
        }
        return calendarDotVO;
    }

    public boolean containData(String date) {
        if (calendarDotVO == null) {
            return false;
        }
        return calendarDotVO.getDots().get(date) == null ?
                false : ((CalendarDotVO.CalendarDotItem) calendarDotVO.getDots().get(date)).isContainData();
    }

    public String getTodayTime() {
        return todayTime;
    }

    public String getSelectTime() {
        return selectTime;
    }

    public void setSelectTime(String selectTime) {
        setSelectTime(selectTime, true);
    }

    public void setSelectTime(String selectTime, boolean close) {
        if (TextUtils.isEmpty(selectTime)) {
            throw new IllegalArgumentException("selectTime can not be empty");
        }
        if (!this.selectTime.equals(selectTime)) {
            this.selectTime = selectTime;
            if (callbk != null) {
                callbk.onSelect(selectTime, selectTime.equals(todayTime));
            }
        }
        viewPackage().dragCalendarLayout.focusCalendar();
        if (close) {
            close();
        }
    }

    /**
     * today - select (unit base on month)
     *
     * @return
     */
    public int getMonthDiff() {
        Date selectDate = DateUtils.stringToDate(selectTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectDate);

        Date todayDate = DateUtils.stringToDate(todayTime);
        Calendar calToday = Calendar.getInstance();
        calToday.setTime(todayDate);

        int yearDiff = calToday.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
        int monthDiff = calToday.get(Calendar.MONTH) - calendar.get(Calendar.MONTH);
        monthDiff += 12 * yearDiff;
        return monthDiff;
    }

    /**
     * today - select (unit base on week)
     *
     * @return
     */
    public int getWeekDiff() {
        Date selectDate = DateUtils.stringToDate(selectTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectDate);

        Date todayDate = DateUtils.stringToDate(todayTime);
        Calendar calToday = Calendar.getInstance();
        calToday.setTime(todayDate);

        // 归整
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        calendar.add(Calendar.DATE, -day_of_week);

        day_of_week = calToday.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        calToday.add(Calendar.DATE, -day_of_week);

        long weekDiff = DateUtils.diff(DateUtils.getTagTimeStr(calToday), DateUtils.getTagTimeStr(calendar))/1000/3600/24/7;
        return (int) weekDiff;
    }

    public void backToday() {
        viewPackage().dragCalendarLayout.backToday();
        setSelectTime(todayTime, false);
    }

    public void close() {
        viewPackage().dragCalendarLayout.setExpand(false);
    }

    public interface ICallbk {
        void onSelect(String selectTime, boolean isToday);
    }

    ICallbk callbk = null;

    public void setCallbk(ICallbk callbk) {
        this.callbk = callbk;
        if (callbk != null) {
            callbk.onSelect(selectTime, selectTime.equals(todayTime));
        }
    }
}
