package com.example.commande_pc.ui.Assembler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commande_pc.MainActivity;
import com.example.commande_pc.adapters.GestionAdapter;
import com.example.commande_pc.databinding.GestionnaireDesCommandesBinding;
import com.example.commande_pc.entity.Assembler;
import com.example.commande_pc.entity.Order;
import com.example.commande_pc.entity.Requester;
import com.example.commande_pc.entity.Role;

import java.util.ArrayList;

public class GestionFragment extends Fragment {
    private GestionnaireDesCommandesBinding binding;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = GestionnaireDesCommandesBinding.inflate(inflater,container,false);
        recyclerView=binding.gestionnaireRecycleView;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        if(MainActivity.getUser().getRoleId()== Role.findRoleByRoleName("assembler").getId()){
            ArrayList<Order> order_save = ((Assembler) (MainActivity.getUser())).getAllOrders();
            recyclerView.setAdapter(new GestionAdapter(this,order_save));
            this.toogleVisibility(!order_save.isEmpty());
        }
        return binding.getRoot();
    }
    public void toogleVisibility(boolean recyclerViewVisible){
        if(recyclerViewVisible){
            recyclerView.setVisibility(View.VISIBLE);
        }
        else{
            recyclerView.setVisibility(View.GONE);
        }
    }
}
