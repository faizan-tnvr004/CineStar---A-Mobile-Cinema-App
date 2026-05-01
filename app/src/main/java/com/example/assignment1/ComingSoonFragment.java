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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ComingSoonFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_coming_soon, container, false);

        ArrayList<Movie> movies = loadMoviesFromJson("coming_soon");

        RecyclerView recycler = view.findViewById(R.id.recyclerMovies);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(new MovieAdapter(requireActivity(), movies, true));

        return view;
    }

    private ArrayList<Movie> loadMoviesFromJson(String key) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            // Read JSON from assets
            InputStream is = requireContext().getAssets().open("movies.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            // Parse JSON
            JSONObject root = new JSONObject(json);
            JSONArray array = root.getJSONArray(key);

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String name = obj.getString("name");
                String genre = obj.getString("genre");
                String duration = obj.getString("duration");
                String trailerUrl = obj.getString("trailerUrl");
                String posterDrawable = obj.getString("posterDrawable");

                Movie movie = new Movie(name, genre, duration, trailerUrl, posterDrawable);
                movie.resolvePosterResId(requireContext());
                movies.add(movie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }
}