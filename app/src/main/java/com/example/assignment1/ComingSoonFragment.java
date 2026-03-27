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

public class ComingSoonFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_coming_soon, container, false);

        // Add at least 3 upcoming movies here — use any drawables you have
        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Inception", "Sci-fi", "148 min",
                "https://www.youtube.com/watch?v=YoHD9XEInc0", R.drawable.inception));
        movies.add(new Movie("The Lion King", "Animation", "118 min",
                "https://www.youtube.com/watch?v=7TavVZMewpY", R.drawable.lionking));
        movies.add(new Movie("Spider-Man", "Action", "148 min",
                "https://www.youtube.com/watch?v=JfVOs4VSpmA", R.drawable.spiderman));

        RecyclerView recycler = view.findViewById(R.id.recyclerMovies);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(new MovieAdapter(requireActivity(), movies, true));

        return view;
    }
}