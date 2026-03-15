package com.example.demozalopay.Helper;

import android.annotation.SuppressLint;

import com.example.demozalopay.Helper.HMac.HMacUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Helpers {
    private static int transIdDefault = 1;

    @SuppressLint("DefaultLocale")
    public static String getAppTransId() {
        if (transIdDefault >= 100000) {
            transIdDefault = 1;
        }

        transIdDefault += 1;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatDateTime = new SimpleDateFormat("yyMMdd_hhmmss");
        String timeString = formatDateTime.format(new Date());
        return String.format("%s%06d", timeString, transIdDefault);
    }

    public static String getMac(String key, String data) {
        return Objects.requireNonNull(HMacUtil.hMacHexStringEncode(HMacUtil.HMACSHA256, key, data));
    }
}
