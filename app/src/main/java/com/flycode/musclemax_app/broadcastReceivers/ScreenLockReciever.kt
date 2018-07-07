package com.flycode.musclemax_app.broadcastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ScreenLockReciever : BroadcastReceiver() {

    private val listener: OnScreenLockListener? = null

    override fun onReceive(context: Context, intent: Intent) {
        listener?.onScreenLock()
    }

    interface OnScreenLockListener {
        fun onScreenLock()
    }
}
