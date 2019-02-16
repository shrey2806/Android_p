package com.example.mycredibleinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mycredibleinfo.APISettings.ApiService;
import com.example.mycredibleinfo.APISettings.ApiUtils;
import com.example.mycredibleinfo.PersonalDetailPOJOS.PersonalDetails2;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalDetails extends AppCompatActivity {

    EditText fullname_etx, email_etx,mobile_etx,location_etx,links_etx;
    Button save;
    ApiService mservice;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

         id=Integer.valueOf(getIntent().getStringExtra("id"));

        fullname_etx=findViewById(R.id.prd_organisation);
        email_etx=findViewById(R.id.prd_designation);
        mobile_etx=findViewById(R.id.pd_mobile);
        location_etx=findViewById(R.id.ed_location);
        links_etx=findViewById(R.id.pd_links);
        save=findViewById(R.id.pd_save);

        mservice= ApiUtils.getUserService();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname=fullname_etx.getText().toString().trim();
                String email=email_etx.getText().toString().trim();
                String mobile=mobile_etx.getText().toString().trim();
                String location=location_etx.getText().toString().trim();
                String links=links_etx.getText().toString().trim();


                if(!TextUtils.isEmpty(fullname) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(mobile) &&!TextUtils.isEmpty(location)){


                    PersonalDetails2 obj=new PersonalDetails2(null,mobile,fullname,links,location,email);
                    savePersonalDetails(obj);



                }else{
                    Toast.makeText(PersonalDetails.this,"Fields cannot be Empty",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void savePersonalDetails(PersonalDetails2 obj) {
        Call<PersonalDetails> personalDetailsCall=mservice.addPesonalDetails(id,obj);
        personalDetailsCall.enqueue(new Callback<PersonalDetails>() {
            @Override
            public void onResponse(Call<PersonalDetails> call, Response<PersonalDetails> response) {

                Intent i=new Intent(PersonalDetails.this, EducationDetailsActivity.class);
                i.putExtra("id",id);
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(Call<PersonalDetails> call, Throwable t) {
                Log.d("Save Personal Details :","Failed "+t.getMessage().toString());
            }
        });





    }
}
