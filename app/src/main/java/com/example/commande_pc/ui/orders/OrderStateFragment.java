package com.example.commande_pc.ui.orders;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commande_pc.LoginActivity;
import com.example.commande_pc.MainActivity;
import com.example.commande_pc.R;
import com.example.commande_pc.adapters.ItemsListAdapter;
import com.example.commande_pc.adapters.OrderListAdapter;
import com.example.commande_pc.databinding.SeeOrderStatusBinding;
import com.example.commande_pc.entity.Item;
import com.example.commande_pc.entity.Order;
import com.example.commande_pc.entity.Requester;
import com.example.commande_pc.entity.Role;
import com.example.commande_pc.entity.StoreKeeper;
import com.example.commande_pc.entity.User;

import java.util.ArrayList;
import java.util.Objects;

public class OrderStateFragment extends Fragment {
    private SeeOrderStatusBinding binding;
    private RecyclerView orderRecycleView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = SeeOrderStatusBinding.inflate(inflater,container,false);
        orderRecycleView = binding.orderRecycleView;
        orderRecycleView.setLayoutManager(new GridLayoutManager(getContext(),1));
        if(MainActivity.getUser().getRoleId() == Role.findRoleByRoleName("requester").getId()){
            ArrayList<Order> orders = ((Requester) (MainActivity.getUser())).getOrders();
            orderRecycleView.setAdapter(new OrderListAdapter(this,orders,null));
            this.toogleVisibility(!orders.isEmpty());


        }else{
            User.logout(requireActivity());
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
            return null;
        }
        return binding.getRoot();
    }
    public void toogleVisibility(boolean recycleViewVisible){
        if(recycleViewVisible){
            orderRecycleView.setVisibility(View.VISIBLE);

        }else{
            orderRecycleView.setVisibility(View.GONE);

        }
    }



}

