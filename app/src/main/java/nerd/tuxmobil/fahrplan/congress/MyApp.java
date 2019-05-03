package nerd.tuxmobil.fahrplan.congress;

import android.app.Application;
import android.util.Log;
import android.util.SparseIntArray;

import org.ligi.tracedroid.TraceDroid;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import nerd.tuxmobil.fahrplan.congress.models.DateInfos;
import nerd.tuxmobil.fahrplan.congress.models.Lecture;
import nerd.tuxmobil.fahrplan.congress.models.Meta;
import nerd.tuxmobil.fahrplan.congress.utils.ConferenceTimeFrame;

public class MyApp extends Application {

    public static Application app = null;

    public static boolean DEBUG = false;

    public static List<Lecture> lectureList = null;

    public static Meta meta;

    public static DateInfos dateInfos = null;

    private static long first_day_start = getMilliseconds("America/Sao_Paulo",
            BuildConfig.SCHEDULE_FIRST_DAY_START_YEAR,
            BuildConfig.SCHEDULE_FIRST_DAY_START_MONTH,
            BuildConfig.SCHEDULE_FIRST_DAY_START_DAY);

    private static long last_day_end = getMilliseconds("America/Sao_Paulo",
            BuildConfig.SCHEDULE_LAST_DAY_END_YEAR,
            BuildConfig.SCHEDULE_LAST_DAY_END_MONTH,
            BuildConfig.SCHEDULE_LAST_DAY_END_DAY);

    public static final ConferenceTimeFrame conferenceTimeFrame =
            new ConferenceTimeFrame(first_day_start, last_day_end);

    public static int room_count = 0;

    public static HashMap<String, Integer> roomsMap = new HashMap<>();

    public enum TASKS {
        NONE,
        FETCH,
        PARSE,
        FETCH_CANCELLED
    }

    // requestCodes für startActivityForResult
    final public static int ALARMLIST = 1;
    final public static int EVENTVIEW = 2;
    final public static int CHANGELOG = 3;
    final public static int STARRED = 4;
    final public static int SETTINGS = 5;

    public static TASKS task_running = TASKS.NONE;

    public static String fahrplan_xml;

    public static int lectureListDay = 0;

    public static SparseIntArray roomList = new SparseIntArray();

    @Override
    public void onCreate() {
        super.onCreate();
        TraceDroid.init(this);
        app = this;
        task_running = TASKS.NONE;
        lectureList = null;
    }

    private static long getMilliseconds(String timeZoneId, int year, int month, int day) {
        TimeZone zone = TimeZone.getTimeZone(timeZoneId);
        Calendar calendar = new GregorianCalendar(zone);
        int zeroBasedMonth = month - 1;
        calendar.set(year, zeroBasedMonth, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static void LogDebug(String tag, String message) {
        if (DEBUG) {
            Log.d(tag, message);
        }
    }
}
