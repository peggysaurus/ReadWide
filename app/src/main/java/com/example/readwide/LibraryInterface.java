package com.example.readwide;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class LibraryInterface extends AsyncTask<String, Long, ArrayList<Book> > {
    String searchAPI = "https://openlibrary.org/search.json?";
    ArrayList<Book> books;

    public LibraryInterface(){

    }

    @Override
    protected ArrayList<Book> doInBackground(String... strings){
        try{
            JSONObject result = basicSearch(strings[0]);
            books = makeBooks(result);
            return books;}
        catch (IOException | JSONException ex){
            return null;
        }


    }

    public JSONObject basicSearch (String search)throws IOException, JSONException{
        Log.d("PeggyNobes","Started basic search");
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
        Log.d("PeggyNobes","Started read json from URL: " + url);

        try {
            URLConnection urlCon = new URL(url).openConnection();
            InputStreamReader in;
            if(urlCon != null) {
                urlCon.setReadTimeout(60 * 1000);
                if (urlCon != null && urlCon.getInputStream() != null) {
                    in = new InputStreamReader(urlCon.getInputStream(),
                            Charset.defaultCharset());

                    Log.d("PeggyNobes", "readJson stream opened");
                    BufferedReader rd = new BufferedReader(in);
                    String jsonText = readAll(rd);
                    JSONObject json = new JSONObject(jsonText);
                    return json;
                }
            } else {
                Log.d("PeggyNobes","Null value somewhere");
                return null;
            }
        } catch (Exception e){
            Log.d("PeggyNobes","Who TF knows what's going on?");
        }
        return null;
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

