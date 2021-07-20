package com.ajayh.aidl.sdk

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Process
import com.ajayh.aidl.IRemoteService

class IRemoteSensorService : Service() {

    override fun onBind(intent: Intent): IBinder {
        SensorData.initSensor(this)
        return binder
    }

    private val binder = object : IRemoteService.Stub() {
        override fun getPid(): Int {
            return Process.myPid()
        }

        override fun getPhoneOrientation(): String? {
            return SensorData.getSensorStatus()
        }
    }
}