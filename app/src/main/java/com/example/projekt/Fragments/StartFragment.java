package com.example.projekt.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.projekt.DatabaseManagement.ItemReader;
import com.example.projekt.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StartFragment extends Fragment {
    private ItemReader.ItemReaderDbHelper dbHelper;
    private List titles;
    private List images;
    private List prices;
    private List ids;
    private List elementList;
    private ListView listView;
    private HashMap<String,Object> hashMap;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper = new ItemReader.ItemReaderDbHelper(getContext());

        rootView = inflater.inflate(R.layout.fragment_start,container,false);

        listView = rootView.findViewById(R.id.simpleListView);

        ids = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_COMPUTER, BaseColumns._ID,null,null);
        titles = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_COMPUTER,ItemReader.ItemEntry.COLUMN_NAME_COMPUTER,null,null);
        images = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_COMPUTER,ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_PHOTO,null,null);
        prices  = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_COMPUTER,ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_PRICE,null,null);

        elementList = new ArrayList();

        for(int i=0;i<titles.size();i++){
            hashMap = new HashMap<>();
            hashMap.put("title",titles.get(i));
            hashMap.put("image",images.get(i));
            hashMap.put("price",prices.get(i)+"zł");
            elementList.add(hashMap);
        }

        String[] from={"image","title","price"};
        int[] to={
                R.id.imageViewListItem,
                R.id.titleListItem,
                R.id.priceListItem
        };
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                getContext(),
                elementList,
                R.layout.list_view_item,
                from,
                to
        );

        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("TAG","------------"+titles.get(i)+"\n"+images.get(i)+"\n"+prices.get(i));
                SharedPreferences computer_set = getContext().getSharedPreferences("COMPUTER_SET", Context.MODE_PRIVATE);
                SharedPreferences.Editor computer_set_editor = computer_set.edit();
                computer_set_editor.putString("ID",ids.get(i).toString());
                computer_set_editor.putString("TITLE",titles.get(i).toString());
                computer_set_editor.putString("IMAGE",images.get(i).toString());
                computer_set_editor.putInt("PRICE",Integer.valueOf(prices.get(i).toString()));
                computer_set_editor.apply();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new UsersOrderFragment()).commit();
            }
        });
        return rootView;
    }
}
