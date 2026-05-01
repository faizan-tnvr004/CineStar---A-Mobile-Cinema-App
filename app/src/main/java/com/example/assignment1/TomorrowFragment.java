package com.example.assignment1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TomorrowFragment extends Fragment {

    private ArrayList<Movie> movieList = new ArrayList<>();

    // Constructor that accepts movie list
    public TomorrowFragment() {}

    public static TomorrowFragment newInstance(ArrayList<Movie> movies) {
        TomorrowFragment fragment = new TomorrowFragment();
        fragment.movieList = movies;
        return fragment;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movieList = movies;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_now_showing, container, false);

        // If movies were passed via arguments
        Bundle args = getArguments();
        if (args != null && movieList.isEmpty()) {
            ArrayList<String> names = args.getStringArrayList("names");
            ArrayList<String> genres = args.getStringArrayList("genres");
            ArrayList<String> durations = args.getStringArrayList("durations");
            ArrayList<String> trailerUrls = args.getStringArrayList("trailerUrls");
            ArrayList<String> posterDrawables = args.getStringArrayList("posterDrawables");

            if (names != null) {
                for (int i = 0; i < names.size(); i++) {
                    Movie movie = new Movie(
                            names.get(i),
                            genres.get(i),
                            durations.get(i),
                            trailerUrls.get(i),
                            posterDrawables.get(i)
                    );
                    movie.resolvePosterResId(requireContext());
                    movieList.add(movie);
                }
            }
        }

        RecyclerView recycler = view.findViewById(R.id.recyclerMovies);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(new MovieAdapter(requireActivity(), movieList, false));

        return view;
    }
}
