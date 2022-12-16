package com.example.projekt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.projekt.DatabaseManagement.Checker;
import com.example.projekt.DatabaseManagement.ItemReader;
import com.example.projekt.Fragments.MessageSystemFragments.ReceivedSmsFragment;
import com.example.projekt.Fragments.MessageSystemFragments.SendSmsFragment;
import com.example.projekt.Fragments.MessageSystemFragments.SentSmsFragment;
import com.example.projekt.Fragments.MessageSystemFragments.ShareFragment;
import com.example.projekt.Fragments.ShopFragments.CartFragment;
import com.example.projekt.Fragments.StartFragment;
import com.example.projekt.Fragments.UserAccount.UserLoginFragment;
import com.example.projekt.Fragments.UserAccount.UserRegisterFragment;
import com.example.projekt.Fragments.ShopFragments.UserOrdersFragment;
import com.example.projekt.ShortcutManager.MyShortcutManager;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private ItemReader.ItemReaderDbHelper dbHelper;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.navigation_draw_open,
                R.string.navigation_draw_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StartFragment()).commit();

        dbHelper = new ItemReader.ItemReaderDbHelper(getApplicationContext());

//        dbHelper.dropTables();

        Checker.check(dbHelper);

        SharedPreferences sharedPreferences = getSharedPreferences("USERDATA", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("USERNAME","")==null || sharedPreferences.getString("USERNAME","")==""){
            toolbar.inflateMenu(R.menu.menu_login);
        } else{
            toolbar.inflateMenu(R.menu.menu_logout);
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SharedPreferences sharedPreferences = getSharedPreferences("USERDATA", Context.MODE_PRIVATE);
                if(sharedPreferences.getString("USERNAME","")=="" || sharedPreferences.getString("USERNAME","")==null) {
                    switch (item.getItemId()) {
                        case R.id.menu_login:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserLoginFragment()).commit();
                            Toast.makeText(getApplicationContext(), R.string.menu_login, Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.menu_register:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserRegisterFragment()).commit();
                            Toast.makeText(getApplicationContext(), R.string.menu_register, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            return false;
                    }
                } else{
                    switch (item.getItemId()) {
                        case R.id.menu_logout:
                            Toast.makeText(getApplicationContext(), R.string.logged_out, Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();

                            SharedPreferences cart = getSharedPreferences("ORDER",Context.MODE_PRIVATE);
                            SharedPreferences.Editor cart_edit = cart.edit();
                            cart_edit.putBoolean("EMPTY",true);
                            cart_edit.apply();

                            finish();
                            startActivity(getIntent());
                            break;
                        default:
                            return false;
                    }
                }
                return false;
            }
        });



        Log.v("TAG",getIntent().getAction());


        MyShortcutManager.deleteDynamicShortcuts(getApplicationContext());
        MyShortcutManager.createDynamicShortcuts(getApplicationContext());

        switch (getIntent().getAction()){
            case "SendSms":
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SendSmsFragment()).commit();
                break;
            case "SentSms":
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SentSmsFragment()).commit();
                break;
            case "Share":
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShareFragment()).commit();
                break;
            case "ReceivedSms":
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReceivedSmsFragment()).commit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.startMenuItem:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StartFragment()).commit();
                Toast.makeText(this, R.string.menu_start, Toast.LENGTH_SHORT).show();
                break;

            case R.id.orderMenuItem:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserOrdersFragment()).commit();
                Toast.makeText(this, R.string.menu_order, Toast.LENGTH_SHORT).show();
                break;

            case R.id.cartMenuItem:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartFragment()).commit();
                Toast.makeText(this, R.string.menu_cart, Toast.LENGTH_SHORT).show();
                break;

            case R.id.sendSmsMenuItem:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SendSmsFragment()).commit();
                Toast.makeText(this, R.string.menu_send_sms, Toast.LENGTH_SHORT).show();
                break;

            case R.id.sentSmsMenuItem:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SentSmsFragment()).commit();
                Toast.makeText(this, R.string.menu_sms_sent, Toast.LENGTH_SHORT).show();
                break;

            case R.id.receivedSmsMenuItem:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReceivedSmsFragment()).commit();
                Toast.makeText(this, R.string.menu_sms_received, Toast.LENGTH_SHORT).show();
                break;

            case R.id.shareMenuItem:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShareFragment()).commit();
                Toast.makeText(this, R.string.menu_share, Toast.LENGTH_SHORT).show();
                break;

            case R.id.gpsMenuItem:
                Intent gpsIntent = new Intent(getApplicationContext(),GPS.class);
                startActivity(gpsIntent);
                Toast.makeText(this, R.string.locate_shop, Toast.LENGTH_SHORT).show();
                break;
            case R.id.aboutMenuItem:
                Toast.makeText(this, R.string.about, Toast.LENGTH_SHORT).show();
                break;
            default:
                return false;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
//        dbHelper.dropTable(ItemReader.ItemEntry.TABLE_NAME_SMS);
        dbHelper.close();
//        MyShortcutManager.deleteDynamicShortcuts(getApplicationContext());
        super.onDestroy();
    }

}