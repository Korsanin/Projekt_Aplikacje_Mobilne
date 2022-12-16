package com.example.projekt.Fragments.MessageSystemFragments;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.projekt.DatabaseManagement.ItemReader;
import com.example.projekt.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;


public class SendSmsFragment extends Fragment {
    private ItemReader.ItemReaderDbHelper dbHelper;
    private View rootView;

    private final String TAG = "SMS";
    private final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 2;
    private SmsManager smsManager;
    private String destinationAddress = "";
    private String scAddress = null;
    private String text = "";
    private PendingIntent sentIntent = null;
    private PendingIntent deliveryIntent = null;
    private long messageId = 0;
    private TextInputEditText phoneNumber;
    private TextInputEditText smsMessage;

    private Button sendSms;

    private SharedPreferences cart;
    private SharedPreferences userdata;
    private Button fillPhoneNumber;
    private Button fillContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper = new ItemReader.ItemReaderDbHelper(getContext());

        rootView = inflater.inflate(R.layout.fragment_send_sms,container,false);

        cart = getContext().getSharedPreferences("ORDER", Context.MODE_PRIVATE);
        userdata = getContext().getSharedPreferences("USERDATA", Context.MODE_PRIVATE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels/2;

        fillPhoneNumber = rootView.findViewById(R.id.fillPhoneNumberToSms);
        fillContent = rootView.findViewById(R.id.fillContentToSms);

        fillContent.setWidth(width);
        fillPhoneNumber.setWidth(width);

        phoneNumber = rootView.findViewById(R.id.phoneInput);
        smsMessage = rootView.findViewById(R.id.messageInput);
        sendSms = rootView.findViewById(R.id.sendSms);

        fillPhoneNumber.setOnClickListener(v->{
            int phone = userdata.getInt("PHONE",-1);

            if(phone!=-1){
                phoneNumber.setText(phone+"");
            }
        });

        fillContent.setOnClickListener(v ->{
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

            smsMessage.setText(text);
        });

        sendSms.setOnClickListener(v -> {
            if(checkPermission(getContext(),Manifest.permission.SEND_SMS)
                    && checkPermission(getContext(),Manifest.permission.RECEIVE_SMS)
            ){
                sendWithSmsManager();
            }
        });

        return rootView;
    }

    private void sendWithSmsManager(){
        destinationAddress = phoneNumber.getText().toString();
        text = smsMessage.getText().toString();
        if(!destinationAddress.equals("")&&!text.equals("") && checkPermission(getContext(),Manifest.permission.SEND_SMS) && checkPermission(getContext(),Manifest.permission.RECEIVE_SMS)){
            try{
                smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(
                        destinationAddress,
                        null,
                        text,
                        null,
                        null
                );
                ItemReader.ItemReaderDbHelper dbHelper = new ItemReader.ItemReaderDbHelper(getContext());
                SQLiteDatabase db_write = dbHelper.getWritableDatabase();
                ContentValues sms = new ContentValues();
                sms.put(ItemReader.ItemEntry.COLUMN_NAME_SMS_PHONE_NUMBER,destinationAddress);
                sms.put(ItemReader.ItemEntry.COLUMN_NAME_SMS_MESSAGE,text);
                sms.put(ItemReader.ItemEntry.COLUMN_NAME_SMS_TYPE,"SENT");
                long newRowIdSms = db_write.insert(ItemReader.ItemEntry.TABLE_NAME_SMS,null,sms);
                Log.v(TAG,newRowIdSms + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_SMS,ItemReader.ItemEntry.COLUMN_NAME_SMS_PHONE_NUMBER, String.valueOf((int)newRowIdSms), BaseColumns._ID) + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_SMS,ItemReader.ItemEntry.COLUMN_NAME_SMS_MESSAGE,String.valueOf((int)newRowIdSms),BaseColumns._ID) + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_SMS,ItemReader.ItemEntry.COLUMN_NAME_SMS_TYPE,String.valueOf((int)newRowIdSms),BaseColumns._ID));
                Log.v(TAG,"SMS send");
                Toast.makeText(getContext(), "SMS send", Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Log.v(TAG,e.getMessage());
            }
        }
        else{
            Log.v(TAG,"Permission denied");
            Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted ->{
                        if(isGranted){

                        }
                    }
            );

    public boolean checkPermission(Context context, String requested_permission){

        if(ContextCompat.checkSelfPermission(
                context,
                requested_permission
        )
                == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            requestPermissionLauncher.launch(requested_permission);
        }
        return true;
    }
}
