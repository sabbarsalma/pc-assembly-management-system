package com.example.commande_pc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commande_pc.R;
import com.example.commande_pc.Utils;
import com.example.commande_pc.database.SqliteDatabaseHelper;
import com.example.commande_pc.entity.Item;
import com.example.commande_pc.entity.Order;
import com.example.commande_pc.entity.Requester;
import com.example.commande_pc.ui.orders.DisplayItemsFragment;
import com.example.commande_pc.ui.orders.OrderStateFragment;

import java.util.ArrayList;

public class DisplayItemAdapter extends RecyclerView.Adapter<DisplayItemAdapter.ViewHolder> {

    private final DisplayItemsFragment displayItemsFragment;
    private  ArrayList<Item>items;


    public DisplayItemAdapter(DisplayItemsFragment displayItemsFragment, ArrayList<Item>items){
        this.displayItemsFragment = displayItemsFragment;
        this.items=items;


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.preview2,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayItemAdapter.ViewHolder holder, int position) {
        holder.type.setText(items.get(position).getType());
        holder.description.setText(items.get(position).getDescription());
        holder.createdAt.setText(Utils.dateToString(items.get(position).getCreatedAt()));


    }


    @Override
    public int getItemCount() {
        return items.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView type, description ,createdAt;

        public ViewHolder(View itemView){
            super(itemView);
            type= itemView.findViewById(R.id.typeTextView1);
            description=itemView.findViewById(R.id.descriptionTextView1);
            createdAt= itemView.findViewById(R.id.created_at_text_view1);

        }

    }
}
