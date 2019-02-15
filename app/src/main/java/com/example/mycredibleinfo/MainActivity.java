package com.example.mycredibleinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycredibleinfo.PojoClasses.LoginAndSignup;
import com.example.mycredibleinfo.PojoClasses.ServerTest;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class    MainActivity extends AppCompatActivity {
   private EditText user_email,password;
    private Button login_btn,signup_btn;
    private String email,pass;
    ApiService mservice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_email=findViewById(R.id.user_email);
        password=findViewById(R.id.password);
        login_btn=findViewById(R.id.login_btn);
        signup_btn=findViewById(R.id.signup_btn);

         mservice=ApiUtils.getUserService();

         //Testing server
         checkServer();

         login_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 email=user_email.getText().toString().trim();
                 pass=password.getText().toString().trim();

                 if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){
                    User n=new User(email,pass);
                     loginUser(n);

                 }



             }
         });

         signup_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 email=user_email.getText().toString().trim();
                 pass=password.getText().toString().trim();

                 if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){
                     User n=new User(email,pass);
                     signUpUser(n);

                 }


             }
         });
    }

    private void signUpUser(User n) {

        Call<LoginAndSignup> loginAndSignupCall=mservice.addNewUser(n);
        loginAndSignupCall.enqueue(new Callback<LoginAndSignup>() {
            @Override
            public void onResponse(Call<LoginAndSignup> call, Response<LoginAndSignup> response) {
                Toast.makeText(MainActivity.this,"Signup Successful",Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,"Please login to continue",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<LoginAndSignup> call, Throwable t) {

            }
        });
    }

    private void loginUser(User n) {
        Call<LoginAndSignup> loginAndSignupCall =mservice.loginUser(n);
        loginAndSignupCall.enqueue(new Callback<LoginAndSignup>() {
            @Override
            public void onResponse(Call<LoginAndSignup> call, Response<LoginAndSignup> response) {
                if(response.body().getData()==null){
                    Toast.makeText(MainActivity.this,"No such User",Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_LONG).show();

                Intent i=new Intent(MainActivity.this,PersonalDetails.class);
                startActivity(i);
                finish();

            }

            @Override
            public void onFailure(Call<LoginAndSignup> call, Throwable t) {

            }
        });
    }

    private void checkServer() {

        Call<ServerTest> serverTestCall=mservice.getServerStatus();
        serverTestCall.enqueue(new Callback<ServerTest>() {
            @Override
            public void onResponse(Call<ServerTest> call, Response<ServerTest> response) {
                Toast.makeText(MainActivity.this,"Status :"+ response.body().getStatus(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ServerTest> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Status Down:"+ t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }
}
