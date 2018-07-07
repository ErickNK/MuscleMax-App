package com.flycode.musclemax_app.ui.main.gym.gymView


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.data.models.Gym
import com.flycode.musclemax_app.data.models.Review
import com.flycode.musclemax_app.data.models.Tag
import com.flycode.musclemax_app.databinding.CustomReviewEntryBinding
import com.flycode.musclemax_app.databinding.CustomTagPickerBinding
import com.flycode.musclemax_app.databinding.GymViewBinding
import com.flycode.musclemax_app.ui.base.BaseFragment
import com.flycode.musclemax_app.ui.main.map.MapFragment
import com.github.reline.GoogleMapsBottomSheetBehavior
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_gym_view.*
import javax.inject.Inject
import javax.inject.Named

class GymViewFragment : BaseFragment<GymViewFragment, GymViewPresenter, GymViewViewModel>(),
        GymViewContract.GymViewFragment, MapFragment.BottomSheetStateListener {

    @Inject override lateinit var viewModel: GymViewViewModel
    @Inject lateinit var gymReviewsAdapter: GymReviewsAdapter
    @field: [Inject Named("main_tag_adapter")]
    lateinit var mainGymTagsAdapter: GymTagsAdapter
    @field: [Inject Named("tag_picker_tag_adapter")]
    lateinit var tagPickerGymTagsAdapter: GymTagsAdapter

    lateinit var customTagPickerBinding: CustomTagPickerBinding
    lateinit var customTagPickerDialog: MaterialDialog

    lateinit var customReviewEntryBinding: CustomReviewEntryBinding
    lateinit var customReviewEntryDialog: MaterialDialog

    lateinit var gymViewBinding: GymViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        gymViewBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_gym_view,container,false)
        gymViewBinding.viewModel = viewModel
        gymViewBinding.setLifecycleOwner(this)

        return gymViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            if (it.getString("gym") != null){
                viewModel.gym = Gson().fromJson(it.getString("gym"), Gym::class.java)
                presenter.init()
            }
        }
        btn_call.setOnClickListener { presenter.callGym() }
        btn_directions.setOnClickListener { presenter.showGymDirections() }
        btn_add_tag.setOnClickListener { customTagPickerDialog.show() }
        btn_add_review.setOnClickListener {
            customReviewEntryBinding.review = Review()
            customReviewEntryDialog.show()
        }
        gym_view_reload_btn.setOnClickListener {
            viewModel.uiState.onError = false
            presenter.init()
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

        mainGymTagsAdapter.addMultipleTags(viewModel.gym.tags)
        chips_recycler_view.adapter = mainGymTagsAdapter
    }

    private fun setupReviewsAdapter() {
        reviews_view_pager.offscreenPageLimit = 3
        gymReviewsAdapter.listItems = viewModel.gym.reviews

        reviews_view_pager.adapter = gymReviewsAdapter
    }

    private fun setupReviewsEntry(){
        customReviewEntryBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.custom_review_entry_layout, null, false)

        customReviewEntryDialog = MaterialDialog.Builder(context!!)
                .customView(customReviewEntryBinding.root, true)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive { _, _ ->
                    //Review Gym
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

        tagPickerGymTagsAdapter.onTagClickedListener = object : GymTagsAdapter.OnTagClickedListener {
            override fun onTagClicked(tag: Tag) {
                presenter.tagGym(tag)
                customTagPickerDialog.hide()
            }
        }
        customTagPickerBinding.chipsRecyclerView.adapter = tagPickerGymTagsAdapter
    }

    private fun setupRatingsEntry(){

    }

    override fun onStateChanged(newState: Int) {
        when(newState){
            GoogleMapsBottomSheetBehavior.STATE_EXPANDED ->{
                showReviewsDetails()
                showOtherDetails()
            }
            GoogleMapsBottomSheetBehavior.STATE_ANCHORED -> {
                showReviewsDetails()
                hideOtherDetails()
            }
            GoogleMapsBottomSheetBehavior.STATE_COLLAPSED,
            GoogleMapsBottomSheetBehavior.STATE_HIDDEN -> {
                hideOtherDetails()
                hideReviewsDetails()
            }
        }
    }

    private fun hideReviewsDetails(){
        if (!viewModel.uiState.isReviewsHidden){
            YoYo.with(Techniques.FadeOut)
                    .duration(500)
                    .repeat(0)
                    .onEnd {
                        reviews_frame.visibility = View.GONE
                    }
                    .playOn(reviews_frame)
            viewModel.uiState.isReviewsHidden = true
        }
    }

    private fun showReviewsDetails(){
        if (viewModel.uiState.isReviewsHidden){
            YoYo.with(Techniques.FadeIn)
                    .duration(500)
                    .delay(100)
                    .repeat(0)
                    .onEnd {
                        reviews_frame.visibility = View.VISIBLE
                    }
                    .playOn(reviews_frame)
            viewModel.uiState.isReviewsHidden = false
        }
    }

    private fun hideOtherDetails(){
        if (!viewModel.uiState.isOtherDetailsHidden){
            YoYo.with(Techniques.SlideOutUp)
                    .duration(500)
                    .repeat(0)
                    .onEnd {
                        top_details_frame.visibility = View.GONE
                    }
                    .playOn(top_details_frame)

            YoYo.with(Techniques.SlideOutUp)
                    .duration(500)
                    .repeat(0)
                    .onEnd {
                        tags_frame.visibility = View.GONE
                    }
                    .playOn(tags_frame)
            viewModel.uiState.isOtherDetailsHidden = true
        }
    }

    private fun showOtherDetails(){
        if (viewModel.uiState.isOtherDetailsHidden){
            YoYo.with(Techniques.SlideInDown)
                    .duration(500)
                    .repeat(0)
                    .onEnd {
                        top_details_frame.visibility = View.VISIBLE
                    }
                    .playOn(top_details_frame)

            YoYo.with(Techniques.SlideInDown)
                    .duration(500)
                    .repeat(0)
                    .onEnd {
                        tags_frame.visibility = View.VISIBLE
                    }
                    .playOn(tags_frame)
            viewModel.uiState.isOtherDetailsHidden = false
        }

    }

    fun onGymClicked(gym: Gym) {
        mapGym(gym)
        presenter.init()
    }

    fun mapGym(gym: Gym){
        viewModel.gym.id = gym.id
        viewModel.gym.name = gym.name
        viewModel.gym.helpline = gym.helpline
        viewModel.gym.location = gym.location
        viewModel.gym.email = gym.email
        viewModel.gym.pictures = gym.pictures
        viewModel.gym.rating = gym.rating
        viewModel.gym.rating_user_count = gym.rating_user_count
        viewModel.gym.tags = gym.tags
        viewModel.gym.reviews = gym.reviews
    }

    companion object {
        const val CALL_GYM_REQUEST_CODE = 0
        const val EMAIL_GYM_REQUEST_CODE = 1
    }
}
