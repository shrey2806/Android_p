package com.example.mycredibleinfo;

import com.example.mycredibleinfo.PojoClasses.LoginAndSignup;
import com.example.mycredibleinfo.PojoClasses.ServerTest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {



    @GET("test")
    Call<ServerTest> getServerStatus();

    @POST("user/login")
    Call<LoginAndSignup> loginUser(@Body User user);

    @POST("user/signup")
    Call<LoginAndSignup> addNewUser(@Body User user);




}
