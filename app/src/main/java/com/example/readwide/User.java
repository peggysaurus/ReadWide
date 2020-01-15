package com.example.readwide;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
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
    @SerializedName("read_books")
    @Expose
    private List<UserBook> userBooks = null;

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

    public List<UserBook> getUserBooks() {
        return userBooks;
    }

    public void setUserBooks(List<UserBook> userBooks) {
        this.userBooks = userBooks;
    }

    public MetricInterpretor getMetricInt(){
        return new MetricInterpretor((new Gson()).toJson(getUserMetrics()));
    }

    public boolean addUserBook (Book b, HashMap<String,String> tags){
        TempBook tBook = new TempBook(b, tags);
        Gson gson = new Gson();
        if(tBook==null){
            Log.d("Peggy","addBook creating null object???");
            return false;
        }
        String tbjson = gson.toJson(tBook);
        UserBook uBook = gson.fromJson(tbjson,UserBook.class);
        getUserBooks().add(uBook);
        return true;
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

class UserBook {
    @SerializedName("ol_key")
    @Expose
    private String olKey = null;

    @SerializedName("tags")
    @Expose
    private HashMap<String, String> tags = null;


    public String getOlKey() {
        return olKey;
    }

    public void setOlKey(String olKey) {
        this.olKey = olKey;
    }

    public HashMap<String, String> getTags() {
        return tags;
    }

    public void setTags(HashMap<String, String> tags) {
        this.tags = tags;
    }

    public String printBook(){
        String details = "Book: " + getOlKey();
        for(String key: tags.keySet()){
            details = details + "; " + key + ": " + tags.get(key);
        }
        return details;
    }

}

class TempBook{

    private String olKey = null;

    private HashMap<String, String> tags = null;

    public TempBook (Book b, HashMap<String,String> tags) {
        setOlKey(b.getKey());
        setTags(tags);
    }
    public String getOlKey() {
        return olKey;
    }

    public void setOlKey(String olKey) {
        this.olKey = olKey;
    }

    public HashMap<String, String> getTags() {
        return tags;
    }

    public void setTags(HashMap<String, String> tags) {
        this.tags = tags;
    }

}