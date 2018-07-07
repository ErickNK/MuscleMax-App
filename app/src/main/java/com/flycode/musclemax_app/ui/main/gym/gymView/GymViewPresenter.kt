package com.flycode.musclemax_app.ui.main.gym.gymView

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat.startActivity
import android.widget.Toast
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.rx2.Rx2Apollo
import com.flycode.musclemax_app.*
import com.flycode.musclemax_app.data.models.Gym
import com.flycode.musclemax_app.data.models.Review
import com.flycode.musclemax_app.data.models.Tag
import com.flycode.musclemax_app.type.Review_Input
import com.flycode.musclemax_app.type.Tagged_Input
import com.flycode.musclemax_app.ui.base.BasePresenter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GymViewPresenter(
        val apolloClient: ApolloClient,
        val mainTagsAdapter: GymTagsAdapter,
        val gymReviewsAdapter: GymReviewsAdapter,
        val tagPickerTagsAdapter: GymTagsAdapter
): BasePresenter<GymViewFragment, GymViewPresenter, GymViewViewModel>(),
        GymViewContract.GymViewPresenter<GymViewFragment> {

    fun init() {
        fetchGymDetails()
    }

    private fun prepareGymDetails(): ApolloQueryCall<GetGymDetailsQuery.Data> {
        return apolloClient.query(
                GetGymDetailsQuery.builder()
                        .id(viewModel.gym.id.toString())
                        .build()
        )
    }

    private fun fetchGymDetails(){
        view?.let {view ->
            viewModel.uiState.isLoading = true
            compositeDisposable.add(
                    Rx2Apollo.from(this.prepareGymDetails())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe ({
                                it.data()?.gym()!![0].let {
                                    viewModel.gym = Gson().fromJson(
                                            Gson().toJson(it),
                                            Gym::class.java
                                    )
                                }
                                mainTagsAdapter.clear()
                                gymReviewsAdapter.clear()
                                mainTagsAdapter.addMultipleTags(viewModel.gym.tags)
                                gymReviewsAdapter.addMultipleReviews(viewModel.gym.reviews)
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

    fun callGym() {
        view?.let {view ->
            //TODO: save the country telephone code together with the national number of the user
            val countryCode = "254"
            if (ActivityCompat.checkSelfPermission(view.context!!,
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                //Creating intents for making a call
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:$countryCode${viewModel.gym.helpline}")
                view.startActivity(callIntent)

            } else {
                Toast.makeText(view.context!!, "We don't have permission.", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    fun emailGym(){
//        val intent = Intent(Intent.ACTION_SEND)//common intent
//        intent.data = Uri.parse("mailto:${viewModel.gym.email}") // only email apps should handle this
//        view?.startActivityForResult(intent,GymViewFragment.EMAIL_GYM_REQUEST_CODE)
//    }

    fun showGymDirections() {
        view?.let { view ->
            val gmmIntentUri = Uri.parse("google.navigation:q=${viewModel.gym.location?.latLng!!}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(view.activity?.packageManager) != null) {
                startActivity(view.context!!,mapIntent,null)
            }else{
                view.showError(view.resources.getString(R.string.no_google_maps)!!)
            }
        }
    }

    fun tagGym(tag: Tag) {
        view?.let {view ->
            viewModel.uiState.isLoading = true
            compositeDisposable.add(
                Rx2Apollo.from(apolloClient.mutate(
                    TagTaggableMutation.builder()
                        .tagged(Tagged_Input.builder()
                                .id(0.toString())
                                .tag_id(tag.id.toString())
                                .taggable_id(viewModel.gym.id.toString())
                                .taggable_type("gym")
                                .build()
                        )
                        .build()
                ))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    it.data()?.tagTaggable()?.let {
                        mainTagsAdapter.addTag(tag)
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
                            tagPickerTagsAdapter.addMultipleTags(tags)
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
                    ReviewGymMutation.builder()
                    .review(Review_Input.builder()
                            .id(0.toString())
                            .content(review.content)
                            .reviewable_id(viewModel.gym.id.toString())
                            .reviewable_type("gym")
                            .owner_id(1.toLong())
                            .build()
                    )
                    .build()
                ))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({
                        it.data()?.review()?.let {
                            val rev = Gson().fromJson(Gson().toJson(it),Review::class.java)
                            gymReviewsAdapter.addReview(rev)
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
        if(gymReviewsAdapter.listItems.isEmpty()){ //EMPTY
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
        if(mainTagsAdapter.tagList.isEmpty()){ //EMPTY
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