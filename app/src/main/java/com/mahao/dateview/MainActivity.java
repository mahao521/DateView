package com.mahao.dateview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mahao.dateview.widget.DatePickView;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatePickView datePickView = (DatePickView) findViewById(R.id.date_view);
        datePickView.setOnItemClickListener(new DatePickView.DateClickListener() {
            @Override
            public void onItemClick(String str) {
                Toast.makeText(MainActivity.this,""+str,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
