package com.landscape.dragcalendar.conf;

import android.view.ViewGroup;

import com.landscape.dragcalendar.presenter.CalendarPresenter;
import com.landscape.dragcalendar.view.ICalendarCard;

import java.util.Calendar;

/**
 * Created by landscape on 2017/1/3.
 */

public enum CalendarType implements IAdapterRefresh,IAdapterConstant {
    MONTH {
        @Override
        public void refresh(ViewGroup view, int position) {
            //给view 填充内容

            //设置开始时间为本周日
            Calendar day = calculateByOffset(position);
            view.setTag(day.get(Calendar.MONTH) + "");
            //找到这个月的第一天所在星期的周日
            day.add(Calendar.DAY_OF_MONTH, -(day.get(Calendar.DAY_OF_MONTH) - 1));
            int day_of_week = day.get(Calendar.DAY_OF_WEEK) - 1;
            day.add(Calendar.DATE, -day_of_week);
            ((ICalendarCard)view).render(day);
        }

        @Override
        public int getCount() {
            return 1200;
        }

        @Override
        public int defPosition() {
            return getCount() - 1;
        }

        @Override
        public Calendar calculateByOffset(int position) {
            return calculateByCalOffset(CalendarPresenter.instance().todayCalendar(), position);
        }

        @Override
        public Calendar calculateByCalOffset(Calendar calendar, int position) {
            //距离param1时间的月数
            int month = defPosition() - position;
            calendar.add(Calendar.MONTH, -month);
            return calendar;
        }
    },

    WEEK {
        @Override
        public void refresh(ViewGroup view, int position) {
            //给view 填充内容

            //设置开始时间为本周日
            Calendar day = calculateByOffset(position);
            int day_of_week = day.get(Calendar.DAY_OF_WEEK) - 1;
            day.add(Calendar.DATE, -day_of_week);
            ((ICalendarCard)view).render(day);
        }

        @Override
        public int getCount() {
            return 4800;
        }

        @Override
        public int defPosition() {
            return getCount() - 1;
        }

        @Override
        public Calendar calculateByOffset(int position) {
            return calculateByCalOffset(CalendarPresenter.instance().todayCalendar(),position);
        }

        @Override
        public Calendar calculateByCalOffset(Calendar calendar, int position) {
            //距离param1时间的周数
            int week = defPosition() - position;
            calendar.add(Calendar.DATE, -week * 7);
            return calendar;
        }
    }


}
