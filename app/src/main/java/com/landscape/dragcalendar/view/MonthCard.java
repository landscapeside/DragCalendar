package com.landscape.dragcalendar.view;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.landscape.dragcalendar.R;
import com.landscape.dragcalendar.adapter.MonthCalendarAdpter;
import com.landscape.dragcalendar.utils.CalendarUtil;
import com.landscape.dragcalendar.utils.DateUtils;

import java.util.Calendar;

/**
 * Created by landscape on 2016/11/30.
 */

public class MonthCard extends LinearLayout implements CardRender {
    public MonthCard(Context context) {
        super(context);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(VERTICAL);
        setBackgroundResource(android.R.color.tertiary_text_light);
        View.inflate(context, R.layout.month, this);

    }

    @Override
    public void render(ViewGroup view1, Calendar today) {
        //一页显示一个月+7天，为42；
        for (int b = 0; b < 6; b++) {
            final ViewGroup view = (ViewGroup) view1.getChildAt(b);
            for (int a = 0; a < 7; a++) {
                final int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);
                // int day_of_year=today.get(Calendar.DAY_OF_YEAR);
                final ViewGroup dayOfWeek = (ViewGroup) view.getChildAt(a);
                //((TextView) dayOfWeek.getChildAt(0)).setText(getStr(today.get(Calendar.DAY_OF_WEEK)));
                ((TextView) dayOfWeek.findViewById(R.id.gongli)).setText(dayOfMonth + "");
                String str = "";
                try {
                    str = new CalendarUtil().getChineseDay(today.get(Calendar.YEAR),
                            today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH));
                } catch (Exception e) {

                }
                dayOfWeek.findViewById(R.id.imv_point).setVisibility(View.INVISIBLE);
//                String str =
                if (str.equals("初一")) {//如果是初一，显示月份
                    str = new CalendarUtil().getChineseMonth(today.get(Calendar.YEAR),
                            today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH));
                }
                ((TextView) dayOfWeek.findViewById(R.id.nongli)).setText(str);
                dayOfWeek.setTag(DateUtils.getTagTimeStr(today));

                dayOfWeek.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO:发消息，告诉Activity我改变选中的日期了
                        if (MonthCalendarAdpter.this.os != null) {
                            os.sendEmptyMessage(MyCalendarFragment.change);
                        }

                        selectTime = dayOfWeek.getTag().toString();
                        today.add(Calendar.DATE, -42);//因为已经渲染过42天，所以today往前推42天， 代表当前page重绘；

                        //
                        //恢复上个选中的day的状态
                        if (day != null) {
                            day.findViewById(R.id.cal_container).setBackgroundDrawable(white);
                            ((TextView) day.findViewById(R.id.gongli)).setTextColor(text_black);
                            //特殊情况
                            if (strToDay.equals(tag)) {
                                day.findViewById(R.id.cal_container).setBackgroundDrawable(yuanOfBlack);
                                ((TextView) day.findViewById(R.id.gongli)).setTextColor(text_black);
                                ((TextView) day.findViewById(R.id.nongli)).setTextColor(last_msg_tv_color);

                            } else {
                                day.findViewById(R.id.cal_container).setBackgroundDrawable(white);
                                ((TextView) day.findViewById(R.id.gongli)).setTextColor(text_black);
                                ((TextView) day.findViewById(R.id.nongli)).setTextColor(last_msg_tv_color);
                            }
                        }
                        //变为红色
                        dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(yuanOfRed);
                        ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_white);
                        ((TextView) dayOfWeek.findViewById(R.id.nongli)).setTextColor(text_white);
                    }
                });
                if (strToDay.equals(DateUtils.getTagTimeStr(today))) {
                    dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(yuanOfBlack);
                    ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_black);
                    ((TextView) dayOfWeek.findViewById(R.id.nongli)).setTextColor(last_msg_tv_color);
                    if (!selectTime.equals(strToDay)) {
                        today.add(Calendar.DATE, 1);
                        continue;
                    }
                } else {
                    dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(white);
                    ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_black);
                }
                //不是当前月的显示为灰色
                if ((Integer.parseInt((String) view1.getTag())) != today.get(Calendar.MONTH)) {
                    ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(last_msg_tv_color);
                    if ((Integer.parseInt((String) view1.getTag())) > today.get(Calendar.MONTH)) {
                        //下个月
                        dayOfWeek.setOnClickListener(lastLister);
                    } else {
                        //上个月
                        dayOfWeek.setOnClickListener(nextLister);
                    }
                    today.add(Calendar.DATE, 1);
                    continue;
                } else {
                    dayOfWeek.setAlpha(1.0f);
                }
                //如果是选中天的话显示为红色
                if (selectTime.equals(DateUtils.getTagTimeStr(today))) {

                    dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(yuanOfRed);
                    ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_white);
                    ((TextView) dayOfWeek.findViewById(R.id.nongli)).setTextColor(text_white);

                    if (strToDay.equals(DateUtils.getTagTimeStr(today))) {
                        dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(yuanOfRed);
                        ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_white);
                        ((TextView) dayOfWeek.findViewById(R.id.nongli)).setTextColor(text_white);
                    }

                    day = dayOfWeek;
                    if (MonthCalendarAdpter.this.os != null) {
                        Message message = os.obtainMessage();
                        message.obj = b;
                        message.what = MyCalendarFragment.change2;
                        os.sendMessage(message);
                    }
                    tag = selectTime;
                } else {
                    dayOfWeek.findViewById(R.id.cal_container).setBackgroundDrawable(white);
                    ((TextView) dayOfWeek.findViewById(R.id.gongli)).setTextColor(text_black);
                    ((TextView) dayOfWeek.findViewById(R.id.nongli)).setTextColor(last_msg_tv_color);
                }
                today.add(Calendar.DATE, 1);
            }
        }
    }
}
