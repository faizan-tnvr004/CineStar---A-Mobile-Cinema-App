package com.example.assignment1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<Movie> movieList;
    private FragmentActivity activity;

    // Updated constructor
    public MovieAdapter(FragmentActivity activity, ArrayList<Movie> movieList, boolean isComingSoon) {
        this.activity     = activity;
        this.movieList    = movieList;
        this.isComingSoon = isComingSoon;
    }

    // Add this field at the top
    private boolean isComingSoon;
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        holder.imgPoster.setImageResource(movie.getPosterResId());
        holder.tvName.setText(movie.getName());
        holder.tvGenreDuration.setText(movie.getGenre() + " / " + movie.getDuration());

        // Trailer button — opens YouTube in browser
        holder.btnTrailer.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(movie.getTrailerUrl()));
            activity.startActivity(intent);
        });

        // Book button — navigate to SeatSelectionFragment with movie data in Bundle
        holder.btnBook.setOnClickListener(v -> {
            SeatSelectionFragment seatFrag = new SeatSelectionFragment();
            Bundle bundle = new Bundle();
            bundle.putString("movieName", movie.getName());
            bundle.putString("movieGenre", movie.getGenre());
            bundle.putString("trailerUrl", movie.getTrailerUrl());
            bundle.putBoolean("isComingSoon", isComingSoon); // pass the tab type
            seatFrag.setArguments(bundle);
            ((MainActivity) activity).navigateTo(seatFrag);
            seatFrag.setArguments(bundle);

            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, seatFrag)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvName, tvGenreDuration;
        Button btnBook;
        MaterialButton btnTrailer;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster       = itemView.findViewById(R.id.imgPoster);
            tvName          = itemView.findViewById(R.id.tvMovieName);
            tvGenreDuration = itemView.findViewById(R.id.tvGenreDuration);
            btnBook         = itemView.findViewById(R.id.btnBook);
            btnTrailer      = itemView.findViewById(R.id.btnTrailer);
        }
    }

}