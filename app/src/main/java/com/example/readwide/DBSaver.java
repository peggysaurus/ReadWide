package com.example.readwide;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class DBSaver extends AsyncTask<String, Integer, String> {
    MainActivity main;

    public DBSaver(MainActivity main){
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
        Document userDoc = Document.parse((new Gson()).toJson(main.user));
        userDoc.getObjectId(new ObjectId(main.user.getId().get$oid()));

        collection.replaceOne(eq("user_id",new ObjectId(main.user.getId().get$oid())),userDoc);
        Log.d("Peggy","Completed save to db as " + userDoc);
        return "Changes saved";
    }
}
