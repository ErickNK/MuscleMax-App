package com.flycode.musclemax_app.trackers

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

import com.flycode.musclemax_app.broadcastReceivers.ScreenLockReciever
import com.flycode.musclemax_app.trackers.filters.StepDetector

class StepTracker (
        val context: Context
) : SensorEventListener,
        ScreenLockReciever.OnScreenLockListener,
        LifecycleObserver{
    private var simpleStepDetector: StepDetector? = null
    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null
    var listener : StepDetector.StepListener? = null

    /*############################### SENSOR EVENT LISTENER ################################*/

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector!!.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]
            )
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    /*############################### SCREEN LOCK LISTENER ################################*/

    override fun onScreenLock() {
        //Try waking the accelerometer
        sensorManager?.unregisterListener(this)
        sensorManager?.registerListener(this,sensor,SensorManager.SENSOR_DELAY_FASTEST)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onInit() {
        //INIT MANAGER
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (sensorManager == null) {
            //TODO: errors
        }

        //INIT SENSOR
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (sensor == null) {
            //TODO: errors
        }

        //INIT FILTER
        simpleStepDetector = StepDetector()
        listener?.let {
            simpleStepDetector!!.registerListener(it)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        sensorManager!!.unregisterListener(this) //stop the sensor
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onPlay() {
        sensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST) //start the sensor
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        sensorManager = null
        sensor = null
        simpleStepDetector = null
    }


}
