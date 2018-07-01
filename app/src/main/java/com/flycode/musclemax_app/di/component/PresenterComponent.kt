package com.flycode.musclemax_app.di.component

import com.flycode.musclemax_app.di.module.DataModule
import com.flycode.musclemax_app.ui.base.UtilityWrapper
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [(DataModule::class)])
interface PresenterComponent {
    fun inject(utilityWrapper: UtilityWrapper)
}
