package com.example.a9puzzleproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public Button[][] buttons = new Button[3][3];
    public int emptyX = 2;
    public int emptyY = 2;

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

        GridLayout gridLayout = findViewById(R.id.gridLayout);

        List<String> numbers = new ArrayList<>();

        for(int i = 1; i <= 8; i++) {
            numbers.add(String.valueOf(i));
        }
        numbers.add("");

        Collections.shuffle(numbers);


        int index = 0;
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                Button button = new Button(this);
                String value = numbers.get(index++);

                button.setText(value);
                button.setOnClickListener(new TileClickListener(i, j));
                buttons[i][j] = button;

                if (value.equals("")) {
                    emptyX = i;
                    emptyY = j;
                }

                gridLayout.addView(button, new GridLayout.LayoutParams(GridLayout.spec(i), GridLayout.spec(j)));
            }
        }
    }

    public class TileClickListener implements View.OnClickListener {
        public int x;
        public int y;

        TileClickListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void onClick(View view) {
            if(isValidMove(x, y)) {
                buttons[emptyX][emptyY].setText(buttons[x][y].getText());
                buttons[x][y].setText("");
                emptyX = x;
                emptyY = y;
            }
            else {
                Toast.makeText(MainActivity.this, "Invalid move", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isValidMove(int x, int y) {
        return (Math.abs(emptyX - x) == 1 && emptyY == y) || (Math.abs(emptyY - y) == 1 && emptyX == x);
    }
}