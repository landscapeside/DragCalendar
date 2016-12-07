package com.landscape.dragcalendar.constant;

/**
 * Created by 1 on 2016/9/9.
 */
public enum Direction {
    STATIC("STATIC"),
    UP("UP"),
    DOWN("DOWN");

    String value;

    Direction(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Direction getDirection(int diffY) {
        if (diffY > 0 ) {
            return DOWN;
        } else if (diffY < 0) {
            return UP;
        } else {
            return STATIC;
        }
    }
}
