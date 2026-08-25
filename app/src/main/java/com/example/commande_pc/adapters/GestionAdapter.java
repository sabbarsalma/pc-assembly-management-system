package com.example.commande_pc.adapters;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commande_pc.R;
import com.example.commande_pc.Utils;
import com.example.commande_pc.database.SqliteDatabaseHelper;
import com.example.commande_pc.entity.Item;
import com.example.commande_pc.entity.Order;
import com.example.commande_pc.entity.Requester;
import com.example.commande_pc.ui.Assembler.GestionFragment;
import com.example.commande_pc.ui.Assembler.MaVue;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GestionAdapter extends RecyclerView.Adapter<GestionAdapter.ViewHolder> {
    private ArrayList<Order> orders_save;
    private final GestionFragment gestionFragment;

    public GestionAdapter(GestionFragment gestionFragment,ArrayList<Order>orders_save){
        this.gestionFragment=gestionFragment;
        this.orders_save=orders_save;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.assembler_first_preview,parent,false));

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        for(Order order: orders_save){
            System.out.println(order.getId());
        }
        long requester_id = orders_save.get(position).getRequester_id();
        SqliteDatabaseHelper dbHelper = new SqliteDatabaseHelper();
        Requester requester =(Requester) dbHelper.findUserById(requester_id);
        String description = requester.getFirstName()+requester.getLastName();
        String truncatedDescription = description.substring(0,Math.min(description.length(),20)) + "...";
        try{
            holder.requesterNameTextview.setText( "Demandeur:"+requester.getFirstName()+" "+requester.getLastName());
            holder.orderIdTextView.setText(String.valueOf("id"+" "+orders_save.get(position).getId()));
            holder.dateTextView.setText(Utils.dateToString(orders_save.get(position).getCreatedAt()));
            holder.display.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<Item> items1 = orders_save.get(holder.getAdapterPosition()).getOrderItems();
                    Order order= orders_save.get(holder.getAdapterPosition());
                    MaVue maVue= MaVue.newInstance1(items1,order);
                    FragmentManager fragmentManager = gestionFragment.getActivity().getSupportFragmentManager();
                    if(fragmentManager.getBackStackEntryCount()>0){
                        fragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                    fragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentManager.beginTransaction()
                            .replace(R.id.container,maVue)
                            .commit();

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }



    }

    @Override
    public int getItemCount() {
        return orders_save.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView orderIdTextView, requesterNameTextview,dateTextView;
        private Button display;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orederidTextView1);
            requesterNameTextview=itemView.findViewById(R.id.requesterTextView1);
            dateTextView = itemView.findViewById(R.id.dateTextView1);
            display = itemView.findViewById(R.id.buttonDisplay1);

        }
    }
}
