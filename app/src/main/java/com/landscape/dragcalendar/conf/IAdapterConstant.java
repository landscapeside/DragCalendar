package com.landscape.dragcalendar.conf;

import java.util.Calendar;

/**
 * Created by landscape on 2017/1/3.
 */

public interface IAdapterConstant {
    int getCount();
    int defPosition();
    Calendar calculateByOffset(int position);
    Calendar calculateByCalOffset(Calendar calendar, int position);
}
