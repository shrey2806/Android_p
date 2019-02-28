package com.example.mycredibleinfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycredibleinfo.APISettings.ApiService;
import com.example.mycredibleinfo.APISettings.ApiUtils;
import com.example.mycredibleinfo.Adapters.SectionsPagerAdapter;
import com.example.mycredibleinfo.PersonalDetailPOJOS.PersonalDetail;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mycredibleinfo.MainActivity.MY_PREF;

public class DisplayActivity extends AppCompatActivity {

    TabLayout mtabLayout;
    ViewPager mviewPager;
    ApiService mservice;
    ImageView userPic;
    Toolbar mtoolbar;


    private TextView name_textView,email_textView;
    String name,email;
    final private String imageUrl = "http://139.59.65.145:9090/user/personaldetail/profilepic/";
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        name_textView=findViewById(R.id.dp_name);
        email_textView=findViewById(R.id.dp_email);

        userPic=findViewById(R.id.dp_imageview);

        mtoolbar=findViewById(R.id.display_toolbar);
        setSupportActionBar(mtoolbar);

        mtabLayout=findViewById(R.id.tabLayout);
        mviewPager=findViewById(R.id.viewPager);




        SectionsPagerAdapter madapter=new SectionsPagerAdapter(getSupportFragmentManager());




        mviewPager.setAdapter(madapter);

        mtabLayout.setupWithViewPager(mviewPager);

        mservice= ApiUtils.getUserService();


        //getEducationDetails();

        SharedPreferences preferences=getSharedPreferences(MY_PREF,MODE_PRIVATE);
        id=preferences.getInt("userid",0);
        email=preferences.getString("userEmail","");
        email_textView.setText(email);

         //id=getIntent().getIntExtra("id",0);

        Log.d("ID is : ",String.valueOf(id));


        getPersonalDetails();
        getProfilePic();
    }

    private void getProfilePic() {

        Uri uri = Uri.parse(imageUrl + id);
        Picasso.get().load(uri).into(userPic);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout_button:
                Toast.makeText(this,"Sign Out Successful",Toast.LENGTH_SHORT).show();
                finish();
                Intent i=new Intent(DisplayActivity.this,MainActivity.class);
                startActivity(i);
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }


    }

    private void getEducationDetails() {

    }

    private void getPersonalDetails() {

        Call<PersonalDetail> personalDetailCall=mservice.retrievePersonalDetails(id);
        personalDetailCall.enqueue(new Callback<PersonalDetail>() {
            @Override
            public void onResponse(Call<PersonalDetail> call, Response<PersonalDetail> response) {
                    if(response.body()!=null){
                        name=response.body().getData().getName();
                        //email=response.body().getData().getEmail();
                        Log.d("Task Done","TaskDone");
                        name_textView.setText(name);

                        //email_textView.setText(email);

                    }else{
                        Toast.makeText(DisplayActivity.this,"Personal Details Empty",Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onFailure(Call<PersonalDetail> call, Throwable t) {
                showToast("Response Failed: " + t.getMessage(), Toast.LENGTH_SHORT);

            }
        });


    }


    public void showToast(String msg, int length)
    {
        Toast.makeText(this, msg, length).show();
    }


}
