package com.landscape.dragcalendar.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created by Administrator on 2016/6/13 0013.
 */
public class CalendarBaseAdapter extends PagerAdapter {

    /**
     * 是否拦截事件
     * */
    public static boolean is=false;

    public static boolean is() {
        return is;
    }

    @Override
    public int getCount() {//无所谓
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {//无所谓
        return false;
    }
}
