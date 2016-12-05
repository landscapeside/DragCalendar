package com.landscape.dragcalendar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.landscape.dragcalendar.view.CalendarCard;
import com.landscape.dragcalendar.view.MonthCard;
import com.landscape.dragcalendar.view.WeekCard;
import com.landscape.dragcalendar.view.WeekView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Administrator on 2016/5/19 0019.
 */
public class WeekCalendarAdapter extends CalendarBaseAdapter {
    private List<View> views = new ArrayList<>();
    WeekCard currentCard;

    public WeekCalendarAdapter(Context context) {
        views.clear();
        for (int i = 0; i < 4; i++) {
            views.add(new WeekCard(context));
        }
    }

    @Override
    public int getCount() {
        return 9600;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    public WeekCard currentCard() {
        return currentCard;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        currentCard = (WeekCard) object;
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ViewGroup view = (ViewGroup) views.get(position % views.size());
        int index = container.indexOfChild(view);
        if (index != -1) {
            container.removeView(view);
        }
        try {
            container.addView(view);
        } catch (Exception e) {

        }
        refresh(view, position);
        return view;
    }

    /**
     * 提供对外的刷新接口
     */
    public void refresh(ViewGroup view, int position) {
        //给view 填充内容

        //设置开始时间为本周日
        Calendar today = new GregorianCalendar();
        today.setTimeInMillis(System.currentTimeMillis());
        int day_of_week = today.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        today.add(Calendar.DATE, -day_of_week);
        //距离当前时间的周数
        int week = getCount() / 2 - position;
        today.add(Calendar.DATE, -week * 7);
        ((CalendarCard)view).render(today);

    }
}
