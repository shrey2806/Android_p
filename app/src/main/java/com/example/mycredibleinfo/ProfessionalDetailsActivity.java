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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycredibleinfo.APISettings.ApiService;
import com.example.mycredibleinfo.APISettings.ApiUtils;
import com.example.mycredibleinfo.ProfessionalDetailPOJOS.ProfessionalDetailData;
import com.example.mycredibleinfo.ProfessionalDetailPOJOS.ProfessionalDetails;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static com.example.mycredibleinfo.MainActivity.MY_PREF;

public class ProfessionalDetailsActivity extends AppCompatActivity {

    EditText organizationEditText, designationEditText;
    Spinner startMonthSpinner, startYearSpinner, endMonthSpinner, endYearSpinner;
    CheckBox currCheckBox;

    String organization, designation;
    String startMonth, startYear, endMonth, endYear;
    private String startDate,endDate;
    private Button save;

    TextView endtext;

    int id;

    ApiService mservice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_details);

        mservice= ApiUtils.getUserService();

        organizationEditText=(EditText)findViewById(R.id.prd_organisation);
        designationEditText=(EditText)findViewById(R.id.pr_designation);
        startMonthSpinner=(Spinner)findViewById(R.id.prd_start_spinner_month);
        startYearSpinner=(Spinner)findViewById(R.id.prd_start_spinner_year);
        endMonthSpinner=(Spinner)findViewById(R.id.prd_end_spinner_month);
        endYearSpinner=(Spinner)findViewById(R.id.prd_end_spinner_year);
        endtext=findViewById(R.id.end_text);
        save=(Button)findViewById(R.id.prd_save);


        currCheckBox=(CheckBox) findViewById(R.id.prd_checkbox);

        currCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currCheckBox.isChecked()){
                    endMonthSpinner.setVisibility(GONE);
                    endYearSpinner.setVisibility(GONE);
                    endtext.setVisibility(GONE);
                }
                else if(!currCheckBox.isChecked()){
                    endMonthSpinner.setVisibility(View.VISIBLE);
                    endYearSpinner.setVisibility(View.VISIBLE);
                    endtext.setVisibility(View.VISIBLE);
                }
            }
        });

       // id=getIntent().getIntExtra("id",0);
        SharedPreferences preferences=this.getSharedPreferences(MY_PREF,MODE_PRIVATE);
        id=preferences.getInt("userid",0);


        Toolbar mtoolbar=findViewById(R.id.profess_toolbar);

        final String isUpdate = getIntent().getStringExtra("isUpdate");
        if (isUpdate == null)
            mtoolbar.setTitle("Set Proffessional Details");
        else {
            mtoolbar.setTitle("Edit Professional Details");
            getProfessionalDetails();
            // getProfilePic();
        }



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                organization = organizationEditText.getText().toString().trim();
                designation = designationEditText.getText().toString().trim();
                startMonth = startMonthSpinner.getSelectedItem().toString();
                startYear = startYearSpinner.getSelectedItem().toString();


                if(currCheckBox.isChecked()){
                    //We have to find current month and year;

                    endMonth=endMonthSpinner.getItemAtPosition(Integer.parseInt(findDate().substring(3,5))-1).toString();
                    endYear=findDate().substring(6);


                }else if(!currCheckBox.isChecked()){
                    endMonth=endMonthSpinner.getSelectedItem().toString();
                    endYear=endYearSpinner.getSelectedItem().toString();
                }

                startDate=startMonth+"/"+startYear;
                endDate=endMonth+"/"+endYear;

                ProfessionalDetails professionalDetails=new ProfessionalDetails(endDate,organization,designation,startDate);
                //saveProfessionalDetails(professionalDetails);

                if (isUpdate == null)
                    saveProfessionalDetails(professionalDetails);
                else {
                    updateProfessionalDetails(professionalDetails);
                }

            }
        });


    }

    private void getProfessionalDetails() {


        Call<ProfessionalDetailData> call = mservice.retrieveProfessionalDetails(id);
        call.enqueue(new Callback<ProfessionalDetailData>() {
            @Override
            public void onResponse(Call<ProfessionalDetailData> call, Response<ProfessionalDetailData> response) {
                if(response.body() != null) {
                    organizationEditText.setText(response.body().getData().getOrganisation());
                    designationEditText.setText(response.body().getData().getDesignation());
                    startMonthSpinner.setSelection(getIndex(startMonthSpinner, response.body().getData().getStart_date().substring(0, 3)));
                    startYearSpinner.setSelection(getIndex(startYearSpinner, response.body().getData().getStart_date().substring(5)));
                    endMonthSpinner.setSelection(getIndex(endMonthSpinner, response.body().getData().getEnd_date().substring(0, 3)));
                    endYearSpinner.setSelection(getIndex(endYearSpinner, response.body().getData().getEnd_date().substring(5)));
                } else {
                    showToast("Professional Details Response Empty", Toast.LENGTH_LONG);
                }
            }
            @Override
            public void onFailure(Call<ProfessionalDetailData> call, Throwable t) {
                showToast("Response Failed: " + t.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }

    private void saveProfessionalDetails(ProfessionalDetails professionalDetails) {

        Call<ProfessionalDetailData> professionalDetailDataCall=mservice.addProfessionalDetails(id,professionalDetails);
        professionalDetailDataCall.enqueue(new Callback<ProfessionalDetailData>() {
            @Override
            public void onResponse(Call<ProfessionalDetailData> call, Response<ProfessionalDetailData> response) {
                //Put intent for user home activity;
                Intent n=new Intent(ProfessionalDetailsActivity.this,DisplayActivity.class);
                n.putExtra("id",id);
                startActivity(n);

                finish();

            }

            @Override
            public void onFailure(Call<ProfessionalDetailData> call, Throwable t) {
                Log.d("Save profess Details :","Failed "+t.getMessage().toString());
            }
        });
    }

    public void updateProfessionalDetails(final ProfessionalDetails professionalDetails)
    {
        Call<ProfessionalDetailData> call = mservice.updateProfessionalDetails(id, professionalDetails);
        call.enqueue(new Callback<ProfessionalDetailData>() {
            @Override
            public void onResponse(Call<ProfessionalDetailData> call, Response<ProfessionalDetailData> response) {
                showToast("Professional Details Updated", Toast.LENGTH_SHORT);
                Intent intent = new Intent(ProfessionalDetailsActivity.this, DisplayActivity.class);
                finish();
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ProfessionalDetailData> call, Throwable t) {
                showToast("Update Professional details Failed: " + t.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i = 0; i < spinner.getCount(); i++){
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

    private String findDate(){
        DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
        String date=df.format(Calendar.getInstance().getTime());
        return date;
    }
}
