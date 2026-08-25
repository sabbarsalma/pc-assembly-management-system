package com.example.commande_pc.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.commande_pc.databinding.OrderConfirmationFragmentBinding;

public class ConfirmationFragment extends Fragment {
    OrderConfirmationFragmentBinding binding;
    Button orderConfirmBtn;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        binding = OrderConfirmationFragmentBinding.inflate(inflater,container,false);
        orderConfirmBtn = binding.confirmButton;
        orderConfirmBtn.setOnClickListener(v -> {
        });
        return binding.getRoot();
    }
}
