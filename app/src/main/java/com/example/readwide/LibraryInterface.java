package com.example.readwide;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.operators.single.SingleObserveOn;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class LibraryInterface extends AppCompatActivity{
    String searchAPI = "https://openlibrary.org/search.json";
    String baseUrl = "https://openlibrary.org/";
    List<Book>books = new ArrayList<>();
    boolean working = false;
//    public ArrayList<Book> getBooks() {
//        return books;
//    }
//
//    public void setBooks(ArrayList<Book> books) {
//        this.books = books;
//    }
//
//    ArrayList<Book> books;

//    public LibraryInterface(){
//
//    }
//
//    @Override
//    protected ArrayList<Book> doInBackground(String... strings){
//        try{
//            JSONArray result = basicSearch(strings[0]);
//            books = makeBooks(result);
//            return books;}
//        catch (IOException | JSONException ex){
//            return null;
//        }
//
//
//    }
//
//    public JSONArray basicSearch (String search)throws IOException, JSONException{
//        Log.d("PeggyNobes","Started basic search");
//        String url = searchAPI + "?q=";
//        for (int i = 0; i < search.length(); i ++ ){
//            if(search.charAt(i) != ' '){
//                url = url + search.charAt(i);
//            }
//            else {
//                url = url + "+";
//            }
//        }
//        return readJsonFromUrl(url);
//    }
//
//    public JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
//        Log.d("PeggyNobes","Started read json from URL: " + url);
//
//        try {
//            URLConnection urlCon = new URL(url).openConnection();
//            InputStreamReader in;
//            if(urlCon != null) {
//                urlCon.setReadTimeout(60 * 1000);
//                if (urlCon != null && urlCon.getInputStream() != null) {
//                    in = new InputStreamReader(urlCon.getInputStream(),
//                            Charset.defaultCharset());
//
//                    Log.d("PeggyNobes", "readJson stream opened");
//                    BufferedReader rd = new BufferedReader(in);
//                    String jsonText = readAll(rd);
//                    JSONArray json = new JSONObject(jsonText).getJSONArray("docs");
//                    return json;
//                }
//            } else {
//                Log.d("PeggyNobes","Null value somewhere");
//                return null;
//            }
//        } catch (JSONException | IOException e){
//            Log.d("PeggyNobes","Who TF knows what's going on?");
//        }
//        return null;
//    }
//
//    private String readAll(Reader rd) throws IOException {
//        Log.d("PeggyNobes","Started readAll");
//        StringBuilder sb = new StringBuilder();
//        int cp;
//        while ((cp = rd.read()) != -1) {
//            sb.append((char) cp);
//        }
//        return sb.toString();
//    }
//
//    public ArrayList<Book> makeBooks(JSONArray bookList) throws JSONException{
//        ArrayList<Book>books = new ArrayList<>();
//        for (int i = 0; i < bookList.length() && i < 20; i++)
//        {
//            books.add(new Book(bookList.getJSONObject(i)));
//        }
//        return books;
//    }

//    ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        apiService = retrofit.create(ApiService.class);
    }

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }

//    public Book getBook(String id){
//        final Book[] book = {null};
//        Call<Book>call = apiService.getBookData("books/" + id);
//        call.enqueue(new Callback<Book>() {
//            @Override
//            public void onResponse(Call<Book> call, Response<Book> response) {
//                book[0] = response.body();
//            }
//
//            @Override
//            public void onFailure(Call<Book> call, Throwable t) {
//
//            }
//        });

        public Call<SearchResult> getBooks(String search) throws IOException {
            ApiService apiService = retrofit.create(ApiService.class);
            Call<SearchResult> call = apiService.getSearchData("search",search);
//            apiService.getSearchData("search", search).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//            Log.d("PeggyNobes","in getBooks, set call");
//            Callback<SearchResult> callback = callBack();

//            return call.;
//            call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new SingleObserver<SearchResult>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                            compositeDisposable.add(d);
//                            working = true;
//                        }
//
//                        @Override
//                        public void onSuccess(SearchResult sr) {
//                            books = sr.getBooks();
//                            Log.d("Peggy","Found books: " + sr.getBooks().size());
//                            working = false;
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            Log.d("PeggyNobes","Error in getBook subscribe: " + e);
//                            working = false;
//                        }
//                    });
            return call;
        }




    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    public interface ApiService {
//        @GET("book/{book_id}.json")
//        Call<Book> getBookData(@Path("book_id") String bookId
////                                   ,@Query("api_key") String apiKey
//                                );
        @GET("{search}.json")
        Call<SearchResult> getSearchData(@Path("search") String searchtype, @Query("q") String search);
        Call<Book> getBookData(@Path("search") String searchtype);
    }

}

