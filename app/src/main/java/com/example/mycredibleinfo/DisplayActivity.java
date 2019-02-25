package com.example.mycredibleinfo;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mycredibleinfo.Adapters.SectionsPagerAdapter;

public class DisplayActivity extends AppCompatActivity {
    TabLayout mtabLayout;
    ViewPager mviewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        mtabLayout=findViewById(R.id.tabLayout);
        mviewPager=findViewById(R.id.viewPager);
        SectionsPagerAdapter madapter=new SectionsPagerAdapter(getSupportFragmentManager());



        mviewPager.setAdapter(madapter);

        mtabLayout.setupWithViewPager(mviewPager);

    }
}
