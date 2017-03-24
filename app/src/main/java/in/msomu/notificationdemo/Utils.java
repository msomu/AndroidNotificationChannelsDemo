package in.msomu.notificationdemo;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by msomu on 23/03/17.
 */

public class Utils {
    public static ArrayList<News> generateRandomNews() {
        ArrayList<News> newsArrayList = new ArrayList<>();
        newsArrayList.add(new News("WhatsApp's one-liner status is back, plus new features", "Apps"));
        newsArrayList.add(new News("Google Assistant for all Android M", "Android"));
        newsArrayList.add(new News("Google+ profiles may not be needed to post Play Store reviews anymore", "Apps"));
        newsArrayList.add(new News("Allo on desktop", "Apps"));
        newsArrayList.add(new News("Delivering RCS messaging to Android users worldwide", "Android"));
        newsArrayList.add(new News("Spaces got shutdown", "General"));
        newsArrayList.add(new News("Samsung to Expand its RCS (Rich Communication Services) Messaging Service", "Android"));
        newsArrayList.add(new News("Streaming on the move! Netflix to soon support HDR technology on mobiles", "General"));
        newsArrayList.add(new News("Google quietly launches Meet, an enterprise-friendly version of Hangouts", "General"));
        newsArrayList.add(new News("Mozilla acquired Pockets", "General"));
        newsArrayList.add(new News("Google brings your Keep notes directly into Docs,.", "Apps"));
        newsArrayList.add(new News("Engadget: YouTube TV is Google's live TV service,.", "General"));
        newsArrayList.add(new News("Lenovo's integrating Amazon's Alexa in Moto smartphones", "Smartphones"));
        newsArrayList.add(new News("YouTube TV is official, includes 40+ networks for $35/month", "General"));
        newsArrayList.add(new News("Humans are watching about 1 billion hours a day of YouTube", "General"));
        newsArrayList.add(new News("Meet your #GoogleAssistant", "General"));
        newsArrayList.add(new News("Beta testing for Gboard and Google Play Services", "Android"));
        newsArrayList.add(new News("Xperia XZ Premium officially named “Best New Smartphone at MWC 2017”", "Smartphones"));
        newsArrayList.add(new News("Samsung is making good progress on its foldable Galaxy X smartphone", "Smartphones"));
        newsArrayList.add(new News("Nokia, Airtel to collaborate on 5G, Internet of Things applications", "Smartphones"));
        newsArrayList.add(new News("Jio and Samsung join hands to bring 5G in India", "General"));
        newsArrayList.add(new News("Pixel Launcher was on Stock 6P for a few seconds early sign", "Android"));
        newsArrayList.add(new News("GOOGLE I/O 2017 EXPERIMENTS CHALLENGE", "General"));
        newsArrayList.add(new News("Google is making Allo chats a lot more animated", "Apps"));
        newsArrayList.add(new News("New UI in testing for Play store", "Apps"));
        newsArrayList.add(new News("Moto G5, Moto G5 Plus", "Smartphones"));
        newsArrayList.add(new News("Lenovo's new Android tablets are ready for kids and workers", "Smartphones"));
        newsArrayList.add(new News("Nokia 3310 Reboot, Nokia P1, Nokia 3, Nokia 5", "Smartphones"));
        newsArrayList.add(new News("LG G6", "Smartphones"));
        newsArrayList.add(new News("Huawei P10", "Smartphones"));
        newsArrayList.add(new News("Xperia XZ Premium hands-on: Sony powerhouse has a Snapdragon 835, 4K LCD", "Smartphones"));
        newsArrayList.add(new News("Gionee A1, A1 Plus", "Smartphones"));
        newsArrayList.add(new News("Jio feature phons", "Smartphones"));
        newsArrayList.add(new News("Hauwei watch with Android wear 2", "Android"));
        newsArrayList.add(new News("Samsung Galaxy Tab S3", "Smartphones"));
        newsArrayList.add(new News("BlackBerry KeyOne", "Smartphones"));
        newsArrayList.add(new News("Samsung Galaxy Book", "Smartphones"));
        newsArrayList.add(new News("Nokia 6", "Smartphones"));
        newsArrayList.add(new News("LG's new X Power 2", "Smartphones"));
        newsArrayList.add(new News("Samsung announces new Gear VR headset with a motion controller", "General"));
        newsArrayList.add(new News("All Fossil watches are getting Android Wear 2.0 in March", "Android"));
        newsArrayList.add(new News("Moto and Huawei are replacing the Android keys with a touchpad, and I like it", "Smartphones"));
        newsArrayList.add(new News("ZTE lacunches first 5G mobiles in MWC 2017", "Smartphones"));
        newsArrayList.add(new News("Hyperloop One is in early talks with the Indian government", "General"));
        return newsArrayList;
    }

    public static News getNews(String channelId) {
        ArrayList<News> newsList = getList(channelId);
        Random r = new Random();
        int Low = 0;
        int High = newsList.size() - 1;
        int result = r.nextInt(High - Low) + Low;
        return newsList.get(result);
    }

    public static ArrayList<News> getList(String channelId) {
        ArrayList<News> channelNews = new ArrayList<>();
        for (News news : generateRandomNews()) {
            if (news.getCategoryId().equals(channelId)) {
                channelNews.add(news);
            }
        }
        return channelNews;
    }
}
