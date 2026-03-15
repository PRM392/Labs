package com.example.demozalopay.Constant;

public class AppInfo {
    // Thông tin sandbox dùng thử (giữ từ config cũ trong payment)
    public static final int APP_ID = 554;
    public static final String APP_NAME = "Merchant Demo";
    public static final String MAC_KEY = "8NdU5pG5R2spGHGhyO99HN1OhD8IQJBn"; // Key1
    public static final String CALLBACK_KEY = "uUfsWgfLkRLzq6W2uNXTCxrfxs51auny"; // Key2 (verify callback trên backend)
    // OpenAPI v2 endpoint cho CreateOrder (sandbox)
    public static final String URL_CREATE_ORDER = "https://sb-openapi.zalopay.vn/v2/create";
}
