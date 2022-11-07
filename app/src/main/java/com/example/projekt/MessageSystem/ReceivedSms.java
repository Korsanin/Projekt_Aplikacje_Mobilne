package com.example.projekt.MessageSystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.projekt.DatabaseManagement.ItemReader;
import com.example.projekt.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReceivedSms extends AppCompatActivity {
    private ItemReader.ItemReaderDbHelper dbHelper;
    private ListView listView;
    private List phone_numbers;
    private List messages;
    private List elementList;
    private HashMap<String,Object> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_sms);

        dbHelper = new ItemReader.ItemReaderDbHelper(getApplicationContext());

        listView = findViewById(R.id.receivedSms);

        phone_numbers = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_SMS,ItemReader.ItemEntry.COLUMN_NAME_SMS_PHONE_NUMBER,"'RECEIVED'",ItemReader.ItemEntry.COLUMN_NAME_SMS_TYPE);
        messages  = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_SMS,ItemReader.ItemEntry.COLUMN_NAME_SMS_MESSAGE,"'RECEIVED'", ItemReader.ItemEntry.COLUMN_NAME_SMS_TYPE);

        elementList = new ArrayList();

        for(int i=0;i<phone_numbers.size();i++){
            hashMap = new HashMap<>();
            hashMap.put("phone_number",phone_numbers.get(i));
            hashMap.put("message",messages.get(i));
            elementList.add(hashMap);
        }

        String[] from={"phone_number","message"};
        int[] to={
                R.id.smsPhoneNumber,
                R.id.smsMessage,
        };
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                getApplicationContext(),
                elementList,
                R.layout.list_view_sms,
                from,
                to
        );
        listView.setAdapter(simpleAdapter);
    }
}