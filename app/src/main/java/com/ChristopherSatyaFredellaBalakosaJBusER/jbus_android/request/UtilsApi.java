package com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.request;

public class UtilsApi {
    /*public static final String BASE_URL_API = "http://10.0.2.2:4000/";*/
    public static final String BASE_URL_API = "http://192.168.155.120:4000/";
    public static BaseApiService getApiService() {
        return
                RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
