package com.example.commande_pc.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commande_pc.MainActivity;
import com.example.commande_pc.R;
import com.example.commande_pc.Utils;
import com.example.commande_pc.database.SqliteDatabaseHelper;
import com.example.commande_pc.entity.Item;
import com.example.commande_pc.entity.Order;
import com.example.commande_pc.entity.Requester;
import com.example.commande_pc.entity.StoreKeeper;
import com.example.commande_pc.ui.home.HomeFragment;
import com.example.commande_pc.ui.orders.DisplayItemsFragment;
import com.example.commande_pc.ui.orders.OnButtonClick;
import com.example.commande_pc.ui.orders.OrderStateFragment;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    private ArrayList<Order> orders;
    private final OrderStateFragment orderStateFragment;
    private final DisplayItemsFragment displayItemsFragment;
    private static ArrayList<Item>items;

    public OrderListAdapter (OrderStateFragment orderStateFragment,ArrayList<Order> orders,DisplayItemsFragment displayItemsFragment){
        this.orderStateFragment = orderStateFragment;
        this.orders = orders;
        this.displayItemsFragment = displayItemsFragment;


    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.state_preview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);
        String description= order.getState();
        String truncatedDescription = description.substring(0,Math.min(description.length(),20)) + "...";
        holder.stateTextview.setText(truncatedDescription);
        holder.orderIdTextView.setText(String.valueOf(orders.get(position).getId()));
        holder.dateTextView.setText(Utils.dateToString(orders.get(position).getCreatedAt()));
        holder.deleteOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.stateTextview.getText().charAt(0)=='E'){
                    new AlertDialog.Builder(v.getContext()).setMessage("Etes vous sûr de vouloir supprimer ?").setTitle("Confirmation").setIcon(R.drawable.web_link).setNegativeButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Requester requester = ((Requester) (MainActivity.getUser()));
                            int position_of_order = holder.getAdapterPosition();
                            if(requester.deleteOrder(orders.get(position_of_order))){
                                orders.remove(position_of_order);
                                SqliteDatabaseHelper sqliteDatabaseHelper = new SqliteDatabaseHelper();
                                sqliteDatabaseHelper.deleteOrder(orders.get(position_of_order).getId());
                                notifyItemRemoved(position_of_order);
                                orderStateFragment.toogleVisibility(!orders.isEmpty());
                            }
                        }
                    }).setPositiveButton("Non",null).show();
                }
                else{
                    holder.deleteOrderButton.setEnabled(false);
                }
            }
        });
        holder.displayMyItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Item> items1 = orders.get(holder.getAdapterPosition()).getOrderItems();

                DisplayItemsFragment displayItemsFragment = DisplayItemsFragment.newInstance(items1);

                FragmentManager fragmentManager =orderStateFragment.getActivity().getSupportFragmentManager();
                if(fragmentManager.getBackStackEntryCount()>0){
                    fragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.container,displayItemsFragment)
                        .commit();



            }
        });


    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView orderIdTextView, stateTextview,dateTextView;
        private Button deleteOrderButton;
        private Button displayMyItems;
        private CardView rootElement;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orederidTextView);
            stateTextview = itemView.findViewById(R.id.stateTextView);
            deleteOrderButton = itemView.findViewById(R.id.deleteOrderButton);
            rootElement = itemView.findViewById(R.id.rootElement);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            displayMyItems= itemView.findViewById(R.id.buttonDisplay);
        }
    }
}
