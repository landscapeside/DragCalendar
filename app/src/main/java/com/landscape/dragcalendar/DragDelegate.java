package com.landscape.dragcalendar;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 1 on 2016/9/18.
 */
public class DragDelegate {
    private DragActionBridge consignor = null;
    Direction direction = Direction.STATIC;
    Direction dragDirection = Direction.STATIC;
    int initY = 0, mActivePointerId = -1;
    private GestureDetectorCompat gestureDetector;
    private float mDragPercent;

    public DragDelegate(DragActionBridge consignor) {
        gestureDetector = new GestureDetectorCompat(((ViewGroup) consignor).getContext(), new YScrollDetector());
        connect(consignor);
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (consignor.target() == null) {
            return false;
        }
        final int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                initY = (int) MotionEventUtil.getMotionEventY(event, mActivePointerId);
                consignor.dragHelper().shouldInterceptTouchEvent(event);
                mDragPercent = 0;
                consignor.beforeMove();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == -1) {
                    return false;
                }
                direction = Direction.getDirection(
                        (int) (MotionEventUtil.getMotionEventY(event, mActivePointerId) - initY));
                if (direction == Direction.DOWN) {
                    if (consignor.isRefreshAble() || ScrollStatus.isLoading(consignor.scrollStatus())) {
                        if (!ScrollStatus.isDragging(consignor.scrollStatus()) && !ScrollStatus.isRefreshing(consignor.scrollStatus()) && ScrollViewCompat.canSmoothDown(consignor.target())) {
                            if (ScrollStatus.isLoading(consignor.scrollStatus())) {
                                return handleMotionEvent(event);
                            } else {
                                return false;
                            }
                        } else {
                            return handleMotionEvent(event);
                        }
                    } else {
                        return false;
                    }
                } else if (direction == Direction.UP) {
                    if (consignor.isLoadAble() || ScrollStatus.isRefreshing(consignor.scrollStatus())) {
                        if (!ScrollStatus.isDragging(consignor.scrollStatus()) && !ScrollStatus.isLoading(consignor.scrollStatus()) && ScrollViewCompat.canSmoothUp(consignor.target())) {
                            if (ScrollStatus.isRefreshing(consignor.scrollStatus())) {
                                return handleMotionEvent(event);
                            } else {
                                return false;
                            }
                        } else {
                            if (ScrollViewCompat.canSmoothDown(consignor.target()) || ScrollStatus.isRefreshing(consignor.scrollStatus())) {
                                return handleMotionEvent(event);
                            }
                        }
                    } else {
                        return false;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = -1;
                dragDirection = Direction.STATIC;
                return false;
            case MotionEvent.ACTION_UP:
                mActivePointerId = -1;
                dragDirection = Direction.STATIC;
                if (ScrollStatus.isDragging(consignor.scrollStatus())) {
                    return handleMotionEvent(event);
                } else {
                    return false;
                }
        }
        return ScrollStatus.isDragging(consignor.scrollStatus());
    }

    public boolean onTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == -1) {
                    return true;
                }
                float originalDragPercent = (float) Math.abs(consignor.contentTop()) / (float)DRAG_MAX_RANGE + .4f;
                mDragPercent = Math.min(1f, Math.abs(originalDragPercent));
                consignor.setDrawPercent(mDragPercent);
                consignor.dragHelper().processTouchEvent(event);
                break;
            case MotionEvent.ACTION_UP:
                mActivePointerId = -1;
                dragDirection = Direction.STATIC;
                consignor.dragHelper().processTouchEvent(event);
                return true;
            default:
                consignor.dragHelper().processTouchEvent(event);
                break;
        }
        initY = (int) MotionEventUtil.getMotionEventY(event, mActivePointerId);
        return true;
    }

    private boolean handleMotionEvent(MotionEvent event) {
        if (!ScrollStatus.isDragging(consignor.scrollStatus())) {
            MotionEvent cancelEvent = MotionEvent.obtain(event);
            cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
            consignor.target().dispatchTouchEvent(cancelEvent);
        }
        return consignor.dragHelper().shouldInterceptTouchEvent(event) && gestureDetector.onTouchEvent(event);
    }

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
            return Math.abs(dx) <= Math.abs(dy);
        }
    }


    public void connect(DragActionBridge consignor) {
        this.consignor = consignor;
    }

    public interface DragActionBridge{
        ScrollStatus scrollStatus();
        int contentTop();
        ViewDragHelper dragHelper();
        View target();
        void setDrawPercent(float drawPercent);
        boolean isRefreshAble();
        boolean isLoadAble();
        void beforeMove();
    }
}
