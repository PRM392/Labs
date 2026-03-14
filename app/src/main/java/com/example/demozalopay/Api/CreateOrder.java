package com.example.demozalopay.Api;

import org.json.JSONObject;

import java.util.Date;

import okhttp3.FormBody;
import okhttp3.RequestBody;

import com.example.demozalopay.Constant.AppInfo;
import com.example.demozalopay.Helper.Helpers;

public class CreateOrder {
    private static class CreateOrderData {
        String appId;
        String appUser;
        String appTime;
        String amount;
        String appTransId;
        String embedData;
        String items;
        String bankCode;
        String description;
        String mac;

        private CreateOrderData(String amount) {
            long appTimeMillis = new Date().getTime();
            appId = String.valueOf(AppInfo.APP_ID);
            appUser = AppInfo.APP_NAME;
            appTime = String.valueOf(appTimeMillis);
            this.amount = amount;
            appTransId = Helpers.getAppTransId();
            embedData = "{}";
            items = "[]";
            bankCode = "zalopayapp";
            description = "Thanh toán thức ăn thú cưng - #" + Helpers.getAppTransId();

            // OpenAPI v2: app_id|app_trans_id|app_user|amount|app_time|embed_data|item
            String inputHMac = String.format("%s|%s|%s|%s|%s|%s|%s",
                    appId,
                    appTransId,
                    appUser,
                    this.amount,
                    appTime,
                    embedData,
                    items);

            mac = Helpers.getMac(AppInfo.MAC_KEY, inputHMac);
        }
    }

    public JSONObject createOrder(String amount) throws Exception {
        CreateOrderData input = new CreateOrderData(amount);

        RequestBody formBody = new FormBody.Builder()
                .add("app_id", input.appId)
                .add("app_user", input.appUser)
                .add("app_time", input.appTime)
                .add("amount", input.amount)
                .add("app_trans_id", input.appTransId)
                .add("embed_data", input.embedData)
                .add("item", input.items)
                .add("bank_code", input.bankCode)
                .add("description", input.description)
                .add("mac", input.mac)
                .build();

        return HttpProvider.sendPost(AppInfo.URL_CREATE_ORDER, formBody);
    }
}

