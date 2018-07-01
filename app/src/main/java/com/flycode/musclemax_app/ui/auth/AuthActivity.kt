package com.flycode.musclemax_app.ui.auth

import android.os.Bundle
import com.flycode.musclemax_app.R
import dagger.android.support.DaggerAppCompatActivity

class AuthActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }
}
