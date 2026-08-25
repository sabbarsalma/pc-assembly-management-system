package com.example.commande_pc.ui.storekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commande_pc.LoginActivity;
import com.example.commande_pc.MainActivity;
import com.example.commande_pc.adapters.ItemsListAdapter;
import com.example.commande_pc.adapters.MaterielItemListadapter;
import com.example.commande_pc.adapters.SoftwareItemListadapter;
import com.example.commande_pc.database.SqliteDatabaseHelper;
import com.example.commande_pc.databinding.FragmentListComponentsBinding;
import com.example.commande_pc.entity.Assembler;
import com.example.commande_pc.entity.Item;
import com.example.commande_pc.entity.Role;
import com.example.commande_pc.entity.StoreKeeper;
import com.example.commande_pc.entity.User;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ListStockComponents extends Fragment {
    private FragmentListComponentsBinding binding;
    private TextInputEditText searchComponentEditText;
    private RecyclerView listStockRecycleView;
    private ConstraintLayout aucunResultatLayout;
    private Button showAllButton;
    private Button showMaterialComponent;
    private Button showSoftwareComponent;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListComponentsBinding.inflate(inflater, container, false);
        searchComponentEditText = binding.searchComponentEditText;
        listStockRecycleView = binding.listStockRecycleView;
        aucunResultatLayout= binding.aucunResultatLayout;
        showMaterialComponent=binding.materielFilterButton;
        showSoftwareComponent=binding.logicielFilterButton;
        listStockRecycleView.setLayoutManager(new GridLayoutManager(getContext(),1));
        if(MainActivity.getUser().getRoleId() == Role.findRoleByRoleName("storekeeper").getId()|| MainActivity.getUser().getRoleId()==Role.findRoleByRoleName("assembler").getId()){
            if(MainActivity.getUser().getRoleId()==Role.findRoleByRoleName("storekeeper").getId()){
                ArrayList<Item> items = ((StoreKeeper) (MainActivity.getUser())).getItems();
                listStockRecycleView.setAdapter(new ItemsListAdapter(this,items));
                this.toogleVisibility(!items.isEmpty());
            } else if (MainActivity.getUser().getRoleId()==Role.findRoleByRoleName("assembler").getId()) {
                ArrayList<Item> items = ((Assembler) (MainActivity.getUser())).getItems();
                listStockRecycleView.setAdapter(new ItemsListAdapter(this,items));
                this.toogleVisibility(!items.isEmpty());

            }
        }else{
            User.logout(requireActivity());
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
            return null;
        }

        showMaterialComponent.setEnabled(true);
        showMaterialComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listStockRecycleView.setLayoutManager(new GridLayoutManager(getContext(),1));
                listStockRecycleView.setAdapter(new MaterielItemListadapter());

            }
        });
        showSoftwareComponent.setEnabled(true);
        showSoftwareComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listStockRecycleView.setLayoutManager(new GridLayoutManager(getContext(),1));
                listStockRecycleView.setAdapter(new SoftwareItemListadapter());

            }
        });
        return binding.getRoot();
    }
    public void toogleVisibility(boolean recycleViewVisible){
        if(recycleViewVisible){
            listStockRecycleView.setVisibility(View.VISIBLE);
            aucunResultatLayout.setVisibility(View.GONE);
        }else{
            listStockRecycleView.setVisibility(View.GONE);
            aucunResultatLayout.setVisibility(View.VISIBLE);
        }
    }
    public TextInputEditText getSearchComponentEditText(){
        return searchComponentEditText;
    }
}
