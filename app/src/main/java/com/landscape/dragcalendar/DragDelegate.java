package com.landscape.dragcalendar;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.landscape.dragcalendar.constant.Direction;
import com.landscape.dragcalendar.constant.ScrollStatus;
import com.landscape.dragcalendar.utils.MotionEventUtil;
import com.landscape.dragcalendar.utils.ScrollViewCompat;

/**
 * Created by 1 on 2016/9/18.
 */
public class DragDelegate {
    private DragActionBridge consignor = null;
    Direction direction = Direction.STATIC, touchDirection = Direction.STATIC;
    int initY = 0, lastY = 0, mActivePointerId = -1;
    private GestureDetectorCompat gestureDetector;

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
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                gestureDetector.onTouchEvent(event);
                consignor.dragHelper().shouldInterceptTouchEvent(event);
                break;
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                initY = (int) MotionEventUtil.getMotionEventY(event, mActivePointerId);
                lastY = initY;
                gestureDetector.onTouchEvent(event);
                consignor.dragHelper().shouldInterceptTouchEvent(event);
                consignor.beforeMove();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == -1) {
                    return false;
                }
                float x = MotionEventUtil.getMotionEventX(event, mActivePointerId);
                float y = MotionEventUtil.getMotionEventY(event, mActivePointerId);
                direction = Direction.getDirection((int) (y - initY));
                if (direction == Direction.DOWN) {
                    if (consignor.isCollapsAble()) {
                        if (!ScrollStatus.isDragging(consignor.scrollStatus())) {
                            if (ScrollViewCompat.canSmoothDown(consignor.target())
                                    && consignor.isScrollContent((int) x, (int) y)) {
                                return false;
                            } else {
                                return handleMotionEvent(event);
                            }
                        } else {
                            return handleMotionEvent(event);
                        }
                    } else {
                        return false;
                    }
                } else if (direction == Direction.UP) {
                    if (consignor.isCollapsAble()) {
                        if (!ScrollStatus.isDragging(consignor.scrollStatus())) {
                            if (ScrollStatus.isMonth(consignor.scrollStatus())) {
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
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = -1;
                return false;
            case MotionEvent.ACTION_UP:
                mActivePointerId = -1;
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
        float y = MotionEventUtil.getMotionEventY(event, mActivePointerId);
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == -1) {
                    return true;
                }
                touchDirection = Direction.getDirection((int) (y - lastY));
                consignor.dragHelper().processTouchEvent(event);
                break;
            case MotionEvent.ACTION_UP:
                mActivePointerId = -1;
                consignor.dragHelper().processTouchEvent(event);
                return true;
            default:
                consignor.dragHelper().processTouchEvent(event);
                break;
        }
        lastY = (int) y;
        return true;
    }

    private boolean handleMotionEvent(MotionEvent event) {
        if (!ScrollStatus.isDragging(consignor.scrollStatus())) {
            MotionEvent cancelEvent = MotionEvent.obtain(event);
            cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
            consignor.target().dispatchTouchEvent(cancelEvent);
        }
        boolean gestureDetectorMove = gestureDetector.onTouchEvent(event);
        boolean dragHelperShouldIntercept = consignor.dragHelper().shouldInterceptTouchEvent(event);
        return gestureDetectorMove && dragHelperShouldIntercept;
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

    public Direction getDirection() {
        return touchDirection == Direction.STATIC ? direction : touchDirection;
    }

    public interface DragActionBridge {
        ScrollStatus scrollStatus();

        ViewDragHelper dragHelper();

        View target();

        boolean isScrollContent(int x, int y);

        void beforeMove();

        boolean isCollapsAble();
    }
}
