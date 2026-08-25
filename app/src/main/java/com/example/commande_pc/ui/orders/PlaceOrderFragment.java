package com.example.commande_pc.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.commande_pc.MainActivity;
import com.example.commande_pc.R;
import com.example.commande_pc.Utils;
import com.example.commande_pc.database.SqliteDatabaseHelper;
import com.example.commande_pc.databinding.ChoosingItems1Binding;
import com.example.commande_pc.databinding.ChoosingItems2Binding;
import com.example.commande_pc.databinding.ChoosingItems4Binding;
import com.example.commande_pc.databinding.ChosingItems3Binding;
import com.example.commande_pc.entity.AllowedCombinaison;
import com.example.commande_pc.entity.Item;
import com.example.commande_pc.entity.OrderItem;
import com.example.commande_pc.entity.OrderItemConstraint;
import com.example.commande_pc.entity.Requester;
import com.example.commande_pc.entity.StoreKeeper;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PlaceOrderFragment extends Fragment {
    private ChoosingItems1Binding binding;
    private Button begin;
    private Button previous;
    static int step = -1;
    private static final ArrayList<OrderItemConstraint> orderItemsConstraints = new ArrayList<>();
    public static ArrayList<ArrayList<OrderItem>> orderItems = new ArrayList<>();
    {
        resetOrdersList();
        orderItemsConstraints.add(new OrderItemConstraint(1,1));
        orderItemsConstraints.add(new OrderItemConstraint(1,1));
        orderItemsConstraints.add(new OrderItemConstraint(1,4));
        orderItemsConstraints.add(new OrderItemConstraint(1,2,true, AllowedCombinaison.MIXT_ALLOWED));
        orderItemsConstraints.add(new OrderItemConstraint(1,3));
        orderItemsConstraints.add(new OrderItemConstraint(1,1));
        orderItemsConstraints.add(new OrderItemConstraint(1,1));
        orderItemsConstraints.add(new OrderItemConstraint(0,1,false));
        orderItemsConstraints.add(new OrderItemConstraint(1,3,false,AllowedCombinaison.ONLY_DIFFRERENT));
    }
    private static final ArrayList<Item> items = ((Requester) (MainActivity.getUser())).getItems();
    private static final String[] ordersSubTypes = Utils.getItemsSubTypes();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = ChoosingItems1Binding.inflate(inflater,container,false);
        begin = binding.button;
        previous = binding.previous;
        previous.setEnabled(false);
        previous.setText("Précédent");
        begin.setText("Démarrer");
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FirstFragment()).addToBackStack(null).commit();
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextStep();
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousStep();
            }
        });
        return binding.getRoot();
    }
    private void nextStep(){
        previous.setEnabled(true);
        step++;
        if(step < 0 || step > 9)
            return;
        Bundle args = new Bundle();
        if(step != 9){
            ArrayList<Item> filteredItems = (ArrayList<Item>) items.stream().filter(item -> item.getSubtype().equalsIgnoreCase(ordersSubTypes[step])).collect(Collectors.toList());
            args.putSerializable("items",filteredItems);
        }


        if(step == 9){
            step--;
            OrderItemConstraint constraint = checkConstraints();
            if(constraint != null){
                Utils.showMessageDialog(getContext(),getConstraintMessage(constraint),"Erreur");
                return;
            }else{
                ((Requester) (MainActivity.getUser())).newOrder(orderItems);
                resetOrdersList();
                Utils.showMessageDialog(getContext(),"Commande passée avec succès","Succès");
            }
            step = 0;
            previous.setEnabled(true);
            begin.setEnabled(true);
            previous.setText("Précédent");
            begin.setText("Démarrer");
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FirstFragment()).addToBackStack(null).commit();
        }
        Fragment next= getFragmentByStep(step);

        if(next != null && step != 9){
            next.setArguments(args);
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, next).addToBackStack(null).commit();
            if(step >=0)
                begin.setText("Suivant");
        }
    }
    private void previousStep(){
        step--;
        if(step < 0 || step >= 8)
            return;
        if(step == 0)
            previous.setEnabled(false);
        ArrayList<Item> filteredItems = (ArrayList<Item>) items.stream().filter(item -> item.getSubtype().equalsIgnoreCase(ordersSubTypes[step])).collect(Collectors.toList());
        Bundle args = new Bundle();
        args.putSerializable("items",filteredItems);
        Fragment next= getFragmentByStep(step);

        if(next != null){
            next.setArguments(args);
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, next).addToBackStack(null).commit();
            if(step >0)
                begin.setText("Suivant");
            else
                begin.setText("Démarrer");
        }
    }
    private Fragment getFragmentByStep(int step) {
        Fragment next = null;
        switch (step) {
            case 0:
                next = new Choosing2Fragment();
                break;
            case 1:
                next = new Choosing3Fragment();
                break;
            case 2:
                next = new Choosing4Fragment();
                break;
            case 3:
                next = new Choosing5Fragment();
                break;
            case 4:
                next = new Choosing6Fragment();
                break;
            case 5:
                next = new Choosing7Fragment();
                break;
            case 6:
                next = new Choosing8Fragment();
                break;
            case 7:
                next = new Choosing9Fragment();
                break;
            case 8:
                next = new Choosing10Fragment();
                break;
            case 9:
                next = new ConfirmationFragment();
                break;
            default:
                break;
        }
        return next;
    }
    public static void addToOrderItems(long item_id, int quantity,int position){
        orderItems.get(position).add(new OrderItem(item_id,quantity));
    }
    public static void removeFromOrderItems(long item_id, int quantity,int position){
        orderItems.get(position).removeIf(o -> o.getItemId() == item_id);
    }
    public static OrderItemConstraint checkConstraints(){
        for(int i=0; i<orderItems.size(); i++){
            OrderItemConstraint constraint = orderItemsConstraints.get(i);
            if(!isValid(orderItems.get(i),constraint)){
                constraint.setErrorPosition(i);
                return constraint;
            }
        }
        return null;
    }
    private static boolean isValid(ArrayList<OrderItem> items,OrderItemConstraint  constraint){
        int quantity = getQuantity(items);
        System.out.println(quantity);
        if(constraint.isRange()){
            return quantity >= constraint.getMin() && quantity <= constraint.getMax();
        }else{
            return quantity == constraint.getMin() || quantity == constraint.getMax();
        }
    }
    private static int getQuantity(ArrayList<OrderItem> items){
        int quantity = 0;
        for (OrderItem item:items) {
            quantity += item.getQuantity();
        }
        return quantity;
    }
    public static String getConstraintMessage(OrderItemConstraint constraint){
        return "Vous devez ajouter " + constraint.getMin() + (constraint.isRange() ?  " à" : " ou")  + constraint.getMax() + " " + ordersSubTypes[constraint.getPosition()] ;
    }
    private void resetOrdersList(){
        orderItems.clear();
        for(int i=0;i<9;i++){
            orderItems.add(new ArrayList<>());
        }
    }
}

