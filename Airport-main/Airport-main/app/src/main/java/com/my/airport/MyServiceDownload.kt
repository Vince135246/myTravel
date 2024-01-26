package com.my.airport

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.util.Date
import java.util.Timer
import java.util.TimerTask

class MyServiceDownload : Service() {

    private val binder = LocalBinder()
    var task = MyTimerTask("VinceTask\n")
    class MyTimerTask(val str:String) : TimerTask() {
        override fun run() {
            println(str)
        }
    }
    override fun onBind(p0: Intent?): IBinder  {
        Timer().schedule(task, Date(), 1000 * 60 * 3)
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    fun getServiceName():String{
        return MyServiceDownload::class.java.name
    }

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): MyServiceDownload = this@MyServiceDownload
    }


}