package com.example.commande_pc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commande_pc.MainActivity;
import com.example.commande_pc.R;
import com.example.commande_pc.database.MemoryDataBaseHelper;
import com.example.commande_pc.entity.Administrator;
import com.example.commande_pc.entity.Requester;
import com.example.commande_pc.entity.User;

import java.util.ArrayList;

public class RequesterListAdapter extends RecyclerView.Adapter<RequesterListAdapter.ViewHolder>{
    private ArrayList<Requester> requesters;
    public RequesterListAdapter() {
        //requesters = MemoryDataBaseHelper.getRequesters();
        requesters = ((Administrator) MainActivity.getUser()).getRequesters();
        //notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RequesterListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.requester_display_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RequesterListAdapter.ViewHolder holder, int position) {
        for (User user : requesters){
            System.out.println(user.getFirstName() + " " + user.getLastName() + " " + user.getEmail() + " " + user.getCreatedAt() + " role " + user.getRoleId());
        }
        holder.requesterNamesInput.setText(String.format("%s %s",requesters.get(position).getFirstName(),requesters.get(position).getLastName()));
        holder.requesterEmailInput.setText(requesters.get(position).getEmail());
        holder.requesterCreatedAtInput.setText(requesters.get(position).getCreatedAt());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(((Administrator)(MainActivity.getUser())).deleteRequester(requesters.get(holder.getAdapterPosition()))){
                    requesters.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return requesters.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView requesterNamesInput;
        private final TextView requesterEmailInput;
        private final TextView requesterCreatedAtInput;
        private final ImageButton deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            requesterNamesInput = itemView.findViewById(R.id.requester_names_input);
            requesterEmailInput = itemView.findViewById(R.id.requester_email_input);
            requesterCreatedAtInput = itemView.findViewById(R.id.requester_created_at_input);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
        public TextView getRequesterNamesInput(){
            return requesterNamesInput;
        }
        public TextView getRequesterEmailInput(){
            return requesterEmailInput;
        }
        public TextView getRequesterCreatedAtInput(){
            return requesterCreatedAtInput;
        }
    }
}