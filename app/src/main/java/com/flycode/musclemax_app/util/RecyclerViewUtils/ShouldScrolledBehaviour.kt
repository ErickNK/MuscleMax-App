package com.flycode.musclemax_app.util.RecyclerViewUtils

import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View

/**
 * scroll when adapter's items are not completely visible. in other words,
 * toolbar is always visible till adapter's items are enough to be scrolled.
 */
class ShouldScrolledBehaviour(private val mLayoutManager: LinearLayoutManager, private val mAdapter: RecyclerView.Adapter<*>) : AppBarLayout.ScrollingViewBehavior() {

    override fun onInterceptTouchEvent(parent: CoordinatorLayout?, child: View?, ev: MotionEvent?): Boolean {
        return !shouldScrolled()
    }

    private fun shouldScrolled(): Boolean {
        // adapter has more items that not shown yet
        if (mLayoutManager.findLastCompletelyVisibleItemPosition() != mAdapter.itemCount - 1) {
            return true
        } else if (mLayoutManager.findLastCompletelyVisibleItemPosition() == mAdapter.itemCount - 1 && mLayoutManager.findFirstCompletelyVisibleItemPosition() != 0) {
            return true
        }// last completely visible item is the last item in adapter but it may be occurred in 2 ways:
        // 1) all items are shown
        // 2) scrolled to the last item (implemented following)
        return false
    }
}