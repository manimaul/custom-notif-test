package com.willkamp.customnotificationtest;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MyActivity extends Activity {
    private static final String BUTTON_CLICK = "com.willkamp.customnotificationtest.BUTTON_CLICK";

    private BroadcastReceiver mBroadcastReceiver;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "button clicked", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mBroadcastReceiver, new IntentFilter(BUTTON_CLICK));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mBroadcastReceiver);
    }

    public void onAddNoteClick(View pView) {
        //remote view to to add to notification
        RemoteViews rmVs = new RemoteViews("com.willkamp.customnotificationtest", R.layout.custnotif);

        Intent nIntent = new Intent(BUTTON_CLICK);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, nIntent, 0);

        rmVs.setOnClickPendingIntent(R.id.button, pi);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.follownu)
                        .setContent(rmVs);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MyActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MyActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // mId (my id)  allows you to update the notification later on.
        int mId = 1234;
        mNotificationManager.notify(mId, mBuilder.build());
    }
}
