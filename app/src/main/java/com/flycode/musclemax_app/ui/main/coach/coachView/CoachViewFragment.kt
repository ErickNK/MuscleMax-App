package com.flycode.musclemax_app.ui.main.coach.coachView


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.afollestad.materialdialogs.MaterialDialog
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.data.models.Review
import com.flycode.musclemax_app.data.models.Tag
import com.flycode.musclemax_app.data.models.User
import com.flycode.musclemax_app.databinding.CoachViewBinding
import com.flycode.musclemax_app.databinding.CustomReviewEntryBinding
import com.flycode.musclemax_app.databinding.CustomTagPickerBinding
import com.flycode.musclemax_app.ui.base.BaseFragment
import com.flycode.musclemax_app.ui.main.MainActivity
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_coach_view.*
import javax.inject.Inject
import javax.inject.Named

class CoachViewFragment
    : BaseFragment<CoachViewFragment, CoachViewPresenter, CoachViewViewModel>(),
        CoachViewContract.CoachViewFragment {

    @Inject override lateinit var viewModel: CoachViewViewModel
    @Inject lateinit var mainActivity: MainActivity
    @Inject lateinit var CoachReviewsAdapter: CoachReviewsAdapter
    @field: [Inject Named("main_tag_adapter")]
    lateinit var mainCoachTagsAdapter: CoachTagsAdapter
    @field: [Inject Named("tag_picker_tag_adapter")]
    lateinit var tagPickerCoachTagsAdapter: CoachTagsAdapter

    lateinit var customTagPickerBinding: CustomTagPickerBinding
    lateinit var customTagPickerDialog: MaterialDialog

    lateinit var customReviewEntryBinding: CustomReviewEntryBinding
    lateinit var customReviewEntryDialog: MaterialDialog

    lateinit var CoachViewBinding: CoachViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        CoachViewBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_coach_view,container,false)
        CoachViewBinding.viewModel = viewModel
        CoachViewBinding.setLifecycleOwner(this)

        return CoachViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (CoachViewBinding.toolbar as Toolbar).navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back_white_24dp)
        (CoachViewBinding.toolbar as Toolbar).setNavigationOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.CoachOverviewFragment)
        }

        (activity as AppCompatActivity).setSupportActionBar(CoachViewBinding.toolbar as Toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "Coaches"

        arguments?.let {
            if (it.getString(COACH) != null){
                val u = it.getString(COACH)
                viewModel.coach = Gson().fromJson(u, User::class.java)
            }
            presenter.init()
        }
        btn_call.setOnClickListener { presenter.callCoach() }
        btn_email.setOnClickListener { presenter.emailCoach() }
        coach_view_reload_btn.setOnClickListener { presenter.init() }
        btn_add_tag.setOnClickListener { customTagPickerDialog.show() }
        btn_add_review.setOnClickListener {
            customReviewEntryBinding.review = Review()
            customReviewEntryDialog.show()
        }
        coach_view_reload_btn.setOnClickListener {
            viewModel.uiState.onError = false
        }

        setupReviewsAdapter()
        setupTagsRecyclerView()
        setupTagsEntry()
        setupReviewsEntry()
    }

    private fun setupTagsRecyclerView() {
        val chipsLayoutManager = ChipsLayoutManager.newBuilder(context)
                .setMaxViewsInRow(3)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build()

        chips_recycler_view.layoutManager = chipsLayoutManager
        chips_recycler_view.addItemDecoration(
                SpacingItemDecoration(resources.getDimensionPixelOffset(R.dimen.tag_spacing),
                        resources.getDimensionPixelOffset(R.dimen.tag_spacing)))

        mainCoachTagsAdapter.addMultipleTags(viewModel.coach.tags)
        chips_recycler_view.adapter = mainCoachTagsAdapter
    }

    private fun setupReviewsAdapter() {
        reviews_view_pager.offscreenPageLimit = 3
        CoachReviewsAdapter.listItems = viewModel.coach.reviews

        reviews_view_pager.adapter = CoachReviewsAdapter
    }

    private fun setupReviewsEntry(){
        customReviewEntryBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.custom_review_entry_layout, null, false)

        customReviewEntryDialog = MaterialDialog.Builder(context!!)
                .customView(customReviewEntryBinding.root, true)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive { _, _ ->
                    //Review Coach
                    presenter.addReview(customReviewEntryBinding.review!!)
                    customReviewEntryDialog.hide()

                    //Check if update
                }
                .build()
    }

    private fun setupTagsEntry(){
        presenter.fetchTags()

        customTagPickerBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.tag_picker_layout, null, false)

        customTagPickerDialog = MaterialDialog.Builder(context!!)
                .customView(customTagPickerBinding.root, true)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .build()

        val chipsLayoutManager = ChipsLayoutManager.newBuilder(context)
                .setMaxViewsInRow(3)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build()

        customTagPickerBinding.chipsRecyclerView.layoutManager = chipsLayoutManager
        customTagPickerBinding.chipsRecyclerView.addItemDecoration(
                SpacingItemDecoration(resources.getDimensionPixelOffset(R.dimen.tag_spacing),
                        resources.getDimensionPixelOffset(R.dimen.tag_spacing)))

        tagPickerCoachTagsAdapter.onTagClickedListener = object : CoachTagsAdapter.OnTagClickedListener {
            override fun onTagClicked(tag: Tag) {
                presenter.tagCoach(tag)
                customTagPickerDialog.hide()
            }
        }
        customTagPickerBinding.chipsRecyclerView.adapter = tagPickerCoachTagsAdapter
    }

    private fun setupRatingsEntry(){

    }

    fun loadImage(remote_location: String) {
        Picasso.get()
                .load(viewModel.coach.pictures[0].remote_location)
                .into(CoachViewBinding.imPicture)
    }

    fun setImageDrawable(){
        CoachViewBinding.imPicture.setImageDrawable(
                TextDrawable.builder().buildRound(
                        "${viewModel.coach.firstname} ${viewModel.coach.lastname}".toCharArray()[0].toString(),
                        ColorGenerator.MATERIAL.getColor("${viewModel.coach.firstname} ${viewModel.coach.lastname}")
                )
        )
    }

    companion object {
        const val CALL_COACH_REQUEST_CODE = 0
        const val EMAIL_COACH_REQUEST_CODE = 1
        const val COACH: String = "coach"
    }


}
