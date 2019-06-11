package com.ippon.pojo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ippon.resourcemanagers.ResourceLoader;
import com.ippon.twitter.TwitterLookup;
import twitter4j.Status;

import java.io.Serializable;
import java.util.Date;

public class TweetPojo implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("createdAt")
    Date createdAt = null;

    @JsonProperty("id")
    long id = -1;

    @JsonProperty("user")
    UserPojo user = null;
    @JsonProperty("inReplyToUserId")
    long inReplyToUserId = -1;
    @JsonProperty("mentionedUsers")
    UserPojo[] mentionedUsers = null;
    @JsonProperty("language")
    String lang = null;
    @JsonProperty("text")
    String text = null;

    Boolean mayBeNSFW = false;

    public TweetPojo() {
    }

    public TweetPojo(Status s) {
        while (s.isRetweet())
            s = s.getRetweetedStatus();
        user = new UserPojo(s.getUser());
        mayBeNSFW = s.isPossiblySensitive();
        createdAt = s.getCreatedAt();
        id = s.getId();
        inReplyToUserId = s.getInReplyToUserId();
        long[] ids = new long[s.getUserMentionEntities().length];
        // converts our UserMentions[] to a long[]
        for (int i = 0; i < ids.length; i++)
            ids[i] = s.getUserMentionEntities()[i].getId();

        mentionedUsers = TwitterLookup.getUsers(id);
        lang = s.getLang();
        text = ResourceLoader.sterilizeText(s.getText());
    }

    public boolean mayBeNSFW() {
        return mayBeNSFW;
    }

    @JsonGetter("user")
    public UserPojo getUser() {
        return user;
    }

    @JsonSetter("user")
    public void setUser(UserPojo user) {
        this.user = user;
    }

    @JsonGetter("createdAt")
    public Date getCreatedAt() {
        return createdAt;
    }

    @JsonGetter("createdAt")
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    @JsonGetter("id")
    public long getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(long id) {
        this.id = id;
    }

    @JsonGetter("inReplyToUserId")
    public long getInReplyToUserId() {
        return inReplyToUserId;
    }

    @JsonSetter("inReplyToUserId")
    public void setInReplyToUserId(long inReplyToUserId) {
        this.inReplyToUserId = inReplyToUserId;
    }

    @JsonGetter("mentionedUsers")
    public UserPojo[] getMentionedUsers() {
        return mentionedUsers;
    }

    @JsonSetter("mentionedUsers")
    public void setMentionedUsers(UserPojo[] mentionedUsers) {
        this.mentionedUsers = mentionedUsers;
    }

    @JsonGetter("lang")
    public String getLang() {
        return lang;
    }

    @JsonSetter("lang")
    public void setLang(String lang) {
        this.lang = lang;
    }

    @JsonGetter("text")
    public String getText() {
        return text;
    }

    @JsonSetter("text")
    public void setText(String text) {
        this.text = text;
    }
}
