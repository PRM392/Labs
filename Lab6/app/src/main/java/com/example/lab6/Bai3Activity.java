package com.example.lab6;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Bai3Activity extends AppCompatActivity {

    Button btnChonMau;
    ConstraintLayout manHinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai3);

        btnChonMau = findViewById(R.id.btnChonMau);
        manHinh = findViewById(R.id.manHinh);

        registerForContextMenu(btnChonMau);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuVang) {
            manHinh.setBackgroundColor(Color.YELLOW);
        } else if (id == R.id.menuDo) {
            manHinh.setBackgroundColor(Color.RED);
        } else if (id == R.id.menuXanh) {
            manHinh.setBackgroundColor(Color.BLUE);
        }
        return super.onContextItemSelected(item);
    }
}
