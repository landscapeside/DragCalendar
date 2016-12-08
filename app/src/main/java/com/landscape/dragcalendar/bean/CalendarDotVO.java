package com.landscape.dragcalendar.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by landscape on 2016/12/8.
 */

public abstract class CalendarDotVO<T> {

    protected Map<String, CalendarDotItem> dots = new HashMap<>();

    public static class CalendarDotItem{
        private String date;
        private boolean containData = false;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public boolean isContainData() {
            return containData;
        }

        public void setContainData(boolean containData) {
            this.containData = containData;
        }
    }

    public void parseData(List<T> sources) {
        for (T t : sources) {
            CalendarDotItem dotItem = parseVO(t);
            dots.put(dotItem.getDate(), dotItem);
        }
    }

    protected abstract CalendarDotItem parseVO(T bean);

    public Map<String, CalendarDotItem> getDots() {
        return dots;
    }
}
