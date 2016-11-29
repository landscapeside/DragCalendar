package com.landscape.dragcalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by landscape on 2016/11/28.
 */

public class DragCalendarLayout extends FrameLayout implements DragDelegate.DragActionBridge {
    ViewDragHelper dragHelper;
    private int contentId = 0;
    DragDelegate dragDelegate = null;
    View mContent = null;
    ViewPager monthPager, weekPager;

    int contentTop = 0;

    public static final int MONTH_HEIGHT = 300;
    public static final int WEEK_HEIGHT = 50;

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
        layout(false);
    }

    private void layout(boolean fixedMonth) {
        if (monthPager == null || weekPager == null) {
            return;
        }
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        if (mContent != null) {
            mContent.layout(paddingLeft, paddingTop + contentTop, width - paddingRight, contentTop + height - paddingBottom);
        }
        if (!fixedMonth) {
            monthPager.layout(
                    paddingLeft,
                    contentTop - monthPager.getMeasuredHeight() + paddingTop,
                    width - paddingRight,
                    contentTop + paddingTop);
        } else {
            // 注释掉看看能否定住月视图

//            monthPager.layout(
//                    paddingLeft,
//                    contentTop - monthPager.getMeasuredHeight() + paddingTop,
//                    width - paddingRight,
//                    contentTop + paddingTop);
        }

        weekPager.layout(
                paddingLeft,
                paddingTop,
                width - paddingRight,
                paddingTop + weekPager.getMeasuredHeight());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflateViews();
        ensureTarget();
    }

    private void inflateViews() {
        monthPager = (ViewPager) View.inflate(getContext(), R.layout.calendar_pager, null);
        addView(monthPager, 0);
        weekPager = (ViewPager) View.inflate(getContext(), R.layout.calendar_pager, null);
        addView(weekPager);

        ViewPager.LayoutParams layoutParams = (ViewPager.LayoutParams) monthPager.getLayoutParams();
        layoutParams.height = dp2px(MONTH_HEIGHT);
        monthPager.setLayoutParams(layoutParams);

        layoutParams = (ViewPager.LayoutParams) weekPager.getLayoutParams();
        layoutParams.height = dp2px(WEEK_HEIGHT);
        weekPager.setLayoutParams(layoutParams);
    }

    private void initAdapter() {

    }

    private void ensureTarget() {
        if (contentId == 0) {
            if (getChildCount() > 0) {
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    if (child != monthPager && child != weekPager) {
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
                    || child == monthPager
                    || child == weekPager;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (child == mContent) {
                status = ScrollStatus.DRAGGING;
                if (contentTop + dy > MONTH_HEIGHT) {
                    return MONTH_HEIGHT;
                } else if (contentTop + dy < WEEK_HEIGHT) {
                    return WEEK_HEIGHT;
                } else {
                    return top;
                }
            } else {

                status = ScrollStatus.DRAGGING;
                if (contentTop + dy > MONTH_HEIGHT) {
                    return MONTH_HEIGHT - monthPager.getMeasuredHeight();
                } else if (contentTop + dy < WEEK_HEIGHT) {
                    return getPaddingTop();
                } else {
                    return top;
                }
            }
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return MONTH_HEIGHT;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (contentTop > dp2px(DRAW_VIEW_MAX_HEIGHT)) {
                shouldCancel = false;
                setRefreshing(true);
            } else if (contentTop < -dp2px(DRAW_VIEW_MAX_HEIGHT)) {
                shouldCancel = false;
                setLoading(true);
            } else if (contentTop > 0) {
                setRefreshing(false);
            } else if (contentTop == 0) {
                if (!ScrollViewCompat.canSmoothDown(mTarget)) {
                    setRefreshing(false);
                } else if (!ScrollViewCompat.canSmoothUp(mTarget)) {
                    setLoading(false);
                }
            } else {
                setLoading(false);
            }
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == mTarget) {
                if (!ScrollViewCompat.canSmoothDown(mTarget)
                        && top < 0) {
                    contentTop = 0;
                    layoutViews();
                } else if (ScrollViewCompat.canSmoothDown(mTarget)
                        && !ScrollViewCompat.canSmoothUp(mTarget)
                        && top > 0) {
                    contentTop = 0;
                    layoutViews();
                } else {
                    refreshView.offsetTopAndBottom(dy);
                    loadView.offsetTopAndBottom(dy);
                    contentTop = top;
                    layoutViews();
                    invalidate();
                }
            } else if (changedView == emptyView && emptyView.isShown()) {
                refreshView.offsetTopAndBottom(dy);
                loadView.offsetTopAndBottom(dy);
                contentTop = top;
                invalidate();
            } else {
                if (!ScrollViewCompat.canSmoothDown(mTarget)
                        && (top + refreshView.getMeasuredHeight() - getPaddingTop()) < 0) {
                    contentTop = 0;
                    layoutViews();
                } else if (ScrollViewCompat.canSmoothDown(mTarget)
                        && !ScrollViewCompat.canSmoothUp(mTarget)
                        && (top - getMeasuredHeight() + getPaddingBottom()) > 0) {
                    contentTop = 0;
                    layoutViews();
                } else {
                    contentTop += dy;
                    layoutViews();
                    invalidate();
                }
            }
        }
    };

    public void setExpand(boolean expand) {
        setExpand(expand, true);
    }

    public void setExpand(boolean expand, boolean anim) {
        if (anim) {
            if (expand) {
                if (dragHelper.smoothSlideViewTo(mContent, 0, dp2px(MONTH_HEIGHT))) {
                    ViewCompat.postInvalidateOnAnimation(this);
                    status = ScrollStatus.MONTH;
                } else {
                    status = ScrollStatus.MONTH;
                    if (dragListener != null) {
                        dragListener.onExpand();
                    }
                }
            } else {
                if (dragHelper.smoothSlideViewTo(mContent, 0, dp2px(WEEK_HEIGHT))) {
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


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
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
        return null;
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
