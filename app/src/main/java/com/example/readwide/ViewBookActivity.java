package com.example.readwide;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.bson.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mongodb.client.model.Filters.eq;

public class ViewBookActivity extends AppCompatActivity {

    //    MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
    MongoClient mongoClient;
    MongoCollection<Document> collection;
    MongoCredential cred = MongoCredential.createCredential("admin", "readwidedb", "readwideadmin".toCharArray());

    Book book;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            connectDB();
        } catch (Exception e) {
            Log.e("Peggy", "Connection failed: " + e);
        }

        Intent intent = this.getIntent();
        String key = intent.getStringExtra("key");
        String bookJson = intent.getStringExtra("book");
        String userJson = intent.getStringExtra("user");
        user = (new Gson()).fromJson(userJson, User.class);
        if (!bookJson.equals("")) {
            book = (new Gson()).fromJson(bookJson, Book.class);
            loadDetails();
        } else if (!key.equals("")) {
            try {
                Call<Book> call = new LibraryInterface().getOneBook(key);
                call.enqueue(new Callback<Book>() {
                    @Override
                    public void onResponse(Call<Book> call, Response<Book> response) {
                        book = response.body();
                        loadDetails();
                    }

                    @Override
                    public void onFailure(Call<Book> call, Throwable t) {

                    }
                });
            } catch (Exception e) {
                Log.e("Peggy", "ViewBook Exception " + e);
            }

        }

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        loadMetricOptions(user);
        Button saveBtn = findViewById(R.id.saveBookBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUserBook();
            }
        });
    }

    private void addUserBook(){
        HashMap<String,String> tags = getTags();
        user.addUserBook(book, tags);
        Log.d("Peggy","Added book: " + user.getUserBooks().get(user.getUserBooks().size()-1).printBook());
    }

    private HashMap<String, String> getTags() {
        HashMap<String, String> tags = new HashMap<>();
        LinearLayout metview = findViewById(R.id.setMetricsView);
        for(int i = 0; i < metview.getChildCount(); i++){
            View child = metview.getChildAt(i);
            if(child instanceof RadioGroup){
                RadioGroup rg = (RadioGroup) child;
                RadioButton rb = findViewById(rg.getCheckedRadioButtonId());
                String key = rg.getTag().toString();
                if(rb != null){
                    String tag = rb.getText().toString();
                    tags.put(key,tag);
                }
            }
        }
        return tags;
    }

    private void connectDB() {
//        MongoClientOptions options = MongoClientOptions.builder().sslEnabled(true).build();
        mongoClient = new MongoClient("mongodb://localhost:27017");

        collection = mongoClient.getDatabase("readwidedb").getCollection("user");
    }

    private void loadDetails() {
        TextView title = findViewById(R.id.bookTitle);
        TextView author = findViewById(R.id.author);
        ImageView cover = findViewById(R.id.bookCover);
        title.setText(book.getTitle());
        String name = "";
        for (int i = 0; i < book.getAuthorName().size(); i++) {
            name = name + book.getAuthorName().get(i);
            if (i < book.getAuthorName().size() - 1) {
                name = name + ", ";
            }
        }
        if (name.equals("")) {
            name = "Unknown Author";
        }
        author.setText(name);
        Picasso.with(this.getApplicationContext()).load(book.getMediumCover()).into(cover);
    }

    private void loadMetricOptions(User user) {
        List<UserMetric> metrics = user.getUserMetrics();
        LinearLayout metview = findViewById(R.id.setMetricsView);
        MetricInterpretor metric = new MetricInterpretor((new Gson()).toJson(metrics));
        Log.d("Peggy","This is after making the interpretor");
        for (String key : metric.getKeys()) {
            addRagioGroup(metview, metric, key);
        }
    }


    private void addRagioGroup(LinearLayout metview, MetricInterpretor met, String key) {

            TextView header = new TextView(this.getApplicationContext());
            header.setText("Define book " + key);
            RadioGroup rg = new RadioGroup(this.getApplicationContext());
            rg.setTag(key);
//            header.setLabelFor(rg.getId());
            for (int j = 0; j < met.getList(key).size(); j++) {
                RadioButton rb = new RadioButton(this.getApplicationContext());
                rb.setText(met.getList(key).get(j));
                rg.addView(rb);
            }
            metview.addView(header);
            metview.addView(rg);
        Log.d("Peggy","Added a radio group " + rg.getTag());
        }
}
