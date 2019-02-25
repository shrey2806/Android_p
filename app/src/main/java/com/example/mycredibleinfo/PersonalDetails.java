package com.example.mycredibleinfo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.mycredibleinfo.PersonalDetailPOJOS.Photo;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalDetails extends AppCompatActivity {

    EditText fullname_etx, email_etx,mobile_etx,location_etx,links_etx;
    Button save;
    ApiService mservice;

    private CircleImageView userPic;
    private Bitmap profilePicBitmap;
    private ByteArrayOutputStream baos;
    private byte[] imageByteArray;
    private String encodedImage;



    int id;

    public static  final int PICK_IMAGE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

         id=Integer.valueOf(getIntent().getStringExtra("id"));

        fullname_etx=findViewById(R.id.pr_name);
        email_etx=findViewById(R.id.pr_email);
       mobile_etx=findViewById(R.id.pd_mobile);
        location_etx=findViewById(R.id.ed_location);
        links_etx=findViewById(R.id.pd_links);
        save=findViewById(R.id.pd_save);

        userPic=findViewById(R.id.pr_image);

        userPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"CHOOSE PICTURE"),PICK_IMAGE);


            }
        });



        mservice= ApiUtils.getUserService();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname=fullname_etx.getText().toString().trim();
                String email=email_etx.getText().toString().trim();
                String mobile=mobile_etx.getText().toString().trim();
                String location=location_etx.getText().toString().trim();
                String links=links_etx.getText().toString().trim();


                if(!TextUtils.isEmpty(fullname) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(mobile) &&!TextUtils.isEmpty(location)){


                    PersonalDetails2 obj=new PersonalDetails2(null,mobile,fullname,links,location,email);
                    savePersonalDetails(obj);

                    saveProfilePic(encodedImage);


                }else{
                    Toast.makeText(PersonalDetails.this,"Fields cannot be Empty",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void saveProfilePic(String encodedImage) {
        Photo photo=new Photo(String.valueOf(id),encodedImage);
    Call<StatusMessagePojo> photocall=mservice.addPhoto(photo);
        photocall.enqueue(new Callback<StatusMessagePojo>() {
            @Override
            public void onResponse(Call<StatusMessagePojo> call, Response<StatusMessagePojo> response) {
                Toast.makeText(PersonalDetails.this,"Photo Uploaded Successfully",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<StatusMessagePojo> call, Throwable t) {
                Toast.makeText(PersonalDetails.this,"Photo Not Uploaded",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            Uri uri=data.getData();
            userPic.setImageURI(uri);

            String picturePath=getPath(this,uri);

            profilePicBitmap = BitmapFactory.decodeFile(picturePath);
            baos = new ByteArrayOutputStream();
            profilePicBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            imageByteArray = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
        }
    }

    private void savePersonalDetails(PersonalDetails2 obj) {
        Call<PersonalDetail> personalDetailsCall=mservice.addPesonalDetails(id,obj);
        personalDetailsCall.enqueue(new Callback<PersonalDetail>() {
            @Override
            public void onResponse(Call<PersonalDetail> call, Response<PersonalDetail> response) {

                Intent i=new Intent(PersonalDetails.this, EducationDetailsActivity.class);
                i.putExtra("id",id);
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(Call<PersonalDetail> call, Throwable t) {
                Log.d("Save Personal Details :","Failed "+t.getMessage().toString());
            }
        });





    }

    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }



}
