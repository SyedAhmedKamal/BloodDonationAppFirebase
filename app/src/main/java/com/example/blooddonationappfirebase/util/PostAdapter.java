package com.example.blooddonationappfirebase.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blooddonationappfirebase.R;
import com.example.blooddonationappfirebase.model.Post;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    ArrayList<Post> postList;
    Context context;
    private static final String TAG = "PostAdapter";

    public PostAdapter(ArrayList<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView bloodType, quant, contact, otherInfo, authorName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            authorName = itemView.findViewById(R.id.tv_author);
            bloodType = itemView.findViewById(R.id.textView10);
            quant = itemView.findViewById(R.id.tv_quan);
            contact = itemView.findViewById(R.id.tv_contact_info);
            otherInfo = itemView.findViewById(R.id.tv_other_info);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.post_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {
            holder.authorName.setText(postList.get(position).getAuthor());
            holder.bloodType.setText(postList.get(position).getBloodType());
            holder.contact.setText(postList.get(position).getContactInfo());
            holder.otherInfo.setText(postList.get(position).getOtherInfo());
            holder.quant.setText(postList.get(position).getQuantity());
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: "+e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

}
