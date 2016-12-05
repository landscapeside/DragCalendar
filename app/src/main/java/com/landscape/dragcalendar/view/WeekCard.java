package com.landscape.dragcalendar.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.landscape.dragcalendar.R;
import com.landscape.dragcalendar.presenter.CalendarPresenter;
import com.landscape.dragcalendar.utils.DateUtils;

import java.util.Calendar;

/**
 * Created by landscape on 2016/11/30.
 */

public class WeekCard extends LinearLayout implements CalendarCard {

    public WeekCard(Context context) {
        super(context);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(HORIZONTAL);
        setBackgroundResource(android.R.color.darker_gray);
        View.inflate(context, R.layout.week, this);

    }

    @Override
    public void render(Calendar today) {
        for (int a = 0; a < 7; a++) {
            final int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);
            final ViewGroup dayOfWeek = (ViewGroup) getChildAt(a);
            ((TextView) dayOfWeek.findViewById(R.id.gongli)).setText(dayOfMonth + "");
            dayOfWeek.setTag(DateUtils.getTagTimeStr(today));
            dayOfWeek.setOnClickListener(v -> CalendarPresenter.instance().setSelectTime(dayOfWeek.getTag().toString()));

            //如果是选中天的话显示为红色
            if (CalendarPresenter.instance().getSelectTime().equals(DateUtils.getTagTimeStr(today))) {
                renderSelect(dayOfWeek, true);
            } else {
                renderNormal(dayOfWeek, true);
            }
            today.add(Calendar.DATE, 1);
        }
    }

    private void renderSelect(ViewGroup dayView, boolean containData) {
        dayView.setBackgroundResource(R.drawable.corner_shape_blue);
        if (containData) {
            ((ImageView) dayView.findViewById(R.id.imv_point)).setImageResource(R.drawable.calendar_item_point_select);
            dayView.findViewById(R.id.imv_point).setVisibility(VISIBLE);
        } else {
            dayView.findViewById(R.id.imv_point).setVisibility(INVISIBLE);
        }
    }

    private void renderNormal(ViewGroup dayView, boolean containData) {
        dayView.setBackgroundResource(android.R.color.transparent);
        if (containData) {
            ((ImageView) dayView.findViewById(R.id.imv_point)).setImageResource(R.drawable.calendar_item_point);
            dayView.findViewById(R.id.imv_point).setVisibility(VISIBLE);
        } else {
            dayView.findViewById(R.id.imv_point).setVisibility(INVISIBLE);
        }
    }
}
