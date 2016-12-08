package com.landscape.dragcalendar.presenter;

import android.text.TextUtils;

import com.landscape.dragcalendar.CalendarBar;
import com.landscape.dragcalendar.DragCalendarLayout;
import com.landscape.dragcalendar.bean.CalendarDotVO;
import com.landscape.dragcalendar.utils.DateUtils;
import com.landscape.dragcalendar.view.MonthView;
import com.landscape.dragcalendar.view.WeekView;

import java.util.Calendar;
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

    public void parseData(List<Object> sources) {
        if (calendarDotVO == null) {
            throw new IllegalArgumentException("Dot Data must not be null");
        }
        calendarDotVO.parseData(sources);
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

    public String getSelectTime() {
        return selectTime;
    }

    public void setSelectTime(String selectTime) {
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
        close();
    }

    public void backToday() {
        setSelectTime(todayTime);
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
