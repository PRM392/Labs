package com.example.lab11;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhone, etGender;
    private Button btnSave, btnViewList;
    private TraineeService traineeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etGender = findViewById(R.id.etGender);
        btnSave = findViewById(R.id.btnSave);
        btnViewList = findViewById(R.id.btnViewList);

        traineeService = TraineeRepository.getTraineeService();

        // Nút POST để gửi dữ liệu
        btnSave.setOnClickListener(v -> saveTrainee());

        // Mở Activity Xem List
        btnViewList.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TraineeListActivity.class);
            startActivity(intent);
        });
    }

    private void saveTrainee() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String gender = etGender.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        Trainee newTrainee = new Trainee(name, email, phone, gender);

        // Gọi hàm Callback bất đồng bộ
        Call<Trainee> call = traineeService.createTrainees(newTrainee);
        call.enqueue(new Callback<Trainee>() {
            @Override
            public void onResponse(Call<Trainee> call, Response<Trainee> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(MainActivity.this, "Save successfully", Toast.LENGTH_SHORT).show();
                    // Clear fields
                    etName.setText("");
                    etEmail.setText("");
                    etPhone.setText("");
                    etGender.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Save Fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Trainee> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Save Fail: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}