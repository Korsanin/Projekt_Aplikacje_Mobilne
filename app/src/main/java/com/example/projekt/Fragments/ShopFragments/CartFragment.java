package com.example.projekt.Fragments.ShopFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projekt.DatabaseManagement.ItemReader;
import com.example.projekt.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartFragment extends Fragment {
    private ItemReader.ItemReaderDbHelper dbHelper;
    private View rootView;
    private Resources resources;
    private SharedPreferences cart;

    private ListView listView;

    private Button buy;
    private Button reset;

    private long user_id;
    private List<Long> orders;
//    private List dates;

    private List computer_ids;
    private List computer_titles;
    private List computer_images;
    private List computer_prices;

    private List mouse_ids;
    private List mouse_titles;
    private List mouse_images;
    private List mouse_prices;

    private List keyboard_ids;
    private List keyboard_titles;
    private List keyboard_images;
    private List keyboard_prices;

    private TextView orderText;

    private HashMap<String,Object> hashMap;
    private List elementList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper = new ItemReader.ItemReaderDbHelper(getContext());

        rootView = inflater.inflate(R.layout.fragment_cart,container,false);

        resources = getResources();

        buy = rootView.findViewById(R.id.buy);
        reset = rootView.findViewById(R.id.reset);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels/2;
        buy.setLayoutParams(new LinearLayout.LayoutParams(width,100));
        reset.setLayoutParams(new LinearLayout.LayoutParams(width,100));

        listView = rootView.findViewById(R.id.cartItems);

        orderText = rootView.findViewById(R.id.cartText);

        cart = getContext().getSharedPreferences("ORDER", Context.MODE_PRIVATE);

        int id = cart.getInt("OrderId",0);

//        dates = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ORDERS, BaseColumns._ID,user_id+"", ItemReader.ItemEntry.COLUMN_NAME_USER_ORDERS_DATE);

        computer_ids = new ArrayList<>();
        mouse_ids = new ArrayList();
        keyboard_ids = new ArrayList();

        computer_titles = new ArrayList<>();
        computer_prices = new ArrayList<>();
        computer_images = new ArrayList<>();

        mouse_titles = new ArrayList<>();
        mouse_prices = new ArrayList<>();
        mouse_images = new ArrayList<>();

        keyboard_titles = new ArrayList<>();
        keyboard_prices = new ArrayList<>();
        keyboard_images = new ArrayList<>();

        elementList = new ArrayList<>();

        computer_ids.addAll(dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ORDER, ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_ID,id+"", ItemReader.ItemEntry.COLUMN_NAME_USER_ORDERS_ID));
        mouse_ids.addAll(dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ORDER, ItemReader.ItemEntry.COLUMN_NAME_MOUSE_ID,id+"", ItemReader.ItemEntry.COLUMN_NAME_USER_ORDERS_ID));
        keyboard_ids.addAll(dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ORDER, ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD_ID,id+"", ItemReader.ItemEntry.COLUMN_NAME_USER_ORDERS_ID));
