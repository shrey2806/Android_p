package com.example.mycredibleinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mycredibleinfo.APISettings.ApiService;
import com.example.mycredibleinfo.APISettings.ApiUtils;
import com.example.mycredibleinfo.EducationDetailPOJOS.EducationDetails;
import com.example.mycredibleinfo.EducationDetailPOJOS.EducationDetailsData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EducationDetailsActivity extends AppCompatActivity {

    private EditText org_etx,course_etx,location_etx;
    private Spinner start_year,end_year;
    private Button save;
    int id;
    ApiService mservice;
    private String organization, degree, location;
    private String startYear, endYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_details2);

        mservice= ApiUtils.getUserService();
        id=getIntent().getIntExtra("id",id);

        org_etx=findViewById(R.id.ed_organisation);
        course_etx=findViewById(R.id.ed_course);
        location_etx=findViewById(R.id.ed_location);

        start_year=findViewById(R.id.ed_spinner_start_year);
        end_year=findViewById(R.id.ed_spinner_end_year);

        save=findViewById(R.id.ed_save);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                organization=org_etx.getText().toString().trim();
                degree=course_etx.getText().toString().trim();
                location=location_etx.getText().toString().trim();
                startYear=start_year.getSelectedItem().toString();
                endYear=end_year.getSelectedItem().toString();

                EducationDetails educationDetails=new
                        EducationDetails(startYear,degree,organization,location,endYear);


                saveEducationDetails(educationDetails);


            }
        });


    }

    private void saveEducationDetails(EducationDetails educationDetails) {

        Call<EducationDetailsData> educationDetailsDataCal=mservice.addEducationDetails(id,educationDetails);

        educationDetailsDataCal.enqueue(new Callback<EducationDetailsData>() {
            @Override
            public void onResponse(Call<EducationDetailsData> call, Response<EducationDetailsData> response) {

                Intent i=new Intent(EducationDetailsActivity.this,ProfessionalDetailsActivity.class);
                i.putExtra("id",id);
                startActivity(i);
                finish();

            }

            @Override
            public void onFailure(Call<EducationDetailsData> call, Throwable t) {
                Log.d("Save Edu Details :","Failed "+t.getMessage().toString());

            }
        });



    }
}

