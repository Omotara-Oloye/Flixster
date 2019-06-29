package com.example.flixster.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel // indicates the class is Parcelable
public class Movie {

    //API variables
    private String title;
    private String overview;
    private String posterPath; //only the path
    private String backdropPath;
    private Double voteAverage;

    public Movie(){}


    //initialize from JSON data
    public Movie(JSONObject movie) throws JSONException {
        title = movie.getString("title");
        overview = movie.getString("overview");
        posterPath = movie.getString("poster_path");
        backdropPath = movie.getString("backdrop_path");
        voteAverage = movie.getDouble("vote_average");
    }


    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }
}
