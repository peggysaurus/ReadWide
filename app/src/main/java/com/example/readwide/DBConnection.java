package com.example.readwide;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.mongodb.DBCallback;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.util.Arrays;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.jsonSchema;

public class DBConnection extends AsyncTask<String, Integer, String> {
    MainActivity main;

    public DBConnection(MainActivity main){
        this.main = main;
    }

    @Override
    protected String doInBackground(String[]strings) {
        String user = "admin";
        String database = "readwidedb";
        char[] pass = "widereadadmin".toCharArray();
        MongoCredential cred = MongoCredential.createCredential(user,database,pass);
        MongoClient mongoClient = new MongoClient(new MongoClientURI(strings[0]));

        MongoDatabase db = mongoClient.getDatabase("readwidedb");
        MongoCollection<Document> collection = db.getCollection("user");
        Document userDoc = collection.find(eq("user_name","Test User 1")).first();
        Log.d("Peggy",userDoc.toJson());
        User mainUser = (new Gson()).fromJson(userDoc.toJson(), User.class);
        main.setUser(mainUser);
        return userDoc.toJson();
    }
}
