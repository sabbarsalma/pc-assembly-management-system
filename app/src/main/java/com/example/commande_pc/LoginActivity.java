package com.example.commande_pc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.commande_pc.database.MemoryDataBaseHelper;
import com.example.commande_pc.database.SqliteDatabaseHelper;
import com.example.commande_pc.entity.User;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private Button submitBtn;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private ProgressBar loader;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        submitBtn = findViewById(R.id.submitBtn);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loader = findViewById(R.id.loader);

        submitBtn.setClickable(false);

        applyFormStateListenerToInputFields(new TextInputEditText[]{emailInput, passwordInput});

        submitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                submitBtn.setClickable(false);
                loader.setVisibility(View.VISIBLE);
//                new Handler().postDelayed(() -> {
//
//                },3000);
                SqliteDatabaseHelper dbHelper = new SqliteDatabaseHelper(getApplicationContext());
                //User user =  MemoryDataBaseHelper.findUserByCredentials(Objects.requireNonNull(emailInput.getText()).toString(), Objects.requireNonNull(passwordInput.getText()).toString());
                User user =  dbHelper.findUserByCredentials(Objects.requireNonNull(emailInput.getText()).toString(), Objects.requireNonNull(passwordInput.getText()).toString());
                loader.setVisibility(View.GONE);
                if(user == null){
                    findViewById(R.id.showErrorBox).setVisibility(View.VISIBLE);
                }else{
                    Utils.putUserIdInPreferences(LoginActivity.this,user.getId());
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                submitBtn.setClickable(true);
            }
        });
    }
    private boolean[] isFormValid() {
        return new boolean[]{emailInput.getText() != null && Utils.isEmailAdressValid(emailInput.getText().toString()),passwordInput.getText() != null && Utils.isPasswordValid(passwordInput.getText().toString())};
    }
    private void updateFormState(){
        boolean[] formState = isFormValid();
        Utils.showErrotOnTextEditInput(emailInput,formState[0],getString(R.string.email_invalid));
        Utils.showErrotOnTextEditInput(passwordInput,formState[1],getString(R.string.password_invalid));
        submitBtn.setClickable(Utils.checkIfAllElementsAreTrue(formState));
    }
    private void applyFormStateListenerToInputFields(TextView[] inputs) {
        for(TextView v : inputs) {
            v.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    updateFormState();
                }
            });
        }
    }
}

