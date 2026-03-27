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

public class NowShowingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_now_showing, container, false);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Frankenstein", "Sci-fi", "150 min",
                "https://www.youtube.com/watch?v=8aulMPhE12g", R.drawable.frank));
        movies.add(new Movie("Zootopia", "Comedy", "147 min",
                "https://www.youtube.com/watch?v=BjkIOU5PhyQ", R.drawable.zoo));
        movies.add(new Movie("Donkey King", "Comedy", "105 min",
                "https://www.youtube.com/watch?v=9o996V_puWU", R.drawable.donkey));

        RecyclerView recycler = view.findViewById(R.id.recyclerMovies);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(new MovieAdapter(requireActivity(), movies, false));

        return view;
    }
}