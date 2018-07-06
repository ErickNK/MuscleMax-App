package com.flycode.musclemax_app.ui.main.map

import android.content.Context
import android.widget.LinearLayout
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.data.models.Picture
import com.squareup.picasso.Picasso
import java.util.*

class GymImagesPager(
        var context: Context
) : android.support.v4.view.PagerAdapter() {
    val listItems: MutableList<Picture> = Arrays.asList()
    val listItemResources: MutableList<Int> = Arrays.asList(
            R.drawable.gym_image1,
            R.drawable.gym_image2,
            R.drawable.gym_image3,
            R.drawable.gym_image4,
            R.drawable.gym_image5,
            R.drawable.gym_image6
    )
    var mLayoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return listItemResources.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = mLayoutInflater.inflate(R.layout.maps_bottom_sheet_pager_item, container, false)
        val imageView = itemView.findViewById(R.id.imageView) as ImageView

//        if (!listItems.isEmpty())
//            Picasso.get()
//                    .load(listItems[position + 1].remote_location)
//                    .into(imageView)
//        else imageView.setImageDrawable(
//                TextDrawable.builder().buildRound(
//                        group.name.toCharArray()[0].toString(),
//                        ColorGenerator.MATERIAL.getColor(group.name)
//                )
//        )
        imageView.setImageResource(listItemResources[position])

        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}