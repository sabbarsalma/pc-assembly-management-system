package com.example.commande_pc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Patterns;

import com.example.commande_pc.database.MemoryDataBaseHelper;
import com.example.commande_pc.database.SqliteDatabaseHelper;
import com.example.commande_pc.entity.User;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Utils {
    private static final String dateFormat = "dd/MM/yyyy à H:m:s";
    private static final String sqlDateFormat = "yyyy-MM-dd H:m:s";

    public static String loginPreferenceName = "loginPref";
    public static String dateToString(Date date){
        return new SimpleDateFormat(Utils.dateFormat).format(date);
    }
    public static String newDateToSQLString(){
        return new SimpleDateFormat(Utils.sqlDateFormat).format(new Date());
    }
    public static Date stringToDate(String dateInString){
        try {
            return new SimpleDateFormat(Utils.sqlDateFormat).parse(dateInString);
        } catch (ParseException e) {
            return new Date();
        }
    }
    public static void putUserIdInPreferences(Activity activity, long userId){
        SharedPreferences.Editor editor = activity.getSharedPreferences(Utils.loginPreferenceName, Context.MODE_PRIVATE).edit();
        editor.putLong("userId", userId);
        editor.apply();
    }
    public static long getUserIdInPreferences(Activity activity){
        return activity.getSharedPreferences(Utils.loginPreferenceName, Context.MODE_PRIVATE).getLong("userId", -1000);
    }
    public static User getLoggedUser(Activity activity){
        //return MemoryDataBaseHelper.findUserById(Utils.getUserIdInPreferences(activity));
        SqliteDatabaseHelper sqliteDatabaseHelper = new SqliteDatabaseHelper(activity.getApplicationContext());
        User user =  sqliteDatabaseHelper.findUserById(Utils.getUserIdInPreferences(activity));
        sqliteDatabaseHelper.close();
        return user;
    }
    public static boolean isEmailAdressValid(String email) {
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static boolean isPasswordValid(String password){
        return password != null && password.trim().length() > 5;
    }
    public static void showErrotOnTextEditInput(TextInputEditText textInputEditText, boolean isValid, String errorMessage){
        if(isValid){
            textInputEditText.setError(null);
        }else{
            textInputEditText.setError(errorMessage);
        }
    }
    public static boolean checkIfAllElementsAreTrue(boolean[] allElements){
        for (boolean element : allElements){
            if(!element) return false;
        }
        return true;
    }
    public static void resetAllTextInputEditText(TextInputEditText ... textInputEditTexts){
        for(TextInputEditText textInputEditText : textInputEditTexts){
            textInputEditText.setText("");
        }
    }
    public static String hashPassword(String password){
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
    public static boolean checkPassword(String bcryptHashString,String password){
        return BCrypt.verifyer().verify(password.toCharArray(), bcryptHashString).verified;
    }
    public static void showMessageDialog(Context context,String message,String title){
        new AlertDialog.Builder(context).setMessage(message).setTitle(title).setIcon(R.drawable.web_link).setNegativeButton("Ok",null).show();
    }
    public static String[] getItemsSubTypes(){
        return new String[]{"boitier", "carte mere", "barrette memoire", "disque dur", "ecran", "clavier souris", "navigateur Web", "suite bureautique", "outils de developpement"};
    }
}