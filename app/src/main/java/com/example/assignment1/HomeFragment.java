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
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // ── TabLayout + ViewPager2 ────────────────────────────────────────
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);

        HomePageAdapter adapter = new HomePageAdapter(requireActivity());
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) tab.setText("Now Showing");
            else               tab.setText("Coming Soon");
        }).attach();

        // ── Today button (already selected, stays on this page) ──────────
        MaterialButton btnToday = view.findViewById(R.id.btnToday);
        btnToday.setOnClickListener(v -> {
            // Already on today — do nothing
        });

        // ── Tomorrow button → navigate to TomorrowFragment ───────────────
        MaterialButton btnTomorrow = view.findViewById(R.id.btnTomorrow);
        btnTomorrow.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).navigateTo(new TomorrowFragment());
        });

        // ── Three-dots menu ───────────────────────────────────────────────
        ImageButton btnMenu = view.findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });

        return view;
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