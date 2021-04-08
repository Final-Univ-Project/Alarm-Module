package hs.capstone.alarmappbynotification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmService: JobIntentService() {
    private val TAG = "AlarmService"
    private var notiMgr: NotificationManagerCompat ?= null

    companion object {
        const val JOB_ID = R.string.JOB_ID
    }

    fun enqueueWork(context: Context, work: Intent) {
        enqueueWork(context, AlarmService::class.java, JOB_ID, work)
    }

    override fun onHandleWork(intent: Intent) {
        val chId: String = getString(R.string.CHANNEL_ID)
        val notiId: Int = R.string.NOTI_ID

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
                this, 0, intent, 0)
        // 알림 Contents와 Channel 설정
        var builder = NotificationCompat.Builder(this, chId)
                .setSmallIcon(R.drawable.alarm_icon)
                .setContentTitle("운동 앱")
                .setContentText("운동하세요!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        notiMgr = NotificationManagerCompat.from(this)
        notiMgr!!.notify(notiId, builder.build())
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "알림 서비스 종료")
    }
}