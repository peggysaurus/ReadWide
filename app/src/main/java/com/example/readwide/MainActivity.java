package com.example.readwide;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateOptions;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public User user;
    private StitchAppClient client = null;

    private RemoteMongoClient mongoClient;

    private RemoteMongoCollection<Document> coll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        client = Stitch.getDefaultAppClient();
        mongoClient = client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        coll = mongoClient.getDatabase("readwidedb").getCollection("user");
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
//                    saveChangesToDB();
                    saveStitch();
                } catch (Exception e){
                    Log.d("Peggy","SaveChanges error " + e);
                }
            }
        }
        else {
            try{
//                connectDB();
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
        searchBtn.findFocus();
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TextView test = findViewById(R.id.testField);
//                test.setVisibility(TextView.INVISIBLE);
                searchBook();
            }
        });

        EditText searchBar = findViewById(R.id.searchText);
        searchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == keyEvent.KEYCODE_ENTER)){
                    searchBook();
                }
                return false;
            }
        });
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        tryStitch();
    }

    private void searchBook() {
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
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
            showStats();
        }
        else{
            Log.d("Peggy","User is null");
        }
    }

    private  void tryStitch(){
        client.getAuth().loginWithCredential(new AnonymousCredential()).continueWithTask(
                new Continuation<StitchUser, Task<List<Document>>>() {

                    @Override
                    public Task<List<Document>> then(@NonNull Task<StitchUser> task) throws Exception {
                        if (!task.isSuccessful()) {
                            Log.e("STITCH", "Login failed!");
                            throw task.getException();
                        }
                        List<Document> docs = new ArrayList<>();
                        return coll
                                .find(new Document("user_id", client.getAuth().getUser().getId()))
                                .limit(100)
                                .into(docs);
                    }
                }
        ).addOnCompleteListener(new OnCompleteListener<List<Document>>() {
            @Override
            public void onComplete(@NonNull Task<List<Document>> task) {
                if (task.isSuccessful()) {
                    for(Document doc : task.getResult()){
                        String json = doc.toJson();
                        Log.d("STITCH", "Found docs: " + json);
                        if(json.contains("user_metrics")) {
                            User u = (new Gson()).fromJson(json, User.class);
                            setUser(u);
                        }
                    }
                    if(user == null){
                        User u = new User();
                        u.setId(client.getAuth().getUser().getId());
                        u.setUserMetrics(getStandardMetrics());
                        setUser(u);
                    }
                    loadDataView();
                    return;
                }
                Log.e("STITCH", "Error: " + task.getException().toString());
                task.getException().printStackTrace();
            }
        });
    }

    private  void saveStitch(){

        Document filterDoc = new Document().append("type","AppRecord");
        filterDoc.append("user_id",client.getAuth().getUser().getId());
        Document userDoc = Document.parse((new Gson()).toJson(user));
        RemoteUpdateOptions options = new RemoteUpdateOptions().upsert(true);

        final Task<RemoteUpdateResult> updateTask = coll.updateOne(filterDoc,userDoc, options);

        updateTask.addOnCompleteListener(new OnCompleteListener <RemoteUpdateResult> () {
            @Override
            public void onComplete(@NonNull Task <RemoteUpdateResult> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getUpsertedId() != null) {
                        String upsertedId = task.getResult().getUpsertedId().toString();
                        Log.d("STITCH", String.format("successfully upserted document with id: %s",
                                upsertedId));
                    } else {
                        long numMatched = task.getResult().getMatchedCount();
                        long numModified = task.getResult().getModifiedCount();
                        Log.d("STITCH", String.format("successfully matched %d and modified %d documents",
                                numMatched, numModified));
                    }
                } else {
                    Log.e("app", "failed to update document with: ", task.getException());
                }
            }
        });
    }

    private List<UserMetric> getStandardMetrics(){
        //TODO Fix this so it doesn't have empty radio buttons
        List <UserMetric> metrics = new ArrayList<>();
        UserMetric um1 = new UserMetric();
        um1.setGenre(new ArrayList<>());
        UserMetric um2 = new UserMetric();
        um1.setLength(new ArrayList<>());
        metrics.add(um1);
        metrics.add(um2);
        return metrics;
    }

    private void saveChangesToDB() {
        String[]args = new String[3];
//        args[0] = "mongodb+srv://ReadWideUser:RWpassJan2020@readwide2020-1x8dq.mongodb.net/test?retryWrites=true&w=majority";
//        args[0] = "mongodb://ReadWideUser:RWpassJan2020@readwide2020-shard-00-00-1x8dq.mongodb.net:27017,readwide2020-shard-00-01-1x8dq.mongodb.net:27017,readwide2020-shard-00-02-1x8dq.mongodb.net:27017/test?ssl=true&replicaSet=ReadWide2020-shard-0&authSource=admin&retryWrites=true&w=majority";
        args[0] = "mongodb://ReadWideUser:RWpassJan2020@readwide2020-shard-00-00-1x8dq.mongodb.net:27017,readwide2020-shard-00-01-1x8dq.mongodb.net:27017,readwide2020-shard-00-02-1x8dq.mongodb.net:27017/test?ssl=true&replicaSet=ReadWide2020-shard-0&authSource=admin&retryWrites=true&w=majority";
        args[1] = user.getId();
        args[2] = "5e1e3b6d42502e7bbf934466";
        Log.d("Peggy","Saving changes - user.getId().get$oid() = " + args[1] +" copied =" + args[2]);
//        DBSaver save = new DBSaver(this);
//        save.execute(args);
    }

    public void setUser(User u){
        this.user = u;
        Log.d("Peggy","User set as " + user.getUserName() + " id = " + user.getId());
    }

    private void connectDB() {
//        MongoClientOptions options = MongoClientOptions.builder().sslEnabled(true).build();
        String[]args = new String[2];
        args[0] = "mongodb+srv://ReadWideUser:RWpassJan2020@readwide2020-1x8dq.mongodb.net/test?retryWrites=true&w=majority";
//        args[0] = "mongodb://ReadWideUser:RWpassJan2020@readwide2020-shard-00-00-1x8dq.mongodb.net:27017,readwide2020-shard-00-01-1x8dq.mongodb.net:27017,readwide2020-shard-00-02-1x8dq.mongodb.net:27017/test?ssl=true&replicaSet=ReadWide2020-shard-0&authSource=admin&retryWrites=true&w=majority";
//        args[0] = "mongodb://ReadWideUser:RWpassJan2020@readwide2020-shard-00-00-1x8dq.mongodb.net:27017,readwide2020-shard-00-01-1x8dq.mongodb.net:27017,readwide2020-shard-00-02-1x8dq.mongodb.net:27017/test?ssl=true&replicaSet=ReadWide2020-shard-0&authSource=admin&retryWrites=true&w=majority";

        args[1] = "5e1e3b6d42502e7bbf934466";
//        DBConnection conn = new DBConnection(this);
//        conn.execute(args);
    }

    public void showStats(){
        Map<String, Map<String, Integer>> counts = user.getAllCounts();
        for (String key : counts.keySet()){
            showStatsOne(key,counts.get(key));
        }
    }

    public void showStatsOne(String key, Map<String, Integer> count){
        List<Integer> colors = getRandomColors();
        LinearLayout results = findViewById(R.id.resultsView);
        LayoutInflater inflater = getLayoutInflater();
        View statsView = inflater.inflate(R.layout.stats_card, null);
//        TextView statsHeader = statsView.findViewById(R.id.statsHeader);
//        statsHeader.setText(key);
        PieChartView pieChartView = statsView.findViewById(R.id.chart);
        if(pieChartView==null){
            Log.d("PeggyCharts","null object");
        }
        List<SliceValue> pieData = new ArrayList<>();
        int i = 0;
        for(String value : count.keySet()){
            if(count.get(value)>0) {
                pieData.add(new SliceValue(count.get(value), colors.get(i)).setLabel(value));
                i++;
            }
        }
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true);
        pieChartData.setHasCenterCircle(true).setCenterText1(key).setCenterText1FontSize(20);
        pieChartView.setPieChartData(pieChartData);
        results.addView(statsView);
    }

    private List<Integer> getRandomColors() {
        List<Integer> colors = new ArrayList<>();
        Random rnd = new Random();
        for (int i = 0; i < 10; i++){
            colors.add(Color.argb( 255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
        }
        return colors;
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
