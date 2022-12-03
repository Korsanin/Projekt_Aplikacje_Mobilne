package com.example.projekt.Fragments.MessageSystemFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projekt.DatabaseManagement.ItemReader;
import com.example.projekt.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SentSmsFragment extends Fragment {
    private ItemReader.ItemReaderDbHelper dbHelper;
    private View rootView;

    private ListView listView;
    private List phone_numbers;
    private List messages;
    private List elementList;
    private HashMap<String,Object> hashMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper = new ItemReader.ItemReaderDbHelper(getContext());

        rootView = inflater.inflate(R.layout.fragment_sent_sms,container,false);

        listView = rootView.findViewById(R.id.sentSms);

        phone_numbers = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_SMS,ItemReader.ItemEntry.COLUMN_NAME_SMS_PHONE_NUMBER,"'SENT'",ItemReader.ItemEntry.COLUMN_NAME_SMS_TYPE);
        messages  = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_SMS,ItemReader.ItemEntry.COLUMN_NAME_SMS_MESSAGE,"'SENT'", ItemReader.ItemEntry.COLUMN_NAME_SMS_TYPE);

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
                getContext(),
                elementList,
                R.layout.list_view_sms,
                from,
                to
        );
        listView.setAdapter(simpleAdapter);

        return rootView;
    }
}
