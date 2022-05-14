package com.example.blooddonationappfirebase.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.blooddonationappfirebase.R;
import com.example.blooddonationappfirebase.databinding.ActivitySignUpBinding;
import com.example.blooddonationappfirebase.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = Objects.requireNonNull(binding.edUsernameTxt.getText()).toString();
                String password = Objects.requireNonNull(binding.edPasswordTxt.getText()).toString();
                String name = Objects.requireNonNull(binding.name.getText()).toString();
                String phoneNo = Objects.requireNonNull(binding.phoneNo.getText()).toString();

                if (email.isEmpty() || email == null) {
                    binding.edUsername.setError("Username empty*");
                } else if (password.isEmpty() || password == null) {
                    binding.edPassword.setError("Password empty*");
                } else if (name.isEmpty() || name == null) {
                    binding.nameLayout.setError("name empty*");
                } else if (phoneNo.isEmpty() || phoneNo == null) {
                    binding.phoneNoLayout.setError("Phone number empty*");
                } else {
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        User userProfile = new User(name, email, password, email, phoneNo);
                                        databaseReference.child("Users").child(task.getResult().getUser().getUid()).child("UserProfile").setValue(userProfile);

                                        Toast.makeText(SignUpActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "onFailure: " + e.getMessage());
                                }
                            });
                }

            }
        });

    }
}