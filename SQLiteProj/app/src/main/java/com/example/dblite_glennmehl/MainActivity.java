package com.example.dblite_glennmehl;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText editSite, editUser, editPassword;
    Button insertBtn, retrieveBtn;
    DatabaseHelper db;

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

        editSite = findViewById(R.id.editSite);
        editUser = findViewById(R.id.editUser);
        editPassword = findViewById(R.id.editPassword);
        insertBtn = findViewById(R.id.insertBtn);
        retrieveBtn = findViewById(R.id.retrieveBtn);
        db = new DatabaseHelper(this);

        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean inserted = db.insertAccount(
                        editSite.getText().toString(),
                        editUser.getText().toString(),
                        editPassword.getText().toString());

                Toast.makeText(MainActivity.this,
                        inserted ? "Inserted Successfully" : "Insert Failed",
                        Toast.LENGTH_SHORT).show();
            }
        });

        retrieveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = db.retrieveAccount(editSite.getText().toString());
                if (cursor != null && cursor.moveToFirst()) {
                    editUser.setText(cursor.getString(1));
                    editPassword.setText(cursor.getString(2));
                    Toast.makeText(MainActivity.this, "Data Retrieved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Site Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}