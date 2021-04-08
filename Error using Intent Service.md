# Error: using Intent Service

본래 코드는 다음과 같습니다.

여기서 IntentService를 더이상 지원하지 않는다?고 하는 오류가 떴습니다.

```kotlin
JobIntentServicepackage hs.capstone.alarmappbynotification

import android.app.IntentService
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmService: IntentService("AlarmService") {
    private var notiMgr: NotificationManagerCompat ?= null

    override fun onHandleIntent(intent: Intent?) {
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

//        startForeground(1, builder.build())

        notiMgr = NotificationManagerCompat.from(this)
        notiMgr!!.notify(notiId, builder.build())
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(this.javaClass.toString(), "알림 서비스 종료")
    }
}
```

그래서 JobIntentService 로 변환하는 중입니다.

하지만 아직 오류가 있네요,,