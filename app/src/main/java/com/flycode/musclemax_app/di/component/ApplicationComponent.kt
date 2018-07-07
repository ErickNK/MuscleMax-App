package com.flycode.musclemax_app.di.component

import android.app.Application
import com.flycode.musclemax_app.di.module.AppModule
import com.flycode.musclemax_app.di.module.BindingsModule
import com.flycode.musclemax_app.di.module.DataModule
import com.flycode.musclemax_app.di.module.GoogleModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    (AndroidSupportInjectionModule::class),
    (BindingsModule::class),
    (AppModule::class),
    (DataModule::class),
    (GoogleModule::class)
])
interface ApplicationComponent : AndroidInjector<DaggerApplication> {
    override fun inject(instance: DaggerApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }
}