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

import com.landscape.dragcalendar.presenter.CalendarPresenter;
import com.landscape.dragcalendar.view.MonthView;
import com.landscape.dragcalendar.view.WeekView;

import static com.landscape.dragcalendar.Range.MONTH_HEIGHT;
import static com.landscape.dragcalendar.Range.WEEK_HEIGHT;
import static com.landscape.dragcalendar.MotionEventUtil.dp2px;

/**
 * Created by landscape on 2016/11/28.
 */

public class DragCalendarLayout extends FrameLayout implements DragDelegate.DragActionBridge {
    CalendarPresenter presenter;
    ViewDragHelper dragHelper;
    private int contentId = 0;
    DragDelegate dragDelegate = null;
    View mContent = null;
    MonthView monthView;
    WeekView weekView;
    ViewGroup calendarTitle;

    int contentTop = -1, titleHeight = 0;

    CalendarDragListener dragListener;
    ScrollStatus status = ScrollStatus.IDLE;

    public DragCalendarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        dragHelper = ViewDragHelper.create(this, 0.3f, dragHelperCallback);
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
            contentTop = weekView.getMeasuredHeight();
        }

        if (mContent != null) {
            mContent.layout(paddingLeft, paddingTop + contentTop + titleHeight, width - paddingRight, titleHeight + contentTop + height - paddingBottom);
        }
        int monthTop = contentTop - monthView.getMeasuredHeight() + paddingTop + titleHeight;
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
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflateViews();
        ensureTarget();
        initPresenter();
    }

    private void initPresenter() {
        presenter = CalendarPresenter.instance();
        presenter.registerDragCalendarLayout(this);
        presenter.registerMonthView(monthView);
        presenter.registerWeekView(weekView);
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
            if (child == mContent) {
                if (contentTop + dy > getPaddingTop() + dp2px(getContext(),MONTH_HEIGHT) + titleHeight) {
                    Log.i("drag", "clampViewPositionVertical1:" + (getPaddingTop() + dp2px(getContext(), MONTH_HEIGHT) + titleHeight));
                    return getPaddingTop() + dp2px(getContext(),MONTH_HEIGHT) + titleHeight;
                } else if (contentTop + dy < getPaddingTop() + dp2px(getContext(),WEEK_HEIGHT) + titleHeight) {
                    Log.i("drag", "clampViewPositionVertical2:"+(getPaddingTop() + dp2px(getContext(),WEEK_HEIGHT) + titleHeight));
                    return getPaddingTop() + dp2px(getContext(),WEEK_HEIGHT) + titleHeight;
                } else {
                    Log.i("drag", "clampViewPositionVertical3:"+top);
                    return top;
                }
            } else if (child == monthView) {
                return Math.max(Math.min(top, getPaddingTop() + titleHeight), getPaddingTop() + titleHeight - monthView.getFixedPos());
            } else if (child == weekView) {
                return getPaddingTop() + titleHeight;
            }
            return top;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return dp2px(getContext(),MONTH_HEIGHT);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
//            if (dragDelegate.getDirection() == Direction.DOWN) {
//                setExpand(true);
//            } else if (dragDelegate.getDirection() == Direction.UP) {
//                setExpand(false);
//            }
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == mContent) {
                if (top < getPaddingTop() + titleHeight + dp2px(getContext(),WEEK_HEIGHT)) {
                    contentTop = getPaddingTop() + titleHeight + dp2px(getContext(),WEEK_HEIGHT);
                } else {
                    contentTop = top;
                }
            } else {
                if ((contentTop + dy) < getPaddingTop() + titleHeight + dp2px(getContext(),WEEK_HEIGHT)) {
                    contentTop = getPaddingTop() + titleHeight + dp2px(getContext(),WEEK_HEIGHT);
                } else {
                    contentTop += dy;
                }
            }
            layout();
//            invalidate();
        }
    };

    public void setExpand(boolean expand) {
        setExpand(expand, true);
    }

    public void setExpand(boolean expand, boolean anim) {
        if (anim) {
            if (expand) {
                if (dragHelper.smoothSlideViewTo(mContent, 0, dp2px(getContext(), MONTH_HEIGHT))) {
                    ViewCompat.postInvalidateOnAnimation(this);
                    status = ScrollStatus.MONTH;
                } else {
                    status = ScrollStatus.MONTH;
                    if (dragListener != null) {
                        dragListener.onExpand();
                    }
                }
            } else {
                if (dragHelper.smoothSlideViewTo(mContent, 0, dp2px(getContext(), WEEK_HEIGHT))) {
                    ViewCompat.postInvalidateOnAnimation(this);
                    status = ScrollStatus.WEEK;
                } else {
                    if (dragListener != null) {
                        dragListener.onFold();
                    }
                    status = ScrollStatus.WEEK;
                }
            }
        } else {

        }
    }

    boolean animContinue = true;

    @Override
    public void computeScroll() {
        animContinue = dragHelper.continueSettling(true);
        if (animContinue) {
            ViewCompat.postInvalidateOnAnimation(this);
        } else {

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
    public int contentTop() {
        return contentTop;
    }

    @Override
    public ViewDragHelper dragHelper() {
        return dragHelper;
    }

    @Override
    public void setDrawPercent(float drawPercent) {

    }

    @Override
    public View target() {
        return mContent;
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
}
