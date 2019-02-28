package com.example.mycredibleinfo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycredibleinfo.APISettings.ApiService;
import com.example.mycredibleinfo.APISettings.ApiUtils;
import com.example.mycredibleinfo.EducationDetailPOJOS.EducationDetails;
import com.example.mycredibleinfo.EducationDetailPOJOS.EducationDetailsData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mycredibleinfo.MainActivity.MY_PREF;

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
        //id=getIntent().getIntExtra("id",id);
        SharedPreferences preferences=this.getSharedPreferences(MY_PREF,MODE_PRIVATE);
        id=preferences.getInt("userid",0);

        org_etx=(EditText) findViewById(R.id.prd_organisation);
        course_etx=(EditText)findViewById(R.id.pr_designation);
        location_etx=(EditText)findViewById(R.id.ed_location);

        start_year=(Spinner)findViewById(R.id.ed_spinner_start_year);
        end_year=(Spinner)findViewById(R.id.ed_spinner_end_year);

        save=(Button)findViewById(R.id.ed_save);

        Toolbar mtoolbar=findViewById(R.id.education_toolbar);

        final String isUpdate = getIntent().getStringExtra("isUpdate");
        if (isUpdate == null)
            mtoolbar.setTitle("Set Educational Details");
        else {
            mtoolbar.setTitle("Edit Educational Details");
            getEducationalDetails();
            // getProfilePic();
        }
        

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


//                saveEducationDetails(educationDetails);

                if (isUpdate == null)
                    saveEducationDetails(educationDetails);
                else {
                    updateEducationDetails(educationDetails);
                }


            }
        });


    }

    private void updateEducationDetails(EducationDetails educationDetails) {

        Call<EducationDetailsData> call = mservice.updateEducationalDetails(id, educationDetails);
        call.enqueue(new Callback<EducationDetailsData>() {
            @Override
            public void onResponse(Call<EducationDetailsData> call, Response<EducationDetailsData> response) {
                showToast("Education Details Updated", Toast.LENGTH_SHORT);
                Intent intent = new Intent(EducationDetailsActivity.this, DisplayActivity.class);
                finish();
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<EducationDetailsData> call, Throwable t) {
                showToast("Update Education details Failed: " + t.getMessage(), Toast.LENGTH_SHORT);
            }
        });



    }

    private void getEducationalDetails() {

        Call<EducationDetailsData> call = mservice.retrieveEducationalDetails(id);
        call.enqueue(new Callback<EducationDetailsData>() {
            @Override
            public void onResponse(Call<EducationDetailsData> call, Response<EducationDetailsData> response) {
                if(response.body() != null) {
                    org_etx.setText(response.body().getData().getOrganisation());
                    course_etx.setText(response.body().getData().getDegree());
                    location_etx.setText(response.body().getData().getLocation());
                    start_year.setSelection(getIndex(start_year, response.body().getData().getStart_year()));
                    end_year.setSelection(getIndex(end_year, response.body().getData().getEnd_year()));
                } else {
                    showToast("Professional Details Response Empty", Toast.LENGTH_LONG);
                }
            }
            @Override
            public void onFailure(Call<EducationDetailsData> call, Throwable t) {
                showToast("Response Failed: " + t.getMessage(), Toast.LENGTH_SHORT);
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

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }

    public void showToast(String msg, int length)
    {
        Toast.makeText(this, msg, length).show();
    }



}

