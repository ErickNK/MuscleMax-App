package com.flycode.musclemax_app

import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.stetho.Stetho
import com.flycode.musclemax_app.di.component.ApplicationComponent
import com.flycode.musclemax_app.di.component.DaggerApplicationComponent
import com.raizlabs.android.dbflow.config.FlowManager
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class MuscleMaxApplication: DaggerApplication() {
    override fun onCreate() {
        super.onCreate()
        //Initialize
        FlowManager.init(this)
        Stetho.initializeWithDefaults(this)
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
    }

    override fun applicationInjector() : AndroidInjector<DaggerApplication>? {
        val appComponent : ApplicationComponent =
                DaggerApplicationComponent.builder().application(this).build()
        appComponent.inject(this)
        return appComponent
    }
}