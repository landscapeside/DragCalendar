package com.landscape.dragcalendar.view;

/**
 * Created by landscape on 2016/12/2.
 */

public interface ICalendarView {
    void backToday();
    int currentIdx();
    void focusCalendar();
    void reDraw();
}
