package com.example.blooddonationappfirebase.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonationappfirebase.R;
import com.example.blooddonationappfirebase.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UserProfileInfoFragment extends Fragment {


    private static final String TAG = "UserProfileInfoFragment";
    TextView username, name, phone, password, signout_btn;
    private FirebaseAuth auth;
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
        return inflater.inflate(R.layout.fragment_user_profile_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.passeord);
        name = view.findViewById(R.id.name);
        phone = view.findViewById(R.id.phone);
        signout_btn = view.findViewById(R.id.sign_out_btn);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (databaseReference==null){
            Log.e(TAG, "onViewCreated: TRUE null");
        }


        try {
            databaseReference.child("Users").child(auth.getUid()).child("UserProfile")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()){
                                User user = snapshot.getValue(User.class);
                                username.setText(user.getUsername());
                                password.setText(user.getPassword());
                                name.setText(user.getName());
                                phone.setText(user.getPhoneNumber());
                            }
                            else {
                                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                databaseReference.child("Users").child(auth.getUid()).removeValue();
                                Objects.requireNonNull(auth.getCurrentUser()).delete();
                                auth.signOut();
                                startActivity(new Intent(requireActivity(), LoginActivity.class));
                                requireActivity().finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }
        catch (Exception e){
            Log.e(TAG, "onViewCreated: "+e.getMessage() );
        }

        signout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(requireActivity(), LoginActivity.class));
                requireActivity().finish();
            }
        });

    }
}