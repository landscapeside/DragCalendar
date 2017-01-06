package com.landscape.dragcalendar.simple;

import android.os.SystemClock;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.MotionEvents;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;

import com.landscape.dragcalendar.DragTestUtils;
import com.landscape.dragcalendar.R;
import com.landscape.dragcalendar.constant.Range;
import com.landscape.dragcalendar.utils.MotionEventUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.doubleClick;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static com.landscape.dragcalendar.DragTestUtils.*;
import static org.junit.Assert.*;

/**
 * Created by landscape on 2016/12/15.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void smoothDownAndUpMuchTimes() {
        SystemClock.sleep(500);
        onView(withId(R.id.drag_layout)).perform(swipeDown());
        SystemClock.sleep(3000);
        onView(withId(R.id.drag_layout))
                .perform(drag(
                        0,
                        MotionEventUtil.dp2px(activityTestRule.getActivity(), Range.MONTH_HEIGHT + 15 + 30),
                        0,
                        MotionEventUtil.dp2px(activityTestRule.getActivity(), Range.MONTH_HEIGHT + 15 - 30)));

//        SystemClock.sleep(10);
        onView(withId(R.id.drag_layout)).perform(swipeDown());
        onView(withId(R.id.drag_layout))
                .perform(drag(
                        0,
                        MotionEventUtil.dp2px(activityTestRule.getActivity(), Range.MONTH_HEIGHT + 45),
                        0,
                        MotionEventUtil.dp2px(activityTestRule.getActivity(), Range.MONTH_HEIGHT + 50)));

        SystemClock.sleep(5000);
    }
}