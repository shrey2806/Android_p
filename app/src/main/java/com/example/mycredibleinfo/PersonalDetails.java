package com.example.mycredibleinfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mycredibleinfo.APISettings.ApiService;
import com.example.mycredibleinfo.APISettings.ApiUtils;
import com.example.mycredibleinfo.PersonalDetailPOJOS.PersonalDetail;
import com.example.mycredibleinfo.PersonalDetailPOJOS.PersonalDetails2;
import com.example.mycredibleinfo.ProfilePicPojo.Photo;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mycredibleinfo.MainActivity.MY_PREF;

public class PersonalDetails extends AppCompatActivity {

    private EditText fullname_etx, email_etx, mobile_etx, location_etx, links_etx;
    private Button save;
    private ApiService mservice;


    private CircleImageView userPic;
    private Bitmap profilePicBitmap;
    private ByteArrayOutputStream baos;
    private byte[] imageByteArray;
    private String encodedImage;


    int id;

    final private String imageUri = "content://media/internal/images/media";
    final private String imageUrl = "http://139.59.65.145:9090/user/personaldetail/profilepic/";

    public static final int PICK_IMAGE = 1;

    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        //id = Integer.parseInt(getIntent().getStringExtra("id"));

        SharedPreferences preferences=this.getSharedPreferences(MY_PREF,MODE_PRIVATE);
        id=preferences.getInt("userid",0);
        email=preferences.getString("userEmail","");


       // Log.d("Id os :", String.valueOf(id));
        fullname_etx = findViewById(R.id.pr_name);
        email_etx = findViewById(R.id.pr_email);
        mobile_etx = findViewById(R.id.pd_mobile);
        location_etx = findViewById(R.id.ed_location);
        links_etx = findViewById(R.id.pd_links);
        save = findViewById(R.id.pd_save);

        userPic = findViewById(R.id.pr_image);
        mservice = ApiUtils.getUserService();

        userPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse(imageUri));
                startActivityForResult(intent, 1);


            }
        });

        Toolbar mtoolbar=findViewById(R.id.personal_toolbar);




        final String isUpdate = getIntent().getStringExtra("isUpdate");
        if (isUpdate == null)
            mtoolbar.setTitle("Set Personal Details");
        else {
          mtoolbar.setTitle("Edit Personal Details");
            getPersonalDetails();

             getProfilePic();
        }




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = fullname_etx.getText().toString().trim();
                String email = email_etx.getText().toString().trim();
                String mobile = mobile_etx.getText().toString().trim();
                String location = location_etx.getText().toString().trim();
                String links = links_etx.getText().toString().trim();


                if (!TextUtils.isEmpty(fullname) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(location)) {


                    PersonalDetails2 obj = new PersonalDetails2(null, mobile, fullname, links, location, email);
                    //savePersonalDetails(obj);

                    if (isUpdate == null)
                        savePersonalDetails(obj);
                    else {
                        updatePersonalDetails(obj);

                    }


                    saveProfilePic(encodedImage);


                } else {
                    Toast.makeText(PersonalDetails.this, "Fields cannot be Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void getProfilePic() {
        Uri uri = Uri.parse(imageUrl + id);
        Picasso.get().load(uri).into(userPic);
    }

    private void getPersonalDetails() {
        Call<PersonalDetail> call = mservice.retrievePersonalDetails(id);
        call.enqueue(new Callback<PersonalDetail>() {
            @Override
            public void onResponse(Call<PersonalDetail> call, Response<PersonalDetail> response) {
                if(response.body() != null) {
                    fullname_etx.setText(response.body().getData().getName());
                    //emailEditText.setText(response.body().getData().getEmail());
                    mobile_etx.setText(response.body().getData().getMobile_no());
                    location_etx.setText(response.body().getData().getLocation());
                    links_etx.setText(response.body().getData().getLinks());
                    email_etx.setText(email);
                } else {
                    showToast("Personal Details Response Empty", Toast.LENGTH_LONG);
                }
            }
            @Override
            public void onFailure(Call<PersonalDetail> call, Throwable t) {
                showToast("Response Failed: " + t.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }

    private void saveProfilePic(String encodedImage) {
        Photo photo = new Photo(String.valueOf(id), encodedImage);
        Call<StatusMessagePojo> photocall = mservice.addPhoto(photo);
        photocall.enqueue(new Callback<StatusMessagePojo>() {
            @Override
            public void onResponse(Call<StatusMessagePojo> call, Response<StatusMessagePojo> response) {
                Toast.makeText(PersonalDetails.this, "Photo Uploaded Successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<StatusMessagePojo> call, Throwable t) {
                Toast.makeText(PersonalDetails.this, "Photo Not Uploaded", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            userPic.setImageURI(uri);

            String picturePath = getPath(this, uri);

            //profilePicBitmap = BitmapFactory.decodeFile(picturePath);
            profilePicBitmap=((BitmapDrawable)userPic.getDrawable()).getBitmap();

            baos = new ByteArrayOutputStream();
            profilePicBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            imageByteArray = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
        }
    }

    private void savePersonalDetails(PersonalDetails2 obj) {
        Call<PersonalDetail> personalDetailsCall = mservice.addPersonalDetails(id, obj);
        personalDetailsCall.enqueue(new Callback<PersonalDetail>() {
            @Override
            public void onResponse(Call<PersonalDetail> call, Response<PersonalDetail> response) {

                PersonalDetail personalDetail = new PersonalDetail();
                personalDetail.setData(response.body().getData());
                int id = Integer.parseInt(response.body().getData().getUid());

                Log.d("User response is: ", response.body().getData().getLocation());
                Log.d("User response is: ", response.body().getData().getId());
                Log.d("User response is: ", response.body().getData().getUid());


                Intent i = new Intent(PersonalDetails.this, EducationDetailsActivity.class);
                i.putExtra("id", id);
                startActivity(i);
                finish();

            }


            @Override
            public void onFailure(Call<PersonalDetail> call, Throwable t) {
                Log.d("Save Personal Details :", "Failed " + t.getMessage().toString());
            }
        });


    }

    public void updatePersonalDetails(final PersonalDetails2 personalDetails2)
    {
        Call<PersonalDetail> call = mservice.updatePersonalDetails(id,personalDetails2);
        call.enqueue(new Callback<PersonalDetail>() {
            @Override
            public void onResponse(Call<PersonalDetail> call, Response<PersonalDetail> response) {
                showToast("Personal Details Updated", Toast.LENGTH_SHORT);
                Intent intent = new Intent(PersonalDetails.this, DisplayActivity.class);

                finish();
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<PersonalDetail> call, Throwable t) {
                showToast("Update Personal details Failed: " + t.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }

    public static String getPath(Context context, Uri uri) {

        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
            Log.d("Not found image:","true");
        }
        return result;
    }
    public void showToast(String msg, int length)
    {
        Toast.makeText(this, msg, length).show();
    }
}
