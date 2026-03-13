package com.prm.lab9;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnBai1).setOnClickListener(v -> startActivity(new Intent(this, Ex1Activity.class)));
        findViewById(R.id.btnBai2).setOnClickListener(v -> startActivity(new Intent(this, Ex2Activity.class)));
        findViewById(R.id.btnBai3).setOnClickListener(v -> startActivity(new Intent(this, Ex3Activity.class)));
        findViewById(R.id.btnBai4).setOnClickListener(v -> startActivity(new Intent(this, Ex4Activity.class)));
        findViewById(R.id.btnBai5).setOnClickListener(v -> startActivity(new Intent(this, Ex5Activity.class)));
    }
}