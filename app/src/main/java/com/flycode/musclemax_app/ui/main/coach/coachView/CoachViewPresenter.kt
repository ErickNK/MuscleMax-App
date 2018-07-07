package com.flycode.musclemax_app.ui.main.coach.coachView

import android.Manifest
import android.content.Intent
import android.net.Uri
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.rx2.Rx2Apollo
import com.flycode.musclemax_app.*
import com.flycode.musclemax_app.data.models.Review
import com.flycode.musclemax_app.data.models.Tag
import com.flycode.musclemax_app.data.models.User
import com.flycode.musclemax_app.type.Review_Input
import com.flycode.musclemax_app.type.Tagged_Input
import com.flycode.musclemax_app.ui.base.BasePresenter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CoachViewPresenter(
        val apolloClient: ApolloClient,
        val mainCoachTagsAdapter: CoachTagsAdapter,
        val tagPickerCoachTagsAdapter: CoachTagsAdapter,
        val coachReviewsAdapter: CoachReviewsAdapter
) : BasePresenter<CoachViewFragment, CoachViewPresenter, CoachViewViewModel>(),
        CoachViewContract.CoachViewPresenter<CoachViewFragment>  {


    fun init(){
        if (!viewModel.coach.pictures.isEmpty())
            view?.loadImage(viewModel.coach.pictures[0].remote_location)
        else view?.setImageDrawable()
        fetchGymDetails()
    }

    private fun prepareUserDetails(): ApolloQueryCall<GetUserDetailsQuery.Data> {
        return apolloClient.query(
                GetUserDetailsQuery.builder()
                        .id(viewModel.coach.id.toString())
                        .type("coach")
                        .build()
        )
    }

    private fun fetchGymDetails(){
        view?.let {view ->
            viewModel.uiState.isLoading = true
            compositeDisposable.add(
                    Rx2Apollo.from(this.prepareUserDetails())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe ({
                                it.data()?.user()!![0].let {
                                    viewModel.coach = Gson().fromJson(
                                            Gson().toJson(it),
                                            User::class.java
                                    )
                                }
                                mainCoachTagsAdapter.clear()
                                coachReviewsAdapter.clear()
                                mainCoachTagsAdapter.addMultipleTags(viewModel.coach.tags)
                                coachReviewsAdapter.addMultipleReviews(viewModel.coach.reviews)
                                checkEmptyTags()
                                checkEmptyReviews()
                                viewModel.uiState.isLoading = false
                            },{
                                viewModel.uiState.isLoading = false
                                viewModel.uiState.onError = true
                                if (it.message != null){
                                    view.showError(it.message.toString())
                                }else{
                                    view.showError(view.resources.getString(R.string.something_went_wrong))
                                }
                            })
            )
        }
    }

    fun onCallPermissionsGranted(){
        view?.let {view ->
            //Creating intents for making a call
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:${viewModel.coach.tel}")
            view.startActivity(callIntent)

        }
    }

    fun callCoach() {
        view?.let {view ->
            view.requestAppPermissions(
                    arrayOf(
                            Manifest.permission.CALL_PHONE
                    ),
                    R.string.we_need_permission_to_function, view.CALL_PHONE_PERMISSION_REQUEST_CODE)
        }
    }

    fun emailCoach(){
        val intent = Intent(Intent.ACTION_SEND)//common intent
        intent.data = Uri.parse("mailto:${viewModel.coach.email}") // only email apps should handle this
        view?.startActivity(intent)
    }

    fun tagCoach(tag: Tag) {
        view?.let {view ->
            viewModel.uiState.isLoading = true
            compositeDisposable.add(
                    Rx2Apollo.from(apolloClient.mutate(
                            TagTaggableMutation.builder()
                                    .tagged(Tagged_Input.builder()
                                            .id(0.toString())
                                            .tag_id(tag.id.toString())
                                            .taggable_id(viewModel.coach.id.toString())
                                            .taggable_type("Coach")
                                            .build()
                                    )
                                    .build()
                    ))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe ({
                                it.data()?.tagTaggable()?.let {
                                    mainCoachTagsAdapter.addTag(tag)
                                }
                                checkEmptyTags()
                                viewModel.uiState.isLoading = false
                            },{
                                viewModel.uiState.isLoading = false
                                viewModel.uiState.onError = true
                                if (it.message != null){
                                    view.showError(it.message.toString())
                                }else{
                                    view.showError(view.resources.getString(R.string.something_went_wrong))
                                }
                            })
            )
        }
    }

    fun fetchTags() {
        view?.let {view ->
            viewModel.uiState.isTagsLoading = true
            compositeDisposable.add(
                    Rx2Apollo.from(apolloClient.query(
                            GetUserTagsQuery.builder().build()))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe ({
                                it.data()?.user()!![0]?.owned_tags()?.let {
                                    val tags = Gson().fromJson<List<Tag>>(Gson().toJson(it),
                                            object : TypeToken<List<Tag>>(){}.type
                                    )
                                    tagPickerCoachTagsAdapter.addMultipleTags(tags)
                                }
                                viewModel.uiState.isTagsLoading = false
                            },{
                                viewModel.uiState.isTagsLoading = false
                                viewModel.uiState.onTagsError = true
                                if (it.message != null){
                                    view.showError(it.message.toString())
                                }else{
                                    view.showError(view.resources.getString(R.string.something_went_wrong))
                                }
                            })
            )
        }
    }

    fun addReview(review: Review) {
        view?.let {view ->
            viewModel.uiState.isLoading = true
            compositeDisposable.add(
                    Rx2Apollo.from(apolloClient.mutate(
                            ReviewCoachMutation.builder()
                                    .review(Review_Input.builder()
                                            .id(0.toString())
                                            .content(review.content)
                                            .reviewable_id(viewModel.coach.id.toString())
                                            .reviewable_type("Coach")
                                            .owner_id(1.toLong())
                                            .build()
                                    )
                                    .build()
                    ))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe ({
                                it.data()?.review()?.let {
                                    val rev = Gson().fromJson(Gson().toJson(it), Review::class.java)
                                    coachReviewsAdapter.addReview(rev)
                                }
                                checkEmptyReviews()
                                viewModel.uiState.isLoading = false
                            },{
                                viewModel.uiState.isLoading = false
                                viewModel.uiState.onError = true
                                if (it.message != null){
                                    view.showError(it.message.toString())
                                }else{
                                    view.showError(view.resources.getString(R.string.something_went_wrong))
                                }
                            })
            )
        }
    }

    private fun checkEmptyReviews(){
        if(coachReviewsAdapter.listItems.isEmpty()){ //EMPTY
            //SHOW NO REVIEWS
            if (viewModel.uiState.isEmptyReviewsHidden){
                viewModel.uiState.isEmptyReviewsHidden = false
            }

        }else{ //NOT EMPTY
            //HIDE EMPTY REVIEWS
            if (!viewModel.uiState.isEmptyReviewsHidden){
                viewModel.uiState.isEmptyReviewsHidden = true
            }
        }
    }

    private fun checkEmptyTags(){
        if(mainCoachTagsAdapter.tagList.isEmpty()){ //EMPTY
            //SHOW NO TAGS
            if (viewModel.uiState.isEmptyTagsHidden){
                viewModel.uiState.isEmptyTagsHidden = false
            }

        }else{ //NOT EMPTY
            //HIDE EMPTY TAGS
            if (!viewModel.uiState.isEmptyTagsHidden){
                viewModel.uiState.isEmptyTagsHidden = true
            }
        }
    }
}