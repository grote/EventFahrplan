package nerd.tuxmobil.fahrplan.congress.alarms

import android.app.Activity
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import nerd.tuxmobil.fahrplan.congress.R
import nerd.tuxmobil.fahrplan.congress.base.BaseActivity
import nerd.tuxmobil.fahrplan.congress.models.Alarm
import nerd.tuxmobil.fahrplan.congress.persistence.*
import nerd.tuxmobil.fahrplan.congress.persistence.FahrplanContract.AlarmsTable.Columns.*
import nerd.tuxmobil.fahrplan.congress.schedule.FahrplanFragment
import java.util.Collections.emptyList


class AlarmsActivity : BaseActivity() {

    private var adapter: AlarmsAdapter = AlarmsAdapter(emptyList(), {})
    private lateinit var alarmsView: RecyclerView
    private lateinit var database: SQLiteDatabase

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        database = openDatabase(this)
        updateAlarmsView()
        setResult(Activity.RESULT_CANCELED)
    }

    override fun onDestroy() {
        super.onDestroy()
        database.close()
    }

    private fun init() {
        setContentView(R.layout.alarms)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val actionBarColor = ContextCompat.getColor(this, R.color.colorActionBar)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(actionBarColor))
        alarmsView = findViewById(R.id.alarms_view)
        alarmsView.setHasFixedSize(true)
        alarmsView.layoutManager = LinearLayoutManager(this)
        alarmsView.adapter = adapter
    }

    private fun updateAlarmsView() {
        val alarms = loadAlarms()
        adapter = AlarmsAdapter(alarms, {
            onDeleteAlarm(it)
        })
        alarmsView.adapter = adapter
    }

    private fun updateFahrplanView() {
        FahrplanFragment.loadAlarms(this)
    }

    private fun loadAlarms(): List<Alarm> {
        var cursor: Cursor? = null
        database.inTransaction {
            cursor = readAlarms()
        }
        if (cursor == null) {
            return emptyList()
        }

        val alarms: ArrayList<Alarm> = ArrayList()
        val nonNullCursor = cursor!!
        nonNullCursor.moveToFirst()
        while (!nonNullCursor.isAfterLast) {
            val alarm = getAlarm(nonNullCursor)
            alarms.add(alarm)
            nonNullCursor.moveToNext()
        }
        cursor!!.close()
        return alarms
    }

    private fun getAlarm(cursor: Cursor): Alarm {
        val id = cursor.getString(cursor.getColumnIndex(ID))
        val alarmTimeInMin = cursor.getInt(cursor.getColumnIndex(ALARM_TIME_IN_MIN))
        val title = cursor.getString(cursor.getColumnIndex(EVENT_TITLE))
        val time = cursor.getLong(cursor.getColumnIndex(TIME))
        val timeText = cursor.getString(cursor.getColumnIndex(TIME_TEXT))
        val eventId = cursor.getString(cursor.getColumnIndex(EVENT_ID))
        val displayTime = cursor.getLong(cursor.getColumnIndex(DISPLAY_TIME))
        val day = cursor.getInt(cursor.getColumnIndex(DAY))
        return Alarm(title, alarmTimeInMin, time, timeText, eventId, displayTime, day, id)
    }

    private fun onDeleteAlarm(alarm: Alarm) {
        discardAlarm(this, alarm)
        database.inTransaction {
            deleteAlarm(alarm.id)
        }
        updateAlarmsView()
        updateFahrplanView()
    }

    private fun onDeleteAlarms() {
        var alarms = emptyList<Alarm>()
        database.inTransaction {
            alarms = loadAlarms()
        }
        for (alarm in alarms) {
            discardAlarm(this, alarm)
        }
        database.inTransaction {
            deleteAlarms()
        }
        updateAlarmsView()
        updateFahrplanView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.alarms_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.alarms_delete -> {
                onDeleteAlarms()
                setResult(Activity.RESULT_OK)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
