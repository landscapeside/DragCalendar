package com.landscape.dragcalendar.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.landscape.dragcalendar.R;
import com.landscape.dragcalendar.adapter.MonthCalendarAdapter;
import com.landscape.dragcalendar.presenter.CalendarPresenter;
import com.landscape.dragcalendar.utils.DateUtils;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.landscape.dragcalendar.utils.MotionEventUtil.dp2px;
import static com.landscape.dragcalendar.constant.Range.MONTH_HEIGHT;

/**
 * Created by landscape on 2016/11/29.
 */

public class MonthView extends LinearLayout implements ICalendarView {
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
        monthPager.setCurrentItem(adapter.getCount()/2, true);
        monthPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Calendar today = new GregorianCalendar();
                today.setTimeInMillis(System.currentTimeMillis());
                //距离当前时间的月数
                int month = adapter.getCount() / 2 - position;
                today.add(Calendar.MONTH, -month);
                CalendarPresenter.instance().setCurrentScrollDate(DateUtils.getTagTimeStrByYearandMonth(today));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void backToday() {
        monthPager.setCurrentItem(adapter.getCount()/2, true);
    }

    @Override
    public int currentIdx() {
        return monthPager.getCurrentItem();
    }

    @Override
    public void focusCalendar() {
        monthPager.setCurrentItem(adapter.getCount()/2 - CalendarPresenter.instance().getMonthDiff(),true);
        adapter.notifyDataSetChanged();
    }

    public int getFixedPos() {
        return adapter.currentCard().getSelectPos();
    }
}
