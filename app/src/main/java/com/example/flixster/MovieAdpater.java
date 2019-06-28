package com.example.flixster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.models.Config;
import com.example.flixster.models.Movie;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdpater  extends RecyclerView.Adapter<MovieAdpater.Viewholder>{

    //instance variables
    ArrayList<Movie> movies;
    //config method for URLS
    Config config;
    //context for rendering
    Context context;

    //initialize this list
    public MovieAdpater(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    //creates and inflates a new view
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //create the inflater
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //create the view object
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        //return the new ViewHolder
        return new Viewholder(movieView);
    }
    //binds an inflated view to a new item
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        //get the movie data from the data set
        Movie movie = movies.get(position);
        //populate the viewholder with data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        //build url for poster image
        String imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());

        //load image using glide library
        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 25, 0))
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .error(R.drawable.flicks_backdrop_placeholder)
                .into(holder.ivPosterImage);
    }
    //returns size of total data set
    @Override
    public int getItemCount() {
        return movies.size();
    }
// create the viewholder as a static inner class

    public static class Viewholder extends RecyclerView. ViewHolder{


        //track view objects
        ImageView ivPosterImage;
        TextView tvTitle;
        TextView tvOverview;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            //lookup view objects by id
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            tvOverview = (TextView)  itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
 }
