package com.example.readwide;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.bson.types.ObjectId;

public class User {
    @SerializedName("user_id")
    @Expose
    private Id id;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_metrics")
    @Expose
    private List<UserMetric> userMetrics = null;
    @SerializedName("read_books")
    @Expose
    private List<UserBook> userBooks = new ArrayList<>();

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
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
        UserBookRecord tBook = new UserBookRecord(b, tags);
        Gson gson = new Gson();
        if(tBook==null){
            Log.d("Peggy","addBook creating null object???");
            return false;
        }
        String tbjson = gson.toJson(tBook);
        Log.d("Peggy","tempBook: " + tbjson);
        UserBook uBook = gson.fromJson(tbjson,UserBook.class);
        Log.d("Peggy","userBook: " + uBook.printBook());
        getUserBooks().add(uBook);
        return true;
    }

    public void addTagToMetric(String key, String tag) {
        MetricInterpretor interpretor = getMetricInt();
        interpretor.addValue(key, tag);
        List<UserMetric> userMetrics = new ArrayList<>();
        userMetrics.add((new Gson()).fromJson(interpretor.getMapAsJson(), UserMetric.class));
        setUserMetrics(userMetrics);
    }

    public Map<String,Map<String,Integer>> getAllCounts(){
        Map<String,Map<String,Integer>> allCounts = new HashMap<>();
        MetricInterpretor interpretor = getMetricInt();
        for(String key : interpretor.metrics.keySet()){
            allCounts.put(key,getCount(key));
        }
        return allCounts;
    }

    public Map<String,Integer> getCount(String key){
        List<String> options = getMetricInt().metrics.get(key);
        Map<String,Integer> counts = new HashMap<>();
        for (String option: options) {
            int count = 0;
            List<UserBook> ubooks = getUserBooks();
            for (UserBook b : ubooks) {
                String tag = b.getTags().get(key);
                if(tag.equals(option)){
                    count++;
                }
            }
            counts.put(option, count);
        }
        return counts;
    }
}

class Id {

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
    @SerializedName("book")
    @Expose
    private Book book = null;

    @SerializedName("tags")
    @Expose
    private HashMap<String, String> tags = null;


    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public HashMap<String, String> getTags() {
        return tags;
    }

    public void setTags(HashMap<String, String> tags) {
        this.tags = tags;
    }

    public String printBook(){
        String details = "Book: " + book.getTitle();
        for(String key: tags.keySet()){
            details = details + "; " + key + ": " + tags.get(key);
        }
        return details;
    }

}