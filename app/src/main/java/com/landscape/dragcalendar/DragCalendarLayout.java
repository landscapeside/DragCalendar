package com.landscape.dragcalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.landscape.dragcalendar.constant.Direction;
import com.landscape.dragcalendar.constant.ScrollStatus;
import com.landscape.dragcalendar.presenter.CalendarPresenter;
import com.landscape.dragcalendar.view.ICalendarView;
import com.landscape.dragcalendar.view.MonthView;
import com.landscape.dragcalendar.view.WeekView;

import java.util.ArrayList;
import java.util.List;

import static com.landscape.dragcalendar.constant.Range.MONTH_HEIGHT;
import static com.landscape.dragcalendar.constant.Range.WEEK_HEIGHT;
import static com.landscape.dragcalendar.utils.MotionEventUtil.dp2px;

/**
 * Created by landscape on 2016/11/28.
 */

public class DragCalendarLayout extends FrameLayout implements DragDelegate.DragActionBridge, ICalendarView {
    ViewDragHelper dragHelper;
    private int contentId = 0;
    DragDelegate dragDelegate = null;
    View mContent = null;
    List<View> childViews = new ArrayList<>();
    MonthView monthView;
    WeekView weekView;
    ViewGroup calendarTitle;

    int contentTop = -1, titleHeight = 0;
    private float mDragPercent;

    CalendarDragListener dragListener;
    ScrollStatus status = ScrollStatus.IDLE, scrollStatus = ScrollStatus.WEEK;

