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

    public Call<SearchResult> getBooks(String search) throws IOException {
        ApiService apiService = retrofit.create(ApiService.class);
        Log.d("Peggy","start retrofit search for: " + search);
        Call<SearchResult> call = apiService.getSearchData("search",search);
        return call;
    }

    public Call<Book> getOneBook(String key) throws IOException {
        ApiService apiService = retrofit.create(ApiService.class);
        Log.d("Peggy","start retrofit search for: " + key);
        Call<Book> call = apiService.getKeyData(key);
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

        @GET("{search}.json")
        Call<Book> getKeyData(@Path("search") String key);
    }

}

