package com.landscape.dragcalendar.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.landscape.dragcalendar.R;
import com.landscape.dragcalendar.adapter.MonthCalendarAdapter;
import com.landscape.dragcalendar.presenter.CalendarPresenter;
import com.landscape.dragcalendar.utils.DateUtils;


import java.util.Calendar;
import java.util.Date;

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
    }

    @Override
    public void focusCalendar() {
        int pageMonth = (Integer.parseInt((String) adapter.currentCard().getTag()));
        Date selectDate = DateUtils.stringToDate(CalendarPresenter.instance().getSelectTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectDate);
        if (pageMonth > calendar.get(Calendar.MONTH)) {
            //上个月
            monthPager.setCurrentItem(monthPager.getCurrentItem() - 1);
        } else if (pageMonth < calendar.get(Calendar.MONTH)) {
            //下个月
            if (monthPager.getCurrentItem() < adapter.getCount() - 1) {
                monthPager.setCurrentItem(monthPager.getCurrentItem() + 1);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public int getFixedPos() {
        return adapter.currentCard().getSelectPos();
    }
}
