package com.example.project5;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editLimit, editThreads;
    private TextView textResult, textTotal;
    private int totalTwins = 0;
    private final Object lock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editLimit = findViewById(R.id.editLimit);
        editThreads = findViewById(R.id.editThreads);
        textResult = findViewById(R.id.textResult);
        textTotal = findViewById(R.id.textTotal);
        Button btnStart = findViewById(R.id.btnStart);

        btnStart.setOnClickListener(v -> startThreads());
    }

    private void startThreads() {
        textResult.setText("");
        textTotal.setText("Total Twins: 0");
        totalTwins = 0;

        int limit, numThreads;

        try {
            limit = Integer.parseInt(editLimit.getText().toString());
            numThreads = Integer.parseInt(editThreads.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            return;
        }

        int chunk = limit / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int start = i * chunk + 1;
            int end = (i == numThreads - 1) ? limit : (i + 1) * chunk;

            int threadNum = i + 1;

            new Thread(() -> findTwinPrimes(start, end, threadNum)).start();
        }
    }

    private void findTwinPrimes(int start, int end, int threadNum) {
        int threadCount = 0;

        for (int i = Math.max(start, 3); i <= end - 2; i++) {
            if (isPrime(i) && isPrime(i + 2)) {
                int twin1 = i, twin2 = i + 2;
                threadCount++;

                String message = threadNum + ":[" + twin1 + "," + twin2 + "] ";

                runOnUiThread(() -> textResult.append(message));
            }
        }

        synchronized (lock) {
            totalTwins += threadCount;
            runOnUiThread(() -> textTotal.setText("Total Twins: " + totalTwins));
        }
    }

    private boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (int i = 3; i <= Math.sqrt(n); i += 2)
            if (n % i == 0) return false;
        return true;
    }
}