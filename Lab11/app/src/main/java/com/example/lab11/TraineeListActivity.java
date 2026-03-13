package com.example.lab11;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TraineeListActivity extends AppCompatActivity {

    private Spinner spinnerId;
    private EditText etUpdateName, etUpdateEmail, etUpdatePhone, etUpdateGender;
    private Button btnUpdate, btnDelete;
    private ListView listViewTrainees;

    private TraineeService traineeService;
    private List<Trainee> traineeList = new ArrayList<>();
    private ArrayAdapter<Trainee> arrayAdapter;
    private Trainee selectedTrainee = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_list);

        // Ánh xạ
        spinnerId = findViewById(R.id.spinnerId);
        etUpdateName = findViewById(R.id.etUpdateName);
        etUpdateEmail = findViewById(R.id.etUpdateEmail);
        etUpdatePhone = findViewById(R.id.etUpdatePhone);
        etUpdateGender = findViewById(R.id.etUpdateGender);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        listViewTrainees = findViewById(R.id.listViewTrainees);

        traineeService = TraineeRepository.getTraineeService();

        // Setup Adapter: Sử dụng chung cho cả Spinner và ListView
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, traineeList);
        spinnerId.setAdapter(arrayAdapter);
        listViewTrainees.setAdapter(arrayAdapter);

        // Khi chọn 1 Trainee trong Spinner, Fill dữ liệu xuống các ô EditTexts
        spinnerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTrainee = traineeList.get(position);
                etUpdateName.setText(selectedTrainee.getName());
                etUpdateEmail.setText(selectedTrainee.getEmail());
                etUpdatePhone.setText(selectedTrainee.getPhone());
                etUpdateGender.setText(selectedTrainee.getGender());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedTrainee = null;
            }
        });

        btnUpdate.setOnClickListener(v -> updateTrainee());
        btnDelete.setOnClickListener(v -> deleteTrainee());

        // Lấy dữ liệu ngay khi vừa mở Activity
        getAllTrainees();
    }

    private void getAllTrainees() {
        Call<List<Trainee>> call = traineeService.getAllTrainees();
        call.enqueue(new Callback<List<Trainee>>() {
            @Override
            public void onResponse(Call<List<Trainee>> call, Response<List<Trainee>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    traineeList.clear();
                    traineeList.addAll(response.body());
                    arrayAdapter.notifyDataSetChanged(); // Cập nhật lại UI Danh sách
                }
            }

            @Override
            public void onFailure(Call<List<Trainee>> call, Throwable t) {
                Toast.makeText(TraineeListActivity.this, "Lỗi lấy danh sách", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTrainee() {
        if (selectedTrainee == null) return;

        String uName = etUpdateName.getText().toString().trim();
        String uEmail = etUpdateEmail.getText().toString().trim();
        String uPhone = etUpdatePhone.getText().toString().trim();
        String uGender = etUpdateGender.getText().toString().trim();

        Trainee updatedTrainee = new Trainee(uName, uEmail, uPhone, uGender);

        Call<Trainee> call = traineeService.updateTrainees(selectedTrainee.getId(), updatedTrainee);
        call.enqueue(new Callback<Trainee>() {
            @Override
            public void onResponse(Call<Trainee> call, Response<Trainee> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TraineeListActivity.this, "Update successfully", Toast.LENGTH_SHORT).show();
                    getAllTrainees(); // Cập nhật lại và hiển thị lên danh sách
                } else {
                    Toast.makeText(TraineeListActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Trainee> call, Throwable t) {
                Toast.makeText(TraineeListActivity.this, "Lỗi update", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteTrainee() {
        if (selectedTrainee == null) return;

        Call<Trainee> call = traineeService.deleteTrainees(selectedTrainee.getId());
        call.enqueue(new Callback<Trainee>() {
            @Override
            public void onResponse(Call<Trainee> call, Response<Trainee> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TraineeListActivity.this, "Delete successfully", Toast.LENGTH_SHORT).show();
                    
                    // Clear dữ liệu trên Input sau khi xóa
                    etUpdateName.setText("");
                    etUpdateEmail.setText("");
                    etUpdatePhone.setText("");
                    etUpdateGender.setText("");
                    
                    getAllTrainees(); // Cập nhật lại và hiển thị lên danh sách
                } else {
                    Toast.makeText(TraineeListActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Trainee> call, Throwable t) {
                Toast.makeText(TraineeListActivity.this, "Lỗi delete", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
