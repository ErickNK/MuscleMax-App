package com.flycode.musclemax_app.ui.flexibleItems

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.flycode.musclemax_app.R
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractExpandableHeaderItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.flexibleadapter.items.ISectionable
import eu.davidea.viewholders.ExpandableViewHolder

class SearchResultsHeaderItem(
        val id: Int = 1,
        var resultCount: Int
) : AbstractExpandableHeaderItem<SearchResultsHeaderItem.ViewHolder, ISectionable<*, *>>() {
    init {
        mHidden = false
        mDraggable = false
        mSelectable = false
    }

    override fun bindViewHolder(
            adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
            holder: ViewHolder?,
            position: Int,
            payloads: MutableList<Any>?) {
        holder?.tv_results_count?.text = resultCount.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other is SearchResultsHeaderItem) {
            val inListItem: SearchResultsHeaderItem =  other
            return this.id == inListItem.id
        }
        return false
    }

    override fun createViewHolder(
            view: View?,
            adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?
    ): ViewHolder = ViewHolder(view, adapter)

    override fun getLayoutRes(): Int = R.layout.search_result_list_header

    override fun hashCode() : Int = id.hashCode()

    /**
     * The ViewHolder used by this item.
     *
     */
    class ViewHolder(
            view: View?,
            adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?
    ) : ExpandableViewHolder(view,adapter,true) {
        val tv_results_count: TextView? = view?.findViewById(R.id.tv_results_count)
    }
}