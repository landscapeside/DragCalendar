package com.landscape.dragcalendar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.landscape.dragcalendar.conf.CalendarType;
import com.landscape.dragcalendar.view.MonthCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/19 0019.
 */
public class MonthCalendarAdapter extends CalendarBaseAdapter {
    private List<View> views = new ArrayList<>();
    MonthCard currentCard;

    public MonthCalendarAdapter(Context context) {
        views.clear();
        for (int i = 0; i < 4; i++) {
            views.add(new MonthCard(context));
        }
    }

    @Override
    public int getCount() {
        return CalendarType.MONTH.getCount();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    public MonthCard currentCard() {
        return currentCard;
    }

    public void showDivider(boolean shown) {
        for (View view : views) {
            ((MonthCard)view).showDivider(shown);
        }
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        currentCard = (MonthCard) object;
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
        CalendarType.MONTH.refresh(view, position);
        return view;
    }
}
