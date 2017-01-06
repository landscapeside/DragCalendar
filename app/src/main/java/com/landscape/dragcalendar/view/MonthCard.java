package com.landscape.dragcalendar.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.landscape.dragcalendar.R;
import com.landscape.dragcalendar.presenter.CalendarPresenter;
import com.landscape.dragcalendar.utils.DateUtils;

import java.util.Calendar;

import static com.landscape.dragcalendar.constant.Range.DAY_HEIGHT;
import static com.landscape.dragcalendar.constant.Range.MONTH_PADDING_BOTTOM;
import static com.landscape.dragcalendar.constant.Range.MONTH_PADDING_TOP;
import static com.landscape.dragcalendar.utils.MotionEventUtil.dp2px;

/**
 * Created by landscape on 2016/11/30.
 */

public class MonthCard extends RelativeLayout implements ICalendarCard {
    int selectPos = 0;
    ViewGroup monthContent = null;
    View leftDivider = null;

    public MonthCard(Context context) {
        super(context);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setBackgroundResource(R.color.calendar_gray_bg);
        View.inflate(context, R.layout.month, this);
        setPadding(0, dp2px(context, MONTH_PADDING_TOP), 0, dp2px(context, MONTH_PADDING_BOTTOM));
        leftDivider = View.inflate(context, R.layout.month_divider, null);
        leftDivider.setLayoutParams(new ViewGroup.LayoutParams(dp2px(context, 1), ViewGroup.LayoutParams.MATCH_PARENT));
        addView(leftDivider);
        LayoutParams lp = (LayoutParams) leftDivider.getLayoutParams();
//        lp.topMargin = dp2px(context, 10);
//        lp.bottomMargin = dp2px(context, 20);
        leftDivider.setLayoutParams(lp);
        showDivider(false);
        monthContent = (ViewGroup) findViewById(R.id.ll_month);
    }

    @Override
    public void render(Calendar today) {
        int pageMonth = (Integer.parseInt((String) getTag()));
        //一页显示一个月+7天，为42；
        for (int b = 0; b < 6; b++) {
            final ViewGroup view = (ViewGroup) monthContent.getChildAt(b);
            int currentMonth = today.get(Calendar.MONTH);
            if (pageMonth != currentMonth && b != 0) {
                view.setVisibility(INVISIBLE);
                today.add(Calendar.DATE, 7);
            } else {
                view.setVisibility(VISIBLE);
                for (int a = 0; a < 7; a++) {
                    final int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);
                    final ViewGroup dayOfWeek = (ViewGroup) view.getChildAt(a);
                    ((TextView) dayOfWeek.findViewById(R.id.gongli)).setText(dayOfMonth + "");
                    dayOfWeek.setTag(DateUtils.getTagTimeStr(today));

                    dayOfWeek.setOnClickListener(v -> CalendarPresenter.instance().setSelectTime(dayOfWeek.getTag().toString()));

                    //不是当前月浅色显示
                    currentMonth = today.get(Calendar.MONTH);
                    if (pageMonth != currentMonth) {
                        renderInvisible(dayOfWeek);
//                        renderGray(dayOfWeek,DateUtils.getTagTimeStr(today));
                        today.add(Calendar.DATE, 1);
                    } else {
                        //如果是选中天的话显示为红色
                        if (CalendarPresenter.instance().getSelectTime().equals(DateUtils.getTagTimeStr(today))) {
                            selectPos = calculatePos(b);
                            renderSelect(dayOfWeek, DateUtils.getTagTimeStr(today));
                        } else {
                            if (DateUtils.diff(CalendarPresenter.instance().today(), DateUtils.getTagTimeStr(today)) >= 0) {
                                renderNormal(dayOfWeek, DateUtils.getTagTimeStr(today));
                            } else {
                                renderGray(dayOfWeek, DateUtils.getTagTimeStr(today));
                            }
                        }
                        today.add(Calendar.DATE, 1);
                    }
                }
            }
        }
    }

    private void renderSelect(ViewGroup dayView, String date) {
        dayView.setVisibility(VISIBLE);
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
        dayView.setVisibility(VISIBLE);
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
        dayView.setVisibility(VISIBLE);
        dayView.findViewById(R.id.cal_container).setBackgroundResource(android.R.color.transparent);
        ((TextView) dayView.findViewById(R.id.gongli)).setTextColor(getResources().getColor(R.color.text_color_gray));
        if (CalendarPresenter.instance().containData(date)) {
            ((ImageView) dayView.findViewById(R.id.imv_point)).setImageResource(R.drawable.calendar_item_point);
            dayView.findViewById(R.id.imv_point).setVisibility(VISIBLE);
        } else {
            dayView.findViewById(R.id.imv_point).setVisibility(INVISIBLE);
        }
    }

    private void renderInvisible(ViewGroup dayView) {
        dayView.setVisibility(INVISIBLE);
    }

    private int calculatePos(int line) {
        return getPaddingTop() + dp2px(getContext(), DAY_HEIGHT) * line;
    }

    public int getSelectPos() {
        return selectPos;
    }

    public void showDivider(boolean shown) {
        leftDivider.setVisibility(shown ? VISIBLE : INVISIBLE);
    }
}
