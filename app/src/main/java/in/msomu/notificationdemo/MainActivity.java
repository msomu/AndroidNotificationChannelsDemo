package in.msomu.notificationdemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String mSelectedChannelId = "";
    int notifyID = 1;
    private ArrayList<News> newsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button triggerNotification = (Button) findViewById(R.id.buttonTriggerNotification);
        Button changeNotification = (Button) findViewById(R.id.buttonChangeNotification);
        Button deleteNotification = (Button) findViewById(R.id.buttonDeleteNotification);
        final Spinner channelSpinners = (Spinner) findViewById(R.id.spinnerChannel);

        //Getting some dummy news
        newsArrayList = Utils.generateRandomNews();
        ArrayList<String> categories = new ArrayList<>();
        for (News news : newsArrayList) {
            categories.add(news.getCategory());
        }
        String[] uniqueCategories = new HashSet<String>(categories).toArray(new String[0]);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, uniqueCategories);
        channelSpinners.setAdapter(dataAdapter);
        channelSpinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                mSelectedChannelId = adapterView.getItemAtPosition(position).toString().toLowerCase();
                checkStatusOfNotification(mSelectedChannelId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mSelectedChannelId = "";
            }
        });
        for (String uniqueCategory : uniqueCategories) {
            createNotificationChannel(uniqueCategory.toLowerCase(), uniqueCategory);
        }
        // Create two notification group for Daily and Weekly
        createNotificationChannelGroup("daily");
        createNotificationChannelGroup("weekly");

        triggerNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mSelectedChannelId))
                    postNotification(mSelectedChannelId);
            }
        });
        changeNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mSelectedChannelId))
                    changeNotificationSettings(mSelectedChannelId);
            }
        });
        deleteNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mSelectedChannelId))
                    deleteNotification(mSelectedChannelId);
            }
        });
    }

    private void checkStatusOfNotification(String channelId) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = mNotificationManager.getNotificationChannel(channelId);
        int importance = notificationChannel.getImportance();
        int lightColor = notificationChannel.getLightColor();
        int lockscreenVisibility = notificationChannel.getLockscreenVisibility();
        String group = notificationChannel.getGroup();
        CharSequence name = notificationChannel.getName();
        String importanceString = "";
        switch (importance) {
            case NotificationManager.IMPORTANCE_DEFAULT:
                importanceString = "IMPORTANCE_DEFAULT";
                break;
            case NotificationManager.IMPORTANCE_HIGH:
                importanceString = "IMPORTANCE_HIGH";
                break;
            case NotificationManager.IMPORTANCE_LOW:
                importanceString = "IMPORTANCE_HIGH";
                break;
            case NotificationManager.IMPORTANCE_MAX:
                importanceString = "IMPORTANCE_HIGH";
                break;
            case NotificationManager.IMPORTANCE_MIN:
                importanceString = "IMPORTANCE_HIGH";
                break;
            case NotificationManager.IMPORTANCE_NONE:
                importanceString = "IMPORTANCE_HIGH";
                break;
            case NotificationManager.IMPORTANCE_UNSPECIFIED:
                importanceString = "IMPORTANCE_UNSPECIFIED";
                break;
            default:
                importanceString = "IMPORTANCE_UNSPECIFIED";
        }

        Toast.makeText(this, "Name : " + name +
                        "'\n' Importance : " + importanceString +
                        " '\n' Light Color : " + lightColor +
                        "'\n' Lock Screen : " + lockscreenVisibility +
                        "'\n' Group : " + group
                , Toast.LENGTH_SHORT).show();
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
        mNotificationManager.deleteNotificationChannel(mChannelId);
    }

    private void postNotification(String channelId) {
        ArrayList<News> newsList = getList(channelId);
        Random r = new Random();
        int Low = 0;
        int High = newsList.size() - 1;
        int result = r.nextInt(High - Low) + Low;
        News news = newsList.get(result);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Create a notification and set the notification channel.
        Notification notification = new Notification.Builder(MainActivity.this, channelId)
                .setContentTitle(news.getCategory())
                .setContentText(news.getTitle())
                .setSmallIcon(R.drawable.bookmark)
                .build();
        // Issue the notification.
        mNotificationManager.notify(notifyID, notification);
        notifyID++;
    }

    private ArrayList<News> getList(String channelId) {
        ArrayList<News> channelNews = new ArrayList<>();
        for (News news : newsArrayList) {
            if (news.getCategoryId().equals(channelId)) {
                channelNews.add(news);
            }
        }
        return channelNews;
    }

    private void createNotificationChannelGroup(String groupId) {
        // The user visible name of the group.
        CharSequence name = getString(R.string.global);
        ;
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannelGroup(new NotificationChannelGroup(groupId, name));
    }

    private void createNotificationChannel(String mChannelId, String channelName) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel mChannel = new NotificationChannel(mChannelId, channelName, importance);
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
