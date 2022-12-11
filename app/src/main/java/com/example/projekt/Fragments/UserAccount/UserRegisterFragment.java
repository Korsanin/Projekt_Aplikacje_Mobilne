package com.example.projekt.Fragments.UserAccount;

import static com.example.projekt.DatabaseManagement.CreateData.CREATE_DATA_TAG;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.projekt.DatabaseManagement.ItemReader;
import com.example.projekt.R;
import com.google.android.material.textfield.TextInputEditText;

public class UserRegisterFragment extends Fragment {
    private TextInputEditText usernameInput;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private TextInputEditText passwordRepeatInput;
    private TextInputEditText phoneNumberInput;
    private TextInputEditText addressInput;
    private Button button;
    private ItemReader.ItemReaderDbHelper dbHelper;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper = new ItemReader.ItemReaderDbHelper(getContext());

        rootView = inflater.inflate(R.layout.fragment_user_register,container,false);

        Resources resources = getResources();

        usernameInput = rootView.findViewById(R.id.userRegisterUsernameInput);
        emailInput = rootView.findViewById(R.id.userRegisterEmailInput);
        passwordInput = rootView.findViewById(R.id.userRegisterPasswordInput);
        passwordRepeatInput = rootView.findViewById(R.id.userRegisterRepeatPasswordInput);
        phoneNumberInput = rootView.findViewById(R.id.userRegisterPhoneNumberInput);
        addressInput = rootView.findViewById(R.id.userRegisterAddressInput);
        button = rootView.findViewById(R.id.buttonRegister);

        button.setOnClickListener(v -> {
            dbHelper = new ItemReader.ItemReaderDbHelper(getContext());

            String username = String.valueOf(usernameInput.getText());
            String email = String.valueOf(emailInput.getText());
            String password = String.valueOf(passwordInput.getText());
            String password_repeat = String.valueOf(passwordRepeatInput.getText());
            String phoneNumber = String.valueOf(phoneNumberInput.getText());
            String address = String.valueOf(addressInput.getText());

            boolean isUsernameAvailable=false;
            boolean isEmailAvailable=false;
            boolean doPasswordMatch=false;

            if(dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT, BaseColumns._ID,"'"+username+"'", ItemReader.ItemEntry.COLUMN_NAME_USER_USERNAME).isEmpty()){
                isUsernameAvailable = true;
            }

            if(dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT, BaseColumns._ID,"'"+email+"'", ItemReader.ItemEntry.COLUMN_NAME_USER_EMAIL).isEmpty()){
                isEmailAvailable = true;
            }

            if(password.equals(password_repeat)){
                doPasswordMatch = true;
            }

            if (isUsernameAvailable && isEmailAvailable && doPasswordMatch){
                SQLiteDatabase db_write = dbHelper.getWritableDatabase();

                ContentValues user_account_values = new ContentValues();

                user_account_values.put(ItemReader.ItemEntry.COLUMN_NAME_USER_USERNAME,username);
                user_account_values.put(ItemReader.ItemEntry.COLUMN_NAME_USER_EMAIL,email);
                user_account_values.put(ItemReader.ItemEntry.COLUMN_NAME_USER_PASSWORD,password);
                user_account_values.put(ItemReader.ItemEntry.COLUMN_NAME_USER_PHONE_NUMBER,phoneNumber);
                user_account_values.put(ItemReader.ItemEntry.COLUMN_NAME_USER_ADDRESS,address);

                long newRowIdUserAccount = db_write.insert(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT,null,user_account_values);

                Log.v(CREATE_DATA_TAG,newRowIdUserAccount + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT,ItemReader.ItemEntry.COLUMN_NAME_USER_USERNAME,String.valueOf((int)newRowIdUserAccount),BaseColumns._ID) + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT,ItemReader.ItemEntry.COLUMN_NAME_USER_EMAIL,String.valueOf((int)newRowIdUserAccount),BaseColumns._ID) + " \n "+dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT,ItemReader.ItemEntry.COLUMN_NAME_USER_PASSWORD,String.valueOf((int)newRowIdUserAccount),BaseColumns._ID)+ " \n "+dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT,ItemReader.ItemEntry.COLUMN_NAME_USER_PHONE_NUMBER,String.valueOf((int)newRowIdUserAccount),BaseColumns._ID)+ " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT,ItemReader.ItemEntry.COLUMN_NAME_USER_ADDRESS,String.valueOf((int)newRowIdUserAccount),BaseColumns._ID));

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new UserLoginFragment()).commit();
            } else{
                if(!isUsernameAvailable){
                    usernameInput.setError(resources.getString(R.string.username_taken));
                }
                if(!isEmailAvailable){
                    emailInput.setError(resources.getString(R.string.email_taken));
                }
                if(!doPasswordMatch){
                    passwordInput.setText(resources.getString(R.string.passwords_dont_match));
                    passwordRepeatInput.setText(resources.getString(R.string.passwords_dont_match));
                }
            }

        });

        return rootView;
    }

}
