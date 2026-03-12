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

public class Ex2Activity extends AppCompatActivity {
    private static final String CHANNEL_ID = "lab8_channel";
    private static final int NOTIFICATION_ID = 2; // ID cố định

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex2);

        findViewById(R.id.btnSendNotification).setOnClickListener(v -> sendNotification());
    }

    private void sendNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "Lab 8", NotificationManager.IMPORTANCE_DEFAULT));
        }

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.red_noti);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_notify_chat) // Nên dùng icon trong suốt để màu hiển thị đúng
                .setLargeIcon(bitmap)
                .setContentTitle("Lab8_Ex2")
                .setContentText("Message push notification")
                .setColor(Color.RED) // Thêm màu đỏ [cite: 11]
                .setAutoCancel(true);

        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
