@file:JvmName("AlarmsDatabase")

package nerd.tuxmobil.fahrplan.congress.persistence

import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import nerd.tuxmobil.fahrplan.congress.persistence.FahrplanContract.AlarmsTable

fun openDatabase(context: Context): SQLiteDatabase =
        AlarmsDBOpenHelper(context.applicationContext).readableDatabase

inline fun SQLiteDatabase.inTransaction(func: SQLiteDatabase.() -> Unit) {
    beginTransaction()
    try {
        func()
        setTransactionSuccessful()
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        endTransaction()
    }
}

@JvmOverloads
fun SQLiteDatabase.readAlarms(chronologically: Boolean = true): Cursor {
    val orderBy = if (chronologically) AlarmsTable.Columns.TIME else null
    return query(
            AlarmsTable.NAME,
            AlarmsDBOpenHelper.allcolumns,
            null,
            null,
            null,
            null,
            orderBy)
}

fun SQLiteDatabase.deleteAlarm(id: String) = delete(
        AlarmsTable.NAME,
        AlarmsTable.Columns.ID + " = ?",
        arrayOf(id))

fun SQLiteDatabase.deleteAlarms() = delete(
        AlarmsTable.NAME,
        null,
        null)
