package com.example.projekt.MessageSystem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.projekt.DatabaseManagement.ItemReader;
import com.example.projekt.R;
import com.google.android.material.textfield.TextInputEditText;

public class SendSms extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        phoneNumber = findViewById(R.id.phoneInput);
        smsMessage = findViewById(R.id.messageInput);
        sendSms = findViewById(R.id.sendSms);


        sendSms.setOnClickListener(v -> {
            if(checkPermission(getApplicationContext(),Manifest.permission.SEND_SMS)
                    && checkPermission(getApplicationContext(),Manifest.permission.RECEIVE_SMS)
            ){
                sendWithSmsManager();
            }
        });

    }

    private void sendWithSmsManager(){
        destinationAddress = phoneNumber.getText().toString();
        text = smsMessage.getText().toString();
        if(!destinationAddress.equals("")&&!text.equals("")){
            smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(
                    destinationAddress,
                    null,
                    text,
                    null,
                    null
            );
            ItemReader.ItemReaderDbHelper dbHelper = new ItemReader.ItemReaderDbHelper(getApplicationContext());
            SQLiteDatabase db_write = dbHelper.getWritableDatabase();
            ContentValues sms = new ContentValues();
            sms.put(ItemReader.ItemEntry.COLUMN_NAME_SMS_PHONE_NUMBER,destinationAddress);
            sms.put(ItemReader.ItemEntry.COLUMN_NAME_SMS_MESSAGE,text);
            sms.put(ItemReader.ItemEntry.COLUMN_NAME_SMS_TYPE,"SENT");
            long newRowIdSms = db_write.insert(ItemReader.ItemEntry.TABLE_NAME_SMS,null,sms);
            Log.v(TAG,newRowIdSms + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_SMS,ItemReader.ItemEntry.COLUMN_NAME_SMS_PHONE_NUMBER, String.valueOf((int)newRowIdSms), BaseColumns._ID) + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_SMS,ItemReader.ItemEntry.COLUMN_NAME_SMS_MESSAGE,String.valueOf((int)newRowIdSms),BaseColumns._ID) + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_SMS,ItemReader.ItemEntry.COLUMN_NAME_SMS_TYPE,String.valueOf((int)newRowIdSms),BaseColumns._ID));
            Log.v(TAG,"SMS send");
            Toast.makeText(this, "SMS send", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.v(TAG,"Permission denied");
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private ActivityResultLauncher<String> requestPermissionLauncherSms =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted ->{
                        if(isGranted){
                            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                            sendIntent.putExtra("sms_body","default content");
                            sendIntent.setType("vnd.android-dir/mms-sms");
                            startActivity(sendIntent);
                        }
                    }
            );

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
            if(requested_permission==Manifest.permission.SEND_SMS){
                requestPermissionLauncherSms.launch(requested_permission);
            }else{
                requestPermissionLauncher.launch(requested_permission);
            }


        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_sms,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.sentSmsMenuItem:
                Toast.makeText(this, R.string.sms_sent, Toast.LENGTH_SHORT).show();
                Intent sms_sent = new Intent(getApplicationContext(), SentSms.class);
                startActivity(sms_sent);
                break;
            case R.id.receivedSmsMenuItem:
                Toast.makeText(this, R.string.sms_received, Toast.LENGTH_SHORT).show();
                Intent sms_received = new Intent(getApplicationContext(), ReceivedSms.class);
                startActivity(sms_received);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


}