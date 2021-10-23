package com.example.lista1_zadanie1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int count;
    private TextView showCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showCount = findViewById(R.id.show_count);
        if (savedInstanceState != null)
            count = savedInstanceState.getInt("counter_state");
        if (showCount != null)
            showCount.setText(Integer.toString(count));
    }

    public void restartButton(View view) {
        if (showCount != null)
            count = 0;
            showCount.setText(Integer.toString(count));
    }

    public void countUpButton(View view) {
        if (showCount != null)
            count++;
            showCount.setText(Integer.toString(count));
    }

    public void countDownButton(View view) {
        if (showCount != null)
            count--;
            showCount.setText(Integer.toString(count));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("counter_state", count);
    }
}