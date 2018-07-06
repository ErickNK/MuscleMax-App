package com.flycode.musclemax_app.ui.main.coach.coachView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.data.models.Review
import com.github.siyamed.shapeimageview.CircularImageView
import com.squareup.picasso.Picasso
import java.util.*

class CoachReviewsAdapter(
        var context: Context
) : android.support.v4.view.PagerAdapter() {
    var listItems: MutableList<Review> = Arrays.asList()
    var mLayoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return listItems.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as FrameLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = mLayoutInflater.inflate(R.layout.review_page_item, container, false)

        val circleImageView = itemView.findViewById<CircularImageView>(R.id.im_picture)
        val reviewer_name = itemView.findViewById<TextView>(R.id.reviewer_name)

        listItems[position].owner?.let {
            if (!it.pictures.isEmpty())
                Picasso.get()
                        .load(it.pictures[0].remote_location)
                        .into(circleImageView)
            else circleImageView.setImageDrawable(
                    TextDrawable.builder().buildRound(
                            it.firstname.toCharArray()[0].toString(),
                            ColorGenerator.MATERIAL.getColor(it.firstname)
                    )
            )

            reviewer_name.text = "${it.firstname} ${it.lastname}"
        }

        itemView.findViewById<TextView>(R.id.review_content).apply {
            this.text = listItems[position].content
        }

        container.addView(itemView)
        return itemView
    }

    fun addReview(Review: Review,index: Int) {
        listItems.add(index,Review)
        notifyDataSetChanged()
    }

    fun addReview(Review: Review) {
        if (listItems.add(Review)) {
            notifyDataSetChanged()
        }
    }

    fun addMultipleReviews(Reviews: List<Review>) {
        val startPosition = listItems.size //Initial start position before adding.
        if (listItems.addAll(Reviews)) {
            notifyDataSetChanged()
        }
    }

    fun deleteReview(Review: Review) {
        val index = listItems.indexOf(Review)
        if (listItems.remove(Review)) {
            notifyDataSetChanged()
        }
    }

    fun updateReview(Review: Review, index: Int) {
        listItems.removeAt(index)
        listItems.add(index, Review)
        notifyDataSetChanged()
    }

    fun clear() {
        listItems.clear()
        notifyDataSetChanged()
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as FrameLayout)
    }
}