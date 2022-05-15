package com.example.blooddonationappfirebase.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.blooddonationappfirebase.R;
import com.example.blooddonationappfirebase.model.Post;
import com.example.blooddonationappfirebase.model.User;
import com.example.blooddonationappfirebase.util.PostAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class DonationInfo extends Fragment {

    private static final String TAG = "DonationInfo";
    FloatingActionButton fab;
    RecyclerView recyclerView;
    private FirebaseAuth auth;
    private ArrayList<Post> postArrayList;
     PostAdapter postAdapter;
    private DatabaseReference databaseReference;

    @Override
    public void onStart() {
        super.onStart();

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            auth.signOut();
            startActivity(new Intent(requireActivity(), LoginActivity.class));
        }

    }

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


        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        postAdapter = new PostAdapter(postArrayList, requireActivity().getApplicationContext());
        recyclerView.setAdapter(postAdapter);

        databaseReference
                .child("Users")
                .child(auth.getUid())
                .child("Posts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d(TAG, "onDataChange: "+snapshot.getChildren());
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            try {
                                if (snapshot.exists()){
                                    Log.d(TAG, "Posts: "+postSnapshot.getValue().toString());
                                    Post post = postSnapshot.getValue(Post.class);
                                    postArrayList.add(post);
                                    postAdapter = new PostAdapter(postArrayList,getContext());
                                    recyclerView.setAdapter(postAdapter);
                                    Log.d(TAG, "onCreate: "+postArrayList.size());
                                }
                                else {
                                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                    databaseReference.child("Users").child(auth.getUid()).removeValue();
                                    auth.signOut();
                                    startActivity(new Intent(requireActivity(), LoginActivity.class));
                                    requireActivity().finish();
                                }
                            }
                            catch (Exception e){
                                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "onCancelled: "+error.getMessage());
                    }
                });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NewPostActivity.class));
                requireActivity().finish();
            }
        });
    }
}