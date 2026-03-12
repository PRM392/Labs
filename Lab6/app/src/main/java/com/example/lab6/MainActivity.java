package com.example.lab6;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import android.content.Intent;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


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

        Button btnBai2 = findViewById(R.id.btnBai2);
        btnBai2.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Bai2Activity.class);
            startActivity(intent);
        });

        Button btnBai3 = findViewById(R.id.btnBai3);
        btnBai3.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Bai3Activity.class);
            startActivity(intent);
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        String itemName = item.getTitle().toString();
        Toast.makeText(this, "Bạn đã chọn item " + itemName, Toast.LENGTH_SHORT).show();
        
        return super.onOptionsItemSelected(item);
    }
}