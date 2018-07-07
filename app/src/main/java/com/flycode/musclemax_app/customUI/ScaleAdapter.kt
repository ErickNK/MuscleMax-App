package com.flycode.musclemax_app.customUI

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView

import com.flycode.musclemax_app.R

class ScaleAdapter(
        val context: Context,
        val currentOrientation: Int)
    : RecyclerView.Adapter<ScaleAdapter.ViewHolder>() {

    var maximumSize: Int = 0
    var typeOfUnit: String? = null
    var backgroundColor = Color.TRANSPARENT
    private var view: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (currentOrientation == 1)
            view = LayoutInflater.from(context)
                    .inflate(R.layout.horizontal_scale_unit, parent, false)
        else
            view = LayoutInflater.from(context)
                    .inflate(R.layout.vertical_scale_unit, parent, false)

        return ViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.one_unit_value.text = (position + 1).toString() + typeOfUnit!!
        holder.main_view.setBackgroundColor(backgroundColor)
    }

    override fun getItemCount(): Int {
        return maximumSize
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var one_unit_value: TextView = itemView.findViewById<View>(R.id.one_unit_value) as TextView
        var main_view: RelativeLayout = itemView.findViewById<View>(R.id.main_view) as RelativeLayout

    }

    companion object {

        val HORIZONTAL_ORIENTATION = 1
        val VERTICAL_ORIENTATION = 2
    }
}
