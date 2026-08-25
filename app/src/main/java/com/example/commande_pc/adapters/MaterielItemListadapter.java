package com.example.commande_pc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commande_pc.MainActivity;
import com.example.commande_pc.R;
import com.example.commande_pc.Utils;
import com.example.commande_pc.database.SqliteDatabaseHelper;
import com.example.commande_pc.entity.MaterielItem;
import com.example.commande_pc.entity.SofwareItem;
import com.example.commande_pc.entity.StoreKeeper;

import java.util.ArrayList;

public class MaterielItemListadapter extends  RecyclerView.Adapter<MaterielItemListadapter.ViewHolder>{
    private ArrayList<MaterielItem> list2;
    public MaterielItemListadapter(){
        SqliteDatabaseHelper db=new SqliteDatabaseHelper();
        list2=db.getMaterielItems("materiel");
    }
    @NonNull
    @Override
    public MaterielItemListadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_item_store_keeper, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MaterielItemListadapter.ViewHolder holder, int position) {
        for(MaterielItem materielItem: list2){
            System.out.println(materielItem.getType()+" "+materielItem.getDescription()+" "+materielItem.getCreatedAt());
        }
        holder.type.setText(list2.get(position).getType());
        holder.description.setText(list2.get(position).getDescription());
        holder.created_at.setText(Utils.dateToString(list2.get(position).getCreatedAt()));
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((StoreKeeper)(MainActivity.getUser())).deleteItem(list2.get(holder.getAdapterPosition()))){
                    list2.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                }
            }
        });

    }
    public int getItemCount() {
        return list2.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView description;
        private final TextView type;
        private final TextView created_at;
        private final Button deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            description=itemView.findViewById(R.id.descriptionTextView);
            type=itemView.findViewById(R.id.typeTextView);
            created_at=itemView.findViewById(R.id.created_at_text_view);
            deleteButton=itemView.findViewById(R.id.deleteItemButton);
        }
    }
}

