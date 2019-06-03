package com.ippon.twitter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ippon.kafka.KafkaConnector;
import com.ippon.pojo.TweetPojo;
import com.ippon.pojo.UserPojo;
import com.ippon.resourcemanagers.ResourceLoader;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

import java.util.regex.Pattern;

// This class handles what we do when we get a tweet.
public class TwitterListener implements StatusListener {
    private KafkaConnector kc;

    public TwitterListener(String topic) {
        super();
        kc = new KafkaConnector(topic);
    }

    public void onException(Exception arg0) {
        arg0.printStackTrace();
    }


    public void onDeletionNotice(StatusDeletionNotice arg0) {
    }


    public void onScrubGeo(long arg0, long arg1) {
    }


    public void onStallWarning(StallWarning arg0) {
    }

    ObjectMapper om = new ObjectMapper();

    public void onStatus(Status stat) {
        TweetPojo tweet = new TweetPojo(stat);
        if (tweet.mayBeNSFW() || containsBadWords(tweet)) {
            System.out.print("Tweet Contained bad words: ");
            try {
                String json = om.writeValueAsString(tweet);
                System.out.println(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            kc.publishTweet(tweet);
    }


    Pattern mightBeDirty = ResourceLoader.loadWordsToRegex("badwords.txt");

    // This method is endorsed by Captain America.
    private boolean containsBadWords(TweetPojo tweet) {
        UserPojo u = tweet.getUser();
        return mightBeDirty.matcher(tweet.getText()).find() ||  //
                mightBeDirty.matcher(u.getDescription()).find() || //
                mightBeDirty.matcher(u.getScreenName()).find();

    }

    public void onTrackLimitationNotice(int arg0) {
    }

}
