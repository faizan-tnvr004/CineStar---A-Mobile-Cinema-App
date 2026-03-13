package com.example.assignment1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BookingActivity extends AppCompatActivity {

    // Containers
    LinearLayout snacksContainer;
    TextView movieNameTv, totalSeatsTv, totalAmountTv,totalSeatPrice;
double price1;
double price2;
    // Data
    String movieName;
    int seatCount, pizzaQty, burgerQty, drinkQty, nachosQty;
    double ticketPricePerSeat;

    // Prices
    double pizzaPrice = 9.99;
    double burgerPrice = 14.99;
    double drinkPrice = 6.99;
    double nachosPrice = 15.00;

    double totalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        // Get data from previous activity
        Intent intent = getIntent();
        movieName = intent.getStringExtra("movieName");
        seatCount = intent.getIntExtra("seatCount", 0);
        ticketPricePerSeat = intent.getDoubleExtra("ticketPricePerSeat", 10.0);

        pizzaQty = intent.getIntExtra("pizzaQty", 0);
        burgerQty = intent.getIntExtra("burgerQty", 0);
        drinkQty = intent.getIntExtra("drinkQty", 0);
        nachosQty = intent.getIntExtra("nachosQty", 0);

        // Set movie name and seat info
        movieNameTv.setText(movieName);
        totalSeatsTv.setText("Total Seats: " + seatCount);

        totalAmount = seatCount * ticketPricePerSeat;
        price1 = totalAmount;
        totalSeatPrice.setText("Total: $" + String.format("%.2f", totalAmount));

        displaySnacks();

        totalAmountTv.setText("Total: $" + String.format("%.2f", totalAmount));
        price2=totalAmount;
    }

    private void init() {
        movieNameTv = findViewById(R.id.movie_name);
        totalSeatsTv = findViewById(R.id.total_seats);
        totalAmountTv = findViewById(R.id.total_amount);
        snacksContainer = findViewById(R.id.snacks_container);
        totalSeatPrice=findViewById(R.id.seatPriceTotal);

        Button btnSendTicket = findViewById(R.id.btnSendTicket);
        btnSendTicket.setOnClickListener(v -> sendEmail());
    }

    // Display snacks with qty and price
    private void displaySnacks() {
        addSnack("Pizza", pizzaQty, pizzaPrice);
        addSnack("Burger", burgerQty, burgerPrice);
        addSnack("Pina Colada", drinkQty, drinkPrice);
        addSnack("Nachos", nachosQty, nachosPrice);
    }

    private void addSnack(String name, int qty, double pricePerItem) {
        if (qty <= 0) return;

        double totalPrice = qty * pricePerItem;

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(0, 10, 0, 10);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView nameTv = new TextView(this);
        nameTv.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        nameTv.setText(name + " x" + qty);
        nameTv.setTextSize(16f);
        nameTv.setTextColor(ContextCompat.getColor(this, android.R.color.white));

        TextView priceTv = new TextView(this);
        priceTv.setText("$" + String.format("%.2f", totalPrice));
        priceTv.setTextSize(16f);
        priceTv.setGravity(Gravity.END);
        priceTv.setTextColor(ContextCompat.getColor(this, android.R.color.white));

        row.addView(nameTv);
        row.addView(priceTv);

        snacksContainer.addView(row);

        totalAmount += totalPrice;
    }

    private void sendEmail() {
        String fixedEmail = "l230501@lhr.nu.edu.pk";

        double seatTotal = price1;          // seats only
        double snacksTotal = totalAmount - price1; // snacks only
        double grandTotal = seatTotal + snacksTotal;

        StringBuilder ticketDetails = new StringBuilder();
        ticketDetails.append("Movie: ").append(movieName != null ? movieName : "N/A").append("\n\n");

        ticketDetails.append("Total Seats: ").append(seatCount)
                .append(" - $").append(String.format("%.2f", seatTotal)).append("\n");

        ticketDetails.append("Snacks Total: $").append(String.format("%.2f", snacksTotal)).append("\n\n");

        ticketDetails.append("GRAND TOTAL: $").append(String.format("%.2f", grandTotal));

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{fixedEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Movie Ticket Booking Confirmation");
        emailIntent.putExtra(Intent.EXTRA_TEXT, ticketDetails.toString());

        try {
            startActivity(Intent.createChooser(emailIntent, "Send Ticket via"));
        } catch (android.content.ActivityNotFoundException ex) {
            android.widget.Toast.makeText(this, "No email client installed.", android.widget.Toast.LENGTH_SHORT).show();
        }
    }

}
