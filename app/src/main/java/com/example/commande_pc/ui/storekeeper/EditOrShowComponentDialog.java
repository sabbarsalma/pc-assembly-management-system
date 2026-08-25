package com.example.commande_pc.ui.storekeeper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.commande_pc.MainActivity;
import com.example.commande_pc.R;
import com.example.commande_pc.adapters.ItemsListAdapter;
import com.example.commande_pc.entity.Item;
import com.example.commande_pc.entity.StoreKeeper;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class EditOrShowComponentDialog extends Dialog {
    private TextView componentType;
    private TextInputEditText subtypeEditText,descriptionEditText,quantityEditText,commentaireEditText;
    private Button increaseQuantityButton,decreaseQuantityButton,updateComponentButton;
    private LinearLayout notFoundLayout;
    private ConstraintLayout editRootLayout;
    private Item item;
    private ItemsListAdapter itemsListAdapter;
    public EditOrShowComponentDialog(@NonNull Context context,ItemsListAdapter itemsListAdapter,int position) {
        super(context);
        this.setContentView(R.layout.edit_component);
        this.item = itemsListAdapter.getItem(position);
        this.itemsListAdapter = itemsListAdapter;
        componentType= this.findViewById(R.id.componentType);
        subtypeEditText = this.findViewById(R.id.subtypeEditText);
        descriptionEditText = this.findViewById(R.id.descriptionEditText);
        quantityEditText = this.findViewById(R.id.quantityEditText);
        increaseQuantityButton= this.findViewById(R.id.increaseQuantityButton);
        decreaseQuantityButton= this.findViewById(R.id.decreaseQuantityButton);
        updateComponentButton= this.findViewById(R.id.addComponentButton);
        commentaireEditText = this.findViewById(R.id.commentaireEditText);
        notFoundLayout = this.findViewById(R.id.notFoundLayout);
        editRootLayout = this.findViewById(R.id.editRootLayout);

        notFoundLayout.setVisibility(View.GONE);
//        SqliteDatabaseHelper databaseConnection = new SqliteDatabaseHelper(this.getContext());
//        Item item = databaseConnection.findItemById(itemParam.getId());
//        databaseConnection.close();
        if(item == null){
            notFoundLayout.setVisibility(View.VISIBLE);
            editRootLayout.setVisibility(View.GONE);
        }else{
            notFoundLayout.setVisibility(View.GONE);
            editRootLayout.setVisibility(View.VISIBLE);

            componentType.setText("Type : " + item.getType().toUpperCase());
            subtypeEditText.setText(item.getSubtype());
            descriptionEditText.setText(item.getDescription());
            quantityEditText.setText(String.valueOf(item.getQuantity()));
            commentaireEditText.setText(String.valueOf(item.getComment()));

            increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentQuantity = Integer.parseInt(Objects.requireNonNull(quantityEditText.getText()).toString());
                    quantityEditText.setText(String.valueOf(currentQuantity + 1));
                }
            });
            decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentQuantity = Integer.parseInt(Objects.requireNonNull(quantityEditText.getText()).toString());
                    if(currentQuantity > 0){
                        quantityEditText.setText(String.valueOf(currentQuantity - 1));
                    }
                }
            });
            updateComponentButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    try{
                        item.setComment(commentaireEditText.getText().toString());
                    }catch (NullPointerException e){
                        item.setComment(null);
                    }
                    try {
                        item.setQuantity(Integer.parseInt(quantityEditText.getText().toString()));
                    }catch (NumberFormatException ignored) {
                    }catch (NullPointerException e){
                        item.setQuantity(0);
                    }
                    StoreKeeper storeKeeper = ((StoreKeeper) (MainActivity.getUser()));
                    if(storeKeeper.updateItem(item)){
                        new AlertDialog.Builder(v.getContext()).setMessage("Le composant est modifé avec succès").setTitle("information").setIcon(R.drawable.web_link).setNeutralButton("Ok",null).show();
                    }
                    dismiss();
                }
            });
        }
    }
}
