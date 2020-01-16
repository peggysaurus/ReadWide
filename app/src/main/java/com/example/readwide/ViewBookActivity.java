package com.example.readwide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientOptions;
//import com.mongodb.MongoClientURI;
//import com.mongodb.MongoCredential;
//import com.mongodb.ServerAddress;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoCursor;
//import com.mongodb.client.MongoDatabase;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Parcelable;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewBookActivity extends AppCompatActivity {

    Book book;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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

        EditText focus = findViewById(R.id.focusField);
        focus.findFocus();
//        focus.setVisibility(EditText.INVISIBLE);
        focus.setInputType(InputType.TYPE_NULL);
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(focus.getWindowToken(),0);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void addUserBook(){
        HashMap<String,String> tags = getTags();
        user.addUserBook(book, tags);
        Log.d("Peggy","Added book: " + user.getUserBooks().get(user.getUserBooks().size()-1).printBook());
        Intent intent = new Intent(ViewBookActivity.this,MainActivity.class);
        String userjson = (new Gson()).toJson(user);
        Log.d("Peggy", "Sending back to Main Activity user: " + userjson);
        intent.putExtra("user", userjson);
        intent.putExtra("action", "saveChanges");
        startActivity(intent);
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
                Log.d("PeggyTags","Found tag: " + key);
                if(rb != null){
                    String tag = rb.getText().toString();
                    if(tag.equals("other")){
                        Log.d("PeggyTags","other tag found gets next field: " + metview.getChildAt(i+1).getTag());
                        View newEntry = metview.getChildAt(i+1);
                        if(newEntry instanceof EditText){
                            tag = ((EditText) newEntry).getText().toString();
                            user.addTagToMetric(key,tag);
                        }
                    }
                    if(tag!=null) {
                        tags.put(key, tag);
                    }
                }
            }
        }
        return tags;
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
        ScrollView sv = findViewById(R.id.bookScroll);
        sv.scrollTo(sv.getTop(), sv.getScrollY());
        EditText focus = findViewById(R.id.focusField);
        focus.findFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        if(getCurrentFocus()!=null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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
            RadioButton rb = new RadioButton(this.getApplicationContext());
            rb.setText("other");
            rb.setTag("other");
            rg.addView(rb);
        final EditText et = new EditText(this.getApplicationContext());
        et.setTag("OtherEntry");
//        et.setEnabled(false);
//        et.setFocusable(false);
        et.setSingleLine(true);
        et.setText("");
        et.setHint("Enter other option");
        et.setEnabled(false);
        et.clearFocus();
        rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    et.setEnabled(false);
//                    et.setFocusable(false);
                }
                else{
                    et.setEnabled(true);
//                    et.setFocusable(true);
                    et.findFocus();
                    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(et,0);
                }
            }
        });
        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == keyEvent.KEYCODE_ENTER)){
                    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(et.getWindowToken(),0);
                }
                return false;
            }
        });
            metview.addView(header);
            metview.addView(rg);
            metview.addView(et);
        Log.d("Peggy","Added a radio group " + rg.getTag());
        }
}
