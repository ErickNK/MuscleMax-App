package com.flycode.musclemax_app.ui.pincode

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.ui.main.MainActivity
import com.goodiebag.pinview.Pinview
import kotlinx.android.synthetic.main.activity_pin_code.*

class PinCodeActivity : AppCompatActivity(), Pinview.PinViewEventListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_code)
        init()
    }

    private fun init(){
        pinView.setPinViewEventListener(this)
    }

    override fun onDataEntered(p0: Pinview?, p1: Boolean) {
        Toast.makeText(this,pinView.value,Toast.LENGTH_LONG).show();
        startActivity(Intent(this, MainActivity::class.java))
    }
}
