package com.landscape.dragcalendar;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.PrecisionDescriber;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.action.Swiper;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;

/**
 * Created by landscape on 2016/12/16.
 */

public class ShakeSwipeAction implements ViewAction {

    private static final int VIEW_DISPLAY_PERCENTAGE = 90;

    private final Swiper swiper = Swipe.FAST;

    @Override
    public Matcher<View> getConstraints() {
        return isDisplayingAtLeast(VIEW_DISPLAY_PERCENTAGE);
    }

    @Override
    public String getDescription() {
        return swiper.toString().toLowerCase() + " swipe";
    }

    @Override
    public void perform(UiController uiController, View view) {

    }
}
