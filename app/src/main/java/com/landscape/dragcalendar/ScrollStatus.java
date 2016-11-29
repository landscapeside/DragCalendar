package com.landscape.dragcalendar;

/**
 * Created by 1 on 2016/9/9.
 */
public enum  ScrollStatus {
    IDLE("IDLE"),
    DRAGGING("DRAGGING"),
    MONTH("MONTH"),
    WEEK("WEEK");

    private String value;

    ScrollStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static boolean isMonth(ScrollStatus status) {
        return status == MONTH;
    }

    public static boolean isWeek(ScrollStatus status) {
        return status == WEEK;
    }

    public static boolean isDragging(ScrollStatus status) {
        return status == DRAGGING;
    }

    public static boolean isIdle(ScrollStatus status) {
        return status == IDLE;
    }
}
