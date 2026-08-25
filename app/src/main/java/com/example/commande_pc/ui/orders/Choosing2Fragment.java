package com.example.commande_pc.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commande_pc.R;
import com.example.commande_pc.adapters.ChoiceItemAdapter;
import com.example.commande_pc.adapters.ItemsListAdapter;
import com.example.commande_pc.databinding.ChoosingItems1Binding;
import com.example.commande_pc.databinding.ChoosingItems2Binding;
import com.example.commande_pc.entity.Item;

import java.util.ArrayList;

public class Choosing2Fragment extends OrderChoiceFragment {
       @Override
    public int getPosition() {
        return 0;
    }
}
