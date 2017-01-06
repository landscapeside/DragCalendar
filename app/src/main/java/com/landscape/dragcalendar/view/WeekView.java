package com.landscape.dragcalendar.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.landscape.dragcalendar.R;
import com.landscape.dragcalendar.adapter.WeekCalendarAdapter;
import com.landscape.dragcalendar.conf.CalendarType;
import com.landscape.dragcalendar.pagelistener.CalendarPagerChangeEnum;
import com.landscape.dragcalendar.presenter.CalendarPresenter;

import static com.landscape.dragcalendar.constant.Range.WEEK_HEIGHT;
import static com.landscape.dragcalendar.utils.MotionEventUtil.dp2px;

/**
 * Created by landscape on 2016/11/29.
 */

public class WeekView extends LinearLayout implements ICalendarView {

    ViewPager weekPager;
    WeekCalendarAdapter adapter;

    public WeekView(Context context) {
        super(context);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(VERTICAL);

        View.inflate(getContext(), R.layout.calendar_pager, this);
        weekPager = (ViewPager) findViewById(R.id.cal_pager);
        ViewGroup.LayoutParams layoutParams = weekPager.getLayoutParams();
        layoutParams.height = dp2px(getContext(), WEEK_HEIGHT);
        weekPager.setLayoutParams(layoutParams);

        adapter = new WeekCalendarAdapter(context);
        weekPager.setAdapter(adapter);
        weekPager.setCurrentItem(CalendarType.WEEK.defPosition());
        weekPager.setOnPageChangeListener(CalendarPagerChangeEnum.WEEK.setAdapter(adapter));
    }

    @Override
    public void backToday() {
        weekPager.setCurrentItem(CalendarType.WEEK.defPosition(), true);
    }

    @Override
    public int currentIdx() {
        return weekPager.getCurrentItem();
    }

    @Override
    public void focusCalendar() {
        weekPager.setCurrentItem(CalendarType.WEEK.defPosition() - CalendarPresenter.instance().getWeekDiff(), true);
        reDraw();
    }

    @Override
    public void reDraw() {
        adapter.notifyDataSetChanged();
    }
}
