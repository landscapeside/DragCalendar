package com.landscape.dragcalendar.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.landscape.dragcalendar.R;
import com.landscape.dragcalendar.bean.CalendarDotVO;
import com.landscape.dragcalendar.presenter.CalendarPresenter;
import com.landscape.dragcalendar.utils.DateUtils;

import java.util.Calendar;
import java.util.Map;

import static com.landscape.dragcalendar.utils.MotionEventUtil.dp2px;
import static com.landscape.dragcalendar.constant.Range.DAY_HEIGHT;

/**
 * Created by landscape on 2016/11/30.
 */

public class MonthCard extends LinearLayout implements ICalendarCard {
    int selectPos = 0;

    public MonthCard(Context context) {
        super(context);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(VERTICAL);
        setBackgroundResource(R.color.calendar_gray_bg);
        View.inflate(context, R.layout.month, this);

    }

    @Override
    public void render(Calendar today) {
        int pageMonth = (Integer.parseInt((String) getTag()));
        //一页显示一个月+7天，为42；
        for (int b = 0; b < 6; b++) {
            final ViewGroup view = (ViewGroup) getChildAt(b);
            for (int a = 0; a < 7; a++) {
                final int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);
                final ViewGroup dayOfWeek = (ViewGroup) view.getChildAt(a);
                ((TextView) dayOfWeek.findViewById(R.id.gongli)).setText(dayOfMonth + "");
                dayOfWeek.setTag(DateUtils.getTagTimeStr(today));

                dayOfWeek.setOnClickListener(v -> CalendarPresenter.instance().setSelectTime(dayOfWeek.getTag().toString()));

                //不是当前月浅色显示
                int currentMonth = today.get(Calendar.MONTH);
                if (pageMonth != currentMonth) {
                    renderGray(dayOfWeek,DateUtils.getTagTimeStr(today));
                    today.add(Calendar.DATE, 1);
                } else {
                    //如果是选中天的话显示为红色
                    if (CalendarPresenter.instance().getSelectTime().equals(DateUtils.getTagTimeStr(today))) {
                        selectPos = calculatePos(b);
                        renderSelect(dayOfWeek,DateUtils.getTagTimeStr(today));
                    } else {
                        renderNormal(dayOfWeek,DateUtils.getTagTimeStr(today));
                    }
                    today.add(Calendar.DATE, 1);
                }
            }
        }
    }

    private void renderSelect(ViewGroup dayView,String date) {
        dayView.findViewById(R.id.cal_container).setBackgroundResource(R.drawable.corner_shape_blue);
        ((TextView) dayView.findViewById(R.id.gongli)).setTextColor(getResources().getColor(R.color.white));
        if (CalendarPresenter.instance().containData(date)) {
            ((ImageView) dayView.findViewById(R.id.imv_point)).setImageResource(R.drawable.calendar_item_point_select);
            dayView.findViewById(R.id.imv_point).setVisibility(VISIBLE);
        } else {
            dayView.findViewById(R.id.imv_point).setVisibility(INVISIBLE);
        }
    }

    private void renderNormal(ViewGroup dayView,String date) {
        dayView.findViewById(R.id.cal_container).setBackgroundResource(android.R.color.transparent);
        ((TextView) dayView.findViewById(R.id.gongli)).setTextColor(getResources().getColor(R.color.white));
        if (CalendarPresenter.instance().containData(date)) {
            ((ImageView) dayView.findViewById(R.id.imv_point)).setImageResource(R.drawable.calendar_item_point);
            dayView.findViewById(R.id.imv_point).setVisibility(VISIBLE);
        } else {
            dayView.findViewById(R.id.imv_point).setVisibility(INVISIBLE);
        }
    }

    private void renderGray(ViewGroup dayView,String date) {
        dayView.findViewById(R.id.cal_container).setBackgroundResource(android.R.color.transparent);
        ((TextView) dayView.findViewById(R.id.gongli)).setTextColor(getResources().getColor(R.color.text_color_gray));
        if (CalendarPresenter.instance().containData(date)) {
            ((ImageView) dayView.findViewById(R.id.imv_point)).setImageResource(R.drawable.calendar_item_point);
            dayView.findViewById(R.id.imv_point).setVisibility(VISIBLE);
        } else {
            dayView.findViewById(R.id.imv_point).setVisibility(INVISIBLE);
        }
    }

    private int calculatePos(int line) {
        return dp2px(getContext(),DAY_HEIGHT) * line;
    }

    public int getSelectPos() {
        return selectPos;
    }
}
