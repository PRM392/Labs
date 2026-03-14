package com.example.demozalopay;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demozalopay.Api.CreateOrder;

import org.json.JSONObject;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QrPaymentActivity extends AppCompatActivity {

    private ImageView imgQr;
    private TextView txtOrderUrl;
    private TextView txtToken;
    private Button btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cho phép gọi mạng trên main thread cho demo ZaloPay
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_qr_payment);

        imgQr = findViewById(R.id.imgQr);
        txtOrderUrl = findViewById(R.id.txtOrderUrl);
        txtToken = findViewById(R.id.txtToken);
        btnShare = findViewById(R.id.btnShare);

        String amount = getIntent().getStringExtra("amount");
        if (amount == null || amount.isEmpty()) {
            amount = "10000";
        }

        try {
            CreateOrder createOrder = new CreateOrder();
            JSONObject data = createOrder.createOrder(amount);
            if (data == null) {
                Toast.makeText(this, "Không tạo được đơn hàng", Toast.LENGTH_SHORT).show();
                return;
            }
            String returnCode = data.optString("returncode", data.optString("return_code", "-1"));
            if (!"1".equals(returnCode)) {
                Toast.makeText(this, "Tạo đơn hàng thất bại: " + returnCode, Toast.LENGTH_SHORT).show();
                return;
            }

            String orderUrl = data.optString("order_url", data.optString("orderurl", ""));
            final String token = data.optString("zptranstoken", data.optString("zp_trans_token", ""));

            if (orderUrl == null || orderUrl.isEmpty()) {
                Toast.makeText(this, "Không lấy được order_url để tạo QR", Toast.LENGTH_SHORT).show();
                return;
            }

            txtOrderUrl.setText(orderUrl);
            txtToken.setText(token);

            try {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.encodeBitmap(orderUrl, BarcodeFormat.QR_CODE, 600, 600);
                imgQr.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi tạo QR", Toast.LENGTH_SHORT).show();
            }

            String finalAmount = amount;
            btnShare.setOnClickListener(v -> {
                String shareContent = "Thanh toán đơn hàng thức ăn thú cưng số tiền " + finalAmount
                        + " qua ZaloPay.\nLink/QR: " + orderUrl
                        + (token.isEmpty() ? "" : ("\nMã giao dịch: " + token));
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
                startActivity(Intent.createChooser(shareIntent, "Chia sẻ mã thanh toán"));
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi tạo đơn hàng", Toast.LENGTH_SHORT).show();
        }
    }
}

