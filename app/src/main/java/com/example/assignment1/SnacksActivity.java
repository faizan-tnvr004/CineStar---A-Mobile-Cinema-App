package com.example.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class SnacksActivity extends AppCompatActivity {

    //prices
    private final double PRICE_BURGER = 14.99;
    private final double PRICE_PIZZA = 9.99;
    private final double PRICE_DRINK = 6.99;
    private final double PRICE_NACHOS = 15.00;
    ArrayList<String> selectedSeats;
    //quantities
    private int burgerQty = 0;
    private int pizzaQty = 0;
    private int drinkQty = 0;
    private int nachosQty = 0;

    //views
    private TextView qty1, qty2, qty3, qty4, totalSnackPrice;
    private Button plus1, minus1, plus2, minus2, plus3, minus3, plus4, minus4, btnProceedBooking;

    //receiveddata
    private String movieName;
    private int seatCount;
    private double ticketTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_snacks);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //receivedatafrompreviousscreen
        Intent intent = getIntent();
        movieName = intent.getStringExtra("movieName");
        seatCount = intent.getIntExtra("seatCount", 0);
        ticketTotal = intent.getDoubleExtra("ticketTotal", 10);
        selectedSeats = intent.getStringArrayListExtra("selectedSeats");

        init();
        setupListeners();
        updateTotal();
    }

    private void init() {
        qty1 = findViewById(R.id.qty1);
        qty2 = findViewById(R.id.qty2);
        qty3 = findViewById(R.id.qty3);
        qty4 = findViewById(R.id.qty4);

        totalSnackPrice = findViewById(R.id.totalSnackPrice);

        plus1 = findViewById(R.id.plus1);
        minus1 = findViewById(R.id.minus1);

        plus2 = findViewById(R.id.plus2);
        minus2 = findViewById(R.id.minus2);

        plus3 = findViewById(R.id.plus3);
        minus3 = findViewById(R.id.minus3);

        plus4 = findViewById(R.id.plus4);
        minus4 = findViewById(R.id.minus4);

        btnProceedBooking = findViewById(R.id.confirm_booking_button);
    }

    private void setupListeners() {

        plus1.setOnClickListener(v -> {
            burgerQty++;
            qty1.setText(String.valueOf(burgerQty));
            updateTotal();
        });

        minus1.setOnClickListener(v -> {
            if (burgerQty > 0) {
                burgerQty--;
                qty1.setText(String.valueOf(burgerQty));
                updateTotal();
            }
        });

        plus2.setOnClickListener(v -> {
            pizzaQty++;
            qty2.setText(String.valueOf(pizzaQty));
            updateTotal();
        });

        minus2.setOnClickListener(v -> {
            if (pizzaQty > 0) {
                pizzaQty--;
                qty2.setText(String.valueOf(pizzaQty));
                updateTotal();
            }
        });

        plus3.setOnClickListener(v -> {
            drinkQty++;
            qty3.setText(String.valueOf(drinkQty));
            updateTotal();
        });

        minus3.setOnClickListener(v -> {
            if (drinkQty > 0) {
                drinkQty--;
                qty3.setText(String.valueOf(drinkQty));
                updateTotal();
            }
        });

        plus4.setOnClickListener(v -> {
            nachosQty++;
            qty4.setText(String.valueOf(nachosQty));
            updateTotal();
        });

        minus4.setOnClickListener(v -> {
            if (nachosQty > 0) {
                nachosQty--;
                qty4.setText(String.valueOf(nachosQty));
                updateTotal();
            }
        });

        btnProceedBooking.setOnClickListener(v -> {

            Intent bookingIntent = new Intent(SnacksActivity.this, BookingActivity.class);

            bookingIntent.putExtra("movieName", movieName);
            bookingIntent.putExtra("seatCount", seatCount);
            bookingIntent.putExtra("ticketTotal", ticketTotal);
            bookingIntent.putExtra("snackTotal", calculateSnackTotal());
            bookingIntent.putExtra("ticketPricePerSeat", 10.0); //or dynamic
            bookingIntent.putExtra("pizzaQty", pizzaQty);
            bookingIntent.putExtra("burgerQty", burgerQty);
            bookingIntent.putExtra("drinkQty", drinkQty);
            bookingIntent.putExtra("nachosQty", nachosQty);
            bookingIntent.putExtra("selectedSeats",selectedSeats);

            startActivity(bookingIntent);
        });
    }

    //updatetotalsnackprice
    private void updateTotal() {
        double total = calculateSnackTotal();
        totalSnackPrice.setText("snacks total: $" + String.format("%.2f", total));
    }

    //calculatesnacktotal
    private double calculateSnackTotal() {
        return (burgerQty * PRICE_BURGER)
                + (pizzaQty * PRICE_PIZZA)
                + (drinkQty * PRICE_DRINK)
                + (nachosQty * PRICE_NACHOS);
    }
}
