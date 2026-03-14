## Demo thanh toán ZaloPay sandbox – Android (Java)

### 1. Chuẩn bị tài khoản sandbox

- Đăng nhập **ZaloPay Sandbox Merchant Portal** và tạo app test.
- Lấy các thông tin:
  - `APP_ID`
  - `KEY1` (MAC key – dùng để ký `mac` khi gọi CreateOrder)
  - Endpoint sandbox `createorder` (ví dụ `https://sandbox.zalopay.com.vn/v001/tpe/createorder`).
- Cài ứng dụng **ZaloPay Sandbox** trên điện thoại test, nạp tiền thử theo hướng dẫn của ZaloPay.

### 2. Cấu hình Gradle & SDK

- Trong `app/build.gradle.kts`:
  - Thêm SDK ZaloPay (AAR local trong `app/libs`), OkHttp và ZXing:

```kotlin
dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    implementation(files("libs/zpdk-release-v3.1.aar"))
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
}
```

- Tải file `zpdk-release-v3.1.aar` từ repo ZaloPay và đặt vào `app/libs`.

### 3. Các lớp helper gọi CreateOrder

- Package: `com.example.demozalopay.payment`.

- `AppInfo.java`: cấu hình ZaloPay:

```java
public class AppInfo {
    public static final int APP_ID = 553;              // thay bằng APP_ID của bạn
    public static final String APP_NAME = "Merchant Demo";
    public static final String MAC_KEY = "KEY1_HERE";  // thay bằng KEY1 của bạn
    public static final String URL_CREATE_ORDER = "https://sandbox.zalopay.com.vn/v001/tpe/createorder";
}
```

- `Helpers.java` + `HMacUtil.java` + `HexStringUtil.java`:
  - Sinh `app_trans_id` dạng `yyMMdd_hhmmssXXXXXX`.
  - Tính HMAC SHA256:

```java
String inputHMac = String.format("%s|%s|%s|%s|%s|%s|%s",
        appId, appTransId, appUser, amount, appTime, embedData, items);
String mac = Helpers.getMac(AppInfo.MAC_KEY, inputHMac);
```

- `HttpProvider.java`:
  - Dùng OkHttp POST lên `AppInfo.URL_CREATE_ORDER` với header `Content-Type: application/x-www-form-urlencoded`, parse JSON và trả về `JSONObject`.

- `CreateOrder.java`:
  - Build body gồm các tham số `appid`, `appuser`, `apptime`, `amount`, `apptransid`, `embeddata`, `item`, `bankcode`, `description`, `mac`.
  - Gọi `HttpProvider.sendPost(...)` và trả về JSON kết quả.

### 4. Cấu hình AndroidManifest & deep link callback

- Trong `AndroidManifest.xml`:
  - Thêm quyền Internet:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

  - Khai báo deep link để ZaloPay callback về app:

```xml
<activity
    android:name=".MainActivity"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data
            android:scheme="demozpdk"
            android:host="app" />
    </intent-filter>
</activity>
```

  - Đăng ký thêm `QrPaymentActivity`:

```xml
<activity
    android:name=".QrPaymentActivity"
    android:exported="false" />
```

### 5. Flow thanh toán bằng ZaloPay SDK (in‑app)

- Layout `activity_main.xml`:
  - Text hiển thị tổng tiền.
  - Hai nút:
    - `btnPayZalo` – Thanh toán ZaloPay (App).
    - `btnPayQr` – Mở màn hình QR.

- `MainActivity.java`:
  - Khởi tạo SDK:

```java
ZaloPaySDK.init(AppInfo.APP_ID, Environment.SANDBOX);
```

  - Khi bấm **Thanh toán ZaloPay (App)**:
    - Gọi `CreateOrder.createOrder(amount)`.
    - Kiểm tra `returncode` hoặc `return_code` trong JSON:
    - Lấy `zptranstoken` hoặc `zp_trans_token`.
    - Gọi:

