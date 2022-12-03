package com.example.projekt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projekt.Adapter.RecyclerViewAdapter;
import com.example.projekt.DatabaseManagement.ItemReader;
import com.example.projekt.UserAccount.UserLogin;
import com.example.projekt.UserAccount.UserRegister;

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

        SharedPreferences sharedPreferences = getSharedPreferences("USERDATA", Context.MODE_PRIVATE);

        imageViewOrder = findViewById(R.id.imageViewOrder);
        textViewTitleOrder = findViewById(R.id.titleOrder);
        textViewPriceOrder = findViewById(R.id.priceOrder);

        SharedPreferences computer_set = getSharedPreferences("COMPUTER_SET", Context.MODE_PRIVATE);

        imageViewOrder.setImageResource(Integer.valueOf(computer_set.getString("IMAGE","")));
        textViewTitleOrder.setText(computer_set.getString("TITLE","").toString());
        textViewPriceOrder.setText(computer_set.getInt("PRICE",0)+"zł");


        textInputEditText = findViewById(R.id.username);

        Log.v("Test",sharedPreferences.getString("USERNAME",""));

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

        if(sharedPreferences.getString("USERNAME","")!=null && sharedPreferences.getString("USERNAME","")!=""){
            textInputEditText.setText(sharedPreferences.getString("USERNAME",""));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        SharedPreferences sharedPreferences = getSharedPreferences("USERDATA", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("USERNAME","")==null || sharedPreferences.getString("USERNAME","")==""){
            menuInflater.inflate(R.menu.menu_login,menu);
        } else{
            menuInflater.inflate(R.menu.menu_logout,menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences sharedPreferences = getSharedPreferences("USERDATA", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("USERNAME","")=="" || sharedPreferences.getString("USERNAME","")==null) {
            switch (item.getItemId()) {
                case R.id.menu_login:
                    Toast.makeText(this, R.string.menu_login, Toast.LENGTH_SHORT).show();
                    Intent login = new Intent(getApplicationContext(), UserLogin.class);
                    startActivity(login);
                    break;
                case R.id.menu_register:
                    Toast.makeText(this, R.string.menu_register, Toast.LENGTH_SHORT).show();
                    Intent register = new Intent(getApplicationContext(), UserRegister.class);
                    startActivity(register);
                    break;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } else{
            switch (item.getItemId()) {
                case R.id.menu_logout:
                    Toast.makeText(this, R.string.menu_logout, Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    finish();
                    startActivity(getIntent());
                    break;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}