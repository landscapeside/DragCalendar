package com.landscape.dragcalendar.adapter;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.landscape.dragcalendar.R;
import com.landscape.dragcalendar.utils.CalendarUtil;
import com.landscape.dragcalendar.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Administrator on 2016/5/19 0019.
 */
public class MonthCalendarAdpter extends CalendarBaseAdpter {
    private List<View> views;
    private Context context;

    private Handler os = null;
    private int last_msg_tv_color;
    private Drawable yuanOfRed;
    private Drawable white;
    private int text_black;
    private int text_white;
    private final Drawable yuanOfBlack;

    private String strToDay = "";

    public MonthCalendarAdpter(List<View> views, Context context) {
        this.views = views;
        this.context = context;
        //选中今天
        Calendar today = new GregorianCalendar();
        today.setTimeInMillis(System.currentTimeMillis());

        strToDay = DateUtils.getTagTimeStr(today);

        selectTime = DateUtils.getTagTimeStr(today);
        text_black = context.getResources().getColor(R.color.black_deep);
        last_msg_tv_color = context.getResources().getColor(R.color.last_msg_tv_color);
        text_white = context.getResources().getColor(R.color.white);
        yuanOfRed = context.getResources().getDrawable(R.drawable.yuan);
        yuanOfBlack = context.getResources().getDrawable(R.drawable.calendar_background);
        white = context.getResources().getDrawable(R.drawable.white);
    }

    @Override
    public int getCount() {
        return 2400;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ViewGroup view = (ViewGroup) views.get(position % views.size());
        int index = container.indexOfChild(view);
        if (index != -1) {
            container.removeView(view);
        }
        try {
            container.addView(view);
        } catch (Exception e) {

        }
        refresh(view, position);
        return view;
    }

    public void getTimeList(ArrayList<String> list) {
    }

    /**
     * 提供对外的刷新接口
     */
    public void refresh(ViewGroup view, int position) {
        //给view 填充内容

        //设置开始时间为本周日
        Calendar today = new GregorianCalendar();
        today.setTimeInMillis(System.currentTimeMillis());
        //距离当前时间的月数
        int month = 1200 - position;
        today.add(Calendar.MONTH, -month);
        view.setTag(today.get(Calendar.MONTH) + "");
        //找到这个月的第一天所在星期的周日
        today.add(Calendar.DAY_OF_MONTH, -(today.get(Calendar.DAY_OF_MONTH) - 1));


        int day_of_week = today.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        today.add(Calendar.DATE, -day_of_week);

        render(view, today);
    }

    /**
     * 渲染page中的view：7天
     */
    private void render(final ViewGroup view1, final Calendar today) {


    }

    View.OnClickListener nextLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (MonthCalendarAdpter.this.os != null) {
                Message message = os.obtainMessage();
                message.what = MyCalendarFragment.pagerNext;
                os.sendMessage(message);
            }
        }
    };
    View.OnClickListener lastLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (MonthCalendarAdpter.this.os != null) {
                Message message = os.obtainMessage();
                message.what = MyCalendarFragment.pagerLast;
                os.sendMessage(message);
            }
        }
    };

    private ViewGroup day = null;

    public ViewGroup getDay() {
        return day;
    }

    private String tag = "";

    public String getSelectTime() {
        return selectTime;
    }

    public void setSelectTime(String selectTime) {
        this.selectTime = selectTime;
    }

}
