package com.example.commande_pc.ui.Assembler;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commande_pc.R;
import com.example.commande_pc.adapters.MaVueAdapter;
import com.example.commande_pc.database.SqliteDatabaseHelper;
import com.example.commande_pc.databinding.VisualisationAssemblerBinding;
import com.example.commande_pc.entity.Item;
import com.example.commande_pc.entity.Order;
import com.example.commande_pc.ui.orders.OrderStateFragment;
import com.example.commande_pc.ui.storekeeper.ListStockComponents;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class MaVue extends Fragment {
    private static final String args_items1="items_key";
    private static final String args_order="order_key";
    private ArrayList<Item> items;
    private Order order;
    private VisualisationAssemblerBinding binding;
    private RecyclerView assrecyclerview;
    private Button visualiser;
    private Button voir_stock;

    public static MaVue newInstance1(ArrayList<Item>items, Order order){
        MaVue maVue= new MaVue();
        Bundle args1=new Bundle();
        args1.putSerializable(args_items1,items);
        args1.putSerializable(args_order,order);
        maVue.setArguments(args1);
        return maVue;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            items=(ArrayList<Item>) getArguments().getSerializable(args_items1);
            order=(Order)getArguments().getSerializable(args_order);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding=VisualisationAssemblerBinding.inflate(inflater,container,false);
        assrecyclerview = binding.otherRecycleview;
        visualiser=binding.changerEtat;
        voir_stock=binding.voirStock;
        assrecyclerview.setLayoutManager(new GridLayoutManager(getContext(),1));
        if(items!=null&& !items.isEmpty()){
            assrecyclerview.setAdapter(new MaVueAdapter(this,items));
            toogleVisibility(true);
        }
        visualiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
                View dialog= LayoutInflater.from(getContext()).inflate(R.layout.newdialog,null);
                CheckBox checkBox1= dialog.findViewById(R.id.checkBox1);
                CheckBox checkBox2= dialog.findViewById(R.id.checkBox2);
                CheckBox checkBox3 = dialog.findViewById(R.id.checkBox3);
                CheckBox checkBox4 = dialog.findViewById(R.id.checkBox4);
                SqliteDatabaseHelper dbHelper = new SqliteDatabaseHelper();
                ArrayList<Order> orders = dbHelper.getAllOrder();
                checkBox1.setOnCheckedChangeListener((buttonView,isChecked)->{
                    for(Order order1 : orders){
                        if(order1.getId()==order.getId()){

                            dbHelper.updateState(order1.getId(),"Accepté");
                            visualiser.setEnabled(false);


                        }
                    }



                });
                checkBox2.setOnCheckedChangeListener((buttonView,isChecked)->{
                    for(Order order1 : orders){
                        if(order1.getId()==order.getId()){
                            dbHelper.updateState(order1.getId(),"Mis en attente");

                        }
                    }

                });
                checkBox3.setOnCheckedChangeListener((buttonView,isChecked)->{
                    for(Order order1 : orders){
                        if(order1.getId()==order.getId()){
                            dbHelper.updateState(order1.getId(),"Rejeté");
                           BottomSheetDialog bottomSheetDialog1 = new BottomSheetDialog(requireContext());
                           View dialog1 = LayoutInflater.from(getContext()).inflate(R.layout.sayreason,null);
                           bottomSheetDialog1.setContentView(dialog1);
                           bottomSheetDialog1.show();
                            if(assrecyclerview.getAdapter()!=null){
                                assrecyclerview.getAdapter().notifyDataSetChanged();
                            }
                            toogleVisibility(!orders.isEmpty());
                            break;

                        }
                    }
                });
                checkBox4.setOnCheckedChangeListener((buttonView,isChecked)->{
                    for(Order order1 : orders){
                        if(order1.getId()==order.getId()){
                            dbHelper.updateState(order1.getId(),"Livré");
                            if(assrecyclerview.getAdapter()!=null){
                                assrecyclerview.getAdapter().notifyDataSetChanged();
                            }
                            toogleVisibility(!orders.isEmpty());
                            break;

                        }
                    }


                });
                bottomSheetDialog.setContentView(dialog);
                bottomSheetDialog.show();

            }

        });
        voir_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListStockComponents listStockComponents= new ListStockComponents();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container,listStockComponents)
                        .addToBackStack(null)
                        .commit();

            }
        });
        return binding.getRoot();
    }
    public  void toogleVisibility(boolean recycleViewVisible){
        if(recycleViewVisible){
            assrecyclerview.setVisibility(View.VISIBLE);
        }
        else {
            assrecyclerview.setVisibility(View.GONE);
        }
    }

}
