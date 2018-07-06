package com.flycode.musclemax_app.ui.main.gym.gymView

import android.arch.lifecycle.ViewModelProviders
import com.apollographql.apollo.ApolloClient
import com.flycode.timespace.di.scope.PerFragmentLevel1
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class GymViewModule {
    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            apolloClient: ApolloClient,
            @Named("main_tag_adapter") tagsAdapter: GymTagsAdapter,
            @Named("tag_picker_tag_adapter") tagPickerTagsAdapter: GymTagsAdapter,
            gymReviewsAdapter: GymReviewsAdapter
    ): GymViewPresenter = GymViewPresenter(
            apolloClient = apolloClient,
            mainTagsAdapter = tagsAdapter,
            tagPickerTagsAdapter = tagPickerTagsAdapter,
            gymReviewsAdapter = gymReviewsAdapter
    )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            GymViewFragment: GymViewFragment,
            GymViewPresenter: GymViewPresenter
    ): GymViewViewModel {
        val viewModel = ViewModelProviders.of(GymViewFragment).get(GymViewViewModel::class.java)
        GymViewPresenter.viewModel = viewModel
        viewModel.presenter = GymViewPresenter
        return viewModel
    }


    @Provides
    @PerFragmentLevel1
    fun provideGymImagesPager(
            gymViewFragment: GymViewFragment
    ): GymReviewsAdapter = GymReviewsAdapter(
            context = gymViewFragment.context!!
    )

    @Named("main_tag_adapter")
    @Provides
    @PerFragmentLevel1
    fun provideTagAdapter(
            gymViewFragment: GymViewFragment
    ): GymTagsAdapter = GymTagsAdapter(
            context = gymViewFragment.context!!
    )

    @Named("tag_picker_tag_adapter")
    @Provides
    @PerFragmentLevel1
    fun provideTagPickerTagAdapter(
            gymViewFragment: GymViewFragment
    ): GymTagsAdapter = GymTagsAdapter(
            context = gymViewFragment.context!!
    )
}
