package com.example.commande_pc.ui.requester;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commande_pc.adapters.RequesterListAdapter;
import com.example.commande_pc.databinding.FragmentListRequesterBinding;

public class ListRequesterFragment extends Fragment {
    private FragmentListRequesterBinding binding;
    private RecyclerView listRequesterRecyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListRequesterBinding.inflate(inflater, container, false);
        listRequesterRecyclerView = binding.requesterListRecycleView;
        listRequesterRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        listRequesterRecyclerView.setAdapter(new RequesterListAdapter());
        return binding.getRoot();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