    public DragCalendarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        dragHelper = ViewDragHelper.create(this, dragHelperCallback);
        dragDelegate = new DragDelegate(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DragCalendarLayout);
        contentId = a.getResourceId(R.styleable.DragCalendarLayout_calendar_content, 0);
        a.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        layout();
    }

    private void layout() {
        if (monthView == null || weekView == null) {
            return;
        }
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        titleHeight = calendarTitle.getMeasuredHeight();
        if (contentTop == -1) {
            contentTop = weekView.getMeasuredHeight() + paddingTop + titleHeight;
        }

        if (mContent != null) {
            mContent.layout(paddingLeft, contentTop, width - paddingRight, height - paddingBottom);
        }
        int monthTop = contentTop - monthView.getMeasuredHeight();
        monthTop = Math.max(Math.min(monthTop, paddingTop + titleHeight), paddingTop + titleHeight - monthView.getFixedPos());
        monthView.layout(
                paddingLeft,
                monthTop,
                width - paddingRight,
                monthTop + monthView.getMeasuredHeight());

        weekView.layout(
                paddingLeft,
                paddingTop + titleHeight,
                width - paddingRight,
                paddingTop + weekView.getMeasuredHeight() + titleHeight);

        calendarTitle.layout(paddingLeft, paddingTop, width - paddingRight, paddingTop + titleHeight);

        // 也同步其他几个隐藏的视图的位置
        for (View view : childViews) {
            if (view != mContent && view != weekView && view != monthView && view != calendarTitle) {
                view.layout(paddingLeft, contentTop, width - paddingRight, height - paddingBottom);
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflateViews();
        initPresenter();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            childViews.add(getChildAt(i));
        }
        ensureTarget();
    }

    private void initPresenter() {
        CalendarPresenter.instance().registerDragCalendarLayout(this);
        CalendarPresenter.instance().registerMonthView(monthView);
        CalendarPresenter.instance().registerWeekView(weekView);
    }

    public void connectBar(CalendarBar calendarBar) {
        CalendarPresenter.instance().registerCalendarBar(calendarBar);
    }

    private void inflateViews() {

        calendarTitle = (ViewGroup) View.inflate(getContext(), R.layout.week_title, null);
        calendarTitle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(calendarTitle);

        monthView = new MonthView(getContext());
        addView(monthView, 0);
        weekView = new WeekView(getContext());
        addView(weekView);
    }

    private void ensureTarget() {
        if (contentId == 0) {
            if (getChildCount() > 0) {
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    if (child != monthView && child != weekView && child != calendarTitle) {
                        mContent = child;
                        mContent.setClickable(true);
                        return;
                    }
                }
            }
        } else {
            mContent = findViewById(contentId);
            mContent.setClickable(true);
        }
    }

    public void resetContent(int contentId) {
        this.contentId = contentId;
        for (View view : childViews) {
            if (view.getId() == contentId) {
                removeView(view);
                addView(view);
                break;
            }
        }
        mContent.setClickable(false);
        ensureTarget();
        layout();
        invalidate();
    }

    ViewDragHelper.Callback dragHelperCallback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mContent
                    || child == monthView
                    || child == weekView;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            status = ScrollStatus.DRAGGING;
            return top;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return titleHeight + dp2px(getContext(), MONTH_HEIGHT);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (dragDelegate.getDirection() == Direction.DOWN) {
                setExpand(true);
            } else if (dragDelegate.getDirection() == Direction.UP) {
                setExpand(false);
            }
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            status = ScrollStatus.DRAGGING;
            if (contentTop + dy > getPaddingTop() + dp2px(getContext(), MONTH_HEIGHT) + titleHeight) {
                contentTop = getPaddingTop() + titleHeight + dp2px(getContext(), MONTH_HEIGHT);
            } else if ((contentTop + dy) < getPaddingTop() + titleHeight + dp2px(getContext(), WEEK_HEIGHT)) {
                contentTop = getPaddingTop() + titleHeight + dp2px(getContext(), WEEK_HEIGHT);
            } else {
                contentTop += dy;
            }
            layout();
            monthView.setVisibility(VISIBLE);
            weekView.setVisibility(VISIBLE);
            int scrollTop = contentTop - getPaddingTop() - titleHeight - dp2px(getContext(), WEEK_HEIGHT);
            float originalDragPercent = (float) Math.abs(scrollTop) / (float) dp2px(getContext(), MONTH_HEIGHT - WEEK_HEIGHT);
            mDragPercent = Math.min(1f, Math.abs(originalDragPercent));
            setDrawPercent(mDragPercent);
        }
    };

    public void setExpand(boolean expand) {
        setExpand(expand, true);
    }

    public void setExpand(boolean expand, boolean anim) {
        dragHelper.abort();
        if (anim) {
            lastAnimState = true;
            if (expand) {
                if (dragHelper.smoothSlideViewTo(mContent, 0, getPaddingTop() + titleHeight + dp2px(getContext(), MONTH_HEIGHT))) {
                    ViewCompat.postInvalidateOnAnimation(this);
                    scrollStatus = ScrollStatus.MONTH;
                } else {
                    status = ScrollStatus.MONTH;
                    scrollStatus = status;
                    if (dragListener != null) {
                        dragListener.onExpand();
                    }
                    // 直接遮挡即可
                    weekView.setVisibility(GONE);
                }
            } else {
                if (dragHelper.smoothSlideViewTo(mContent, 0, getPaddingTop() + titleHeight + dp2px(getContext(), WEEK_HEIGHT))) {
                    ViewCompat.postInvalidateOnAnimation(this);
                    scrollStatus = ScrollStatus.WEEK;
                } else {
                    if (dragListener != null) {
                        dragListener.onFold();
                    }
                    status = ScrollStatus.WEEK;
                    scrollStatus = status;
                    weekView.setVisibility(VISIBLE);
                }
            }
        } else {

        }
    }

    boolean lastAnimState = true, animContinue = true;

    @Override
    public void computeScroll() {
        animContinue = dragHelper.continueSettling(true);
        if (animContinue && lastAnimState == animContinue) {
            ViewCompat.postInvalidateOnAnimation(this);
        } else if (!animContinue && lastAnimState != animContinue) {
            if (ScrollStatus.isMonth(scrollStatus)) {
                weekView.setVisibility(GONE);
            } else if (ScrollStatus.isWeek(scrollStatus)) {
                weekView.setVisibility(VISIBLE);
            } else if (ScrollStatus.isIdle(scrollStatus)) {

            }
            status = scrollStatus;
            lastAnimState = animContinue;
        }
    }

    public void setCollapseAble(boolean collapseAble) {

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return dragDelegate.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return dragDelegate.onTouchEvent(event);
    }

    @Override
    public ScrollStatus scrollStatus() {
        return status;
    }

    @Override
    public ViewDragHelper dragHelper() {
        return dragHelper;
    }

    public void setDrawPercent(float drawPercent) {
        monthView.setAlpha(drawPercent);
        weekView.setAlpha(1 - drawPercent);
        if (percentListener != null) {
            percentListener.percentChanged(drawPercent);
        }
    }

    @Override
    public View target() {
        return mContent;
    }

    @Override
    public boolean isScrollContent(int x, int y) {
        return dragHelper.findTopChildUnder(x, y) == mContent;
    }

    @Override
    public void beforeMove() {

    }

    @Override
    public boolean isCollapsAble() {
        return true;
    }

    public void setCalendarDragListener(CalendarDragListener calendarDragListener) {
        dragListener = calendarDragListener;
    }

    @Override
    public void backToday() {
        monthView.backToday();
        weekView.backToday();
    }

    @Override
    public int currentIdx() {
        return 0;
    }

    @Override
    public void focusCalendar() {
        monthView.focusCalendar();
        weekView.focusCalendar();
    }

    public interface IPercentListener {
        void percentChanged(float percent);
    }

    IPercentListener percentListener = null;

    public void setPercentListener(IPercentListener percentListener) {
        this.percentListener = percentListener;
    }
}
