package com.landscape.dragcalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
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
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    private void ensureTarget() {
        if (contentId == 0) {
            if (getChildCount() > 0) {
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    if (child != refreshView && child != loadView) {
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
            return true;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return DRAG_MAX_RANGE;
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


    @Override
    public ScrollStatus scrollStatus() {
        return null;
    }

    @Override
    public int contentTop() {
        return 0;
    }

    @Override
    public ViewDragHelper dragHelper() {
        return null;
    }

    @Override
    public View target() {
        return null;
    }

    @Override
    public void setDrawPercent(float drawPercent) {

    }

    @Override
    public boolean isRefreshAble() {
        return false;
    }

    @Override
    public boolean isLoadAble() {
        return false;
    }

    @Override
    public void beforeMove() {

    }
}