//        Log.v("TAG",computer_ids.toString()+"\n"+mouse_ids.toString()+"\n"+keyboard_ids.toString()+"\n");

        for(int i=0;i<computer_ids.size();i++){
            computer_titles.addAll(dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_COMPUTER, ItemReader.ItemEntry.COLUMN_NAME_COMPUTER,""+computer_ids.get(i), BaseColumns._ID));
            computer_images.addAll(dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_COMPUTER, ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_PHOTO,""+computer_ids.get(i), BaseColumns._ID));
            computer_prices.addAll(dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_COMPUTER, ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_PRICE,""+computer_ids.get(i), BaseColumns._ID));

            mouse_titles.addAll(dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_MOUSE, ItemReader.ItemEntry.COLUMN_NAME_MOUSE,""+mouse_ids.get(i), BaseColumns._ID));
            mouse_images.addAll(dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_MOUSE, ItemReader.ItemEntry.COLUMN_NAME_MOUSE_PHOTO,""+mouse_ids.get(i), BaseColumns._ID));
            mouse_prices.addAll(dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_MOUSE, ItemReader.ItemEntry.COLUMN_NAME_MOUSE_PRICE,""+mouse_ids.get(i), BaseColumns._ID));

            keyboard_titles.addAll(dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD, ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD,""+keyboard_ids.get(i), BaseColumns._ID));
            keyboard_images.addAll(dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD, ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD_PHOTO,""+keyboard_ids.get(i), BaseColumns._ID));
            keyboard_prices.addAll(dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD, ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD_PRICE,""+keyboard_ids.get(i), BaseColumns._ID));
        }

        Log.v("TAG",computer_ids.toString() + "\n" + computer_titles.toString() + "\n" + computer_images.toString() +"\n" + computer_prices.toString());
        Log.v("TAG",mouse_ids.toString() + "\n" + mouse_titles.toString() + "\n" + mouse_images.toString() +"\n" + mouse_prices.toString());
        Log.v("TAG",keyboard_ids.toString() + "\n" + keyboard_titles.toString() + "\n" + keyboard_images.toString() +"\n" + keyboard_prices.toString());

        for(int i=0;i<computer_titles.size();i++){
            hashMap = new HashMap<>();
            hashMap.put("computer_title",computer_titles.get(i));
            hashMap.put("computer_image",Integer.parseInt(computer_images.get(i).toString()));
            hashMap.put("computer_price",computer_prices.get(i)+"zł");

            hashMap.put("mouse_title",mouse_titles.get(i));
            hashMap.put("mouse_image",Integer.parseInt(mouse_images.get(i).toString()));
            hashMap.put("mouse_price",mouse_prices.get(i)+"zł");

            hashMap.put("keyboard_title",keyboard_titles.get(i));
            hashMap.put("keyboard_image",Integer.parseInt(keyboard_images.get(i).toString()));
            hashMap.put("keyboard_price",keyboard_prices.get(i)+"zł");
            elementList.add(hashMap);
        }
        String[] from = {"computer_title","computer_image","computer_price","mouse_title","mouse_image","mouse_price","keyboard_title","keyboard_image","keyboard_price"};

        int[] to={
                R.id.titleListOrderComputer,
                R.id.imageViewListOrderComputer,
                R.id.priceListOrderComputer,
                R.id.titleListOrderMouse,
                R.id.imageViewListOrderMouse,
                R.id.priceListOrderMouse,
                R.id.titleListOrderKeyboard,
                R.id.imageViewListOrderKeyboard,
                R.id.priceListOrderKeyboard
        };

        SimpleAdapter simpleAdapter = new SimpleAdapter(
                getContext(),
                elementList,
                R.layout.list_view_order,
                from,
                to
        );

        listView.setAdapter(simpleAdapter);

        String text = computer_ids.size()+" ";

        if(computer_ids.size()>1 && computer_ids.size()<5){
            text+=resources.getString(R.string.order2);
        } else if(computer_ids.size()==1){
            text+=resources.getString(R.string.order);
        } else{
            text+=resources.getString(R.string.order3);
        }

        orderText.setText(text);



        buy.setOnClickListener(v->{
            SharedPreferences.Editor editor = cart.edit();
            editor.putBoolean("EMPTY",true);
            editor.commit();
            Toast.makeText(getContext(), "Dodano do koszyka", Toast.LENGTH_SHORT).show();
        });

        reset.setOnClickListener(v->{
            SharedPreferences.Editor editor = cart.edit();
            editor.putBoolean("EMPTY",true);
            editor.commit();
            dbHelper.deleteDataUserOrders(cart.getInt("OrderId",0)+"");
            dbHelper.deleteDataUserOrderByOrdersId(cart.getInt("OrderId",0)+"");
            Toast.makeText(getContext(), "Zresetowano koszyk", Toast.LENGTH_SHORT).show();
        });

        return rootView;
    }
}
