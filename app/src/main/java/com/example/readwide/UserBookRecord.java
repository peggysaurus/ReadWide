package com.example.readwide;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class UserBookRecord {
    Book book;
    HashMap<String,String> tags;
    Date addedOn;

    public UserBookRecord(Book b, HashMap<String,String> t){
        this.book = b;
        this.tags = t;
        this.addedOn = Calendar.getInstance().getTime();
    }
}
