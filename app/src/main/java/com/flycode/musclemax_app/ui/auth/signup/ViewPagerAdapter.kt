package com.flycode.musclemax_app.ui.auth.signup

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

import com.flycode.musclemax_app.ui.auth.signup.DetailsOverview.DetailsOverviewFragment
import com.flycode.musclemax_app.ui.auth.signup.HeightDetails.HeightDetailsFragment
import com.flycode.musclemax_app.ui.auth.signup.PersonalDetails.PersonalDetailsFragment
import com.flycode.musclemax_app.ui.auth.signup.WeightDetails.WeightDetailsFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> return PersonalDetailsFragment.newInstance()
            1 -> return WeightDetailsFragment.newInstance()
            2 -> return HeightDetailsFragment.newInstance()
            3 -> return DetailsOverviewFragment.newInstance()
            else -> return null
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return null
    }

    override fun getCount(): Int {
        return 4
    }
}
