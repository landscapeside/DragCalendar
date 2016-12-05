package com.landscape.dragcalendar.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.landscape.dragcalendar.R;
import com.landscape.dragcalendar.adapter.MonthCalendarAdapter;


import static com.landscape.dragcalendar.MotionEventUtil.dp2px;
import static com.landscape.dragcalendar.Range.MONTH_HEIGHT;

/**
 * Created by landscape on 2016/11/29.
 */

public class MonthView extends LinearLayout {
    ViewPager monthPager;
    MonthCalendarAdapter adapter;

    public MonthView(Context context) {
        super(context);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(VERTICAL);

        View.inflate(getContext(), R.layout.calendar_pager, this);
        monthPager = (ViewPager) findViewById(R.id.cal_pager);
        ViewGroup.LayoutParams layoutParams = monthPager.getLayoutParams();
        layoutParams.height = dp2px(getContext(), MONTH_HEIGHT);
        monthPager.setLayoutParams(layoutParams);

        adapter = new MonthCalendarAdapter(context);
        monthPager.setAdapter(adapter);
        monthPager.setCurrentItem(1200, true);
    }

    public int getFixedPos() {
        return adapter.currentCard().getSelectPos();
    }
}
