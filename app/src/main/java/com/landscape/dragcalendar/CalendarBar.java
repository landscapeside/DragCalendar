package com.landscape.dragcalendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by landscape on 2016/12/5.
 */

public class CalendarBar extends FrameLayout {

    TextView tvDate;
    View imgCollapse,backToday;

    public CalendarBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(getContext(), R.layout.month_tag, this);
        tvDate = (TextView) findViewById(R.id.tv_short_date);
        imgCollapse = findViewById(R.id.arrow_collapse);
        backToday = findViewById(R.id.tv_back_today);
    }

    public void setDate(String date,boolean isToday) {
        tvDate.setText(date);
        backToday.setVisibility(isToday?GONE:VISIBLE);
    }

    public void setCloseCallbk(OnClickListener clickListener) {
        imgCollapse.setOnClickListener(clickListener);
    }

    public void setBackCallbk(OnClickListener clickListener) {
        backToday.setOnClickListener(clickListener);
    }

}
