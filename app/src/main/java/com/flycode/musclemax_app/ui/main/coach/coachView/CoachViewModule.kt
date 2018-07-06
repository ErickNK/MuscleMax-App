package com.flycode.musclemax_app.ui.main.coach.coachView

import android.arch.lifecycle.ViewModelProviders
import com.apollographql.apollo.ApolloClient
import com.flycode.timespace.di.scope.PerFragmentLevel1
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class CoachViewModule {
    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            apolloClient: ApolloClient,
            @Named("main_tag_adapter") coachTagsAdapter: CoachTagsAdapter,
            @Named("tag_picker_tag_adapter") tagPickerCoachTagsAdapter: CoachTagsAdapter,
            coachReviewsAdapter: CoachReviewsAdapter
    ): CoachViewPresenter = CoachViewPresenter(
            apolloClient = apolloClient,
            mainCoachTagsAdapter = coachTagsAdapter,
            tagPickerCoachTagsAdapter = tagPickerCoachTagsAdapter,
            coachReviewsAdapter = coachReviewsAdapter
    )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            CoachViewFragment: CoachViewFragment,
            CoachViewPresenter: CoachViewPresenter
    ): CoachViewViewModel {
        val viewModel = ViewModelProviders.of(CoachViewFragment).get(CoachViewViewModel::class.java)
        CoachViewPresenter.viewModel = viewModel
        viewModel.presenter = CoachViewPresenter
        return viewModel
    }

    @Provides
    @PerFragmentLevel1
    fun provideGymImagesPager(
            CoachViewFragment: CoachViewFragment
    ): CoachReviewsAdapter = CoachReviewsAdapter(
            context = CoachViewFragment.context!!
    )

    @Named("main_tag_adapter")
    @Provides
    @PerFragmentLevel1
    fun provideTagAdapter(
            CoachViewFragment: CoachViewFragment
    ): CoachTagsAdapter = CoachTagsAdapter(
            context = CoachViewFragment.context!!
    )

    @Named("tag_picker_tag_adapter")
    @Provides
    @PerFragmentLevel1
    fun provideTagPickerTagAdapter(
            CoachViewFragment: CoachViewFragment
    ): CoachTagsAdapter = CoachTagsAdapter(
            context = CoachViewFragment.context!!
    )
}