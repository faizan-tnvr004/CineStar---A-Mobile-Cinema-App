package com.example.assignment1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class SeatSelectionFragment extends Fragment {

    TextView txtTitle;
    ImageButton btnBack;
    String movieName;
    String trailerUrl;
    boolean isComingSoon;
    int posterResId;
    TextView seatCountText;
    ArrayList<String> seats = new ArrayList<>();
    int selectedCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_seat_selection, container, false);

        // Receive bundle data
        Bundle args = getArguments();
        if (args != null) {
            movieName    = args.getString("movieName", "");
            trailerUrl   = args.getString("trailerUrl", "");
            isComingSoon = args.getBoolean("isComingSoon", false);
            posterResId = args.getInt("posterResId", R.drawable.frank);

            seats = args.getStringArrayList("selectedSeats") != null
                    ? args.getStringArrayList("selectedSeats")
                    : new ArrayList<>();
            selectedCount = args.getInt("seatCount", 0);
        }

        txtTitle      = view.findViewById(R.id.txtTitle);
        btnBack       = view.findViewById(R.id.btnBack);
        seatCountText = view.findViewById(R.id.seatCountText);
        txtTitle.setText(movieName);

        btnBack.setOnClickListener(v ->
                ((MainActivity) requireActivity()).navigateTo(new HomeFragment()));

        Button btnSnacks = view.findViewById(R.id.btnSnacks);
        Button btnBook   = view.findViewById(R.id.btnBook);

        if (isComingSoon) {
            setupComingSoonMode(view, btnSnacks, btnBook);
        } else {
            setupNowShowingMode(btnSnacks, btnBook);
            setupSeatSelector(view);
            restoreSelectedSeats(view);
            updateSeatCount();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Get back seats from SnacksFragment
        Bundle args = getArguments();
        if (args != null && args.containsKey("selectedSeats") && args.containsKey("seatCount")) {
            ArrayList<String> returnedSeats = args.getStringArrayList("selectedSeats");
            int returnedCount = args.getInt("seatCount", 0);
            if (returnedSeats != null) {
                seats.clear();
                seats.addAll(returnedSeats);
                selectedCount = returnedCount;
                restoreSelectedSeats(getView());
                updateSeatCount();
            }
        }
    }

    private void setupNowShowingMode(Button btnSnacks, Button btnBook) {
        btnSnacks.setVisibility(View.VISIBLE);
        btnBook.setVisibility(View.VISIBLE);
        btnSnacks.setText("Proceed to Snacks");
        btnBook.setText("Book Seats");

        btnSnacks.setOnClickListener(v -> {
            if (seats.isEmpty()) {
                Toast.makeText(getContext(), "Please select at least one seat", Toast.LENGTH_SHORT).show();
                return;
            }
            SnacksFragment snacksFrag = new SnacksFragment();
            Bundle bundle = new Bundle();
            bundle.putString("movieName", movieName);
            bundle.putStringArrayList("selectedSeats", seats);
            bundle.putInt("seatCount", selectedCount);
            bundle.putInt("posterResId", posterResId);
            snacksFrag.setArguments(bundle);
            Log.d("SeatSelection", "Sending seats: " + seats + ", count: " + selectedCount);
            ((MainActivity) requireActivity()).navigateTo(snacksFrag);
        });

        btnBook.setOnClickListener(v -> {
            if (seats.isEmpty()) {
                Toast.makeText(getContext(), "Please select at least one seat", Toast.LENGTH_SHORT).show();
                return;
            }
            TicketSummaryFragment summaryFrag = new TicketSummaryFragment();
            Bundle bundle = new Bundle();
            bundle.putString("movieName", movieName);
            bundle.putStringArrayList("selectedSeats", seats);
            bundle.putInt("seatCount", selectedCount);
            bundle.putDouble("snackTotal", 0.0);
            bundle.putInt("posterResId", posterResId);
            summaryFrag.setArguments(bundle);
            ((MainActivity) requireActivity()).navigateTo(summaryFrag);
            Toast.makeText(getContext(), "Booking Confirmed!", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupComingSoonMode(View view, Button btnSnacks, Button btnBook) {
        GridLayout grid = view.findViewById(R.id.seatGrid);
        for (int i = 0; i < grid.getChildCount(); i++) grid.getChildAt(i).setEnabled(false);

        seatCountText.setVisibility(View.GONE);
        btnSnacks.setText("Coming Soon");
        btnSnacks.setEnabled(false);
        btnSnacks.setVisibility(View.VISIBLE);

        btnBook.setText("Watch Trailer");
        btnBook.setVisibility(View.VISIBLE);
        btnBook.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(trailerUrl));
            startActivity(intent);
        });
    }

    private void setupSeatSelector(View view) {
        GridLayout grid = view.findViewById(R.id.seatGrid);

        for (int i = 0; i < grid.getChildCount(); i++) {
            View child = grid.getChildAt(i);
            if (!(child instanceof Button)) continue;
            Button seat = (Button) child;

            // Hardcoded reserved seats
            if (seat.getId() == R.id.A1 ||
                    seat.getId() == R.id.B4 ||
                    seat.getId() == R.id.C7) {
                seat.setBackgroundResource(R.drawable.seat_reserved);
                seat.setEnabled(false);
                seat.setTag("reserved");
            } else {
                seat.setTag("available");
                seat.setBackgroundResource(R.drawable.seat_available);
            }

            seat.setOnClickListener(v -> {
                if ("reserved".equals(seat.getTag())) return;

                String seatName = seat.getText().toString();
                if ("available".equals(seat.getTag())) {
                    seat.setBackgroundResource(R.drawable.seat_selected);
                    seat.setTag("selected");
                    seats.add(seatName);
                    selectedCount++;
                } else if ("selected".equals(seat.getTag())) {
                    seat.setBackgroundResource(R.drawable.seat_available);
                    seat.setTag("available");
                    seats.remove(seatName);
                    selectedCount--;
                }
                updateSeatCount();
            });
        }
    }

    private void restoreSelectedSeats(View view) {
        GridLayout grid = view.findViewById(R.id.seatGrid);
        for (int i = 0; i < grid.getChildCount(); i++) {
            View child = grid.getChildAt(i);
            if (!(child instanceof Button)) continue;
            Button seat = (Button) child;
            if (seats.contains(seat.getText().toString())) {
                seat.setBackgroundResource(R.drawable.seat_selected);
                seat.setTag("selected");
            }
        }
    }

    private void updateSeatCount() {
        seatCountText.setText(String.valueOf(selectedCount));
    }
}