package com.example.mycredibleinfo.APISettings;

import com.example.mycredibleinfo.EducationDetailPOJOS.EducationDetails;
import com.example.mycredibleinfo.EducationDetailPOJOS.EducationDetailsData;
import com.example.mycredibleinfo.PersonalDetailPOJOS.PersonalDetail;
import com.example.mycredibleinfo.PersonalDetailPOJOS.PersonalDetails2;
import com.example.mycredibleinfo.PersonalDetailPOJOS.Photo;
import com.example.mycredibleinfo.PersonalDetails;
import com.example.mycredibleinfo.PojoClasses.LoginAndSignup;
import com.example.mycredibleinfo.PojoClasses.ServerTest;
import com.example.mycredibleinfo.ProfessionalDetailPOJOS.ProfessionalDetailData;
import com.example.mycredibleinfo.ProfessionalDetailPOJOS.ProfessionalDetails;
import com.example.mycredibleinfo.StatusMessagePojo;
import com.example.mycredibleinfo.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {



        @GET("test")
        Call<ServerTest> getServerStatus();

        @POST("user/login")
        Call<LoginAndSignup> loginUser(@Body User user);

        @POST("user/signup")
        Call<LoginAndSignup> addNewUser(@Body User user);


        @POST("user/personaldetail/{id}")
        Call<PersonalDetail> addPesonalDetails(@Path("id")int id, @Body PersonalDetails2 personalDetails2);


        @POST("user/educationdetail/{id}")
        Call<EducationDetailsData> addEducationDetails(@Path("id")int id,@Body EducationDetails educationDetails);


        @POST("user/professionaldetail/{id}")
        Call<ProfessionalDetailData> addProfessionalDetails(@Path("id")int id, @Body ProfessionalDetails professionalDetails);



        @POST("user/personaldetail/pp/post")
        Call<StatusMessagePojo> addPhoto(@Body Photo photo);
}
