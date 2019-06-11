package com.ippon.main;

import com.ippon.resourcemanagers.ResourceLoader;
import com.ippon.twitter.TwitterListener;
import com.ippon.twitter.TwitterLookup;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;

import java.util.List;
import java.util.Scanner;

public class TwitterMiner {

    public static void main(String[] args) {
        System.out.print("What is the name of the topic? ");
        Scanner s = new Scanner(System.in);
        String topic = s.nextLine();
        System.out.print("What are the keywords to search for? ");
        String keywords = s.nextLine();

        s.close();
        TwitterStream t = TwitterStreamFactory.getSingleton();
        List<String> userAuthInfo = ResourceLoader.loadWords("userAuthInfo");
        TwitterLookup.authenticate("userAuthInfo");
        t.setOAuthConsumer(userAuthInfo.get(0), userAuthInfo.get(1));
        t.setOAuthAccessToken(new AccessToken(userAuthInfo.get(2),
                userAuthInfo.get(3), Long.parseLong(userAuthInfo.get(4))));
        t.addListener(new TwitterListener(topic));
        FilterQuery fq = new FilterQuery();
        //fq.language("en");
        fq.filterLevel("none");
        // Filter on key words
        System.out.println("Listening for: " + keywords);

        fq.track(keywords.split(" "));
        t.filter(fq);
    }

}