```java
ZaloPaySDK.getInstance().payOrder(this, token, "demozpdk://app", new PayOrderListener() {
    public void onPaymentSucceeded(String transactionId, String transToken, String appTransID) { ... }
    public void onPaymentCanceled(String zpTransToken, String appTransID) { ... }
    public void onPaymentError(ZaloPayError error, String zpTransToken, String appTransID) { ... }
});
```

  - Override `onNewIntent`:

```java
@Override
protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    ZaloPaySDK.getInstance().onResult(intent);
}
```

### 6. Flow QR/link + mã chia sẻ

- Layout `activity_qr_payment.xml`:
  - `ImageView imgQr` – hiển thị QR.
  - `TextView txtOrderUrl` – hiển thị `order_url`.
  - `TextView txtToken` – hiển thị `zp_trans_token`.
  - `Button btnShare` – chia sẻ link/mã.

- `QrPaymentActivity.java`:
  - Nhận `amount` từ `Intent`.
  - Gọi lại `CreateOrder.createOrder(amount)`.
  - Lấy:

```java
String orderUrl = data.optString("order_url", data.optString("orderurl", ""));
String token = data.optString("zptranstoken", data.optString("zp_trans_token", ""));
```

  - Tạo QR bằng ZXing:

```java
BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
Bitmap bitmap = barcodeEncoder.encodeBitmap(orderUrl, BarcodeFormat.QR_CODE, 600, 600);
imgQr.setImageBitmap(bitmap);
```

  - Nút chia sẻ:

```java
String shareContent = "Thanh toán đơn hàng ...\nLink/QR: " + orderUrl + "\nMã giao dịch: " + token;
Intent shareIntent = new Intent(Intent.ACTION_SEND);
shareIntent.setType("text/plain");
shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
startActivity(Intent.createChooser(shareIntent, "Chia sẻ mã thanh toán"));
```

### 7. Cách test trên sandbox

1. Build & run app trên thiết bị Android đã cài ZaloPay Sandbox.
2. Mở app, chọn sản phẩm demo (ở đây fix cứng 10.000 VND).
3. Nhấn **Thanh toán ZaloPay (App)**:
   - App sẽ gọi CreateOrder, mở ZaloPay Sandbox.
   - Đăng nhập ví test và xác nhận thanh toán.
   - Sau khi thanh toán xong, ZaloPay quay lại app qua `demozpdk://app` và callback `PayOrderListener`.
4. Nhấn **Thanh toán bằng QR ZaloPay**:
   - App tạo order mới, hiển thị QR và `order_url`.
   - Dùng một thiết bị khác (hoặc cùng thiết bị nếu hỗ trợ) mở ZaloPay Sandbox, quét QR và thanh toán.
   - Có thể bấm **Chia sẻ link/mã thanh toán** để gửi link/mã cho người khác.

### 8. Troubleshooting thường gặp

- **Không tạo được đơn hàng (`returncode != 1`)**:
  - Kiểm tra lại `APP_ID`, `MAC_KEY`, endpoint `URL_CREATE_ORDER`.
  - Kiểm tra format chuỗi HMAC có đúng thứ tự tham số theo tài liệu.
- **Không mở được ZaloPay app**:
  - Kiểm tra đã cài đúng app **ZaloPay Sandbox**.
  - Kiểm tra scheme callback `"demozpdk://app"` trùng với cấu hình trong `payOrder` và `AndroidManifest`.
- **QR không hiển thị**:
  - In log `orderUrl` để chắc chắn endpoint trả đúng link.
  - Kiểm tra thiết bị có đủ bộ nhớ, không bị lỗi khi encode QR. 
 
---

### 9. Workflow chi tiết end-to-end

#### 9.1. Người dùng bấm “Thanh toán ZaloPay (App)”

1. Người dùng đang ở `MainActivity`.
2. Bấm nút **Thanh toán ZaloPay (App)** (`btnPayZalo`).
3. `onClick` gọi:
   - Khởi tạo `CreateOrder`.
   - Gọi `createOrder(amount)` để tạo đơn trên server ZaloPay.
