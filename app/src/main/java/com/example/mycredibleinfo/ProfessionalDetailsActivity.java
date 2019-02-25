package com.example.mycredibleinfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

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

public class ProfessionalDetailsActivity extends AppCompatActivity {

    EditText organizationEditText, designationEditText;
    Spinner startMonthSpinner, startYearSpinner, endMonthSpinner, endYearSpinner;
    CheckBox currCheckBox;

    String organization, designation;
    String startMonth, startYear, endMonth, endYear;
    private String startDate,endDate;
    private Button save;

    int id;

    ApiService mservice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_details);

        mservice= ApiUtils.getUserService();

        organizationEditText=findViewById(R.id.prd_organisation);
        designationEditText=findViewById(R.id.pr_designation);
        startMonthSpinner=findViewById(R.id.prd_start_spinner_month);
        startYearSpinner=findViewById(R.id.prd_start_spinner_year);
        endMonthSpinner=findViewById(R.id.prd_end_spinner_month);
        endYearSpinner=findViewById(R.id.prd_end_spinner_year);

        save=findViewById(R.id.prd_save);


        currCheckBox=findViewById(R.id.prd_checkbox);

        currCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currCheckBox.isChecked()){
                    endMonthSpinner.setVisibility(GONE);
                    endYearSpinner.setVisibility(GONE);

                }
                else if(!currCheckBox.isChecked()){
                    endMonthSpinner.setVisibility(View.VISIBLE);
                    endYearSpinner.setVisibility(View.VISIBLE);

                }
            }
        });

        id=getIntent().getIntExtra("id",0);


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
                saveProfessionalDetails(professionalDetails);

            }
        });


    }

    private void saveProfessionalDetails(ProfessionalDetails professionalDetails) {

        Call<ProfessionalDetailData> professionalDetailDataCall=mservice.addProfessionalDetails(id,professionalDetails);
        professionalDetailDataCall.enqueue(new Callback<ProfessionalDetailData>() {
            @Override
            public void onResponse(Call<ProfessionalDetailData> call, Response<ProfessionalDetailData> response) {
                //Put intent for user home activity;


            }

            @Override
            public void onFailure(Call<ProfessionalDetailData> call, Throwable t) {
                Log.d("Save profess Details :","Failed "+t.getMessage().toString());
            }
        });
    }


    private String findDate(){
        DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
        String date=df.format(Calendar.getInstance().getTime());
        return date;
    }
}
