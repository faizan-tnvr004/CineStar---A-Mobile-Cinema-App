package com.example.assignment1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class HomeScreen extends AppCompatActivity {
    MaterialButton BtnM1t;
    MaterialButton BtnM2t;
    MaterialButton BtnM3t;
    Button BtnM1R;
    Button BtnM2R;
    Button BtnM3R;
    TextView m1,m2,m3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.BtnM1t).setOnClickListener(v->openTrailer("https://www.youtube.com/watch?v=8aulMPhE12g"));
        findViewById(R.id.BtnM2t).setOnClickListener(v->openTrailer("https://www.youtube.com/watch?v=BjkIOU5PhyQ"));
        findViewById(R.id.BtnM3t).setOnClickListener(v->openTrailer("https://www.youtube.com/watch?v=9o996V_puWU"));
        init();
        BtnM1R.setOnClickListener(v->{
            Intent intent = new Intent(this, SeatSelection.class);
            intent.putExtra("movieName", m1.getText().toString());
            startActivity(intent);

                });
        BtnM2R.setOnClickListener(v->{
            Intent intent = new Intent(this, SeatSelection.class);
            intent.putExtra("movieName", m2.getText().toString());
            startActivity(intent);

        });
        BtnM3R.setOnClickListener(v->{
            Intent intent = new Intent(this, SeatSelection.class);
            intent.putExtra("movieName", m3.getText().toString());
            startActivity(intent);

        });

    }
    void openTrailer(String url){

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData((Uri.parse(url)));
        startActivity(intent);

    }
    void init(){
        BtnM1R=findViewById(R.id.BtnM1R);
        BtnM2R=findViewById(R.id.BtnM2R);
        BtnM3R=findViewById(R.id.BtnM3R);
        m1=findViewById(R.id.m1);
        m2=findViewById(R.id.m2);
        m3=findViewById(R.id.m3);


    }
}