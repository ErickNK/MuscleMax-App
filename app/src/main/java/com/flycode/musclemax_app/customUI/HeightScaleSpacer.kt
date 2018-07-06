package com.flycode.musclemax_app.customUI

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View


class HeightScaleSpacer : RecyclerView.ItemDecoration() {
    var offset = 0

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)

        //TODO: check if first or last item
        if (parent.getChildAdapterPosition(view) == 0) {
            //FIRST CHILD
            outRect.top = offset
        } else if (parent.getChildAdapterPosition(view) == state!!.itemCount - 1) {
            //LAST CHILD
            outRect.bottom = offset
        }

    }
}
