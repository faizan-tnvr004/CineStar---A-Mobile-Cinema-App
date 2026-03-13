package com.example.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;


public class SeatSelection extends AppCompatActivity {
TextView txtTitle;
ImageButton btnBack;
String Moviename;

TextView seatCountText;
    ArrayList<String> Seats = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seat_selection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
         Moviename = getIntent().getStringExtra("movieName");
        txtTitle.setText(Moviename);
        SeatSelector();
        btnBack.setOnClickListener(v->{
           startActivity(new Intent(this, HomeScreen.class));
        });
        Button btnSnacks = findViewById(R.id.btnSnacks);
        Button btnBook = findViewById(R.id.btnBook);

        btnSnacks.setOnClickListener(v -> {
            Intent intent = new Intent(SeatSelection.this, SnacksActivity.class);
            sendData(intent);
            startActivity(intent);
        });

        btnBook.setOnClickListener(v -> {
            Intent intent = new Intent(SeatSelection.this, BookingActivity.class);
            sendData(intent);
            startActivity(intent);
        });

    }
    void sendData(Intent intent){
        intent.putExtra("movieName", Moviename);
        intent.putStringArrayListExtra("selectedSeats", Seats);
        intent.putExtra("seatCount", selectedCount);
    }

    void init(){
        txtTitle = findViewById(R.id.txtTitle);
        btnBack = findViewById(R.id.btnBack);
        seatCountText = findViewById(R.id.seatCountText);
    }
    void updateCount(int count){
        String count1 = Integer.toString(count);
        seatCountText.setText(count1);
    }
    int selectedCount =0;
     void SeatSelector(){
         GridLayout grid = findViewById(R.id.seatGrid);
         for (int i =0; i <grid.getChildCount(); i++){
            View child = grid.getChildAt(i);
            if (!(child instanceof Button)){ //since gridlayout has space as well
             continue;
         }
            else{
            Button seat = (Button) child; //i have used type casting here. this way
                //i can use the gridlayout buttons

                //harcoding some seats here as booked
                if (seat.getId() == R.id.A1 || seat.getId() == R.id.B4 || seat.getId() == R.id.C7){
                    seat.setBackgroundResource(R.drawable.seat_reserved);

                    seat.setEnabled(false);
                    seat.setTag("reserved");

                    seat.setBackgroundResource(R.drawable.seat_reserved);

                }
                else{
                   seat.setTag("available");
                   seat.setBackgroundResource(R.drawable.seat_available);
                    }

                seat.setOnClickListener( v -> {
                    if ("reserved".equals( seat.getTag())) {
                        return; //if seat was already reserved/booked
                    } else if ("available".equals( seat.getTag())) {
                        seat.setBackgroundResource(R.drawable.seat_selected);
                        seat.setTag("selected");
                        selectedCount++;
                        Seats.add(seat.getText().toString());
                        updateCount(selectedCount);

                    }
                    else if("selected".equals( seat.getTag())) {
                        seat.setBackgroundResource(R.drawable.seat_available);
                        seat.setTag("available");
                        Seats.remove(seat.getText().toString());
                        selectedCount--;
                        updateCount(selectedCount);


                    }
                });
                }

            }
         }


    }

