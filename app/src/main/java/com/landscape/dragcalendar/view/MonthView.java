package com.landscape.dragcalendar.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import static com.landscape.dragcalendar.MotionEventUtil.dp2px;

import com.landscape.dragcalendar.R;

/**
 * Created by landscape on 2016/11/29.
 */

public class MonthView extends LinearLayout {
    public static final int MONTH_HEIGHT = 300;
    ViewPager monthPager;
    int selectionPos = 0;


    public MonthView(Context context) {
        super(context);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(VERTICAL);
        monthPager = (ViewPager) View.inflate(getContext(), R.layout.calendar_pager, this);
        ViewPager.LayoutParams layoutParams = (ViewPager.LayoutParams) monthPager.getLayoutParams();
        layoutParams.height = dp2px(getContext(),MONTH_HEIGHT);
        monthPager.setLayoutParams(layoutParams);

    }

    public int getFixedPos() {
        return selectionPos;
    }

}
