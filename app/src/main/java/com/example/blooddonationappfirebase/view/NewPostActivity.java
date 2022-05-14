package com.example.blooddonationappfirebase.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.blooddonationappfirebase.databinding.ActivityNewPostBinding;
import com.example.blooddonationappfirebase.model.Post;
import com.example.blooddonationappfirebase.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class NewPostActivity extends AppCompatActivity {

    private static final String TAG = "NewPostActivity";
    private ActivityNewPostBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private static String author;
    private static String userId;
    private static String postId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.createPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                auth = FirebaseAuth.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference();

                String bloodType = binding.bloodType.getText().toString();
                String quantity = binding.quantity.getText().toString();
                String contactNo = binding.contactNo.getText().toString();
                String otherInfo = binding.otherInfo.getText().toString();

                if (bloodType.isEmpty()){
                    Toast.makeText(NewPostActivity.this, "Blood type cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(quantity.isEmpty()){
                    Toast.makeText(NewPostActivity.this, "Quantity cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(contactNo.isEmpty()){
                    Toast.makeText(NewPostActivity.this, "Contact no cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(otherInfo.isEmpty()){
                    Toast.makeText(NewPostActivity.this, "Other info cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else{

                    MaterialDialog mDialog = new MaterialDialog.Builder(NewPostActivity.this)
                            .setTitle("Confirm?")
                            .setMessage("Are you sure want to create this post?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    createPostNow(bloodType, quantity, contactNo, otherInfo);
                                    dialogInterface.dismiss();
                                    finish();
                                }
                            })
                            .setNegativeButton("No",new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .build();

                    // Show Dialog
                    mDialog.show();

                }

            }
        });

    }

    private void createPostNow(String bloodType, String quantity, String contactNo, String otherInfo) {
        // getting user info
        databaseReference.child("Users").child(auth.getUid()).child("UserProfile")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        if (user.getUsername()!=null){
                            Log.i(TAG, "onDataChange: "+user.getName());
                            NewPostActivity.author = user.getName();
                            NewPostActivity.userId = auth.getUid();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(NewPostActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });


        NewPostActivity.postId = databaseReference.push().getKey();
        Post postData = new Post(bloodType, quantity, contactNo, otherInfo, postId, auth.getUid(), NewPostActivity.author);

        databaseReference
                .child("Users")
                .child(auth.getUid())
                .child("Posts")
                .child(NewPostActivity.postId)
                .setValue(postData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(NewPostActivity.this, "Post created", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Post discarded", Toast.LENGTH_SHORT).show();
    }
}