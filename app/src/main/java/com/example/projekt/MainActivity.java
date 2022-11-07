package com.example.projekt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.projekt.DatabaseManagement.Checker;
import com.example.projekt.DatabaseManagement.ItemReader;
import com.example.projekt.MessageSystem.SendEmail;
import com.example.projekt.MessageSystem.SendSms;
import com.example.projekt.ShortcutManager.MyShortcutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ItemReader.ItemReaderDbHelper dbHelper;
    private List titles;
    private List images;
    private List prices;
    private List ids;
    private List elementList;
    private ListView listView;
    private HashMap<String,Object> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyShortcutManager.createDynamicShortcuts(getApplicationContext());

        dbHelper = new ItemReader.ItemReaderDbHelper(getApplicationContext());

        // Test

//        dbHelper.dropTables();

//        dbHelper.dropTable(ItemReader.ItemEntry.TABLE_NAME_COMPUTER);

//        dbHelper.truncateTable(ItemReader.ItemEntry.TABLE_NAME_COMPUTER);

        Checker.check(dbHelper);

//        dbHelper.createTables();
//        CreateData.CreateDatabaseData(dbHelper);

//        Log.v("TEST",dbHelper.readDataSms(ItemReader.ItemEntry.COLUMN_NAME_SMS_MESSAGE,null,null).toString());

        ids = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_COMPUTER,BaseColumns._ID,null,null);
        titles = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_COMPUTER,ItemReader.ItemEntry.COLUMN_NAME_COMPUTER,null,null);
        images = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_COMPUTER,ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_PHOTO,null,null);
        prices  = dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_COMPUTER,ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_PRICE,null,null);

        listView = findViewById(R.id.simpleListView);

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
                getApplicationContext(),
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
                Intent intent = new Intent(getApplicationContext(),UsersOrder.class);
                intent.putExtra("id",ids.get(i).toString());
                intent.putExtra("title",titles.get(i).toString());
                intent.putExtra("image",images.get(i).toString());
                intent.putExtra("price",Integer.valueOf(prices.get(i).toString()));
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.orderMenuItem:
                Toast.makeText(this, R.string.menu_order, Toast.LENGTH_SHORT).show();
                break;
            case R.id.smsMenuItem:
                Toast.makeText(this, R.string.menu_sms, Toast.LENGTH_SHORT).show();
                Intent sms = new Intent(getApplicationContext(), SendSms.class);
                startActivity(sms);
                break;
            case R.id.shareMenuItem:
                Toast.makeText(this, R.string.menu_share, Toast.LENGTH_SHORT).show();
                Intent email = new Intent(getApplicationContext(), SendEmail.class);
                startActivity(email);
                break;
            case R.id.saveSettingsMenuItem:
                Toast.makeText(this, R.string.menu_save_settings, Toast.LENGTH_SHORT).show();
                break;
            case R.id.aboutMenuItem:
                Toast.makeText(this, R.string.about, Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onDestroy() {
//        dbHelper.dropTable(ItemReader.ItemEntry.TABLE_NAME_SMS);
        dbHelper.close();
//        MyShortcutManager.deleteDynamicShortcuts(getApplicationContext());
        super.onDestroy();
    }


}