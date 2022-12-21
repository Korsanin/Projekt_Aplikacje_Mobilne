package com.example.projekt.Fragments.ShopFragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt.Adapter.RecyclerViewAdapter;
import com.example.projekt.Adapter.RecyclerViewAdapterKeyboard;
import com.example.projekt.Adapter.RecyclerViewAdapterMouse;
import com.example.projekt.DatabaseManagement.ItemReader;
import com.example.projekt.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UsersOrderFragment extends Fragment {
    private ItemReader.ItemReaderDbHelper dbHelper;
    private View rootView;
    private String TAG = "UserOrder";

    private ImageView imageViewOrder;
    private TextView textViewTitleOrder;
    private TextView textViewPriceOrder;
    private Bundle bundle;
    private com.google.android.material.textfield.TextInputEditText usernameInput;
    private SeekBar seekBar;
    private TextView textViewAmount;
    private LinearLayoutManager mousesLinearLayoutManager;
    private LinearLayoutManager keyboardsLinearLayoutManager;
    private RecyclerViewAdapterMouse mousesRecyclerViewAdapter;
    private RecyclerViewAdapterKeyboard keyboardsRecyclerViewAdapter;
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
    private Button addToCart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper = new ItemReader.ItemReaderDbHelper(getContext());
        rootView = inflater.inflate(R.layout.fragment_users_order,container,false);

        Resources resources = getResources();

        SharedPreferences userdata = getContext().getSharedPreferences("USERDATA", Context.MODE_PRIVATE);

        SharedPreferences order = getContext().getSharedPreferences("ORDER", Context.MODE_PRIVATE);

        imageViewOrder = rootView.findViewById(R.id.imageViewOrder);
        textViewTitleOrder = rootView.findViewById(R.id.titleOrder);
        textViewPriceOrder = rootView.findViewById(R.id.priceOrder);

        SharedPreferences computer_set = getContext().getSharedPreferences("COMPUTER_SET", Context.MODE_PRIVATE);

        imageViewOrder.setImageResource(Integer.valueOf(computer_set.getString("IMAGE","")));
        textViewTitleOrder.setText(computer_set.getString("TITLE","").toString());
        textViewPriceOrder.setText(computer_set.getInt("PRICE",0)+"zł");


        usernameInput = rootView.findViewById(R.id.username);

        Log.v("Test",userdata.getString("USERNAME",""));

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

        mousesRecyclerViewAdapter = new RecyclerViewAdapterMouse(getContext(),mouseImgs,mouseNames,mousePrices,mouseIds);

        mouses.setAdapter(mousesRecyclerViewAdapter);
        mouses.setLayoutManager(mousesLinearLayoutManager);

        keyboardsLinearLayoutManager = new LinearLayoutManager(getContext());
        keyboardsLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        keyboards = rootView.findViewById(R.id.keyboards);

        keyboardsRecyclerViewAdapter = new RecyclerViewAdapterKeyboard(getContext(),keyboardImgs,keyboardNames,keyboardPrices,keyboardIds);

        keyboards.setAdapter(keyboardsRecyclerViewAdapter);
        keyboards.setLayoutManager(keyboardsLinearLayoutManager);

        addToCart = rootView.findViewById(R.id.addToCart);

        addToCart.setOnClickListener(v->{
            if(!usernameInput.getText().toString().equals("")){
                Log.v(TAG,usernameInput.getText().toString());
                SQLiteDatabase db_read = dbHelper.getReadableDatabase();
                SQLiteDatabase db_write = dbHelper.getWritableDatabase();
                if(order.getBoolean("EMPTY",true)){
                    try{
                        long user_id = (long) dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT,BaseColumns._ID,"'"+usernameInput.getText().toString()+"'", ItemReader.ItemEntry.COLUMN_NAME_USER_USERNAME).get(0);

                        Date date = Calendar.getInstance().getTime();

                        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

                        String date_output = format.format(date);
                        Log.v(TAG,date_output);

                        ContentValues new_order = new ContentValues();

                        new_order.put(ItemReader.ItemEntry.COLUMN_NAME_USER_ID,user_id);
                        new_order.put(ItemReader.ItemEntry.COLUMN_NAME_USER_ORDERS_DATE,date_output);

                        long orders_id = db_write.insert(ItemReader.ItemEntry.TABLE_NAME_USER_ORDERS,null,new_order);

                        Log.v(TAG, orders_id + "\n" + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ORDERS, ItemReader.ItemEntry.COLUMN_NAME_USER_ID,String.valueOf((int)orders_id),BaseColumns._ID)+ dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ORDERS, ItemReader.ItemEntry.COLUMN_NAME_USER_ORDERS_DATE,String.valueOf((int)orders_id),BaseColumns._ID));

//                        Log.v(TAG,id + "\n" + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ORDER,ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_ID, String.valueOf(id),BaseColumns._ID));

                        SharedPreferences.Editor editor = order.edit();
                        editor.putBoolean("EMPTY",false);
                        editor.putInt("OrderId", Integer.parseInt(String.valueOf(orders_id)));
                        editor.apply();
                    } catch (Exception e){
                        Log.v(TAG,e.getMessage());
                    }
                }
                try{
                    String mouse_id = order.getString("MOUSE","-1");
                    String keyboard_id = order.getString("KEYBOARD","-1");
                    String computer_id = computer_set.getString("ID","-1");
                    int orders_id = order.getInt("OrderId",0);
                    int amount = seekBar.getProgress();

                    Log.v(TAG,mouse_id+"\n " + keyboard_id+"\n " + computer_id+"\n "+ orders_id+"\n "+amount);

                    ContentValues orderValues = new ContentValues();

                    orderValues.put(ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_ID,computer_id);
                    orderValues.put(ItemReader.ItemEntry.COLUMN_NAME_MOUSE_ID,mouse_id);
                    orderValues.put(ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD_ID,keyboard_id);
                    orderValues.put(ItemReader.ItemEntry.COLUMN_NAME_USER_ORDERS_ID,orders_id);
                    orderValues.put(ItemReader.ItemEntry.COLUMN_NAME_AMOUNT,amount);

                    long id = db_write.insert(ItemReader.ItemEntry.TABLE_NAME_USER_ORDER,null,orderValues);

                    SharedPreferences.Editor editor = order.edit();

                    editor.putString("KEYBOARD", "-1");
                    editor.putString("MOUSE", "-1");
                    editor.apply();

                    Toast.makeText(getContext(), getString(R.string.added_to_cart), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Log.v(TAG,e.getMessage());
                }
            } else{
                Toast.makeText(getContext(), resources.getString(R.string.must_login), Toast.LENGTH_SHORT).show();
            }
        });


        if(userdata.getString("USERNAME","")!=null && !userdata.getString("USERNAME","").equals("")){
            usernameInput.setText(userdata.getString("USERNAME",""));
        }

        return rootView;
    }
}
