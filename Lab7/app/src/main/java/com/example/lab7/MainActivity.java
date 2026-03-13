package com.example.lab7;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnRequestPermission = findViewById(R.id.btn_request_permission);
        Button btnOpenSettingsPermission = findViewById(R.id.btn_open_settings_permission);

        btnRequestPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickRequestPermission();
            }
        });

        btnOpenSettingsPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickOpenSettingsPermission();
            }
        });
    }

    private void ClickRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            // Check if user has previously selected "Don't ask again"
            if (getSharedPreferences("PermissionPrefs", MODE_PRIVATE).getBoolean("dont_ask_again", false)) {
                Toast.makeText(this, "Permission Denied (Don't ask again checked)", Toast.LENGTH_SHORT).show();
                return;
            }
            showCustomPermissionDialog();
        }
    }

    private void showCustomPermissionDialog() {
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_permission, null);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        TextView tvMessage = dialogView.findViewById(R.id.tv_message);
        tvMessage.setText(android.text.Html.fromHtml("Allow <b>PermissionAndroid</b> to access this device's location?"));

        TextView btnDeny = dialogView.findViewById(R.id.btn_deny);
        TextView btnAllow = dialogView.findViewById(R.id.btn_allow);
        CheckBox cbDontAskAgain = dialogView.findViewById(R.id.cb_dont_ask_again);

        btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbDontAskAgain.isChecked()) {
                    getSharedPreferences("PermissionPrefs", MODE_PRIVATE)
                            .edit()
                            .putBoolean("dont_ask_again", true)
                            .apply();
                }
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        });

        btnAllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void ClickOpenSettingsPermission() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}