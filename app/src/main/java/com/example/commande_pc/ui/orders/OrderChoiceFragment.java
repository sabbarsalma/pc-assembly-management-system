package com.example.commande_pc.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commande_pc.R;
import com.example.commande_pc.Utils;
import com.example.commande_pc.adapters.ChoiceItemAdapter;
import com.example.commande_pc.databinding.ChoosingItems2Binding;
import com.example.commande_pc.entity.Item;

import java.util.ArrayList;

public abstract class OrderChoiceFragment extends Fragment {
    RecyclerView recyclerView;
    TextView choosing;
    private ChoosingItems2Binding binding;
    private ArrayList<Item> items;
    public OrderChoiceFragment(){
        super();
    }
    public void addToList(long item_id,int quantity){
        PlaceOrderFragment.addToOrderItems(item_id,quantity,getPosition());
    }
    public void deleteFromList(long item_id,int quantity){
        PlaceOrderFragment.removeFromOrderItems(item_id,quantity,getPosition());
    }
    protected void applyAdapterToRecycleView(){
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        Bundle bundle = getArguments();

        items = (ArrayList<Item>) bundle.getSerializable("items");
        if(items == null)
            items = new ArrayList<>();

        recyclerView.setAdapter(new ChoiceItemAdapter(this,items));
    }
    abstract int getPosition();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        binding = ChoosingItems2Binding.inflate(inflater,container,false);
        recyclerView = binding.recycleview;
        choosing = binding.choosing;
        choosing.setText(getString(R.string.choice_title) + Utils.getItemsSubTypes()[this.getPosition()]);
        applyAdapterToRecycleView();
        return binding.getRoot();
    }
}
