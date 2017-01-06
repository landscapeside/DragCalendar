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

public class WeekCard extends LinearLayout implements ICalendarCard {

    public WeekCard(Context context) {
        super(context);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(HORIZONTAL);
        setBackgroundResource(R.color.calendar_gray_bg);
        View.inflate(context, R.layout.week, this);

    }

    @Override
    public void render(Calendar today) {
        for (int a = 0; a < 7; a++) {
            final int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);
            final ViewGroup dayOfWeek = (ViewGroup) getChildAt(a);
            dayOfWeek.setTag(DateUtils.getTagTimeStr(today));
            dayOfWeek.setOnClickListener(v -> CalendarPresenter.instance().setSelectTime(dayOfWeek.getTag().toString()));

            //如果是选中天的话显示为红色
            if (CalendarPresenter.instance().getSelectTime().equals(DateUtils.getTagTimeStr(today))) {
                ((TextView) dayOfWeek.findViewById(R.id.gongli)).setText(DateUtils.getTagTimeStrByMouthandDay(today));
                renderSelect(dayOfWeek, DateUtils.getTagTimeStr(today));
            } else {
                ((TextView) dayOfWeek.findViewById(R.id.gongli)).setText(dayOfMonth + "");
                if (DateUtils.diff(CalendarPresenter.instance().today(), DateUtils.getTagTimeStr(today)) >= 0) {
                    renderNormal(dayOfWeek, DateUtils.getTagTimeStr(today));
                } else {
                    renderGray(dayOfWeek, DateUtils.getTagTimeStr(today));
                }
            }
            today.add(Calendar.DATE, 1);
        }
    }

    private void renderSelect(ViewGroup dayView, String date) {
        dayView.findViewById(R.id.cal_container).setBackgroundResource(R.drawable.corner_shape_blue);
        ((TextView) dayView.findViewById(R.id.gongli)).setTextColor(getResources().getColor(R.color.white));
        if (CalendarPresenter.instance().containData(date)) {
            ((ImageView) dayView.findViewById(R.id.imv_point)).setImageResource(R.drawable.calendar_item_point_select);
            dayView.findViewById(R.id.imv_point).setVisibility(VISIBLE);
        } else {
            dayView.findViewById(R.id.imv_point).setVisibility(INVISIBLE);
        }
    }

    private void renderNormal(ViewGroup dayView, String date) {
        dayView.findViewById(R.id.cal_container).setBackgroundResource(android.R.color.transparent);
        ((TextView) dayView.findViewById(R.id.gongli)).setTextColor(getResources().getColor(R.color.white));
        if (CalendarPresenter.instance().containData(date)) {
            ((ImageView) dayView.findViewById(R.id.imv_point)).setImageResource(R.drawable.calendar_item_point);
            dayView.findViewById(R.id.imv_point).setVisibility(VISIBLE);
        } else {
            dayView.findViewById(R.id.imv_point).setVisibility(INVISIBLE);
        }
    }

    private void renderGray(ViewGroup dayView, String date) {
        dayView.findViewById(R.id.cal_container).setBackgroundResource(android.R.color.transparent);
        ((TextView) dayView.findViewById(R.id.gongli)).setTextColor(getResources().getColor(R.color.text_color_gray));
        if (CalendarPresenter.instance().containData(date)) {
            ((ImageView) dayView.findViewById(R.id.imv_point)).setImageResource(R.drawable.calendar_item_point);
            dayView.findViewById(R.id.imv_point).setVisibility(VISIBLE);
        } else {
            dayView.findViewById(R.id.imv_point).setVisibility(INVISIBLE);
        }
    }
}
