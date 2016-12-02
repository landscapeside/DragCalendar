package com.landscape.dragcalendar.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.landscape.dragcalendar.R;
import com.landscape.dragcalendar.adapter.WeekCalendarAdapter;

import static com.landscape.dragcalendar.MotionEventUtil.dp2px;

/**
 * Created by landscape on 2016/11/29.
 */

public class WeekView extends LinearLayout {

    public static final int WEEK_HEIGHT = 50;
    ViewPager weekPager;
    WeekCalendarAdapter adapter;

    public WeekView(Context context) {
        super(context);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(VERTICAL);

        weekPager = (ViewPager) View.inflate(getContext(), R.layout.calendar_pager, this);
        ViewPager.LayoutParams layoutParams = (ViewPager.LayoutParams) weekPager.getLayoutParams();
        layoutParams.height = dp2px(getContext(), WEEK_HEIGHT);
        weekPager.setLayoutParams(layoutParams);

        adapter = new WeekCalendarAdapter(context);
        weekPager.setAdapter(adapter);
        weekPager.setCurrentItem(adapter.getCount() / 2);
    }
}
