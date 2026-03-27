package com.example.assignment1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SeatSelectionFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Reuse your existing seat selection layout
        View view = inflater.inflate(R.layout.activity_seat_selection, container, false);

        // Retrieve the movie name passed from the adapter
        if (getArguments() != null) {
            String movieName = getArguments().getString("movieName");
            // Find the TextView in your seat selection layout that shows the movie name
            // and set it — adjust the ID to match your actual layout
            TextView tvMovie = view.findViewById(R.id.txtTitle);
            if (tvMovie != null) tvMovie.setText(movieName);
        }

        return view;
    }
}