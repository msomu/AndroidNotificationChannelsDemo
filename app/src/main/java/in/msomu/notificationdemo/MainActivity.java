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
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String mSelectedChannelId = "";
    int notifyID = 1;
    private ArrayList<News> newsArrayList;
    private RadioButton selectedRadio;

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
//        final RadioButton radioDaily = (RadioButton) findViewById(R.id.radioButtonDaily);
//        final RadioButton radioWeekly = (RadioButton) findViewById(R.id.radioButtonWeekly);
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupChannelGroup);
        radioGroup.check(R.id.radioButtonDaily);
        selectedRadio = (RadioButton) findViewById(R.id.radioButtonDaily);

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
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                selectedRadio = (RadioButton) findViewById(checkedRadioButtonId);
                checkStatusOfNotification(mSelectedChannelId,selectedRadio.getText().toString().toLowerCase());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mSelectedChannelId = "";
            }
        });
        // Create two notification group for Daily and Weekly
        createNotificationChannelGroup("daily","Daily News");
        createNotificationChannelGroup("weekly","Weekly News");
        for (String uniqueCategory : uniqueCategories) {
            createNotificationChannel(uniqueCategory.toLowerCase()+"_daily", uniqueCategory, "daily");
            createNotificationChannel(uniqueCategory.toLowerCase()+"_weekly", uniqueCategory, "weekly");
        }


        triggerNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mSelectedChannelId)) {
                    int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                    selectedRadio = (RadioButton) findViewById(checkedRadioButtonId);
                    postNotification(mSelectedChannelId,selectedRadio.getText().toString().toLowerCase());
                    notifyID++;
                }
            }
        });
        changeNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mSelectedChannelId)){
                    int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                    selectedRadio = (RadioButton) findViewById(checkedRadioButtonId);
                    changeNotificationSettings(mSelectedChannelId,selectedRadio.getText().toString().toLowerCase());
                }
            }
        });
        deleteNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mSelectedChannelId)){
                    int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                    selectedRadio = (RadioButton) findViewById(checkedRadioButtonId);
                    deleteNotificationChannel(mSelectedChannelId,selectedRadio.getText().toString().toLowerCase());
                }
            }
        });
    }

    private void checkStatusOfNotification(String channelId,String groupName) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel =
                mNotificationManager.getNotificationChannel(channelId+"_"+groupName);
        int importance = notificationChannel.getImportance();
        int lightColor = notificationChannel.getLightColor();
        int lockScreenVisibility = notificationChannel.getLockscreenVisibility();
        String group = notificationChannel.getGroup();
        CharSequence name = notificationChannel.getName();
        String importanceString;
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
                        "'\n' Lock Screen : " + lockScreenVisibility +
                        "'\n' Group : " + group
                , Toast.LENGTH_SHORT).show();
    }

    private void changeNotificationSettings(String mChannelId,String group) {
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, mChannelId+"_"+group);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        startActivity(intent);
    }

    private void deleteNotificationChannel(String mChannelId,String group) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.deleteNotificationChannel(mChannelId+"_"+group);
    }

    private void postNotification(String channelId,String group) {
        News news = Utils.getNews(channelId);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(MainActivity.this, channelId+"_"+group)
                .setContentTitle(news.getCategory())
                .setContentText(news.getTitle())
                .setSmallIcon(R.drawable.bookmark)
                .setGroup(group)
                .build();
        mNotificationManager.notify(notifyID, notification);
    }

    private void createNotificationChannelGroup(String groupId,String groupName) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannelGroup(new NotificationChannelGroup(groupId, groupName));
    }

    private void createNotificationChannel(String mChannelId, String channelName, String groupName) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel =
                new NotificationChannel(mChannelId, channelName,
                        NotificationManager.IMPORTANCE_LOW);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.RED);
        mChannel.setGroup(groupName);
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
