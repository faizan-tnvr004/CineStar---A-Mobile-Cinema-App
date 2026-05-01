package com.example.assignment1;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class TomorrowPageAdapter extends FragmentStateAdapter {

    private ArrayList<Movie> movies;

    public TomorrowPageAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Movie> movies) {
        super(fragmentActivity);
        this.movies = movies;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Only one page — tomorrow movies
        TomorrowFragment fragment = new TomorrowFragment();
        fragment.setMovies(movies);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 1; // single page for tomorrow
    }
}
