package com.landscape.dragcalendar;

import android.support.test.espresso.action.CoordinatesProvider;
import android.view.View;

/**
 * Created by landscape on 2016/12/15.
 */

public class SmoothCoordinatesProvider implements CoordinatesProvider {

    private int x;
    private int y;

    public SmoothCoordinatesProvider(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public float[] calculateCoordinates(View view) {
        return new float[]{x,y};
    }
}
