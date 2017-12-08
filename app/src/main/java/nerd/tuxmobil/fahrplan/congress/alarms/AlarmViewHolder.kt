package nerd.tuxmobil.fahrplan.congress.alarms

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import nerd.tuxmobil.fahrplan.congress.R
import nerd.tuxmobil.fahrplan.congress.models.Alarm
import nerd.tuxmobil.fahrplan.congress.persistence.FahrplanContract.AlarmsTable.Defaults.ALARM_TIME_IN_MIN_DEFAULT

class AlarmViewHolder(

        val view: View,
        private val onDelete: (alarm: Alarm) -> Unit

) :

        RecyclerView.ViewHolder(view) {

    private val badgeView by lazy { view.findViewById<TextView>(R.id.alarm_badge) }
    private val titleView by lazy { view.findViewById<TextView>(R.id.alarm_title) }
    private val startTimeView by lazy { view.findViewById<TextView>(R.id.alarm_start_time) }
    private val deleteAlarmView by lazy { view.findViewById<ImageButton>(R.id.alarm_delete) }

    fun bind(alarm: Alarm) {
        view.tag = alarm
        if (alarm.alarmTimeInMin == ALARM_TIME_IN_MIN_DEFAULT) {
            badgeView.text = "?"
        } else {
            val text = "${alarm.alarmTimeInMin}"
            badgeView.text = text
        }
        titleView.text = alarm.eventTitle
        startTimeView.text = alarm.timeText
        deleteAlarmView.setOnClickListener {
            onDelete.invoke(alarm)
        }
    }

}
