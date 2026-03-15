package com.example.demozalopay;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.demozalopay.Constant.AppInfo;
import com.example.demozalopay.Api.CreateOrder;

import org.json.JSONObject;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class MainActivity extends AppCompatActivity {

    private Button btnPayZalo;
    private TextView txtProductName;
    private TextView txtQuantityValue;
    private TextView txtTotalValue;

    private String amountStr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cho phép gọi mạng trên main thread cho demo ZaloPay
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo ZaloPay SDK ở môi trường SANDBOX
        ZaloPaySDK.init(AppInfo.APP_ID, Environment.SANDBOX);

        // Nếu activity được mở bằng deep link sau khi thanh toán, xử lý luôn kết quả
        if (getIntent() != null && getIntent().getData() != null) {
            ZaloPaySDK.getInstance().onResult(getIntent());
        }

        btnPayZalo = findViewById(R.id.btnPayZalo);
        txtProductName = findViewById(R.id.txtProductName);
        txtQuantityValue = findViewById(R.id.txtQuantityValue);
        txtTotalValue = findViewById(R.id.txtTotalValue);

        // Nhận dữ liệu từ màn nhập số lượng
        Intent srcIntent = getIntent();
        int quantity = srcIntent.getIntExtra("quantity", 0);
        int amount = srcIntent.getIntExtra("amount", 0);
        String productName = srcIntent.getStringExtra("product_name");
        if (productName == null || productName.isEmpty()) {
            productName = "Chú chim chích chòe";
        }
        if (quantity <= 0) {
            quantity = 0;
        }

        amountStr = amount > 0 ? String.valueOf(amount) : null;

        txtProductName.setText(productName);
        txtQuantityValue.setText(String.valueOf(quantity));
        txtTotalValue.setText(formatCurrency(amount));

        btnPayZalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateOrder orderApi = new CreateOrder();
                try {
                    if (amountStr == null) {
                        Toast.makeText(MainActivity.this, "Vui lòng quay lại và nhập số lượng trước khi thanh toán", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject data = orderApi.createOrder(amountStr);
                    if (data == null) {
                        Toast.makeText(MainActivity.this, "Không tạo được đơn hàng", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String returnCode = data.optString("returncode", data.optString("return_code", "-1"));
                    if ("1".equals(returnCode)) {
                        String token = data.optString("zptranstoken", data.optString("zp_trans_token", ""));
                        if (token.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Không lấy được zp_trans_token", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        ZaloPaySDK.getInstance().payOrder(MainActivity.this, token, "demozpdk://app",
                                new PayOrderListener() {
                                    @Override
                                    public void onPaymentSucceeded(String transactionId, String transToken, String appTransID) {
                                        openResultScreen("Thanh toán thành công");
                                    }

                                    @Override
                                    public void onPaymentCanceled(String zpTransToken, String appTransID) {
                                        openResultScreen("Thanh toán bị hủy");
                                    }

                                    @Override
                                    public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                                        openResultScreen("Thanh toán thất bại");
                                    }
                                });
                    } else {
                        Toast.makeText(MainActivity.this, "Tạo đơn hàng thất bại: " + returnCode, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Lỗi tạo đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    private String formatCurrency(int value) {
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance(java.util.Locale.forLanguageTag("vi-VN"));
        return nf.format(value) + " VND";
    }

    private void openResultScreen(String message) {
        Intent intent = new Intent(MainActivity.this, PaymentResultActivity.class);
        intent.putExtra("message", message);
        startActivity(intent);
    }
}