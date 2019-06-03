package com.ippon.pojo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ippon.resourcemanagers.ResourceLoader;
import twitter4j.User;

import java.io.Serializable;

public class UserPojo implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("id")
    Long id = null;
    @JsonProperty("screenName")
    String screenName = null;
    @JsonProperty("email")
    String email = null;
    @JsonProperty("name")
    String name = null;
    @JsonProperty("description")
    String description = null;
    @JsonProperty("lang")
    String lang = null;
    @JsonProperty("location")
    String location = null;


    @JsonGetter("id")
    public Long getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonGetter("screenName")
    public String getScreenName() {
        return screenName;
    }
    @JsonSetter("screenName")
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
    @JsonGetter("email")
    public String getEmail() {
        return email;
    }
    @JsonSetter("email")
    public void setEmail(String email) {
        this.email = email;
    }
    @JsonGetter("name")
    public String getName() {
        return name;
    }
    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }
    @JsonGetter("description")
    public String getDescription() {
        return description;
    }
    @JsonSetter("description")
    public void setDescription(String description) {
        this.description = description;
    }
    @JsonGetter("lang")
    public String getLang() {
        return lang;
    }
    @JsonSetter("lang")
    public void setLang(String lang) {
        this.lang = lang;
    }
    @JsonGetter("location")
    public String getLocation() {
        return location;
    }
    @JsonSetter("location")
    public void setLocation(String location) {
        this.location = location;
    }

    public UserPojo(User u) {
        id = u.getId();
        screenName = u.getScreenName();
        email = u.getEmail();
        name = u.getName();
        description = ResourceLoader.sterilizeText(u.getDescription());
        lang = u.getLang();
        location = u.getLocation();
    }

    public UserPojo() {
    }
}
