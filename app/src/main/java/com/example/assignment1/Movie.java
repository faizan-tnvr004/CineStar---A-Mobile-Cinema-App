package com.example.assignment1;

public class Movie {
    private String name;
    private String genre;
    private String duration;
    private String trailerUrl;
    private int posterResId; // e.g. R.drawable.frank

    public Movie(String name, String genre, String duration, String trailerUrl, int posterResId) {
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.trailerUrl = trailerUrl;
        this.posterResId = posterResId;
    }

    public String getName()       { return name; }
    public String getGenre()      { return genre; }
    public String getDuration()   { return duration; }
    public String getTrailerUrl() { return trailerUrl; }
    public int getPosterResId()   { return posterResId; }
}