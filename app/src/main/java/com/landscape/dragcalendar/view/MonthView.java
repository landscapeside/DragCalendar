package com.landscape.dragcalendar.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.landscape.dragcalendar.R;
import com.landscape.dragcalendar.adapter.MonthCalendarAdapter;
import com.landscape.dragcalendar.conf.CalendarType;
import com.landscape.dragcalendar.pagelistener.CalendarPagerChangeEnum;
import com.landscape.dragcalendar.presenter.CalendarPresenter;

import static com.landscape.dragcalendar.constant.Range.MONTH_HEIGHT;
import static com.landscape.dragcalendar.utils.MotionEventUtil.dp2px;

/**
 * Created by landscape on 2016/11/29.
 */

public class MonthView extends LinearLayout implements ICalendarView {
    ViewPager monthPager;
    int scrollState = ViewPager.SCROLL_STATE_IDLE;
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
        monthPager.setCurrentItem(CalendarType.MONTH.defPosition(), true);
        monthPager.setOnPageChangeListener(
                CalendarPagerChangeEnum.MONTH
                        .setAdapter(adapter)
                        .setPageScrollStateChangeListener(state -> scrollState = state));
    }

    @Override
    public void backToday() {
        monthPager.setCurrentItem(CalendarType.MONTH.defPosition(), true);
    }

    @Override
    public int currentIdx() {
        return monthPager.getCurrentItem();
    }

    @Override
    public void focusCalendar() {
        monthPager.setCurrentItem(CalendarType.MONTH.defPosition() - CalendarPresenter.instance().getMonthDiff(), true);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void reDraw() {
        if (scrollState == ViewPager.SCROLL_STATE_IDLE) {
            adapter.notifyDataSetChanged();
//            adapter.refresh(adapter.currentCard(), adapter.getCount() - 1 - CalendarPresenter.instance().getScrollMonthDiff());
        } else {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
//                    adapter.refresh(adapter.currentCard(), adapter.getCount() - 1 - CalendarPresenter.instance().getScrollMonthDiff());
                }
            }, 500);
        }
    }

    public int getFixedPos() {
        return adapter.currentCard().getSelectPos();
    }
}
