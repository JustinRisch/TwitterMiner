package com.ippon.twitter;

import com.ippon.pojo.UserPojo;
import com.ippon.resourcemanagers.ResourceLoader;
import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class TwitterLookup extends Thread {

    public static String ConsumerKey = "", Secret = "", AccessToken = "", AccessTokenSecret = "", name = "";
    public static Long userId = -1L;
    private static Consumer<String> log = System.out::println;
    public static Twitter twitter = TwitterFactory.getSingleton();

    public static void authenticate(String file) {
        List<String> userAuthInfo = ResourceLoader.loadWords(file);
        ConsumerKey = userAuthInfo.get(0);
        Secret = userAuthInfo.get(1);
        AccessToken = userAuthInfo.get(2);
        AccessTokenSecret = userAuthInfo.get(3);
        userId = Long.parseLong(userAuthInfo.get(4));
        log.accept("ConsumerKey: " + ConsumerKey);
        log.accept("Secret: " + Secret);
        log.accept("AccessToken: " + AccessToken);
        log.accept("AccessTokenSecret: " + AccessTokenSecret);
        log.accept("userId: " + userId);

        twitter.setOAuthConsumer(ConsumerKey, Secret);
        twitter.setOAuthAccessToken(new AccessToken(AccessToken, AccessTokenSecret, userId));
        try {
            name = twitter.getScreenName();
            System.out.println("Logged in as: " + name);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }


    public static Status getTweet(Long id) throws NumberFormatException, TwitterException {
        try {
            ResponseList<Status> s = twitter.lookup(id);
            if (s.size() > 0)
                return s.get(0);
            else
                return null;
        } catch (TwitterException e) {
            try {
                if (e != null && e.getRateLimitStatus() != null && e.getRateLimitStatus().getRemaining() == 0) {
                    wait(e.getRateLimitStatus());
                    return getTweet(id);
                } else {
                    e.printStackTrace();
                    return null;
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }

        }
    }


    public static Status getTweet(String id) throws NumberFormatException, TwitterException {
        return getTweet(Long.valueOf(id));
    }


    static LinkedList<UserPojo> newUsers = new LinkedList<>();

    public static UserPojo getUser(String screenName) {
        try {

            UserPojo up = new UserPojo(twitter.lookupUsers(screenName).get(0));
            return up;
        } catch (TwitterException e) {
            wait(e.getRateLimitStatus());
            return getUser(screenName);
        }
    }

    public static UserPojo[] getUsers(long... i) {
        UserPojo[] pojos = new UserPojo[i.length];
        try {
            ResponseList<User> users = twitter.lookupUsers(i);
            for (int x = 0; x < pojos.length; x++) {
                pojos[x] = new UserPojo(users.get(x));
            }

        } catch (TwitterException e) {
            if (e.getRateLimitStatus() != null && e.getRateLimitStatus().getRemaining() < 1) {
                wait(e.getRateLimitStatus());
                return getUsers(i);
            }
            e.printStackTrace();
        }
        return pojos;
    }


    private static void wait(RateLimitStatus rls) {
        if (rls == null || rls.getRemaining() > 0)
            return;
        try {
            long secs = rls.getSecondsUntilReset() + 5;
            // wait until there's more API calls.
            for (int i = 0; i < secs; i += 10) {
                log.accept("Rate Limited: Waiting: " + (secs - i));
                Thread.sleep(Math.min(secs - i, 10) * 1000);
            }
        } catch (InterruptedException e1) {
        }
    }


}
