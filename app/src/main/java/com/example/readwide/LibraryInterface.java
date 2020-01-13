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

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    ArrayList<Book> books;

    public LibraryInterface(){

    }

    @Override
    protected ArrayList<Book> doInBackground(String... strings){
        try{
            JSONArray result = basicSearch(strings[0]);
            books = makeBooks(result);
            return books;}
        catch (IOException | JSONException ex){
            return null;
        }


    }

    public JSONArray basicSearch (String search)throws IOException, JSONException{
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

    public JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
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
                    JSONArray json = new JSONObject(jsonText).getJSONArray("docs");
                    return json;
                }
            } else {
                Log.d("PeggyNobes","Null value somewhere");
                return null;
            }
        } catch (JSONException | IOException e){
            Log.d("PeggyNobes","Who TF knows what's going on?");
        }
        return null;
    }

    private String readAll(Reader rd) throws IOException {
        Log.d("PeggyNobes","Started readAll");
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public ArrayList<Book> makeBooks(JSONArray bookList) throws JSONException{
        ArrayList<Book>books = new ArrayList<>();
        for (int i = 0; i < bookList.length() && i < 20; i++)
        {
            books.add(new Book(bookList.getJSONObject(i)));
        }
        return books;
    }
}

