package com.example.assignment1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TomorrowFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tomorrow, container, false);

        // ── Today button → go back to HomeFragment ───────────────────────
        MaterialButton btnToday = view.findViewById(R.id.btnToday);
        btnToday.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).navigateTo(new HomeFragment());
        });

        // ── Tomorrow button (already selected) ───────────────────────────
        MaterialButton btnTomorrow = view.findViewById(R.id.btnTomorrow);
        // Already on tomorrow — do nothing on click

        // ── Three-dots menu ──────────────────────────────────────────────
        ImageButton btnMenu = view.findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });

        // ── Load tomorrow movies from JSON ───────────────────────────────
        ArrayList<Movie> movies = loadMoviesFromJson();

        RecyclerView recycler = view.findViewById(R.id.recyclerTomorrow);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        // Pass isTomorrow=true so bookings get a future date
        recycler.setAdapter(new MovieAdapter(requireActivity(), movies, false));

        return view;
    }

    private ArrayList<Movie> loadMoviesFromJson() {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            InputStream is = requireContext().getAssets().open("movies.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONObject root = new JSONObject(json);
            JSONArray array = root.getJSONArray("tomorrow");

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Movie movie = new Movie(
                        obj.getString("name"),
                        obj.getString("genre"),
                        obj.getString("duration"),
                        obj.getString("trailerUrl"),
                        obj.getString("posterDrawable")
                );
                movie.resolvePosterResId(requireContext());
                movies.add(movie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }
}
