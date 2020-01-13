package com.example.readwide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class LibraryInterface {
    String searchAPI = "https://openlibrary.org/search.json?";

    public LibraryInterface(){

    }

    public JSONObject basicSearch (String search)throws IOException, JSONException{
        String url = searchAPI + "q=";
        for (int i = 0; i < search.length(); i ++ ){
            if(search.charAt(i) != ' '){
                url = url + search.charAt(i);
            }
            else {
                url = url + "+";
            }
        }
        return readJsonFromUrl(url);
    }

    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public ArrayList<Book> makeBooks(JSONObject results) throws JSONException{
        ArrayList<Book>books = new ArrayList<>();
        JSONArray bookList = results.getJSONArray("docs");
        for (int i = 0; i < bookList.length(); i++)
        {
            books.add(new Book(bookList.getJSONObject(i)));
        }
        return books;
    }
}

