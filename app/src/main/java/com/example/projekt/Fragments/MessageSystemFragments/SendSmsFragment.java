package com.example.projekt.Fragments.MessageSystemFragments;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.telephony.SmsManager;
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper = new ItemReader.ItemReaderDbHelper(getContext());

        rootView = inflater.inflate(R.layout.fragment_send_sms,container,false);

        phoneNumber = rootView.findViewById(R.id.phoneInput);
        smsMessage = rootView.findViewById(R.id.messageInput);
        sendSms = rootView.findViewById(R.id.sendSms);


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
