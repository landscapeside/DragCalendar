package com.landscape.dragcalendar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.landscape.dragcalendar.conf.CalendarType;
import com.landscape.dragcalendar.view.WeekCard;

import java.util.ArrayList;
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
        return CalendarType.WEEK.getCount();
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
        CalendarType.WEEK.refresh(view, position);
        return view;
    }
}
