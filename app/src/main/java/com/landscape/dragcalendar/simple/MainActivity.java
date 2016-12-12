package com.landscape.dragcalendar.simple;

import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.landscape.dragcalendar.CalendarBar;
import com.landscape.dragcalendar.DragCalendarLayout;
import com.landscape.dragcalendar.R;
import com.landscape.dragcalendar.presenter.CalendarPresenter;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    CalendarBar calendarBar;
    DragCalendarLayout dragCalendarLayout;
    CalendarPresenter presenter;
    AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        calendarBar = (CalendarBar) findViewById(R.id.cal_bar);
        calendarBar.initHeight();
        dragCalendarLayout = (DragCalendarLayout) findViewById(R.id.drag_layout);
        appBarLayout = (AppBarLayout) toolbar.getParent();
        initCalBar();
        setToolBarAnim();
    }

    private void setToolBarAnim() {
        dragCalendarLayout.setPercentListener(new DragCalendarLayout.IPercentListener() {
            @Override
            public void percentChanged(float percent) {
                int height = appBarLayout.getMeasuredHeight();
                appBarLayout.setTranslationY((int) (-height * percent));
            }
        });
    }

    private void initCalBar() {
        presenter = CalendarPresenter.instance();
        presenter.registerCalendarBar(calendarBar);
        calendarBar.setBackCallbk(v -> presenter.backToday());
        calendarBar.setCloseCallbk(v -> presenter.close());
        presenter.setCallbk((selectTime,isToday) -> {
            calendarBar.setDate(selectTime,isToday);
            Toast.makeText(this, selectTime, Toast.LENGTH_SHORT).show();
        });
    }
}
