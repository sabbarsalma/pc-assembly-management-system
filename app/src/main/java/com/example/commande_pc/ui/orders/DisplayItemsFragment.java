package com.example.commande_pc.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commande_pc.MainActivity;
import com.example.commande_pc.adapters.DisplayItemAdapter;
import com.example.commande_pc.databinding.DisplayItemsBinding;
import com.example.commande_pc.entity.Item;
import com.example.commande_pc.entity.Order;
import com.example.commande_pc.entity.Requester;
import com.example.commande_pc.entity.Role;

import java.util.ArrayList;

public class DisplayItemsFragment extends Fragment {
    private static final String args_items="items_key";
    private ArrayList<Item> items;
    private DisplayItemsBinding binding;
    private RecyclerView itemRecycleView;

    public static DisplayItemsFragment newInstance(ArrayList<Item>items){
        DisplayItemsFragment fragment = new DisplayItemsFragment();
        Bundle args= new Bundle();
        args.putSerializable(args_items,items);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            items = (ArrayList<Item>) getArguments().getSerializable(args_items);
            System.out.println("Je ne suis pas vide");
        }else{
            System.out.println("Je suis vide");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding= DisplayItemsBinding.inflate(inflater,container,false);
        itemRecycleView=binding.itemsRecycleView;
        itemRecycleView.setLayoutManager(new GridLayoutManager(getContext(),1));
        if(items!=null && !items.isEmpty()){

            itemRecycleView.setAdapter(new DisplayItemAdapter(this,items));
            toogleVisibility(true);

        }else {
            toogleVisibility(false);
        }
        return binding.getRoot();

    }



    public void toogleVisibility(boolean recycleViewVisible){
        if(recycleViewVisible){
            itemRecycleView.setVisibility(View.VISIBLE);

        }else{
            itemRecycleView.setVisibility(View.GONE);

        }
    }
}
