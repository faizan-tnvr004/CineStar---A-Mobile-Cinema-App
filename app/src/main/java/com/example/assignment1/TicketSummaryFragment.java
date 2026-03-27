package com.example.assignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class TicketSummaryFragment extends Fragment {

    private static final double TICKET_PRICE_PER_SEAT = 10.0;

    // Snack prices
    private final double BURGER_PRICE = 14.99;
    private final double PIZZA_PRICE  = 9.99;
    private final double DRINK_PRICE  = 6.99;
    private final double NACHOS_PRICE = 15.00;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ticket_summary, container, false);

        // ── Receive bundle data ───────────────────────────────────────────
        Bundle args = getArguments();
        String movieName          = "";
        int seatCount             = 0;
        int posterResId           = R.drawable.frank;
        double snackTotal         = 0;
        int burgerQty = 0, pizzaQty = 0, drinkQty = 0, nachosQty = 0;
        ArrayList<String> selectedSeats = new ArrayList<>();

        if (args != null) {
            movieName     = args.getString("movieName", "");
            seatCount     = args.getInt("seatCount", 0);
            snackTotal    = args.getDouble("snackTotal", 0);
            posterResId   = args.getInt("posterResId", R.drawable.frank);
            burgerQty     = args.getInt("burgerQty", 0);
            pizzaQty      = args.getInt("pizzaQty", 0);
            drinkQty      = args.getInt("drinkQty", 0);
            nachosQty     = args.getInt("nachosQty", 0);
            selectedSeats = args.getStringArrayList("selectedSeats");
            if (selectedSeats == null) selectedSeats = new ArrayList<>();
        }

        // ── Views ─────────────────────────────────────────────────────────
        ImageButton btnBack       = view.findViewById(R.id.btnBack);
        ImageView imgPoster       = view.findViewById(R.id.imgMoviePoster);
        TextView tvMovieName      = view.findViewById(R.id.movie_name);
        TextView tvTotalSeats     = view.findViewById(R.id.total_seats);
        TextView tvSeatPriceTotal = view.findViewById(R.id.seatPriceTotal);
        TextView tvTotalAmount    = view.findViewById(R.id.total_amount);
        LinearLayout snacksContainer = view.findViewById(R.id.snacks_container);
        MaterialButton btnSendTicket = view.findViewById(R.id.btnSendTicket);

        // ── Populate header ───────────────────────────────────────────────
        imgPoster.setImageResource(posterResId);
        tvMovieName.setText(movieName);

        // ── Tickets ───────────────────────────────────────────────────────
        double ticketTotal = seatCount * TICKET_PRICE_PER_SEAT;
        tvTotalSeats.setText("Total Seats: " + seatCount);
        tvSeatPriceTotal.setText("Tickets Total: $" + String.format("%.2f", ticketTotal));

        // ── Snacks ────────────────────────────────────────────────────────
        addSnackRow(view, snacksContainer, "Burger Combo", burgerQty, BURGER_PRICE);
        addSnackRow(view, snacksContainer, "Pizza",        pizzaQty,  PIZZA_PRICE);
        addSnackRow(view, snacksContainer, "Pina Colada",  drinkQty,  DRINK_PRICE);
        addSnackRow(view, snacksContainer, "Nachos",       nachosQty, NACHOS_PRICE);

        // ── Grand Total ───────────────────────────────────────────────────
        double grandTotal = ticketTotal + snackTotal;
        tvTotalAmount.setText("$" + String.format("%.2f", grandTotal));

        // ── Save to SharedPreferences ─────────────────────────────────────
        saveLastBooking(movieName, seatCount, grandTotal);

        // ── Back button ───────────────────────────────────────────────────
        btnBack.setOnClickListener(v ->
                ((MainActivity) requireActivity()).navigateTo(new HomeFragment()));

        // ── Send Ticket via Email ─────────────────────────────────────────
        String finalMovieName  = movieName;
        int    finalSeatCount  = seatCount;
        double finalTicketTotal = ticketTotal;
        double finalSnackTotal  = snackTotal;
        double finalGrandTotal  = grandTotal;

        btnSendTicket.setOnClickListener(v ->
                sendEmail(finalMovieName, finalSeatCount,
                        finalTicketTotal, finalSnackTotal, finalGrandTotal));

        return view;
    }

    // ── Add a snack row dynamically (only if qty > 0) ─────────────────────
    private void addSnackRow(View root, LinearLayout container,
                             String name, int qty, double priceEach) {
        if (qty <= 0) return;

        double rowTotal = qty * priceEach;

        LinearLayout row = new LinearLayout(getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(0, 8, 0, 8);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView nameTv = new TextView(getContext());
        nameTv.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        nameTv.setText(name + " x" + qty);
        nameTv.setTextSize(16f);
        nameTv.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));

        TextView priceTv = new TextView(getContext());
        priceTv.setText("$" + String.format("%.2f", rowTotal));
        priceTv.setTextSize(16f);
        priceTv.setGravity(Gravity.END);
        priceTv.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));

        row.addView(nameTv);
        row.addView(priceTv);
        container.addView(row);
    }

    // ── SharedPreferences ─────────────────────────────────────────────────
    private void saveLastBooking(String movieName, int seatCount, double totalPrice) {
        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("LastBooking", Context.MODE_PRIVATE);
        prefs.edit()
                .putString("movieName", movieName)
                .putInt("seatCount", seatCount)
                .putFloat("totalPrice", (float) totalPrice)
                .apply();
    }

    // ── Send Email ────────────────────────────────────────────────────────
    private void sendEmail(String movieName, int seatCount,
                           double ticketTotal, double snackTotal, double grandTotal) {

        String fixedEmail = "l230501@lhr.nu.edu.pk";

        String body = "Movie: " + movieName + "\n\n"
                + "Seats: " + seatCount
                + " - $" + String.format("%.2f", ticketTotal) + "\n"
                + "Snacks: $" + String.format("%.2f", snackTotal) + "\n\n"
                + "GRAND TOTAL: $" + String.format("%.2f", grandTotal);

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL,   new String[]{fixedEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Movie Ticket Booking Confirmation");
        emailIntent.putExtra(Intent.EXTRA_TEXT,    body);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send Ticket via"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "No email client installed.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}