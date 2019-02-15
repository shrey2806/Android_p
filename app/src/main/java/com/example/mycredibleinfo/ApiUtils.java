package com.example.mycredibleinfo;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {

    private ApiUtils(){

    }


    public static final String BASE_URL = "http://139.59.65.145:9090/";
    private static Retrofit retrofit = null;
    public static ApiService getUserService()
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);


        //return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }
}
