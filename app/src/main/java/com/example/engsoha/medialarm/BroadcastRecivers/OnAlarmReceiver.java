package com.example.engsoha.medialarm.BroadcastRecivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.util.Log;

import com.example.engsoha.medialarm.Data.DatabaseHelper;
import com.example.engsoha.medialarm.Activities.AlarmActivity;
import com.example.engsoha.medialarm.WakeReminderIntentService;

public class OnAlarmReceiver extends BroadcastReceiver {
    private static final String TAG = ComponentInfo.class.getCanonicalName();
    DatabaseHelper databaseHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received wake up from alarm manager.");

        long rowid = intent.getExtras().getLong(DatabaseHelper.KEY_ID);

        WakeReminderIntentService.acquireStaticLock(context);

        databaseHelper = new DatabaseHelper(context);
        String status = databaseHelper.checkMedicineById(rowid);
        //if Medicine is not exist in database don't fire alarm
        if (status == "Existed") {
            Intent notificationIntent = new Intent(context, AlarmActivity.class);
            notificationIntent.putExtra(DatabaseHelper.KEY_ID, rowid);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(notificationIntent);
        }


    }
}
