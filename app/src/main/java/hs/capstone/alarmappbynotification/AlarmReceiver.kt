package hs.capstone.alarmappbynotification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver: BroadcastReceiver() {
    private val TAG = "AlarmReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, intent?.action.toString())

        if (context != null && intent != null) {
            val alarmIntent = Intent(context, AlarmService::class.java)
            context.startService(alarmIntent)
            AlarmService().enqueueWork(context, intent)
        }

        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            Log.d(this.javaClass.toString(), "부팅 완료")
        }
    }

}