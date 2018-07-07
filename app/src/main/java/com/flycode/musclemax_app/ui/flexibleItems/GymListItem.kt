package com.flycode.musclemax_app.ui.flexibleItems

//import com.amulyakhare.textdrawable.TextDrawable
//import com.amulyakhare.textdrawable.util.ColorGenerator
import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.data.models.Gym
import com.github.siyamed.shapeimageview.CircularImageView
import com.squareup.picasso.Picasso
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.*
import eu.davidea.flexibleadapter.utils.FlexibleUtils
import eu.davidea.viewholders.FlexibleViewHolder

/**
 * Where AbstractFlexibleItem implements IFlexible!
 */
class GymListItem(
        header : AbstractExpandableHeaderItem<*, ISectionable<*, *>>,
        var gym: Gym,
        var context : Context? = null
) : AbstractSectionableItem<GymListItem.MyViewHolder, AbstractExpandableHeaderItem<*, ISectionable<*, *>>>(header),
        IFilterable<String>,
        IHolder<Gym> {

    init {
        mDraggable = false
        mSwipeable = false
    }
    var listener: GymListItemListener? = null

    override fun getModel(): Gym = gym

    //Todo implement filter algorithm
    override fun filter(constraint: String?): Boolean {
        var found = false
        constraint?.let {
            found = gym.name.toLowerCase().trim().contains(constraint.toString())
        }
        return found
    }


    override fun equals(other: Any?): Boolean {
        if (other is GymListItem) {
            val inListItem: GymListItem =  other
            return this.gym.id == inListItem.gym.id
        }
        return false
    }

    /**
     * You should implement also this method if equals() is implemented.
     * This method, if implemented, has several implications that Adapter handles better:
     * - The Hash, increases performance in big list during Update & Filter operations.
     * - You might want to activate stable ids via Constructor for RV, if your id
     *   is unique (read more in the wiki page: "Setting Up Advanced") you will benefit
     *   of the animations also if notifyDataSetChanged() is invoked.
     */
    override fun hashCode() : Int = gym.id.hashCode()

    /**
     * For the item type we need an int value: the layoutResID is sufficient.
     */
    override fun getLayoutRes():Int = R.layout.gym_list_item

    /**
     * Delegates the creation of the ViewHolder to the user (AutoMap).
     * The infladed view is already provided as well as the Adapter.
     */

    override fun createViewHolder(view: View?,
                                  adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?
    ): MyViewHolder? = MyViewHolder(view, adapter)

    /**
     * The Adapter and the Payload are provided to perform and get more specific
     * information.
     */
    override fun bindViewHolder(
            adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
            holder: MyViewHolder?,
            position: Int,
            payloads: MutableList<Any>?
    ) {
        if (adapter?.hasFilter()!!) {
            val filter = adapter.getFilter(String::class.java)
            FlexibleUtils.highlightText(
                    holder?.tv_name!!,
                    gym.name,
                    filter,
                    context?.resources?.getColor(R.color.text_highlight_color)!!
            )
        } else {
            holder?.tv_name?.text = gym.name
        }
        holder?.rating_average?.text = gym.rating.toString()
        holder?.ratingBar?.rating = gym.rating
        holder?.rating_users_count?.text = gym.rating_user_count.toString()


        if (!gym.pictures.isEmpty())
            Picasso.get()
                    .load(gym.pictures[0].remote_location)
                    .into(holder?.im_picture)
        else holder?.im_picture?.setImageDrawable(
                TextDrawable.builder().buildRound(
                        gym.name.toCharArray()[0].toString(),
                        ColorGenerator.MATERIAL.getColor(gym.name)
                )
        )

        holder?.main_view?.setOnClickListener {
            listener?.onGymClicked(gym)
        }


    }

    /**
     * The ViewHolder used by this item.
     *
     */
    class MyViewHolder(
            view: View? ,
            adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?
    ) : FlexibleViewHolder(view,adapter) {

        val main_view: CardView? = view?.findViewById(R.id.main_view)
        val im_picture: CircularImageView? = view?.findViewById(R.id.im_picture)
        val tv_name: TextView? = view?.findViewById(R.id.tv_name)
        val ratingBar: RatingBar? = view?.findViewById(R.id.ratingBar)
        val rating_users_count: TextView? = view?.findViewById(R.id.rating_users_count)
        val rating_average: TextView? = view?.findViewById(R.id.rating_average)

    }

    interface GymListItemListener {
        fun onGymClicked(gym: Gym)
    }
}