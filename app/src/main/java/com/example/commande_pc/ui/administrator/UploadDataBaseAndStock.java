package com.example.commande_pc.ui.administrator;


import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.commande_pc.database.SqliteDatabaseHelper;
import com.example.commande_pc.databinding.UploadDbAndStockBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class UploadDataBaseAndStock extends Fragment {
    private UploadDbAndStockBinding binding;
    private Button ajouterFichier;
    private Button reinitialiser;
    private TextInputEditText votrefichier;
    private static final int PICK_FILE_REQUEST_CODE = 1;
    String file="";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        binding=UploadDbAndStockBinding.inflate(inflater,container,false);
        ajouterFichier=binding.buttonAjouterUnFichier;
        reinitialiser=binding.buttonReinitialiser;
        votrefichier=binding.attachmentPath;

        ajouterFichier.setEnabled(true);
        ajouterFichier.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                openFileChooser();


            }
        });
        reinitialiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SqliteDatabaseHelper db=new SqliteDatabaseHelper();
                Toast progressToast = Toast.makeText(getActivity(),"Réinitialisation en cours",Toast.LENGTH_SHORT);
                progressToast.show();
                Handler handler= new Handler();
                handler.postDelayed(()->{
                    db.reinitializeStock(file);
                    progressToast.cancel();
                    Toast.makeText(getActivity(),"Vous avez réinitialiser avec succes",Toast.LENGTH_SHORT).show();


                },1000);


            }
        });







        return binding.getRoot();

    }
    private void openFileChooser(){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
       try{
           startActivityForResult(Intent.createChooser(intent,"Select a file"),PICK_FILE_REQUEST_CODE);
       }catch (android.content.ActivityNotFoundException e){
           Toast.makeText(getActivity(),"Please install a file Manager",Toast.LENGTH_SHORT).show();
       }
    }
    public void onActivityResult(int code,int code2,Intent data){
        super.onActivityResult(code, code2, data);
        if(code==PICK_FILE_REQUEST_CODE&& code2==getActivity().RESULT_OK){
            if (data!=null){
                Uri uri=data.getData();
                votrefichier.setText(uri.getPath());

                Toast.makeText(getActivity(),"File Selected:" +uri.getPath(),Toast.LENGTH_SHORT).show();
                readMyFile(uri);
            }
        }
    }

    public  void readMyFile(Uri urifile)
    {
        System.out.println("J'ai été appelé");


        try {


            InputStream inputStream=getActivity().getContentResolver().openInputStream(urifile);
            BufferedReader reader= new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder s= new StringBuilder();
            String line;
            while((line=reader.readLine())!=null){
                s.append(line).append("\n");
            }
            file=s.toString();



        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}
