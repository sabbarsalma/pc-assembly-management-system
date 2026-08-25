package com.example.commande_pc.ui.requester;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.commande_pc.MainActivity;
import com.example.commande_pc.R;
import com.example.commande_pc.Utils;
import com.example.commande_pc.database.MemoryDataBaseHelper;
import com.example.commande_pc.databinding.FragmentAddRequesterBinding;
import com.example.commande_pc.entity.Administrator;
import com.example.commande_pc.entity.Requester;
import com.google.android.material.textfield.TextInputEditText;

public class AddRequesterFragment extends Fragment {
    private FragmentAddRequesterBinding binding;
    private TextInputEditText requesterLastNameEditTest;
    private TextInputEditText requesterFirstNameEditTest;
    private TextInputEditText requesterEmailEditTest;
    private TextInputEditText requesterPasswordEditTest;
    private TextInputEditText requesterConfirmPasswordEditTest;
    private Button requesterAddBtn;
    private ProgressBar requesterAddLoader;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddRequesterBinding.inflate(inflater, container, false);

        requesterLastNameEditTest = binding.requesterLastNameEditTest;
        requesterFirstNameEditTest = binding.requesterFistNameEditTest;
        requesterEmailEditTest      = binding.requesterEmailEditTest;
        requesterPasswordEditTest = binding.requesterPasswordEditTest;
        requesterConfirmPasswordEditTest = binding.requesterConfirmPasswordEditTest;
        requesterAddBtn = binding.requesterAddBtn;
        requesterAddLoader = binding.requesterAddLoader;

        requesterAddBtn.setEnabled(false);
        requesterAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requesterAddBtn.setEnabled(false);
                requesterAddLoader.setVisibility(View.VISIBLE);
                Administrator administrator = (Administrator) MainActivity.getUser();
                Requester requester = new Requester(requesterLastNameEditTest.getText().toString(), requesterFirstNameEditTest.getText().toString(), requesterEmailEditTest.getText().toString(),requesterPasswordEditTest.getText().toString());
                if(administrator.addRequester(requester) != -1){
                    Utils.showMessageDialog(getContext(),"Requester ajouté avec succès","Information");
                    requesterAddLoader.setVisibility(View.GONE);
                }else{
                    Utils.showMessageDialog(getContext(),"Une erreur est survenue lors de l'ajout.Veuillez réessayer!","Erreur");
                    requesterAddLoader.setVisibility(View.GONE);
                }
                Utils.resetAllTextInputEditText(requesterLastNameEditTest,requesterFirstNameEditTest,requesterEmailEditTest,requesterPasswordEditTest);
                //MemoryDataBaseHelper.addRequester(requesterLastNameEditTest.getText().toString(),requesterFirstNameEditTest.getText().toString(),requesterEmailEditTest.getText().toString(),requesterPasswordEditTest.getText().toString());
            }
        });
        this.applyTextChangeListenerToInputField(requesterEmailEditTest,requesterPasswordEditTest,requesterConfirmPasswordEditTest);
        return binding.getRoot();
    }

    private boolean[] isFormValid(){
        return new boolean[]{
                requesterEmailEditTest.getText() != null && Utils.isEmailAdressValid(requesterEmailEditTest.getText().toString()),
                requesterPasswordEditTest.getText() != null && Utils.isPasswordValid(requesterPasswordEditTest.getText().toString()),
                requesterPasswordEditTest.getText() != null && requesterConfirmPasswordEditTest.getText() != null && requesterPasswordEditTest.getText().toString().equals(requesterConfirmPasswordEditTest.getText().toString())
        };
    }

    private void updateFormState(){
        boolean[] formState = this.isFormValid();
        Utils.showErrotOnTextEditInput(requesterEmailEditTest,formState[0],getString(R.string.email_invalid));
        Utils.showErrotOnTextEditInput(requesterPasswordEditTest,formState[1],getString(R.string.password_invalid));
        Utils.showErrotOnTextEditInput(requesterConfirmPasswordEditTest,formState[2],getString(R.string.password_confirmation_invalid));
        requesterAddBtn.setEnabled(Utils.checkIfAllElementsAreTrue(formState));
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void applyTextChangeListenerToInputField(TextInputEditText ... inputFields){
        for(TextInputEditText inputField : inputFields){
            inputField.addTextChangedListener(new TextWatcher() {

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
}
