package com.example.assignment1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private boolean showingTomorrow = false;
    private MaterialButton btnToday, btnTomorrow;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    // Views for tomorrow mode
    private View tomorrowContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // ── TabLayout + ViewPager2 ────────────────────────────────────────
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        HomePageAdapter adapter = new HomePageAdapter(requireActivity());
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) tab.setText("Now Showing");
            else               tab.setText("Coming Soon");
        }).attach();

        // ── Today / Tomorrow buttons ──────────────────────────────────────
        btnToday = view.findViewById(R.id.btnToday);
        btnTomorrow = view.findViewById(R.id.btnTomorrow);

        btnToday.setOnClickListener(v -> {
            showingTomorrow = false;
            updateDateButtons();
            // Show tabs + viewpager (today mode)
            tabLayout.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
        });

        btnTomorrow.setOnClickListener(v -> {
            showingTomorrow = true;
            updateDateButtons();
            // Load tomorrow movies into the Now Showing tab's recycler
            loadTomorrowMovies();
        });

        // ── Three-dots menu ───────────────────────────────────────────────
        ImageButton btnMenu = view.findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> {
            // Open the navigation drawer
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });

        return view;
    }

    private void updateDateButtons() {
        if (showingTomorrow) {
            btnTomorrow.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.light_red)));
            btnTomorrow.setTextColor(getResources().getColor(R.color.white));
            btnToday.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#1C2230")));
            btnToday.setTextColor(android.graphics.Color.parseColor("#8892B0"));
        } else {
            btnToday.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.light_red)));
            btnToday.setTextColor(getResources().getColor(R.color.white));
            btnTomorrow.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#1C2230")));
            btnTomorrow.setTextColor(android.graphics.Color.parseColor("#8892B0"));
        }
    }

    private void loadTomorrowMovies() {
        // Hide tabs, show tomorrow movies directly in viewpager's first tab
        tabLayout.setVisibility(View.GONE);

        ArrayList<Movie> tomorrowMovies = loadMoviesFromJson("tomorrow");

        // Replace the viewpager with a temporary fragment showing tomorrow movies
        TomorrowFragment tomorrowFrag = new TomorrowFragment();
        Bundle bundle = new Bundle();
        // Pass movie data as serializable strings
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<String> durations = new ArrayList<>();
        ArrayList<String> trailerUrls = new ArrayList<>();
        ArrayList<String> posterDrawables = new ArrayList<>();

        for (Movie m : tomorrowMovies) {
            names.add(m.getName());
            genres.add(m.getGenre());
            durations.add(m.getDuration());
            trailerUrls.add(m.getTrailerUrl());
            posterDrawables.add(m.getPosterDrawableName());
        }
        bundle.putStringArrayList("names", names);
        bundle.putStringArrayList("genres", genres);
        bundle.putStringArrayList("durations", durations);
        bundle.putStringArrayList("trailerUrls", trailerUrls);
        bundle.putStringArrayList("posterDrawables", posterDrawables);
        tomorrowFrag.setArguments(bundle);

        // Hide viewpager, show tomorrow fragment inline
        viewPager.setVisibility(View.GONE);

        // Use a child fragment to show tomorrow movies
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.viewPager, tomorrowFrag, "tomorrow")
                .commit();

        // Actually, better approach: just swap viewpager content
        // Let's use a simpler approach - replace viewpager visibility and
        // show tomorrow movies using the existing recycler structure
        viewPager.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.GONE);

        // Remove child fragment approach, use adapter swap instead
        getChildFragmentManager()
                .beginTransaction()
                .remove(tomorrowFrag)
                .commitAllowingStateLoss();

        // Simplest approach: create an adapter that shows only tomorrow movies
        viewPager.setAdapter(new TomorrowPageAdapter(requireActivity(), tomorrowMovies));
    }

    private ArrayList<Movie> loadMoviesFromJson(String key) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            InputStream is = requireContext().getAssets().open("movies.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONObject root = new JSONObject(json);
            JSONArray array = root.getJSONArray(key);

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

    // ── Read from SharedPreferences and show AlertDialog ──────────────────
    private void showLastBooking() {
        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("LastBooking", Context.MODE_PRIVATE);

        String movieName = prefs.getString("movieName", null);

        if (movieName == null) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Last Booking")
                    .setMessage("No previous booking found.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        int seatCount    = prefs.getInt("seatCount", 0);
        float totalPrice = prefs.getFloat("totalPrice", 0f);

        String message = "Movie: " + movieName + "\n"
                + "Seats: " + seatCount + "\n"
                + "Total Price: $" + String.format("%.2f", totalPrice);

        new AlertDialog.Builder(requireContext())
                .setTitle("Last Booking")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}