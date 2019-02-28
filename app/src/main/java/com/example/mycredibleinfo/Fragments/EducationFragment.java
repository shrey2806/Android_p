package com.example.mycredibleinfo.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycredibleinfo.APISettings.ApiService;
import com.example.mycredibleinfo.APISettings.ApiUtils;
import com.example.mycredibleinfo.EducationDetailPOJOS.EducationDetailsData;
import com.example.mycredibleinfo.EducationDetailsActivity;
import com.example.mycredibleinfo.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.mycredibleinfo.MainActivity.MY_PREF;

public class EducationFragment extends Fragment {



    private TextView organizationTextView,locationTextView,deegre_txtview,startyear_txtview,endyear_textView;

    ApiService mservice;
    int userId;
    private Button Update;


    public EducationFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_education, container, false);
        organizationTextView = rootView.findViewById(R.id.education_organisation);
        deegre_txtview=rootView.findViewById(R.id.edu_deegre);
        startyear_txtview=rootView.findViewById(R.id.edu_start_year);
        endyear_textView=rootView.findViewById(R.id.edu_end_year);
        locationTextView = rootView.findViewById(R.id.edu_loaction);


        mservice= ApiUtils.getUserService();


        SharedPreferences prefs = this.getActivity().getSharedPreferences(MY_PREF, MODE_PRIVATE);
        userId = prefs.getInt("userid", 0);

        getEducationDetails(userId);

        Update=rootView.findViewById(R.id.edu_update);

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EducationDetailsActivity.class);
                intent.putExtra("isUpdate", "true");
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getEducationDetails(userId);
    }

    public void getEducationDetails(final int userId)
    {
        Call<EducationDetailsData> call = mservice.retrieveEducationalDetails(userId);
        call.enqueue(new Callback<EducationDetailsData>() {
            @Override
            public void onResponse(Call<EducationDetailsData> call, Response<EducationDetailsData> response) {
                if(response.body() != null) {
                    organizationTextView.setText(response.body().getData().getOrganisation());
                    //degreeTextView.setText(response.body().getData().getDegree());
                    locationTextView.setText(response.body().getData().getLocation());
                    deegre_txtview.setText(response.body().getData().getDegree());
                    startyear_txtview.setText(response.body().getData().getStart_year());
                    endyear_textView.setText(response.body().getData().getEnd_year());

                }
            }

            @Override
            public void onFailure(Call<EducationDetailsData> call, Throwable t) {
                showToast("Get education details filed: " + t.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }

    public void showToast(String msg, int length)
    {
        Toast.makeText(getContext(), msg, length).show();
    }



}