4. `CreateOrder`:
   - Sinh `app_trans_id` duy nhất bằng `Helpers.getAppTransId()`.
   - Ghép dữ liệu HMAC theo format:  
     `app_id|app_trans_id|app_user|amount|app_time|embed_data|item`.
   - Ký HMAC SHA256 bằng `MAC_KEY` (Key1) trong `AppInfo`.
   - Gửi POST form lên `https://sb-openapi.zalopay.vn/v2/create`.
5. ZaloPay trả về JSON chứa:
   - `return_code`, `return_message`, `sub_return_code`, ...
   - `zp_trans_token`, `order_url`, `qr_code`, ...
6. `MainActivity` đọc:
   - `returnCode = data.optString("returncode", data.optString("return_code", "-1"))`.
   - Nếu `returnCode == "1"`:
     - Lấy `zp_trans_token` (dùng key `"zptranstoken"` hoặc `"zp_trans_token"`).
     - Gọi `ZaloPaySDK.getInstance().payOrder(...)` để mở app ZaloPay.
7. Người dùng thanh toán trong app ZaloPay Sandbox.
8. Sau khi thanh toán xong, ZaloPay gọi lại app qua deep link `demozpdk://app`:
   - Android đưa Intent mới về `MainActivity.onNewIntent`.
   - `ZaloPaySDK.getInstance().onResult(intent)` phân tích kết quả.
   - `PayOrderListener` được gọi:
     - `onPaymentSucceeded` → hiển thị Toast “Thanh toán thành công”.
     - `onPaymentCanceled` → Toast “Thanh toán bị hủy”.
     - `onPaymentError` → Toast “Thanh toán thất bại”.

#### 9.2. Người dùng bấm “Thanh toán bằng QR ZaloPay”

1. Vẫn từ `MainActivity`, người dùng bấm **Thanh toán bằng QR ZaloPay** (`btnPayQr`).
2. App mở `QrPaymentActivity` và truyền `amount`.
3. `QrPaymentActivity.onCreate`:
   - Lấy `amount` từ Intent (mặc định `"10000"` nếu null).
   - Gọi `CreateOrder.createOrder(amount)` giống như flow trên.
   - Kiểm tra `returnCode`:
     - Nếu khác `"1"` → Toast “Tạo đơn hàng thất bại: <mã>”.
4. Nếu thành công:
   - Lấy `order_url` (URL thanh toán) và `zp_trans_token`.
   - Gán:
     - `txtOrderUrl.setText(orderUrl)`.
     - `txtToken.setText(token)`.
   - Generate QR từ `order_url`:
     - Dùng `BarcodeEncoder.encodeBitmap(orderUrl, BarcodeFormat.QR_CODE, 600, 600)`.
     - Gán vào `imgQr`.
5. Người dùng dùng app ZaloPay Sandbox (trên cùng hoặc máy khác) để quét QR:
   - ZaloPay hiển thị thông tin, xác nhận thanh toán.
6. Nút **Chia sẻ link/mã thanh toán**:
   - Tạo chuỗi text chứa `amount`, `order_url`, `zp_trans_token`.
   - Mở Intent `ACTION_SEND` để chia sẻ qua Zalo, Messenger, v.v.

#### 9.3. Từ màn hình bán hàng sang thanh toán

- Ở đề tài bán hàng thức ăn chó mèo, bạn có thể:
  - Tính tổng tiền giỏ hàng (ví dụ `cartTotal`).
  - Khi bấm “Thanh toán”, gọi:

```java
Intent intent = new Intent(CurrentActivity.this, MainActivity.class);
intent.putExtra("amount", String.valueOf(cartTotal));
startActivity(intent);
```

- Trong `MainActivity`, thay biến `amount` cố định bằng giá trị lấy từ Intent:

```java
private String amount;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // ...
    amount = getIntent().getStringExtra("amount");
    if (amount == null || amount.isEmpty()) {
        amount = "10000";
    }
}
```

- Tương tự, khi mở `QrPaymentActivity` bạn truyền đúng `amount` từ giỏ hàng để QR thể hiện đúng số tiền.
