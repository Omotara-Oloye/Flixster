package com.example.flixster;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flixster.models.Config;
import com.example.flixster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    //constants variables

    // the base-URL for the Movie Database API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";

    //parameter name for the API key
    public final static String API_KEY_PARAMETER = "api_key";

    //tag for all login calls
    public final static String TAG = "MainActivity";

    //instance variables

     AsyncHttpClient client;


    //the list of movies that are now currently playing
    ArrayList<Movie> movies;

    //track the adapter
    MovieAdapter adapter;
    //Recycler View
    RecyclerView rvMovies;

    //image config
    Config config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize the client variable
        client = new AsyncHttpClient();
        //initialize the list of movies
        movies = new ArrayList<>();

        //intitalize the adapter -- movies array cannot be recreated after this point
        adapter = new MovieAdapter(movies);

        //resolve the recycler view and connect a layout manager
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);


        //get the  configuration
        getConfig();

    }

    //get the list of currently paying movies from the API
    private void getNowPlaying(){
        //creates the URL
        String url = API_BASE_URL + "/movie/now_playing" ;
        //assess the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAMETER,getString(R.string.api_key)); //requires an API key
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load the results into the movie list
                try {
                    JSONArray results = response.getJSONArray("results");
                    //iterate the results list to get each movie
                    for(int x = 0; x < results.length(); x++){
                        Movie movie = new Movie(results.getJSONObject(x));
                        movies.add(movie);
                        //notify adapter a movie was added
                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failure to parse movie list", e, true);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failure to get movies from now_playing", throwable, true);
            }
        });
    }


    //access the configuration from the API database
    private void getConfig (){
        //creates the URL
        String url = API_BASE_URL + "/configuration" ;
         //assess the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAMETER,getString(R.string.api_key)); //requires an API key
        //execute GET request by expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    //
                    config = new Config(response);
                    Log.i(TAG, String.format("Loaded confirguration with imageBaseUrl %s and posterSize %s",
                            config.getImageBaseURL(), config.getPosterSize()));
                    //pass config to the adapter
                    adapter.setConfig(config);
                    //get the list of movies from the now_playing list
                    getNowPlaying();
                }catch (JSONException e){
                    logError("Failed parsing configuration", e, true);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get Configuration", throwable, true);
            }

        });

    }

    //handles error, logs and alerts the user
    private void logError (String errorMessage, Throwable error, boolean alertUser){
        //log every error
        Log.e(TAG, errorMessage, error);
        //alert the user
        if (alertUser){
            //show a long toast with the error message
            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
