package com.example.commande_pc.ui.storekeeper;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.commande_pc.LoginActivity;
import com.example.commande_pc.MainActivity;
import com.example.commande_pc.R;
import com.example.commande_pc.Utils;
import com.example.commande_pc.database.SqliteDatabaseHelper;
import com.example.commande_pc.databinding.AddComponentBinding;
import com.example.commande_pc.entity.Item;
import com.example.commande_pc.entity.MaterielItem;
import com.example.commande_pc.entity.Role;
import com.example.commande_pc.entity.SofwareItem;
import com.example.commande_pc.entity.StoreKeeper;
import com.example.commande_pc.entity.User;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class AddComponentFragment extends Fragment {
    private AddComponentBinding binding;
    private Button increaseQuantityButton;
    private Button decreaseQuantityButton;
    private TextInputEditText quantityEditText;
    private TextInputEditText subtypeEditText;
    private TextInputEditText descriptionEditText;
    private TextInputEditText commentaireEditText;
    private RadioGroup typeMaterialRadioGroup;
    private Button addComponentButton;
    String typeComposant = "materiel";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(MainActivity.getUser().getRoleId() != Role.findRoleByRoleName("storekeeper").getId()){
            User.logout(this.requireActivity());
            Intent intent = new Intent(this.getContext(), LoginActivity.class);
            this.startActivity(intent);
            this.requireActivity().finish();
            return null;
        }
        binding = AddComponentBinding.inflate(inflater, container, false);
        increaseQuantityButton = binding.increaseQuantityButton;
        decreaseQuantityButton = binding.decreaseQuantityButton;
        quantityEditText = binding.quantityEditText;
        typeMaterialRadioGroup = binding.typeMaterialRadioGroup;
        subtypeEditText = binding.subtypeEditText;
        descriptionEditText = binding.descriptionEditText;
        addComponentButton = binding.addComponentButton;
        commentaireEditText = binding.commentaireEditText;
        addComponentButton.setEnabled(false);

        quantityEditText.setText("1");

        typeMaterialRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.materialRadioButton){
                    typeComposant = "materiel";
                }else{
                    typeComposant = "logiciel";
                }
            }
        });

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
                if(currentQuantity > 1){
                    quantityEditText.setText(String.valueOf(currentQuantity - 1));
                }
            }
        });
        addComponentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subType = subtypeEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                int quantity = 1;
                try {
                    quantity = Integer.parseInt(Objects.requireNonNull(quantityEditText.getText()).toString());
                }catch (NumberFormatException ignored) {
                }
                String commentaire = commentaireEditText.getText().toString();
                StoreKeeper storeKeeper = (StoreKeeper)(MainActivity.getUser());
                Item item = null;
                if(typeComposant.equals("materiel")){
                    item = new MaterielItem(subType,description,quantity,commentaire, null);
                }else{
                    item = new SofwareItem(subType,description,quantity,commentaire, null);
                }
                if(storeKeeper.addItem(item) == -1){
                    Toast.makeText(getActivity().getApplicationContext(),"Une erreur est survenue.Veuillez réessayer!",Toast.LENGTH_SHORT).show();
                }else{
                    Utils.resetAllTextInputEditText(subtypeEditText,descriptionEditText,commentaireEditText);
                    quantityEditText.setText("1");
                    //Toast.makeText(getActivity().getApplicationContext(),"Composant ajouté avec succès",Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(v.getContext()).setMessage("Le composant est enregistré avec succès").setTitle("information").setIcon(R.drawable.web_link).setNeutralButton("Ok",null).show();
                }
            }
        });
        ApplyTextWatcherOnEditText(subtypeEditText,descriptionEditText,quantityEditText);
        return binding.getRoot();
    }
    private void ApplyTextWatcherOnEditText(TextInputEditText ...textInputEditTexts){
        for(TextInputEditText textInputEditText : textInputEditTexts){
            textInputEditText.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    updateFormState();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }
    private boolean[] fieldsvalids(){
        int nombre = 0;
        try {
            nombre = Integer.parseInt(Objects.requireNonNull(quantityEditText.getText()).toString());
        }catch (NumberFormatException ignored) {}
        return new boolean[]{
                subtypeEditText.getText() != null && subtypeEditText.getText().toString().trim().length() > 0
                , descriptionEditText.getText() != null && descriptionEditText.getText().toString().trim().length() > 0, quantityEditText.getText() != null && nombre >= 1};
    }
    private void updateFormState(){
        boolean[] fieldsValidity = this.fieldsvalids();
        Utils.showErrotOnTextEditInput(subtypeEditText,fieldsValidity[0],"Veuillez entrez le sous titre");
        Utils.showErrotOnTextEditInput(descriptionEditText,fieldsValidity[1],"Veuillez entrez la description");
        Utils.showErrotOnTextEditInput(quantityEditText,fieldsValidity[2],"Quantité invalide");
        addComponentButton.setEnabled(Utils.checkIfAllElementsAreTrue(fieldsValidity));
    }
}