package com.example.readwide;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("user_id")
    @Expose
    private UserId userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_metrics")
    @Expose
    private List<UserMetric> userMetrics = null;

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<UserMetric> getUserMetrics() {
        return userMetrics;
    }

    public void setUserMetrics(List<UserMetric> userMetrics) {
        this.userMetrics = userMetrics;
    }

    public MetricInterpretor getMetricInt(){
        return new MetricInterpretor((new Gson()).toJson(getUserMetrics()));
    }

}

class UserId {

    @SerializedName("$oid")
    @Expose
    private String $oid;

    public String get$oid() {
        return $oid;
    }

    public void set$oid(String $oid) {
        this.$oid = $oid;
    }

}

class UserMetric {

    @SerializedName("genre")
    @Expose
    private List<String> genre = null;
    @SerializedName("length")
    @Expose
    private List<String> length = null;

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public List<String> getLength() {
        return length;
    }

    public void setLength(List<String> length) {
        this.length = length;
    }

}