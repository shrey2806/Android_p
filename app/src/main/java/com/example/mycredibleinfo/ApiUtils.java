package com.example.mycredibleinfo;

import retrofit2.Retrofit;

public class ApiUtils {
    private ApiUtils(){

    }


    public static final String BASE_URL = "http://139.59.65.145:9090/";

    public static ApiService getUserService()
    {
        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }
}
