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
import android.widget.ScrollView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

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
                EditText searchText = findViewById(R.id.searchText);
                String search = searchText.getText().toString();
                LibraryInterface oll = new LibraryInterface();
                try {
                    oll.execute(search);
//                    Log.d("PeggyNobes","" +books.get().size());
                    ArrayList<Book> books = oll.getBooks();
                    Log.d("PeggyNobes","Got books: " + books.size());
                    displayResutls(books);
//                } catch (JSONException e){
//                    Log.d("PeggyNobes","Search click throws JSONexception");
//                } catch (IOException ex){
//                    Log.d("PeggyNobes","Search click throws IOException");
                } catch (NetworkOnMainThreadException e){
                    Log.d("PeggyNobes","networkonmainthreadexception going on");
                } catch (Exception e) {
                    Log.d("PeggyNobes", "Other Excaption");
                }
            }
        });
    }

    private void displayResutls(ArrayList<Book> books) {
        ScrollView scroll = findViewById(R.id.resultsView);
        LayoutInflater inflater = getLayoutInflater();
        for (Book b : books){
            scroll.addView(b.getSmallView(inflater));
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
}
