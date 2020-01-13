package com.example.readwide;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Book {
    String title;
    ArrayList <Author> authors = new ArrayList<Author>();
    String cover_i;


    public Book (JSONObject json) throws JSONException {
        this.title = json.getString("title");
        JSONArray author_names = json.getJSONArray("author_name");
        for (int i = 0; i < author_names.length(); i++){
            String auth = author_names.getString(i);
            authors.add(new Author(auth));
        }
        this.cover_i = json.getString("cover_i");

    }

    public String getSmallCover(){
        return "https://covers.openlibrary.org/b/id/" + cover_i + "-S.jpg";
    }

    public String getMediumCover(){
        return "https://covers.openlibrary.org/b/id/" + cover_i + "-M.jpg";
    }

    public View getSmallView(LayoutInflater inflater){

        View bookView = inflater.inflate(R.layout.result_card, null);
        TextView title_tv = bookView.findViewById(R.id.title_tv);
        title_tv.setText(this.title);
        TextView author_tv = bookView.findViewById(R.id.author_tv);
        author_tv.setText("put name here");
//        ImageView cover_iv = bookView.findViewById(R.id.cover_iv);
//        cover_iv.setImageDrawable();
        return bookView;
    }
}
