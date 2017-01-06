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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    CalendarBar calendarBar;
    DragCalendarLayout dragCalendarLayout;
    CalendarPresenter presenter;
    AppBarLayout appBarLayout;

//    ListView listTest;
//    QuickAdapter adapter;

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

        dragCalendarLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.scroll_test).setVisibility(View.GONE);
                findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                dragCalendarLayout.resetContent(R.id.empty_view);
            }
        }, 500);


//        listTest = (ListView) findViewById(R.id.list_test);
//        adapter = new QuickAdapter<String>(this,R.layout.item_test,mockData()) {
//            @Override
//            protected void convert(BaseAdapterHelper helper, String item) {
//                helper.setText(R.id.tv_name, item);
//            }
//        };
//        listTest.setAdapter(adapter);
//        listTest.setOnItemClickListener((parent, view, position, id) -> Toast.makeText(MainActivity.this, "you click test" + (position + 1), Toast.LENGTH_SHORT).show());
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
        presenter.setCallbk(new CalendarPresenter.ICallbk() {

            @Override
            public void onCalendarBarChange(String currentTime, boolean isToday) {
                calendarBar.setDate(currentTime,isToday);
            }

            @Override
            public void onScroll(String currentTime) {

            }

            @Override
            public boolean onSelect(String selectTime) {
                Toast.makeText(MainActivity.this, selectTime, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private List<String> mockData() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add("test" + i);
        }
        return data;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CalendarPresenter.instance().destroy();
    }
}
