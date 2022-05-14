package com.example.blooddonationappfirebase.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.blooddonationappfirebase.R;
import com.example.blooddonationappfirebase.databinding.ActivityHomeBinding;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Dash Board");

        getSupportFragmentManager().beginTransaction().replace(binding.fragmentContainerViewTag.getId(), new DonationInfo()).commit();

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getText().equals("Home")) {
                    // go to home screen
                    getSupportFragmentManager().beginTransaction().replace(binding.fragmentContainerViewTag.getId(), new DonationInfo()).commit();
                }
                else{
                    // go to profile
                    getSupportFragmentManager().beginTransaction().replace(binding.fragmentContainerViewTag.getId(), new UserProfileInfoFragment()).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}