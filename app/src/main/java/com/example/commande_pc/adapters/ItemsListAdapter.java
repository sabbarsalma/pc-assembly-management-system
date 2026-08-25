package com.example.commande_pc.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commande_pc.LaunchActivity;
import com.example.commande_pc.LoginActivity;
import com.example.commande_pc.MainActivity;
import com.example.commande_pc.R;
import com.example.commande_pc.Utils;
import com.example.commande_pc.database.SqliteDatabaseHelper;
import com.example.commande_pc.entity.Item;
import com.example.commande_pc.entity.Role;
import com.example.commande_pc.entity.StoreKeeper;
import com.example.commande_pc.entity.User;
import com.example.commande_pc.ui.storekeeper.EditOrShowComponentDialog;
import com.example.commande_pc.ui.storekeeper.ListStockComponents;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListAdapter.ViewHolder>{
    private ArrayList<Item> items;
    private final ListStockComponents listStockComponents;
    public ItemsListAdapter(ListStockComponents listStockComponents,ArrayList<Item> newItems){
        this.listStockComponents = listStockComponents;
        items = newItems;
        listStockComponents.getSearchComponentEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<Item> filtered = new ArrayList<Item>();
                for(Item item : items){
                    if(s == null || s.toString().trim().isEmpty()){
                        filtered.add(item);
                    }else if((
                            item.getComment() != null && item.getComment().toLowerCase().contains(s.toString().toLowerCase())
                    ) ||
                            (
                                    item.getDescription().toLowerCase().contains(s.toString().toLowerCase())
                            )||
                            (
                                    item.getSubtype().toLowerCase().contains(s.toString().toLowerCase())
                            ))
                    {
                        filtered.add(item);
                    }
                }
                setItems(filtered);
//                setItems(items.stream().filter(item -> {
//                    if(s == null || s.toString().trim().isEmpty()){
//                        return true;
//                    }
//                    return  (
//                            item.getComment() != null && item.getComment().toLowerCase().contains(s.toString().toLowerCase())
//                    ) ||
//                            (
//                                    item.getDescription().toLowerCase().contains(s.toString().toLowerCase())
//                            )||
//                            (
//                                    item.getSubtype().toLowerCase().contains(s.toString().toLowerCase())
//                            );
//                }).collect(Collectors.toList()));
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setItems(ArrayList<Item> filteredItems) {
        this.items = filteredItems;
        notifyDataSetChanged();
        listStockComponents.toogleVisibility(!items.isEmpty());
    }
    public Item getItem(int position) {
        return items.get(position);
    }
    private ItemsListAdapter getMySelf(){
        return this;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_item_store_keeper,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String description = items.get(position).getDescription();
        String truncatedDescription = description.substring(0,Math.min(description.length(),20)) + "...";
        holder.descriptionTextView.setText(truncatedDescription);
        holder.createdAtTextView.setText(Utils.dateToString(items.get(position).getCreatedAt()));
        holder.typeTextView.setText(items.get(position).getType().toUpperCase());
        holder.deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext()).setMessage("Etes vous sûr de vouloir supprimer ?").setTitle("Confirmation").setIcon(R.drawable.web_link).setNegativeButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StoreKeeper storeKeeper = ((StoreKeeper) (MainActivity.getUser()));
                        int position_of_item = holder.getAdapterPosition();
                        if(storeKeeper.deleteItem(items.get(position_of_item))){
                            items.remove(position_of_item);
                            notifyItemRemoved(position_of_item);
                            listStockComponents.toogleVisibility(!items.isEmpty());
                        }
                    }
                }).setPositiveButton("Non",null).show();
            }
        });
        holder.rootElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditOrShowComponentDialog editOrShowComponentDialog = new EditOrShowComponentDialog(v.getContext(),getMySelf(),holder.getAdapterPosition());
                editOrShowComponentDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView descriptionTextView,createdAtTextView,typeTextView;
        private Button deleteItemButton;
        private CardView rootElement;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            createdAtTextView = itemView.findViewById(R.id.created_at_text_view);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            deleteItemButton = itemView.findViewById(R.id.deleteItemButton);
            rootElement = itemView.findViewById(R.id.rootElement);
        }
    }
}
