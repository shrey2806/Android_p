package com.example.mycredibleinfo.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.mycredibleinfo.ProfessionalDetailPOJOS.ProfessionalDetailData;
import com.example.mycredibleinfo.ProfessionalDetailsActivity;
import com.example.mycredibleinfo.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.mycredibleinfo.MainActivity.MY_PREF;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfessionalFragment extends Fragment {


    TextView org_tv,desig_tv,strt_month_tv,end_month_tv;
    ApiService mservice;
    int userId;

    private Button update;
    public ProfessionalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_professional,container,false);

        org_tv=rootView.findViewById(R.id.pro_organisation);
        desig_tv=rootView.findViewById(R.id.pro_degination);
        strt_month_tv=rootView.findViewById(R.id.pro_start_date);
        end_month_tv=rootView.findViewById(R.id.pro_end_Date);

        mservice= ApiUtils.getUserService();

        SharedPreferences prefs = this.getActivity().getSharedPreferences(MY_PREF, MODE_PRIVATE);
        userId = prefs.getInt("userid", 0);

        getProffessionalDetails(userId);

        update=rootView.findViewById(R.id.profess_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfessionalDetailsActivity.class);
                intent.putExtra("isUpdate", "true");
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getProffessionalDetails(userId);
    }

    private void getProffessionalDetails(int userId) {

        Call<ProfessionalDetailData> professionalDetailDataCall=mservice.retrieveProfessionalDetails(userId);
        professionalDetailDataCall.enqueue(new Callback<ProfessionalDetailData>() {
            @Override
            public void onResponse(Call<ProfessionalDetailData> call, Response<ProfessionalDetailData> response) {
                if(response!=null){
                    org_tv.setText(response.body().getData().getOrganisation());
                    desig_tv.setText(response.body().getData().getDesignation());
                    strt_month_tv.setText(response.body().getData().getStart_date());
                    end_month_tv.setText(response.body().getData().getEnd_date());
                }
            }

            @Override
            public void onFailure(Call<ProfessionalDetailData> call, Throwable t) {

                showToast("Get education details filed: " + t.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }

    public void showToast(String msg, int length)
    {
        Toast.makeText(getContext(), msg, length).show();
    }



}
