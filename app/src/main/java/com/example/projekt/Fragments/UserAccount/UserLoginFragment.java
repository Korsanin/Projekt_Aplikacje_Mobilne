package com.example.projekt.Fragments.UserAccount;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projekt.DatabaseManagement.ItemReader;
import com.example.projekt.R;
import com.google.android.material.textfield.TextInputEditText;

public class UserLoginFragment extends Fragment {
    private Button button;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private CheckBox checkBox;
    private ItemReader.ItemReaderDbHelper dbHelper;
    public String TAG = "LOGIN";

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper = new ItemReader.ItemReaderDbHelper(getContext());

        rootView = inflater.inflate(R.layout.fragment_user_login,container,false);

        Resources resources = getResources();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("USERDATA", Context.MODE_PRIVATE);

        SharedPreferences savedData = getContext().getSharedPreferences("SAVED_DATA", Context.MODE_PRIVATE);

        button = rootView.findViewById(R.id.buttonLogin);
        checkBox = rootView.findViewById(R.id.rememberMe);

        DisplayMetrics displayMetrics = new DisplayMetrics();

        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        button.setWidth(displayMetrics.widthPixels/2);
        checkBox.setWidth(displayMetrics.widthPixels/2);

        emailInput = rootView.findViewById(R.id.userEmailInput);
        passwordInput = rootView.findViewById(R.id.userPasswordInput);

        if(!savedData.getString("EMAIL","").equals("") && !savedData.getString("PASSWORD","").equals("")){
            emailInput.setText(savedData.getString("EMAIL",""));
            passwordInput.setText(savedData.getString("PASSWORD",""));
        }

        button.setOnClickListener(v ->{
            dbHelper = new ItemReader.ItemReaderDbHelper(getContext());

            try {
                long id = (long) dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT, BaseColumns._ID,"'"+emailInput.getText()+"'",ItemReader.ItemEntry.COLUMN_NAME_USER_EMAIL).get(0);

                String password = (String) dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT, ItemReader.ItemEntry.COLUMN_NAME_USER_PASSWORD,id+"",BaseColumns._ID).get(0);

                String password_in = passwordInput.getText()+"";

                if(password.equals(password_in)){
                    Toast.makeText(getContext(), resources.getString(R.string.logged_in), Toast.LENGTH_SHORT).show();
                    int phone_number = (int) dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT, ItemReader.ItemEntry.COLUMN_NAME_USER_PHONE_NUMBER,id+"",BaseColumns._ID).get(0);
                    String email = (String) dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT, ItemReader.ItemEntry.COLUMN_NAME_USER_EMAIL,id+"",BaseColumns._ID).get(0);
                    String username = (String) dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT, ItemReader.ItemEntry.COLUMN_NAME_USER_USERNAME,id+"",BaseColumns._ID).get(0);
                    boolean rememberMe = checkBox.isChecked();
//                    Intent intent = new Intent(getApplicationContext(), UsersOrder.class);
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
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                }
                else{
                    emailInput.setError(resources.getString(R.string.failed_to_log_in));
                    passwordInput.setError(resources.getString(R.string.failed_to_log_in));
                }
            }
            catch (Exception e){
                Log.v(TAG,e.getMessage());
                emailInput.setError(resources.getString(R.string.failed_to_log_in));
                passwordInput.setError(resources.getString(R.string.failed_to_log_in));
            }
        });

        return rootView;
    }
}
