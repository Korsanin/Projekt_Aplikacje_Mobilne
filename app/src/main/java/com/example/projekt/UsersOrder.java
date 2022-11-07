package com.example.projekt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.projekt.Adapter.RecyclerViewAdapter;
import com.example.projekt.DatabaseManagement.ItemReader;

import java.util.List;

public class UsersOrder extends AppCompatActivity {
    private ImageView imageViewOrder;
    private TextView textViewTitleOrder;
    private TextView textViewPriceOrder;
    private Bundle bundle;
    private com.google.android.material.textfield.TextInputEditText textInputEditText;
    private SeekBar seekBar;
    private TextView textViewAmount;
    private ItemReader.ItemReaderDbHelper dbHelper;
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


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_order);

        imageViewOrder = findViewById(R.id.imageViewOrder);
        textViewTitleOrder = findViewById(R.id.titleOrder);
        textViewPriceOrder = findViewById(R.id.priceOrder);

        Intent intent = getIntent();

        bundle = intent.getExtras();

        imageViewOrder.setImageResource(Integer.valueOf(bundle.get("image").toString()));
        textViewTitleOrder.setText(bundle.get("title").toString());
        textViewPriceOrder.setText(bundle.get("price").toString()+"zł");


        textInputEditText = findViewById(R.id.userNameAndSurname);
        seekBar = findViewById(R.id.seekBar);
        textViewAmount = findViewById(R.id.textViewIlosc);

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        if(i==1){
                            textViewAmount.setText(i + " sztuka");
                        }
                        else if(i>1 && i<5){
                            textViewAmount.setText(i + " sztuki");
                        }
                        else{
                            textViewAmount.setText(i + " sztuk");
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

        dbHelper = new ItemReader.ItemReaderDbHelper(getApplicationContext());

        mouseImgs = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_MOUSE,ItemReader.ItemEntry.COLUMN_NAME_MOUSE_PHOTO,null,null);
        mouseNames = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_MOUSE,ItemReader.ItemEntry.COLUMN_NAME_MOUSE,null,null);
        mousePrices = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_MOUSE,ItemReader.ItemEntry.COLUMN_NAME_MOUSE_PRICE,null,null);
        mouseIds = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_MOUSE,BaseColumns._ID,null,null);

        keyboardImgs = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD,ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD_PHOTO,null,null);
        keyboardNames = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD,ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD,null,null);
        keyboardPrices = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD,ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD_PRICE,null,null);
        keyboardIds = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD,BaseColumns._ID,null,null);

        mousesLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mousesLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mouses = findViewById(R.id.mouses);

        mousesRecyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(),mouseImgs,mouseNames,mousePrices,mouseIds);

        mouses.setAdapter(mousesRecyclerViewAdapter);
        mouses.setLayoutManager(mousesLinearLayoutManager);

        keyboardsLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        keyboardsLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        keyboards = findViewById(R.id.keyboards);

        keyboardsRecyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(),keyboardImgs,keyboardNames,keyboardPrices,keyboardIds);

        keyboards.setAdapter(keyboardsRecyclerViewAdapter);
        keyboards.setLayoutManager(keyboardsLinearLayoutManager);
    }
}