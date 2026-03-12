package com.prm.lab8;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.Date;

public class Ex3Activity extends AppCompatActivity {
    private static final String CHANNEL_ID = "lab8_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex3);

        findViewById(R.id.btnSendNotification).setOnClickListener(v -> sendNotification());
    }

    private void sendNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "Lab 8", NotificationManager.IMPORTANCE_DEFAULT));
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentTitle("Lab8_Ex3")
                .setContentText("Message push notification")
                .setColor(Color.RED)
                .setAutoCancel(true);

        // Dùng thời gian hiện tại làm ID để mỗi thông báo là độc lập [cite: 13]
        int dynamicId = (int) new Date().getTime();
        manager.notify(dynamicId, builder.build());
    }
}