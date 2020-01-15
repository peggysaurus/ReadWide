package com.example.readwide;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.os.NetworkOnMainThreadException;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import org.bson.Document;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = this.getIntent();
        String userJson = intent.getStringExtra("user");
        String action = intent.getStringExtra("action");

        if(action != null || userJson != null){
            user = (new Gson()).fromJson(userJson, User.class);
            loadDataView();
            if(action.equals("saveChanges")){
                try{
                    saveChangesToDB();
                } catch (Exception e){
                    Log.d("Peggy","SaveChanges error " + e);
                }
            }
        }
        else {
            try{
                connectDB();
            } catch (Exception e) {
                Log.d("Peggy","Connection issue " + e);
            }
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Button searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TextView test = findViewById(R.id.testField);
//                test.setVisibility(TextView.INVISIBLE);
                clearResults();
                LinearLayout results = findViewById(R.id.resultsView);
                ProgressBar prog = new ProgressBar(getApplicationContext());
                results.addView(prog);

                Log.d("Peggy", "Search clicked");
                EditText searchText = findViewById(R.id.searchText);
                String search = searchText.getText().toString();
                Log.d("Peggy","Searching for " + search);
                if (search.equals("")) {
                    Log.d("Peggy", "Search is empty");
                } else {
                while (search.contains(" ")) {
                    StringBuilder sb = new StringBuilder(search);
                    sb.setCharAt(search.indexOf(" "), '+');
                    search = sb.toString();
                }
                LibraryInterface oll = new LibraryInterface();

                    try {
                        Call<SearchResult> call = oll.getBooks(search);
                        call.enqueue(callBack());
                    } catch (NetworkOnMainThreadException e) {
                        Log.d("PeggyNobes", "networkonmainthreadexception going on");
                    } catch (Exception e) {
                        Log.d("PeggyNobes", "Other Exception: " + e);
                    }
                }
            }
        });

    }

    public void loadDataView() {
        Log.d("Peggy","Started loadDataView");
        LinearLayout container = findViewById(R.id.resultsView);
        if(user!=null) {
            int totalRead = user.getUserBooks().size();
            TextView header = new TextView(this.getApplicationContext());
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            header.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            header.setTextSize(24);
            header.setLayoutParams(layoutParams);
            header.setText("You've read");
            TextView totalText = new TextView(this.getApplicationContext());
            totalText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            totalText.setTextSize(36);
            totalText.setTypeface(totalText.getTypeface(), Typeface.BOLD);
            totalText.setLayoutParams(layoutParams);
            String totalStr = "" + totalRead;
            totalText.setText(totalStr);
            TextView footer = new TextView(this.getApplicationContext());
            footer.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            footer.setTextSize(24);
            footer.setLayoutParams(layoutParams);
            footer.setText("books so far!");

            container.addView(header);
            container.addView(totalText);
            container.addView(footer);
        }
        else{
            Log.d("Peggy","User is null");
        }
    }

    private void saveChangesToDB() {
        String[]args = new String[3];
        args[0] = "mongodb://10.0.2.2:27017";
        args[1] = user.getId().get$oid();
        args[2] = "5e1e3b6d42502e7bbf934466";
        Log.d("Peggy","Saving changes - user.getId().get$oid() = " + args[1] +" copied =" + args[2]);
        DBSaver save = new DBSaver(this);
        save.execute(args);
    }

    public void setUser(User u){
        this.user = u;
        Log.d("Peggy","User set as " + user.getUserName() + " id = " + user.getId().get$oid());
    }

    private void connectDB() {
//        MongoClientOptions options = MongoClientOptions.builder().sslEnabled(true).build();
        String[]args = new String[2];
        args[0] = "mongodb://10.0.2.2:27017";
        args[1] = "5e1e3b6d42502e7bbf934466";
        DBConnection conn = new DBConnection(this);
        conn.execute(args);
    }

    private void displayResutls(List<Book> books) {
        LinearLayout results = findViewById(R.id.resultsView);
        LayoutInflater inflater = getLayoutInflater();
        for (Book b : books){
            results.addView(b.getSmallView(inflater, this.getApplicationContext(), new Intent(MainActivity.this, ViewBookActivity.class),user));
        }

    }

    public void afterSearch(List<Book> books){
//        books = oll.getBooks("unkindness+of+ghosts");
        clearResults();
        Log.d("PeggyNobes","Got books: " + books.size());
        if(books.isEmpty()){
            TextView test = new TextView(this.getApplicationContext());
            test.setText("No results found. Please try again");
            LinearLayout results = findViewById(R.id.resultsView);
            results.addView(test);
        }
        else if(books.get(0) == null){
            Log.d("PeggyNobes","book is null");
        }
        else{
            displayResutls(books);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void clearResults(){
        LinearLayout results = findViewById(R.id.resultsView);
        results.removeAllViews();
    }
    public Callback<SearchResult> callBack() {
        return new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if(response.isSuccessful()) {
                    List<Book>books = response.body().getBooks();
                    for (Book b : books) {
                        Log.d("Peggy", "Book found: " + b.getTitle());
                    }
                    Log.d("Peggy", "Number of books received: " + books.size());
                    afterSearch(books);
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.e("Peggy", t.toString());
            }
        };
    }
}
