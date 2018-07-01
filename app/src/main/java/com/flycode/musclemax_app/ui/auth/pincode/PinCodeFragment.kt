package com.flycode.musclemax_app.ui.auth.pincode


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flycode.musclemax_app.R
import dagger.android.support.DaggerFragment


class PinCodeFragment : DaggerFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pin_code, container, false)
    }


}
