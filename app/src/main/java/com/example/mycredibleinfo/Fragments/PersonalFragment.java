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
import com.example.mycredibleinfo.PersonalDetailPOJOS.PersonalDetail;
import com.example.mycredibleinfo.PersonalDetails;
import com.example.mycredibleinfo.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.mycredibleinfo.MainActivity.MY_PREF;


public class PersonalFragment extends Fragment {
    private TextView nameTextView, mobileTextView, locationTextView, linksTextView, emailTextView;

    private int id;
    private ApiService mservice;

    private Button update;

    private String email;

    public PersonalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_personal,container,false);

        mservice= ApiUtils.getUserService();

        nameTextView=rootView.findViewById(R.id.per_name);
        mobileTextView=rootView.findViewById(R.id.per_mobile);
        locationTextView=rootView.findViewById(R.id.per_loaction);
        linksTextView=rootView.findViewById(R.id.per_links);
        emailTextView=rootView.findViewById(R.id.per_email);

        SharedPreferences preferences=this.getActivity().getSharedPreferences(MY_PREF,MODE_PRIVATE);
        id=preferences.getInt("userid",0);
        email=preferences.getString("userEmail","");

        getPersonalDetails(id);


        update=rootView.findViewById(R.id.personal_update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PersonalDetails.class);
                intent.putExtra("isUpdate", "true");
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPersonalDetails(id);

    }

    private void getPersonalDetails(int id) {

        Call<PersonalDetail> personalDetailCall=mservice.retrievePersonalDetails(id);
        personalDetailCall.enqueue(new Callback<PersonalDetail>() {
            @Override
            public void onResponse(Call<PersonalDetail> call, Response<PersonalDetail> response) {
                if(response.body()!=null){
                    nameTextView.setText(response.body().getData().getName());
                    mobileTextView.setText(response.body().getData().getMobile_no());
                    locationTextView.setText(response.body().getData().getLocation());
                    linksTextView.setText(response.body().getData().getLinks());
                    emailTextView.setText(email);
                }
            }

            @Override
            public void onFailure(Call<PersonalDetail> call, Throwable t) {
                Toast.makeText(getContext(),"Retrieving Failed"+ t.getMessage(),Toast.LENGTH_SHORT);
            }
        });

    }


}
