package com.example.proj8;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.*;

import java.io.*;
import java.net.*;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    EditText editCondition;
    Button btnSearch;
    TextView txtTotalCount, txtResults;

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

        // Initialize UI elements
        editCondition = findViewById(R.id.editCondition);
        btnSearch = findViewById(R.id.btnSearch);
        txtTotalCount = findViewById(R.id.txtTotalCount);
        txtResults = findViewById(R.id.txtResults);

        // Button click triggers network request
        btnSearch.setOnClickListener(v -> {
            String condition = editCondition.getText().toString().trim();
            if (!condition.isEmpty()) {
                fetchTrials(condition);
            }
        });
    }

    private void fetchTrials(String condition) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String urlStr = "https://clinicaltrials.gov/api/v2/studies?"
                        + "query.term=" + URLEncoder.encode(condition, "UTF-8")
                        + "&pageSize=10";
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                InputStream in = (responseCode == HttpURLConnection.HTTP_OK) ?
                        new BufferedInputStream(conn.getInputStream()) :
                        new BufferedInputStream(conn.getErrorStream());
                String result = convertStreamToString(in);
                conn.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    parseAndDisplayResults(result);
                } else {
                    showOnMainThread("API error: " + responseCode + "\n" + result);
                }

            } catch (Exception e) {
                e.printStackTrace();
                showOnMainThread("Error: " + e.getMessage());
            }
        });
    }

    private void parseAndDisplayResults(String jsonStr) {
        try {
            JSONObject root = new JSONObject(jsonStr);
            JSONArray studies = root.getJSONArray("studies");
            int total = studies.length(); // Adjust as needed based on actual total count

            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < studies.length(); i++) {
                JSONObject study = studies.getJSONObject(i);
                JSONObject protocolSection = study.getJSONObject("protocolSection");
                JSONObject identificationModule = protocolSection.getJSONObject("identificationModule");

                String nctId = identificationModule.optString("nctId", "N/A");
                String briefTitle = identificationModule.optString("briefTitle", "N/A");

                builder.append(nctId).append(" - ").append(briefTitle).append("\n\n");
            }

            String finalText = builder.toString();
            new Handler(Looper.getMainLooper()).post(() -> {
                txtTotalCount.setText("Total count: " + total);
                txtResults.setText(finalText);
            });

        } catch (JSONException e) {
            e.printStackTrace();
            showOnMainThread("Failed to parse response.");
        }
    }

    private void showOnMainThread(String message) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show()
        );
    }

    private String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null)
            sb.append(line);

        reader.close();
        return sb.toString();
    }
}