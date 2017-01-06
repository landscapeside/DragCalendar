package com.landscape.dragcalendar.presenter;

import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.landscape.dragcalendar.CalendarBar;
import com.landscape.dragcalendar.DragCalendarLayout;
import com.landscape.dragcalendar.utils.DateUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by landscape on 2017/1/4.
 */
@RunWith(AndroidJUnit4.class)
public class CalendarPresenterTest {
    CalendarBar calendarBar;
    DragCalendarLayout dragCalendarLayout;

    @Before
    public void setUp() throws Exception {
        calendarBar = new CalendarBar(InstrumentationRegistry.getTargetContext());
        dragCalendarLayout = new DragCalendarLayout(InstrumentationRegistry.getTargetContext());
        CalendarPresenter.instance().mockToday(DateUtils.getCalendar("2017-01-02").getTimeInMillis());
        CalendarPresenter.instance().registerCalendarBar(calendarBar);
        CalendarPresenter.instance().registerDragCalendarLayout(dragCalendarLayout);
    }

    @Test
    public void testMockToday() throws Exception {
        assertEquals("2017-01-02", CalendarPresenter.instance().today());
        assertEquals("2017-01-02", CalendarPresenter.instance().getSelectTime());
    }

    @Test
    public void testOthers() throws Exception {
        assertEquals("2017-01-02", DateUtils.getTagTimeStr(CalendarPresenter.instance().todayCalendar()));
        assertEquals("2017-01-02", DateUtils.getTagTimeStr(CalendarPresenter.instance().selectCalendar()));
    }

    @Test
    public void testSetSelectTime() throws Exception {
        final AtomicInteger count = new AtomicInteger(0);
        final String testDate1 = "2017-01-01", testDate2 = "2016-12-30", testDate3 = "2017-01-05";
        CalendarPresenter.instance().setCallbk(new CalendarPresenter.ICallbk() {
            @Override
            public void onCalendarBarChange(String currentTime, boolean isToday) {
                if (count.get() > 0) {
                    assertEquals(testDate1, currentTime);
                    assertEquals(false, isToday);
                } else {
                    count.getAndIncrement();
                }
            }

            @Override
            public void onScroll(String currentTime) {

            }

            @Override
            public boolean onSelect(String selectTime) {
                if (count.get() > 0) {
                    assertEquals(testDate1, selectTime);
                } else {
                    count.getAndIncrement();
                }
                return true;
            }
        });
        CalendarPresenter.instance().setSelectTime(testDate1);

        SystemClock.sleep(500);
        count.set(0);
        CalendarPresenter.instance().setCallbk(new CalendarPresenter.ICallbk() {
            @Override
            public void onCalendarBarChange(String currentTime, boolean isToday) {
                if (count.get() > 0) {
                    assertEquals(testDate2, currentTime);
                    assertEquals(false, isToday);
                } else {
                    count.getAndIncrement();
                }
            }

            @Override
            public void onScroll(String currentTime) {

            }

            @Override
            public boolean onSelect(String selectTime) {
                if (count.get() > 0) {
                    assertEquals(testDate2, selectTime);
                } else {
                    count.getAndIncrement();
                }
                return true;
            }
        });
        CalendarPresenter.instance().setSelectTime(testDate2);

        SystemClock.sleep(500);
        count.set(0);
        CalendarPresenter.ICallbk mockCall = mock(CalendarPresenter.ICallbk.class);
        CalendarPresenter.instance().setCallbk(mockCall);
        CalendarPresenter.instance().setSelectTime(testDate3);
        verify(mockCall, never()).onSelect(anyString());
    }

    @Test
    public void testBackToday() throws Exception {
        final AtomicInteger count = new AtomicInteger(0);
        String selectDay = "2016-12-15";
        CalendarPresenter.instance().setSelectTime(selectDay);
        SystemClock.sleep(500);
        CalendarPresenter.instance().setCallbk(new CalendarPresenter.ICallbk() {
            @Override
            public void onCalendarBarChange(String currentTime, boolean isToday) {
                if (count.get() > 0) {
                    assertEquals(CalendarPresenter.instance().getSelectTime(), CalendarPresenter.instance().today());
                    assertEquals(CalendarPresenter.instance().today(), currentTime);
                    assertFalse(isToday);
                } else {
//                    count.getAndIncrement();
                }
            }

            @Override
            public void onScroll(String currentTime) {

            }

            @Override
            public boolean onSelect(String selectTime) {
                if (count.get() > 0) {
                    assertEquals("2017-01-02", selectTime);
                } else {
                    count.getAndIncrement();
                }
                return true;
            }
        });
        CalendarPresenter.instance().backToday();
        assertEquals(CalendarPresenter.instance().getSelectTime(), CalendarPresenter.instance().today());
    }
}