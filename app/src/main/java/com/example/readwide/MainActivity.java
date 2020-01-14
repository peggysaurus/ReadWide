package com.example.readwide;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                Log.d("Peggy","Search clicked");
                EditText searchText = findViewById(R.id.searchText);
                String search = searchText.getText().toString();
                LibraryInterface oll = new LibraryInterface();
                try {
//                    oll.execute(search);
//                    Log.d("PeggyNobes","" +books.get().size());
                    List<Book> books = new ArrayList<>();

                    Call<SearchResult> call = oll.getBooks("unkindness+of+ghosts");
                    call.enqueue(callBack());
//                    Log.d("Peggy","OLL Working:" + oll.working);
//                    boolean wait = oll.working;
//                    TextView test = findViewById(R.id.testField);
//                    while (wait){
//                        test.setText("do something");
//                        wait = oll.working;
//                        Log.d("Peggy","OLL Working:" + wait);
//                    }
//                    afterSearch(books);
//
//                } catch (JSONException e){
//                    Log.d("PeggyNobes","Search click throws JSONexception");
//                } catch (IOException ex){
//                    Log.d("PeggyNobes","Search click throws IOException");
                } catch (NetworkOnMainThreadException e){
                    Log.d("PeggyNobes","networkonmainthreadexception going on");
                } catch (Exception e) {
                    Log.d("PeggyNobes", "Other Exception: " + e);
                }
            }
        });
    }

    private void displayResutls(List<Book> books) {
        LinearLayout results = findViewById(R.id.resultsView);
        LayoutInflater inflater = getLayoutInflater();
        for (Book b : books){
            results.addView(b.getSmallView(inflater, this.getApplicationContext()));
        }

    }

    public void afterSearch(List<Book> books){
//        books = oll.getBooks("unkindness+of+ghosts");
        Log.d("PeggyNobes","Got books: " + books.size());
        if(books.get(0) == null){
            Log.d("PeggyNobes","book is null");
        }

        TextView test = findViewById(R.id.testField);
        test.setText(books.get(0).getTitle());
        displayResutls(books);
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
