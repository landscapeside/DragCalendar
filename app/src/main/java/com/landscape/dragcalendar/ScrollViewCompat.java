package com.landscape.dragcalendar;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

/**
 * Created by 1 on 2016/9/8.
 */
public class ScrollViewCompat {

    /**
     * 该视图控件还能否向下拉动
     *
     * @param mTarget
     * @return true-未到顶部，false-到顶部
     */
    public static boolean canSmoothDown(View mTarget) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else if (mTarget instanceof RecyclerView) {
                final RecyclerView recyclerView = (RecyclerView) mTarget;
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if ((lm.findFirstVisibleItemPosition() == 0)) {
                    View firstView = lm.findViewByPosition(0);
                    return firstView.getTop() < 0;
                } else {
                    return true;
                }
            } else {
                return mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, -1);
        }
    }

    /**
     * 该视图控件还能否向上拉动
     *
     * @param mTarget
     * @return true-未到底部，false-到底部
     */
    public static boolean canSmoothUp(View mTarget) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                View lastChild = absListView.getChildAt(absListView.getChildCount() - 1);
                if (lastChild != null) {
                    if (absListView.getFirstVisiblePosition() == 0 && absListView.getLastVisiblePosition() == (absListView.getCount() - 1)) {
                        return false;
                    }
                    return (absListView.getLastVisiblePosition() < (absListView.getCount() - 1))
                            && lastChild.getBottom() > absListView.getPaddingBottom();
                } else {
                    return false;
                }
            } else if (mTarget instanceof RecyclerView) {
                final RecyclerView recyclerView = (RecyclerView) mTarget;
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int count = recyclerView.getAdapter().getItemCount() - 1;
                if (lm.canScrollVertically()) {
                    return !(lm.findLastVisibleItemPosition() == count);
                } else {
                    return false;
                }
            } else {
                View scrollChild = ((ViewGroup) mTarget).getChildAt(0);
                if (scrollChild == null) {
                    return false;
                } else {
                    int childHeight = scrollChild.getMeasuredHeight();
                    return (mTarget.getScrollY() + mTarget.getHeight()) < childHeight;
                }
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, 1);
        }
    }

}
