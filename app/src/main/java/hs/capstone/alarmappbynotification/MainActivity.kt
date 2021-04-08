package hs.capstone.alarmappbynotification

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import java.util.*

class MainActivity: Activity() {
    private val TAG = "MainActivity"

    lateinit var alarm_setTime: TimePicker
    lateinit var alarm_cancel: Button
    lateinit var alarm_set: Button

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    private fun setAlarm(pickTime: Calendar) {
        // 알림 설정 방법
        // 1. set() : 일회성?
        // 2. setInexactRepeating() : 끌때까지 반복? (안끄면 10분마다)
        // 3. setWindow() : ?

        alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this, R.string.alarm_requestId, intent, 0)
        }
        alarmMgr?.set(
                AlarmManager.RTC_WAKEUP,
                pickTime.timeInMillis,
                alarmIntent
        )
//            alarmMgr?.setInexactRepeating(
//                    AlarmManager.RTC_WAKEUP,
//                    calendar.timeInMillis,
//                    1000*60*10,
//                    alarmIntent
//            )
        Log.d(TAG, "알림을 설정함")
    }
    private fun removeAlarm() {
        alarmMgr = getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        alarmIntent = PendingIntent.getService(
                this,
                R.string.alarm_requestId,
                intent,
                PendingIntent.FLAG_NO_CREATE
        )

        alarmMgr?.cancel(alarmIntent)
        alarmIntent.cancel()
    }

    // 기존 알림 채널을 만들면 아무작업도 실행되지 않으므로
    // 앱을 시작할 때 이를 호출하는 것이 안전
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chId = getString(R.string.CHANNEL_ID)
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(chId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        alarm_setTime = findViewById(R.id.alarm_setTime)
        alarm_cancel = findViewById(R.id.alarm_cancel)
        alarm_set = findViewById(R.id.alarm_set)

        // App 채널 생성
        createNotificationChannel()

        alarm_set.setOnClickListener {
            // 1. 현재 시각 가져오기
            //   1) year: Int = timeCont.get(Calender.YEAR)
            //   2) System.currentTimeMillis()

            // 2. 트리거 시간 설정
            // Calender객체로 pick한 시간 저장

//            1)
//            val timeCont = Calendar.getInstance()
//            timeCont.set(Calendar.YEAR, 2021)  // default: 현재시각
//            timeCont.set(Calendar.MONTH, 4)
//            timeCont.set(Calendar.DAY_OF_MONTH, 2)
//            timeCont.set(Calendar.HOUR_OF_DAY, alarm_setTime.hour)
//            timeCont.set(Calendar.MINUTE, alarm_setTime.minute)
//            timeCont.set(Calendar.SECOND, 0)

//            2)
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, alarm_setTime.hour)
                set(Calendar.MINUTE, alarm_setTime.minute)
            }

            setAlarm(calendar)

            Log.d(TAG, "알림 확인 버튼 누름")
        }

        alarm_cancel.setOnClickListener {
            removeAlarm()

            Log.d(TAG, "알림 취소 버튼 누름")
        }
    }
}