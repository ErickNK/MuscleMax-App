package com.flycode.musclemax_app.broadcastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class ServiceStartedReceiver : BroadcastReceiver() {

    var listener: OnServiceStartedListener? = null

    override fun onReceive(context: Context, intent: Intent) {
        listener!!.onServiceStarted()
    }

    interface OnServiceStartedListener {
        fun onServiceStarted()
    }
}
