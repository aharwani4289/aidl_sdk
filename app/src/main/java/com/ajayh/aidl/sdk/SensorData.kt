package com.ajayh.aidl.sdk

import android.app.Service
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

object SensorData {

    private var mSensorManager: SensorManager? = null
    private var mRotationSensor: Sensor? = null
    private const val SENSOR_DELAY = 8 * 1000
    private var message: String? = null

    fun initSensor(context: Context) {
        try {
            mSensorManager = context.getSystemService(Service.SENSOR_SERVICE) as SensorManager
            mRotationSensor = mSensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            mSensorManager?.registerListener(sensorEventListener, mRotationSensor, SENSOR_DELAY)
        } catch (e: Exception) {

        }
    }

    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
                val rotationMatrix = FloatArray(16)
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

                // Remap coordinate system
                val remappedRotationMatrix = FloatArray(16)
                SensorManager.remapCoordinateSystem(
                    rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z,
                    remappedRotationMatrix
                )

                // Convert to orientations
                val orientations = FloatArray(3)
                SensorManager.getOrientation(remappedRotationMatrix, orientations)

                // Convert values in radian to degrees
                for (i in 0..2) {
                    orientations[i] = Math.toDegrees(orientations[i].toDouble()).toFloat()
                }

                // Optionally convert the result from radians to degrees
                orientations[0] = Math.toDegrees(orientations[0].toDouble()).toFloat()
                orientations[1] = Math.toDegrees(orientations[1].toDouble()).toFloat()
                orientations[2] = Math.toDegrees(orientations[2].toDouble()).toFloat()

                message = "Yaw: $orientations[0]  Pitch: $orientations[1] Roll (not used): $orientations[2]".trimMargin()
                Log.i("Ajay", message.toString())
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }
    }

    fun getSensorStatus(): String? {
        return message
    }
}