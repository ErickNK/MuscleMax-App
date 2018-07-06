package com.flycode.musclemax_app.ui.main.coach.coachOverview

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.rx2.Rx2Apollo
import com.flycode.musclemax_app.GetCoachesQuery
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.data.models.User
import com.flycode.musclemax_app.ui.base.BasePresenter
import com.flycode.musclemax_app.ui.flexibleItems.CoachesHeaderItem
import com.flycode.musclemax_app.ui.flexibleItems.CoachesListItem
import com.flycode.musclemax_app.ui.main.coach.coachView.CoachViewFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.ISectionable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CoachOverviewPresenter(
        val apolloClient: ApolloClient,
        val mainListAdapter: FlexibleAdapter<CoachesHeaderItem>
) : BasePresenter<CoachOverviewFragment, CoachOverviewPresenter, CoachOverviewViewModel>(),
        CoachOverviewContract.CoachOverviewPresenter<CoachOverviewFragment>   {

    private fun prepareFetchCoaches(): ApolloQueryCall<GetCoachesQuery.Data> {
        return apolloClient.query(GetCoachesQuery.builder().build())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun fetchCoaches(){
        view?.let {view ->
            viewModel.uiState.isLoading = true
            compositeDisposable.add(
                    Rx2Apollo.from(this.prepareFetchCoaches())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe ({
                                it.data()?.user()?.let {
                                    viewModel.coaches = Gson().fromJson(
                                            Gson().toJson(it),
                                            object : TypeToken<MutableList<User>>() {}.type
                                    )

                                    val coachesListItems =
                                            ArrayList<CoachesListItem>().apply {
                                                this.addAll(viewModel.coaches.map {
                                                    CoachesListItem(viewModel.allCoachesHeader,it,context = view.context).apply {
                                                        this.listener = view
                                                    }
                                                })
                                            }

                                    if (!coachesListItems.isEmpty()){
                                        viewModel.allCoachesHeader.subItems?.clear()
                                        viewModel.allCoachesHeader.addSubItems(0,coachesListItems as List<ISectionable<*, *>>)
                                        viewModel.allCoachesHeader.entries = coachesListItems.size
                                        mainListAdapter.notifyDataSetChanged()
                                        mainListAdapter.expandAll()
                                    }
                                    viewModel.uiState.isLoading = false
                                }
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

    fun onCoachClicked(coach: User) {
        val args = Bundle().apply {
            this.putString(CoachViewFragment.COACH,Gson().toJson(coach))
        }
        NavHostFragment.findNavController(view!!).navigate(R.id.CoachViewFragment,args)
    }
}