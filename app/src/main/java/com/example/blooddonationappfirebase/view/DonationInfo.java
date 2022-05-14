package com.example.blooddonationappfirebase.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.blooddonationappfirebase.R;
import com.example.blooddonationappfirebase.model.Post;
import com.example.blooddonationappfirebase.util.PostAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DonationInfo extends Fragment {

    private static final String TAG = "DonationInfo";
    FloatingActionButton fab;
    RecyclerView recyclerView;
    private FirebaseAuth auth;
    private ArrayList<Post> postArrayList;
     PostAdapter postAdapter;
    private DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_donation_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab = view.findViewById(R.id.floatingActionButton);
        recyclerView = view.findViewById(R.id.recyclerView);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        postArrayList = new ArrayList<>();


        databaseReference
                .child("Users")
                .child(auth.getUid())
                .child("Posts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d(TAG, "onDataChange: "+snapshot.getChildren());
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            Log.d(TAG, "Posts: "+postSnapshot.getValue().toString());
                            Post post = postSnapshot.getValue(Post.class);
                            postArrayList.add(post);
                            postAdapter = new PostAdapter(postArrayList,requireContext());
                            recyclerView.setAdapter(postAdapter);
                            Log.d(TAG, "onCreate: "+postArrayList.size());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NewPostActivity.class));
            }
        });
    }
}