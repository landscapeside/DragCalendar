package com.landscape.dragcalendar.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created by Administrator on 2016/6/13 0013.
 */
public class CalendarBaseAdapter extends PagerAdapter {

    int mChildCount = 0;

    /**
     * 是否拦截事件
     * */
    public static boolean is=false;

    public static boolean is() {
        return is;
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
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
