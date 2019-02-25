package com.example.mycredibleinfo.Adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mycredibleinfo.Fragments.EducationFragment;
import com.example.mycredibleinfo.Fragments.PersonalFragment;
import com.example.mycredibleinfo.Fragments.ProfessionalFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position){
        switch (position){

            case 0:
                PersonalFragment personalFragment=new PersonalFragment();
                return personalFragment;
            case 1:
                EducationFragment educationFragment=new EducationFragment();
                return educationFragment;
            case 2:
                ProfessionalFragment professionalFragment = new ProfessionalFragment();
                return professionalFragment;
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {


        switch (position){
            case 0:
                return "PERSONAL";
            case 1:
                return "EDUCATION";
            case 2:
                return "PROFESSIONAL";

            default:
                    return null;
        }
    }
}

/*
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".PersonalDetails">

    <android.support.constraint.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"

        >


        <de.hdodenhof.circleimageview.CircleImageView
            android:id="@+id/pr_image"
            android:layout_width="91dp"
            android:layout_height="78dp"
            android:layout_marginStart="153dp"
            android:layout_marginTop="16dp"
            android:layout_marginEnd="153dp"
            android:layout_marginBottom="1dp"
            android:src="@mipmap/ic_launcher_round"
            app:layout_constraintBottom_toTopOf="@+id/textView"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintVertical_bias="0.333" />

        <TextView
            android:id="@+id/textView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/personal_details"
            android:textSize="22sp"
            android:textStyle="bold"
            tools:layout_editor_absoluteX="125dp"
            tools:layout_editor_absoluteY="108dp" />

        <EditText
            android:id="@+id/prd_organisation"
            android:layout_width="299dp"
            android:layout_height="wrap_content"
            android:layout_marginStart="8dp"
            android:layout_marginTop="12dp"
            android:layout_marginEnd="8dp"
            android:ems="10"
            android:hint="@string/full_name"
            android:inputType="textPersonName"
            android:textSize="14sp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintHorizontal_bias="0.302"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/textView" />

        <EditText
            android:id="@+id/prd_designation"
            android:layout_width="302dp"
            android:layout_height="wrap_content"
            android:layout_marginStart="8dp"
            android:layout_marginTop="12dp"
            android:layout_marginEnd="8dp"
            android:ems="10"
            android:hint="@string/email"
            android:inputType="textPersonName"
            android:textSize="14sp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintHorizontal_bias="0.311"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/prd_organisation" />

        <EditText
            android:id="@+id/ed_location"
            android:layout_width="304dp"
            android:layout_height="wrap_content"
            android:layout_marginStart="8dp"
            android:layout_marginTop="12dp"
            android:layout_marginEnd="8dp"
            android:ems="10"
            android:hint="@string/location"
            android:inputType="textPersonName"
            android:textSize="14sp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintHorizontal_bias="0.318"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/pd_mobile" />

        <EditText
            android:id="@+id/pd_links"
            android:layout_width="308dp"
            android:layout_height="wrap_content"
            android:layout_marginStart="8dp"
            android:layout_marginTop="12dp"
            android:layout_marginEnd="8dp"
            android:ems="10"
            android:hint="@string/links"
            android:inputType="textPersonName"
            android:textSize="14sp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintHorizontal_bias="0.333"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/ed_location" />

        <EditText
            android:id="@+id/pd_mobile"
            android:layout_width="305dp"
            android:layout_height="wrap_content"
            android:layout_marginStart="8dp"
            android:layout_marginTop="12dp"
            android:layout_marginEnd="8dp"
            android:ems="10"
            android:hint="@string/mobile"
            android:inputType="textPersonName"
            android:textSize="14sp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintHorizontal_bias="0.288"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/prd_designation" />

        <android.support.constraint.Guideline
            android:id="@+id/guideline"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            app:layout_constraintGuide_begin="40dp" />

        <Button
            android:id="@+id/pd_save"
            android:layout_width="111dp"
            android:layout_height="48dp"
            android:layout_marginStart="8dp"
            android:layout_marginTop="16dp"
            android:layout_marginEnd="8dp"
            android:text="@string/save"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintHorizontal_bias="0.411"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/pd_links"
            app:layout_constraintVertical_bias="1.0" />

    </android.support.constraint.ConstraintLayout>
</ScrollView>
 */