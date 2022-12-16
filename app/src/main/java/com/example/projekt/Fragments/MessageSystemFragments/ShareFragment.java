package com.example.projekt.Fragments.MessageSystemFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projekt.DatabaseManagement.ItemReader;
import com.example.projekt.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ShareFragment extends Fragment {
    private ItemReader.ItemReaderDbHelper dbHelper;
    private View rootView;

    private String email;
    private String subject;
    private String body;
    private TextInputEditText emailInput;
    private TextInputEditText subjectInput;
    private TextInputEditText bodyInput;
    private Button button;

    private SharedPreferences cart;
    private SharedPreferences userdata;
    private Button fillEmail;
    private Button fillContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper = new ItemReader.ItemReaderDbHelper(getContext());

        rootView = inflater.inflate(R.layout.fragment_share,container,false);

        cart = getContext().getSharedPreferences("ORDER", Context.MODE_PRIVATE);
        userdata = getContext().getSharedPreferences("USERDATA", Context.MODE_PRIVATE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels/2;

        fillEmail = rootView.findViewById(R.id.fillEmailToShare);
        fillContent = rootView.findViewById(R.id.fillContentToShare);

        fillContent.setWidth(width);
        fillEmail.setWidth(width);

        emailInput = rootView.findViewById(R.id.emailInput);
        subjectInput = rootView.findViewById(R.id.emailSubjectInput);
        bodyInput = rootView.findViewById(R.id.emailBodyInput);

        button = rootView.findViewById(R.id.sendEmail);

        fillEmail.setOnClickListener(v ->{
            emailInput.setText(userdata.getString("EMAIL",null));
        });

        fillContent.setOnClickListener(v ->{
            subjectInput.setText(getString(R.string.order));

            ArrayList computer_ids = new ArrayList<>();
            ArrayList mouse_ids = new ArrayList();
            ArrayList keyboard_ids = new ArrayList();

            ArrayList computer_titles = new ArrayList<>();
            ArrayList mouse_titles = new ArrayList<>();
            ArrayList keyboard_titles = new ArrayList<>();
            int id = cart.getInt("LastOrder",-1);

            computer_ids.addAll(dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ORDER, ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_ID,id+"", ItemReader.ItemEntry.COLUMN_NAME_USER_ORDERS_ID));
            mouse_ids.addAll(dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ORDER, ItemReader.ItemEntry.COLUMN_NAME_MOUSE_ID,id+"", ItemReader.ItemEntry.COLUMN_NAME_USER_ORDERS_ID));
            keyboard_ids.addAll(dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ORDER, ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD_ID,id+"", ItemReader.ItemEntry.COLUMN_NAME_USER_ORDERS_ID));
            Log.v("Tescik",computer_ids.toString());

            for(int i=0;i<computer_ids.size();i++){
                computer_titles.addAll(dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_COMPUTER, ItemReader.ItemEntry.COLUMN_NAME_COMPUTER,""+computer_ids.get(i), BaseColumns._ID));
                mouse_titles.addAll(dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_MOUSE, ItemReader.ItemEntry.COLUMN_NAME_MOUSE,""+mouse_ids.get(i), BaseColumns._ID));
                keyboard_titles.addAll(dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD, ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD,""+keyboard_ids.get(i), BaseColumns._ID));
            }
            Log.v("Tescik",computer_titles.toString());

            String text = "";

            for(int i = 0;i<computer_titles.size();i++){
                text+=computer_titles.get(i)+",";
            }
            for(int i = 0;i<mouse_titles.size();i++){
                text+=mouse_titles.get(i)+", ";
            }
            for(int i = 0;i<keyboard_titles.size();i++){
                if(i!=keyboard_titles.size()-1){
                    text+=keyboard_titles.get(i)+", ";
                } else{
                    text+=keyboard_titles.get(i);
                }
            }
            Log.v("Tescik",text);

            bodyInput.setText(text);
        });

        button.setOnClickListener(v ->{
            email = String.valueOf(emailInput.getText());
            subject = String.valueOf(subjectInput.getText());
            body = String.valueOf(bodyInput.getText());

            sendEmail(email,subject,body);
        });

        return rootView;
    }

    public void sendEmail(String email, String subject, String body){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Wybierz metodę wysyłania: "));
    }
}
