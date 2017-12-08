@file:JvmName("AlarmsRepository")

package nerd.tuxmobil.fahrplan.congress.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import nerd.tuxmobil.fahrplan.congress.utils.getAlarmManager
import nerd.tuxmobil.fahrplan.congress.models.Alarm

@JvmOverloads
fun scheduleAlarm(context: Context, eventId: String, day: Int, title: String, startTime: Long, alarmTime: Long, discardExisting: Boolean = false) {
    val intent = AlarmReceiver.AlarmIntentBuilder()
            .setContext(context)
            .setLectureId(eventId)
            .setDay(day)
            .setTitle(title)
            .setStartTime(startTime)
            .setIsAddAlarm()
            .build()

    val requestCode = Integer.parseInt(eventId)
    val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
    val alarmManager = context.getAlarmManager()
    if (discardExisting) {
        alarmManager.cancel(pendingIntent)
    }
    alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
}

fun discardAlarm(context: Context, alarm: Alarm) {
    discardAlarm(context, alarm.eventId, alarm.day, alarm.eventTitle, alarm.time)
}

fun discardAlarm(context: Context, eventId: String, day: Int, title: String, startTime: Long) {
    val intent = AlarmReceiver.AlarmIntentBuilder()
            .setContext(context)
            .setLectureId(eventId)
            .setDay(day)
            .setTitle(title)
            .setStartTime(startTime)
            .setIsDeleteAlarm()
            .build()

    val requestCode = Integer.parseInt(eventId)
    val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
    context.getAlarmManager().cancel(pendingIntent)
}
