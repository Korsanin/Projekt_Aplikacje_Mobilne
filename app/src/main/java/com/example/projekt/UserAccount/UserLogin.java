package com.example.projekt.UserAccount;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projekt.DatabaseManagement.ItemReader;
import com.example.projekt.R;
import com.example.projekt.UsersOrder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class UserLogin extends AppCompatActivity {
    private Button button;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private CheckBox checkBox;
    private ItemReader.ItemReaderDbHelper dbHelper;
    public String TAG = "LOGIN";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        SharedPreferences sharedPreferences = getSharedPreferences("USERDATA", Context.MODE_PRIVATE);

        SharedPreferences savedData = getSharedPreferences("SAVED_DATA", Context.MODE_PRIVATE);

        button = findViewById(R.id.buttonLogin);
        checkBox = findViewById(R.id.rememberMe);

        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        button.setWidth(displayMetrics.widthPixels/2);
        checkBox.setWidth(displayMetrics.widthPixels/2);
        
        emailInput = findViewById(R.id.userEmailInput);
        passwordInput = findViewById(R.id.userPasswordInput);

        if(!savedData.getString("EMAIL","").equals("") && !savedData.getString("PASSWORD","").equals("")){
            emailInput.setText(savedData.getString("EMAIL",""));
            passwordInput.setText(savedData.getString("PASSWORD",""));
        }
        
        button.setOnClickListener(v ->{
            dbHelper = new ItemReader.ItemReaderDbHelper(getApplicationContext());

            try {
                long id = (long) dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT, BaseColumns._ID,"'"+emailInput.getText()+"'",ItemReader.ItemEntry.COLUMN_NAME_USER_EMAIL).get(0);

                String password = (String) dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT, ItemReader.ItemEntry.COLUMN_NAME_USER_PASSWORD,id+"",BaseColumns._ID).get(0);

                String password_in = passwordInput.getText()+"";

                if(password.equals(password_in)){
                    Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
                    int phone_number = (int) dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT, ItemReader.ItemEntry.COLUMN_NAME_USER_PHONE_NUMBER,id+"",BaseColumns._ID).get(0);
                    String email = (String) dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT, ItemReader.ItemEntry.COLUMN_NAME_USER_EMAIL,id+"",BaseColumns._ID).get(0);
                    String username = (String) dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT, ItemReader.ItemEntry.COLUMN_NAME_USER_USERNAME,id+"",BaseColumns._ID).get(0);
                    boolean rememberMe = checkBox.isChecked();
                    Intent intent = new Intent(getApplicationContext(), UsersOrder.class);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("PHONE",phone_number);
                    editor.putString("EMAIL",email);
                    editor.putString("USERNAME",username);
                    editor.putBoolean("REMEMBER_ME",rememberMe);
                    if(rememberMe){
                        SharedPreferences.Editor saved_data = savedData.edit();
                        saved_data.putString("EMAIL",email);
                        saved_data.putString("PASSWORD",password);
                        saved_data.apply();
                    }else{
                        SharedPreferences.Editor saved_data = savedData.edit();
                        saved_data.clear();
                        saved_data.apply();
                    }
                    editor.apply();
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, "Unable to log in", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e){
                Log.v(TAG,e.getMessage());
                Toast.makeText(this, "Unable to log in", Toast.LENGTH_SHORT).show();
            }
        });
    }
}