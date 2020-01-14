package com.example.readwide;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewBookActivity extends AppCompatActivity {

    Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = this.getIntent();
        String key = intent.getStringExtra("key");
        if(!key.equals("")){
            try{
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
            } catch (Exception e){
                Log.e("Peggy", "ViewBook Exception " + e);
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
    }

    private void loadDetails(){
        //TODO fill in the fields with the book info
        TextView title = findViewById(R.id.bookTitle);
        TextView author = findViewById(R.id.author);
        ImageView cover = findViewById(R.id.bookCover);
        title.setText(book.getTitle());
        String name = "";
//        for(int i = 0; i < book.getAuthorName().size(); i++){
//            name = name + book.getAuthorName().get(i);
//            if(i < book.getAuthorName().size()-1){
//                name = name + ", ";
//            }
//        }
        if(name.equals("")){
            name = "Unknown Author";
        }
        author.setText(name);
        Picasso.with(this.getApplicationContext()).load(book.getMediumCover()).into(cover);
        Log.d("Peggy","loadDetails method not implemented. Book found is " + book.getTitle());
    }

}
