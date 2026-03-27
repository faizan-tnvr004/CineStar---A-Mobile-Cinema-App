package com.example.assignment1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class SnacksFragment extends Fragment {

    private ArrayList<Snack> snackList = new ArrayList<>();
    private SnackAdapter adapter;
    private TextView tvTotal;

    // Data received from SeatSelectionFragment
    private String movieName;
    private ArrayList<String> selectedSeats;
    private int seatCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_snacks, container, false);
        Log.d("SnacksFragment", "Received seats: " + selectedSeats + ", seatCount: " + seatCount);
        // Receive bundle data
        Bundle args = getArguments();
        if (args != null) {
            movieName     = args.getString("movieName", "");
            selectedSeats = args.getStringArrayList("selectedSeats");
            seatCount     = args.getInt("seatCount", 0);


        }

        tvTotal = view.findViewById(R.id.tvSnackTotal);
        ListView listView = view.findViewById(R.id.listSnacks);

        // Populate snack data — use your existing drawables
        snackList.add(new Snack("Burger Combo",  14.99, R.drawable.food1));
        snackList.add(new Snack("Pizza",          9.99, R.drawable.food2));
        snackList.add(new Snack("Pina Colada",    6.99, R.drawable.food3));
        snackList.add(new Snack("Nachos",        15.00, R.drawable.food4));

        // Adapter — updates total whenever +/- is pressed
        adapter = new SnackAdapter(requireContext(), snackList, this::updateTotal);
        listView.setAdapter(adapter);

        updateTotal(); // set initial total

        Button btnConfirm = view.findViewById(R.id.btnConfirmBooking);
        btnConfirm.setOnClickListener(v -> {
            double snackTotal = calculateTotal();

            // Pass all data forward to TicketSummaryFragment
            TicketSummaryFragment summaryFrag = new TicketSummaryFragment();
            Bundle bundle = new Bundle();
            bundle.putString("movieName", movieName);
            bundle.putStringArrayList("selectedSeats", selectedSeats);
            bundle.putInt("seatCount", seatCount);
            bundle.putDouble("snackTotal", snackTotal);
            bundle.putInt("burgerQty",  snackList.get(0).getQuantity());
            bundle.putInt("pizzaQty",   snackList.get(1).getQuantity());
            bundle.putInt("drinkQty",   snackList.get(2).getQuantity());
            bundle.putInt("nachosQty",  snackList.get(3).getQuantity());
            bundle.putInt("posterResId", getArguments() != null ? getArguments().getInt("posterResId") : 0);
            summaryFrag.setArguments(bundle);

            ((MainActivity) requireActivity()).navigateTo(summaryFrag);
            Toast.makeText(getContext(), "Booking Confirmed!", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void updateTotal() {
        tvTotal.setText(String.format("Snacks Total: $%.2f", calculateTotal()));
    }

    private double calculateTotal() {
        double total = 0;
        for (Snack s : snackList) {
            total += s.getPrice() * s.getQuantity();
        }
        return total;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Intercept the system back button
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Prepare bundle to send back
                Bundle result = new Bundle();
                result.putString("movieName", movieName);
                result.putStringArrayList("selectedSeats", selectedSeats);
                result.putInt("seatCount", seatCount);
                result.putInt("posterResId", getArguments() != null ? getArguments().getInt("posterResId") : 0);

                // Send data back
                getParentFragmentManager().setFragmentResult("snack_request", result);

                // Navigate back
                setEnabled(false); // disable callback to let default behavior happen
                requireActivity().onBackPressed();
            }
        });
    }
}

