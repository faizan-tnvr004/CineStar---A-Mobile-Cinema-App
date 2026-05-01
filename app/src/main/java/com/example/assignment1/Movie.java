package com.example.assignment1;

import android.content.Context;

public class Movie {
    private String name;
    private String genre;
    private String duration;
    private String trailerUrl;
    private int posterResId; // e.g. R.drawable.frank
    private String posterDrawableName; // e.g. "frank" — for JSON mapping

    // Constructor for hardcoded usage (backward compatible)
    public Movie(String name, String genre, String duration, String trailerUrl, int posterResId) {
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.trailerUrl = trailerUrl;
        this.posterResId = posterResId;
    }

    // Constructor for JSON parsing
    public Movie(String name, String genre, String duration, String trailerUrl, String posterDrawableName) {
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.trailerUrl = trailerUrl;
        this.posterDrawableName = posterDrawableName;
    }

    public String getName()       { return name; }
    public String getGenre()      { return genre; }
    public String getDuration()   { return duration; }
    public String getTrailerUrl() { return trailerUrl; }
    public int getPosterResId()   { return posterResId; }
    public String getPosterDrawableName() { return posterDrawableName; }

    // Resolve drawable resource ID from drawable name
    public int resolvePosterResId(Context context) {
        if (posterResId != 0) return posterResId;
        if (posterDrawableName != null && !posterDrawableName.isEmpty()) {
            posterResId = context.getResources().getIdentifier(
                    posterDrawableName, "drawable", context.getPackageName());
        }
        return posterResId;
    }
}