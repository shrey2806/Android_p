package com.example.mycredibleinfo.APISettings;

import com.example.mycredibleinfo.EducationDetailPOJOS.EducationDetails;
import com.example.mycredibleinfo.EducationDetailPOJOS.EducationDetailsData;
import com.example.mycredibleinfo.PersonalDetailPOJOS.PersonalDetail;
import com.example.mycredibleinfo.PersonalDetailPOJOS.PersonalDetails2;

import com.example.mycredibleinfo.PersonalDetails;
import com.example.mycredibleinfo.PojoClasses.LoginAndSignup;
import com.example.mycredibleinfo.PojoClasses.ServerTest;
import com.example.mycredibleinfo.ProfessionalDetailPOJOS.ProfessionalDetailData;
import com.example.mycredibleinfo.ProfessionalDetailPOJOS.ProfessionalDetails;
import com.example.mycredibleinfo.ProfilePicPojo.Photo;
import com.example.mycredibleinfo.StatusMessagePojo;
import com.example.mycredibleinfo.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {



        @GET("test")
        Call<ServerTest> getServerStatus();

        @POST("user/login")
        Call<LoginAndSignup> loginUser(@Body User user);

        @POST("user/signup")
        Call<LoginAndSignup> addNewUser(@Body User user);

        @GET("user/personaldetail/{id}")
        Call<PersonalDetail> retrievePersonalDetails(@Path("id") int id);


        @POST("user/personaldetail/{id}")
        Call<PersonalDetail> addPersonalDetails(@Path("id")int id, @Body PersonalDetails2 personalDetails2);

        @PUT("user/personaldetail/{id}")
        Call<PersonalDetail> updatePersonalDetails(@Path("id") int id, @Body PersonalDetails2 personalDetails2);

       @POST("user/personaldetail/pp/post")
        Call<StatusMessagePojo> addPhoto(@Body Photo photo);


        @GET("user/professionaldetail/{id}")
        Call<ProfessionalDetailData> retrieveProfessionalDetails(@Path("id") int id);


        @POST("user/educationdetail/{id}")
        Call<EducationDetailsData> addEducationDetails(@Path("id")int id,@Body EducationDetails educationDetails);

        @GET("user/educationdetail/{id}")
        Call<EducationDetailsData> retrieveEducationalDetails(@Path("id") int id);

        @PUT("user/educationdetail/{id}")
        Call<EducationDetailsData> updateEducationalDetails(@Path("id") int id, @Body EducationDetails educationDetails);

        @PUT("user/professionaldetail/{id}")
        Call<ProfessionalDetailData> updateProfessionalDetails(@Path("id") int id, @Body ProfessionalDetails professionalDetails);

        @POST("user/professionaldetail/{id}")
        Call<ProfessionalDetailData> addProfessionalDetails(@Path("id")int id, @Body ProfessionalDetails professionalDetails);








}
