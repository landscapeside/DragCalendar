package com.landscape.dragcalendar.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.landscape.dragcalendar.R;
import com.landscape.dragcalendar.adapter.MonthCalendarAdapter;
import com.landscape.dragcalendar.presenter.CalendarPresenter;

import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.landscape.dragcalendar.MotionEventUtil.dp2px;

/**
 * Created by landscape on 2016/11/29.
 */

public class MonthView extends LinearLayout {
    public static final int MONTH_HEIGHT = 300;
    ViewPager monthPager;
    MonthCalendarAdapter adapter;

    public MonthView(Context context) {
        super(context);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(VERTICAL);

        monthPager = (ViewPager) View.inflate(getContext(), R.layout.calendar_pager, this);
        ViewPager.LayoutParams layoutParams = (ViewPager.LayoutParams) monthPager.getLayoutParams();
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
