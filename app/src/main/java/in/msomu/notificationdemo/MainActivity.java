package in.msomu.notificationdemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private String TECH_CHANNEL_ID = "techNews";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button triggerNotification = (Button) findViewById(R.id.buttonTrigerNotification);
        Button changeNotification = (Button) findViewById(R.id.buttonChangeNotification);
        final Button deleteNotification = (Button) findViewById(R.id.buttonDeleteNotificationChannel);
        createNotificationChannel(TECH_CHANNEL_ID);
        createNotificationChannelGroup(TECH_CHANNEL_ID);
        triggerNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postNotification(TECH_CHANNEL_ID);
            }
        });
        changeNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeNotificationSettings(TECH_CHANNEL_ID);
            }
        });
        deleteNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNotification(TECH_CHANNEL_ID);
            }
        });
    }

    private void changeNotificationSettings(String mChannelId) {
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationChannel mChannel = mNotificationManager.getNotificationChannel(mChannelId);
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, mChannelId);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        startActivity(intent);
    }

    private void deleteNotification(String mChannelId) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // The id of the channel.
        NotificationChannel mChannel = mNotificationManager.getNotificationChannel(mChannelId);
        mNotificationManager.deleteNotificationChannel(mChannel.getId());
    }

    private void postNotification(String channelId) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Sets an ID for the notification, so it can be updated.
        int notifyID = 1;
        // Create a notification and set the notification channel.
        Notification notification = new Notification.Builder(MainActivity.this)
                .setContentTitle("New Message")
                .setContentText("You've received new messages.")
                .setSmallIcon(R.drawable.bookmark)
                .setChannel(channelId)
                .build();
        // Issue the notification.
        mNotificationManager.notify(notifyID, notification);
    }

    private void createNotificationChannelGroup(String groupId) {
        // The user visible name of the group.
        CharSequence name = getString(R.string.global);
        ;
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannelGroup(new NotificationChannelGroup(groupId, name));
    }

    private void createNotificationChannel(String mChannelId) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // The user visible name of the channel.
        CharSequence name = getString(R.string.tech);
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel mChannel = new NotificationChannel(mChannelId, name, importance);
        // Configure the notification channel.
        mChannel.enableLights(true);
        // Sets the notification light color for notifications posted to this
        // channel, if the device supports this feature.
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        mNotificationManager.createNotificationChannel(mChannel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
