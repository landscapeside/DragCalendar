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
    private ViewBuilder viewBuilder;
    private String selectTime, todayTime,currentDate;
    private CalendarDotVO calendarDotVO = null;

    public class ViewBuilder {
        private DragCalendarLayout dragCalendarLayout;
        private MonthView monthView;
        private WeekView weekView;
        private CalendarBar calendarBar;
    }

    public ViewBuilder viewBuilder() {
        if (viewBuilder == null) {
            viewBuilder = new ViewBuilder();
        }
        return viewBuilder;
    }

    public void registerDragCalendarLayout(DragCalendarLayout dragCalendarLayout) {
        viewBuilder().dragCalendarLayout = dragCalendarLayout;
    }

    public void registerMonthView(MonthView monthView) {
        viewBuilder().monthView = monthView;
    }

    public void registerWeekView(WeekView weekView) {
        viewBuilder().weekView = weekView;
    }

    public void registerCalendarBar(CalendarBar calendarBar) {
        viewBuilder().calendarBar = calendarBar;
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
        currentDate = DateUtils.getTagTimeStr(today);
    }

    public void mockToday(long timeMills) {
        Calendar today = new GregorianCalendar();
        today.setTimeInMillis(timeMills);
        selectTime = DateUtils.getTagTimeStr(today);
        todayTime = selectTime;
        currentDate = DateUtils.getTagTimeStr(today);
    }

    public void destroy() {
        instance = null;
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
        viewBuilder().dragCalendarLayout.reDraw();
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

    public String today() {
        return todayTime;
    }

    public Calendar todayCalendar() {
        return DateUtils.getCalendar(todayTime);
    }

    public String getSelectTime() {
        return selectTime;
    }

    public Calendar selectCalendar() {
        return DateUtils.getCalendar(selectTime);
    }

    public void setSelectTime(String selectTime, boolean autoReset) {
        if (TextUtils.isEmpty(selectTime)) {
            throw new IllegalArgumentException("selectTime can not be empty");
        }
        if (DateUtils.diff(todayTime, selectTime) < 0) {
            if (autoReset) {
                selectTime = todayTime;
            } else {
                return;
            }
        }
        boolean close  = false;
        this.selectTime = selectTime;
        if (callbk != null) {
            close = callbk.onSelect(selectTime);
        }
        notifyCalendarBar(selectTime);
        viewBuilder().dragCalendarLayout.focusCalendar();
        if (close) {
            close();
        }
    }

    public void setSelectTime(String selectTime) {
        setSelectTime(selectTime,false);
    }

    public void setCurrentScrollDate(String currentScrollDate) {
        if (TextUtils.isEmpty(currentScrollDate)) {
            throw new IllegalArgumentException("currentScrollDate can not be empty");
        }
        if (!currentDate.equals(currentScrollDate)) {
            currentDate = currentScrollDate;
            currentDateCallbk();
            notifyCalendarBar(currentScrollDate);
        }
    }

    private void currentDateCallbk() {
        if (callbk != null) {
            callbk.onScroll(currentDate);
        }
    }

    private void notifyCalendarBar(String barDate) {
        if (callbk != null) {
            boolean isToday;
            if (DateUtils.diffMonth(todayTime, barDate) == 0) {
                isToday = todayTime.equals(selectTime);
            } else {
                isToday = false;
            }
            callbk.onCalendarBarChange(barDate,isToday);
        }
    }

    /**
     * today - select (unit base on month)
     *
     * @return
     */
    public int getMonthDiff() {
        return DateUtils.diffMonth(todayTime, selectTime);
    }

    /**
     * today - select (unit base on week)
     *
     * @return
     */
    public int getWeekDiff() {
        return DateUtils.diffWeek(todayTime,selectTime);
    }

    public void backToday() {
        setSelectTime(todayTime);
        viewBuilder().dragCalendarLayout.backToday();
    }

    public void close() {
        viewBuilder().dragCalendarLayout.setExpand(false);
    }

    public interface ICallbk {
        void onCalendarBarChange(String currentTime, boolean isToday);
        void onScroll(String currentTime);
        boolean onSelect(String selectTime);
    }

    ICallbk callbk = null;

    public void setCallbk(ICallbk callbk) {
        this.callbk = callbk;
        currentDateCallbk();
        notifyCalendarBar(currentDate);
    }
}
