package com.example.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {
    //the base url
    String imageBaseURL;

    //the poster size for fetching images
    String posterSize;

    //backdrop to use when handling landscape mode
    String backdropSize;


    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        //get the image base URL
        imageBaseURL = images.getString("secure_base_url");
        //get the poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        //use the poster size option at in index 3 or w342 as a fallback
        posterSize = posterSizeOptions.optString(3, "w342");

        //parse the backdrop size and use index 1 or w780
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w780");

    }

    //helper method used to construct URLS
    public String getImageUrl(String size, String path){
        return String.format("%s%s%s", imageBaseURL, size, path); //concentraate all three
    }
    public String getImageBaseURL() {
        return imageBaseURL;
    }

    public String getPosterSize() {
        return posterSize;
    }
}
