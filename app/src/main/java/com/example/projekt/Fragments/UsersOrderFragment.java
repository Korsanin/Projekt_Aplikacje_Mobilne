package com.example.projekt.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt.Adapter.RecyclerViewAdapter;
import com.example.projekt.DatabaseManagement.ItemReader;
import com.example.projekt.R;

import java.util.List;

public class UsersOrderFragment extends Fragment {
    private ItemReader.ItemReaderDbHelper dbHelper;
    private View rootView;

    private ImageView imageViewOrder;
    private TextView textViewTitleOrder;
    private TextView textViewPriceOrder;
    private Bundle bundle;
    private com.google.android.material.textfield.TextInputEditText textInputEditText;
    private SeekBar seekBar;
    private TextView textViewAmount;
    private LinearLayoutManager mousesLinearLayoutManager;
    private LinearLayoutManager keyboardsLinearLayoutManager;
    private RecyclerViewAdapter mousesRecyclerViewAdapter;
    private RecyclerViewAdapter keyboardsRecyclerViewAdapter;
    private RecyclerView mouses;
    private RecyclerView keyboards;
    private List mouseImgs;
    private List keyboardImgs;
    private List mouseIds;
    private List keyboardIds;
    private List mouseNames;
    private List keyboardNames;
    private List mousePrices;
    private List keyboardPrices;
    private Button buy;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper = new ItemReader.ItemReaderDbHelper(getContext());
        rootView = inflater.inflate(R.layout.fragment_users_order,container,false);

        Resources resources = getResources();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("USERDATA", Context.MODE_PRIVATE);

        imageViewOrder = rootView.findViewById(R.id.imageViewOrder);
        textViewTitleOrder = rootView.findViewById(R.id.titleOrder);
        textViewPriceOrder = rootView.findViewById(R.id.priceOrder);

        SharedPreferences computer_set = getContext().getSharedPreferences("COMPUTER_SET", Context.MODE_PRIVATE);

        imageViewOrder.setImageResource(Integer.valueOf(computer_set.getString("IMAGE","")));
        textViewTitleOrder.setText(computer_set.getString("TITLE","").toString());
        textViewPriceOrder.setText(computer_set.getInt("PRICE",0)+"zł");


        textInputEditText = rootView.findViewById(R.id.username);

        Log.v("Test",sharedPreferences.getString("USERNAME",""));

        seekBar = rootView.findViewById(R.id.seekBar);
        textViewAmount = rootView.findViewById(R.id.textViewIlosc);

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        if(i==1){
                            textViewAmount.setText(i + resources.getString(R.string.item));
                        }
                        else if(i>1 && i<5){
                            textViewAmount.setText(i + resources.getString(R.string.item2));
                        }
                        else{
                            textViewAmount.setText(i + resources.getString(R.string.item3));
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        dbHelper = new ItemReader.ItemReaderDbHelper(getContext());

        mouseImgs = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_MOUSE,ItemReader.ItemEntry.COLUMN_NAME_MOUSE_PHOTO,null,null);
        mouseNames = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_MOUSE,ItemReader.ItemEntry.COLUMN_NAME_MOUSE,null,null);
        mousePrices = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_MOUSE,ItemReader.ItemEntry.COLUMN_NAME_MOUSE_PRICE,null,null);
        mouseIds = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_MOUSE,BaseColumns._ID,null,null);

        keyboardImgs = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD,ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD_PHOTO,null,null);
        keyboardNames = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD,ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD,null,null);
        keyboardPrices = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD,ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD_PRICE,null,null);
        keyboardIds = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD,BaseColumns._ID,null,null);

        mousesLinearLayoutManager = new LinearLayoutManager(getContext());
        mousesLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mouses = rootView.findViewById(R.id.mouses);

        mousesRecyclerViewAdapter = new RecyclerViewAdapter(getContext(),mouseImgs,mouseNames,mousePrices,mouseIds);

        mouses.setAdapter(mousesRecyclerViewAdapter);
        mouses.setLayoutManager(mousesLinearLayoutManager);

        keyboardsLinearLayoutManager = new LinearLayoutManager(getContext());
        keyboardsLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        keyboards = rootView.findViewById(R.id.keyboards);

        keyboardsRecyclerViewAdapter = new RecyclerViewAdapter(getContext(),keyboardImgs,keyboardNames,keyboardPrices,keyboardIds);

        keyboards.setAdapter(keyboardsRecyclerViewAdapter);
        keyboards.setLayoutManager(keyboardsLinearLayoutManager);

        buy = rootView.findViewById(R.id.buy);

        buy.setOnClickListener(v->{

        });


        if(sharedPreferences.getString("USERNAME","")!=null && sharedPreferences.getString("USERNAME","")!=""){
            textInputEditText.setText(sharedPreferences.getString("USERNAME",""));
        }

        return rootView;
    }
}
