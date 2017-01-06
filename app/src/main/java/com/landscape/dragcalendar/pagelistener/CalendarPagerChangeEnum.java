package com.landscape.dragcalendar.pagelistener;

import android.support.v4.view.ViewPager;

import com.landscape.dragcalendar.adapter.CalendarBaseAdapter;
import com.landscape.dragcalendar.adapter.MonthCalendarAdapter;
import com.landscape.dragcalendar.conf.CalendarType;
import com.landscape.dragcalendar.presenter.CalendarPresenter;
import com.landscape.dragcalendar.utils.DateUtils;

import java.util.Calendar;

/**
 * Created by landscape on 2016/12/27.
 */

public enum CalendarPagerChangeEnum implements ViewPager.OnPageChangeListener {
    MONTH{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            CalendarPresenter.instance()
                    .setCurrentScrollDate(DateUtils.getTagTimeStr(
                            CalendarType.MONTH.calculateByOffset(position)));
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (stateChangeListener != null) {
                stateChangeListener.onStateChange(state);
            }
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                ((MonthCalendarAdapter)adapter).showDivider(true);
            } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                ((MonthCalendarAdapter)adapter).showDivider(false);
            }
        }
    },
    WEEK{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Calendar calendar = CalendarPresenter.instance().selectCalendar();
            int week = CalendarType.WEEK.defPosition() - position;
            if (week != CalendarPresenter.instance().getWeekDiff()) {
                calendar.add(Calendar.DATE, -(week - CalendarPresenter.instance().getWeekDiff()) * 7);
                CalendarPresenter.instance().setSelectTime(DateUtils.getTagTimeStr(calendar),true);
//                if (Math.abs(DateUtils.diffMonth(oldSelectTime, CalendarPresenter.instance().getSelectTime())) > 0) {
//                    CalendarPresenter.instance().setCurrentScrollDate(CalendarPresenter.instance().getSelectTime());
//                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public CalendarBaseAdapter adapter;
    public StateChangeListener stateChangeListener = null;

    public CalendarPagerChangeEnum setAdapter(CalendarBaseAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    public CalendarPagerChangeEnum setPageScrollStateChangeListener(StateChangeListener stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
        return this;
    }

    public interface StateChangeListener{
        void onStateChange(int state);
    }
}
