package com.flycode.musclemax_app.ui.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.databinding.BaseActivityBinding
import com.flycode.musclemax_app.databinding.BaseNavDrawerHeadingBindings
import com.flycode.musclemax_app.ui.base.BaseActivity
import com.squareup.picasso.Picasso
import javax.inject.Inject

class MainActivity : BaseActivity<MainActivity, MainPresenter, MainViewModel>()
        , MainContract.MainActivity {

    //Todo: find a way to make non null completely.
    @Inject override lateinit var viewModel: MainViewModel
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var baseActivityBinding : BaseActivityBinding
    private lateinit var baseNavDrawerHeadingBindings : BaseNavDrawerHeadingBindings

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        baseActivityBinding =  DataBindingUtil.setContentView(this, R.layout.activity_base_nav)
        baseActivityBinding.viewModel = viewModel

        //DRAWER HEADING
        baseNavDrawerHeadingBindings = DataBindingUtil
                .inflate(
                        layoutInflater,
                        R.layout.base_navigation_drawer_header,
                        null,
                        false
                )
        baseNavDrawerHeadingBindings.viewModel = viewModel

        //DRAWER LISTENER
        actionBarDrawerToggle = ActionBarDrawerToggle(
                this,
                baseActivityBinding.baseDrawerLayout,
                R.string.navigation_open,
                R.string.navigation_close
        )
        baseActivityBinding.baseDrawerLayout.addDrawerListener(actionBarDrawerToggle)

        //NAV VIEW
        baseActivityBinding.baseNavView.addHeaderView(baseNavDrawerHeadingBindings.root)
        baseActivityBinding.baseNavView.setupWithNavController(findNavController(R.id.base_nav_fragment))
    }

    override fun onSupportNavigateUp() = findNavController(R.id.base_nav_fragment).navigateUp()

    public override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        actionBarDrawerToggle.syncState()
    }

    fun toggleDrawer() {
        if (baseActivityBinding.baseDrawerLayout.isDrawerOpen(baseActivityBinding.baseNavView)) {
            baseActivityBinding.baseDrawerLayout.closeDrawer(baseActivityBinding.baseNavView)
        } else
            baseActivityBinding.baseDrawerLayout.openDrawer(baseActivityBinding.baseNavView)
    }

    fun loadUserProfilePicture(local_location: String){
        Picasso.get()
            .load(local_location)
            .into(baseNavDrawerHeadingBindings.profileImage)
    }
}
