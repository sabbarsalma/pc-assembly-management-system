package com.example.commande_pc.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.commande_pc.databinding.OrderFirstFragmentBinding;

public class FirstFragment extends Fragment {
    OrderFirstFragmentBinding orderFirstFragmentBinding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        orderFirstFragmentBinding = OrderFirstFragmentBinding.inflate(inflater,container,false);
        return orderFirstFragmentBinding.getRoot();
    }
}
