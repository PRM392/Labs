package com.example.lab6;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.PopupMenu;
import androidx.appcompat.app.AppCompatActivity;

public class Bai2Activity extends AppCompatActivity {

    Button btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai2);

        btnMenu = findViewById(R.id.btnMenu);

        btnMenu.setOnClickListener(v -> ShowMenu());
    }

    private void ShowMenu() {
        PopupMenu popupMenu = new PopupMenu(this, btnMenu);
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
        
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.menuThem) {
                    btnMenu.setText("Menu Them");
                } else if (id == R.id.menuSua) {
                    btnMenu.setText("Menu Sua");
                } else if (id == R.id.menuXoa) {
                    btnMenu.setText("Menu Xoa");
                }
                return false;
            }
        });
        popupMenu.show();
    }
}
