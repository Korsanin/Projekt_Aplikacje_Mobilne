package com.example.projekt.Receiver;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.projekt.DatabaseManagement.ItemReader;

public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = "SMS";
    public static final String pdu_type = "pdus";
    private static String SMS = "android.provider.Telephony.SMS_RECEIVED";
    private String message="";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS)){
            Bundle bundle = intent.getExtras();
            SmsMessage[] smsMessages = null;
            String format = bundle.getString("format");
            Log.v(TAG,"format=" + format);
            Object[] pdus = (Object[]) bundle.get(pdu_type);

            if(pdus!=null){
                smsMessages = new SmsMessage[pdus.length];
                for (int i=0;i< smsMessages.length;i++){
                    smsMessages[i] = SmsMessage.createFromPdu(
                            (byte[]) pdus[i],
                            format
                    );
                    String originatingAddress = ""+smsMessages[i].getOriginatingAddress();
                    message += "Sms from: " + originatingAddress+"\n";
                    message += "Sms text: "+ smsMessages[i].getMessageBody() + "\n";
                    Log.v(TAG,"--------------------> message: " + message);
                    ItemReader.ItemReaderDbHelper dbHelper = new ItemReader.ItemReaderDbHelper(context);
                    SQLiteDatabase db_write = dbHelper.getWritableDatabase();
                    ContentValues sms = new ContentValues();
                    sms.put(ItemReader.ItemEntry.COLUMN_NAME_SMS_PHONE_NUMBER,originatingAddress);
                    sms.put(ItemReader.ItemEntry.COLUMN_NAME_SMS_MESSAGE,smsMessages[i].getMessageBody());
                    sms.put(ItemReader.ItemEntry.COLUMN_NAME_SMS_TYPE,"RECEIVED");
                    long newRowIdSms = db_write.insert(ItemReader.ItemEntry.TABLE_NAME_SMS,null,sms);
                    Log.v(TAG,newRowIdSms + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_SMS,ItemReader.ItemEntry.COLUMN_NAME_SMS_PHONE_NUMBER, String.valueOf((int)newRowIdSms), BaseColumns._ID) + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_SMS,ItemReader.ItemEntry.COLUMN_NAME_SMS_MESSAGE,String.valueOf((int)newRowIdSms),BaseColumns._ID) + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_SMS,ItemReader.ItemEntry.COLUMN_NAME_SMS_TYPE,String.valueOf((int)newRowIdSms),BaseColumns._ID));
                    Toast.makeText(context, message,Toast.LENGTH_LONG).show();

                }
            }
        }
    }
}