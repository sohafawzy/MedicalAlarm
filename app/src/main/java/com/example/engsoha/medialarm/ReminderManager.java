package com.example.engsoha.medialarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.engsoha.medialarm.BroadcastRecivers.OnAlarmReceiver;
import com.example.engsoha.medialarm.Data.DatabaseHelper;

import java.util.Calendar;

/**
 * Created by engsoha on 10/12/16.
 */
public class ReminderManager {
    private Context mContext;
    private AlarmManager mAlarmManager;
    DatabaseHelper databaseHelper;
    String interval;

    public ReminderManager(Context context) {
        mContext = context;
        mAlarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setReminder(Long taskId, Calendar when) {
        databaseHelper = new DatabaseHelper(mContext);
        interval = databaseHelper.getMedicineDataById(taskId).trim();
        int interval_int = Integer.parseInt(interval);
        Intent i = new Intent(mContext, OnAlarmReceiver.class);
        i.putExtra(DatabaseHelper.KEY_ID, (long) taskId);
        PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i,
                PendingIntent.FLAG_ONE_SHOT);

        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), interval_int * 60 * 1000, pi);
  }

}