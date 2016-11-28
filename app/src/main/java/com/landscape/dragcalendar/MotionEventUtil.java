package com.landscape.dragcalendar;

import android.view.MotionEvent;

/**
 * Created by 1 on 2016/9/9.
 */
public class MotionEventUtil {
    public static float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = android.support.v4.view.MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (index < 0) {
            return -1;
        }
        return android.support.v4.view.MotionEventCompat.getY(ev, index);
    }
}
