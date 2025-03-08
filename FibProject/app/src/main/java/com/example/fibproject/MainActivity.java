package com.example.fibproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    public int fib1 = 0;
    public int fib2 = 1;
    public TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        resultText = findViewById(R.id.result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(v -> {
            int nextFib = fib1 + fib2;

            resultText.setText(String.valueOf(nextFib));

            fib1 = fib2;
            fib2 = nextFib;
        });

        Button btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(v -> {
            fib1 = 0;
            fib2 = 1;

            resultText.setText(String.valueOf(fib1));
        });
    }
}