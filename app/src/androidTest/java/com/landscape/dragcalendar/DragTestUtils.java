package com.landscape.dragcalendar;

import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;

/**
 * Created by landscape on 2016/12/15.
 */

public class DragTestUtils {

    public static ViewAction drag(int startX, int startY, int endX, int endY) {
        return new GeneralSwipeAction(
                Swipe.FAST,
                new SmoothCoordinatesProvider(startX, startY),
                new SmoothCoordinatesProvider(endX, endY),
                Press.FINGER);
    }
}
