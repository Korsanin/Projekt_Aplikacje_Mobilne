package com.example.projekt.DatabaseManagement;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.projekt.R;

public class CreateData {
    public static final String CREATE_DATA_TAG = "CREATE_DATA";
    public static void CreateDatabaseData(ItemReader.ItemReaderDbHelper dbHelper){
        SQLiteDatabase db_write = dbHelper.getWritableDatabase();

        ContentValues computer_values = new ContentValues();

        computer_values.put(ItemReader.ItemEntry.COLUMN_NAME_COMPUTER,"MSI MAG Codex 5 11TC-460EU");
        computer_values.put(ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_PHOTO, R.drawable.computer1);
        computer_values.put(ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_PRICE,4999);

        long newRowIdComputer = db_write.insert(ItemReader.ItemEntry.TABLE_NAME_COMPUTER,null,computer_values);

        Log.v(CREATE_DATA_TAG,newRowIdComputer + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_COMPUTER,ItemReader.ItemEntry.COLUMN_NAME_COMPUTER, String.valueOf((int)newRowIdComputer),BaseColumns._ID) + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_COMPUTER,ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_PHOTO,String.valueOf((int)newRowIdComputer),BaseColumns._ID) + " \n "+dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_COMPUTER,ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_PRICE,String.valueOf((int)newRowIdComputer),BaseColumns._ID));

        ContentValues mouse_values = new ContentValues();

        mouse_values.put(ItemReader.ItemEntry.COLUMN_NAME_MOUSE,"Steelseries Rival 3");
        mouse_values.put(ItemReader.ItemEntry.COLUMN_NAME_MOUSE_PHOTO,R.drawable.mouse1);
        mouse_values.put(ItemReader.ItemEntry.COLUMN_NAME_MOUSE_PRICE,129);

        long newRowIdMouse = db_write.insert(ItemReader.ItemEntry.TABLE_NAME_MOUSE,null,mouse_values);

        Log.v(CREATE_DATA_TAG,newRowIdMouse + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_MOUSE,ItemReader.ItemEntry.COLUMN_NAME_MOUSE,String.valueOf((int)newRowIdMouse), BaseColumns._ID) + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_MOUSE,ItemReader.ItemEntry.COLUMN_NAME_MOUSE_PHOTO,String.valueOf((int)newRowIdMouse),BaseColumns._ID) + " \n "+dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_MOUSE,ItemReader.ItemEntry.COLUMN_NAME_MOUSE_PRICE,String.valueOf((int)newRowIdMouse),BaseColumns._ID));

        ContentValues keyboard_values = new ContentValues();

        keyboard_values.put(ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD,"Sharkoon Skiller SGK3 Red");
        keyboard_values.put(ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD_PHOTO,R.drawable.keyboard1);
        keyboard_values.put(ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD_PRICE,329);

        long newRowIdKeyboard = db_write.insert(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD,null,keyboard_values);

        Log.v(CREATE_DATA_TAG,newRowIdKeyboard + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD,ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD,String.valueOf((int)newRowIdKeyboard),BaseColumns._ID) + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD,ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD_PHOTO,String.valueOf((int)newRowIdKeyboard),BaseColumns._ID) + " \n "+dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD,ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD_PRICE,String.valueOf((int)newRowIdKeyboard),BaseColumns._ID));

        ContentValues computer_values2 = new ContentValues();

        computer_values2.put(ItemReader.ItemEntry.COLUMN_NAME_COMPUTER,"MSI Aegis 3 VR7RD");
        computer_values2.put(ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_PHOTO, R.drawable.computer2);
        computer_values2.put(ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_PRICE,6789);

        long newRowIdComputer2 = db_write.insert(ItemReader.ItemEntry.TABLE_NAME_COMPUTER,null,computer_values2);

        Log.v(CREATE_DATA_TAG,newRowIdComputer2 + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_COMPUTER,ItemReader.ItemEntry.COLUMN_NAME_COMPUTER, String.valueOf((int)newRowIdComputer2),BaseColumns._ID) + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_COMPUTER,ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_PHOTO,String.valueOf((int)newRowIdComputer2),BaseColumns._ID) + " \n "+dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_COMPUTER,ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_PRICE,String.valueOf((int)newRowIdComputer2),BaseColumns._ID));

        ContentValues mouse_values2 = new ContentValues();

        mouse_values2.put(ItemReader.ItemEntry.COLUMN_NAME_MOUSE,"SteelSeries Aerox 5 Wireless");
        mouse_values2.put(ItemReader.ItemEntry.COLUMN_NAME_MOUSE_PHOTO,R.drawable.mouse2);
        mouse_values2.put(ItemReader.ItemEntry.COLUMN_NAME_MOUSE_PRICE,749);

        long newRowIdMouse2 = db_write.insert(ItemReader.ItemEntry.TABLE_NAME_MOUSE,null,mouse_values2);

        Log.v(CREATE_DATA_TAG,newRowIdMouse2 + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_MOUSE,ItemReader.ItemEntry.COLUMN_NAME_MOUSE,String.valueOf((int)newRowIdMouse2),BaseColumns._ID) + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_MOUSE,ItemReader.ItemEntry.COLUMN_NAME_MOUSE_PHOTO,String.valueOf((int)newRowIdMouse2),BaseColumns._ID) + " \n "+dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_MOUSE,ItemReader.ItemEntry.COLUMN_NAME_MOUSE_PRICE,String.valueOf((int)newRowIdMouse2),BaseColumns._ID));

        ContentValues keyboard_values2 = new ContentValues();

        keyboard_values2.put(ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD,"SteelSeries Apex Pro");
        keyboard_values2.put(ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD_PHOTO,R.drawable.keyboard2);
        keyboard_values2.put(ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD_PRICE,699);

        long newRowIdKeyboard2 = db_write.insert(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD,null,keyboard_values2);

        Log.v(CREATE_DATA_TAG,newRowIdKeyboard2 + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD,ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD,String.valueOf((int)newRowIdKeyboard2),BaseColumns._ID) + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD,ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD_PHOTO,String.valueOf((int)newRowIdKeyboard2),BaseColumns._ID) + " \n "+dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD,ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD_PRICE,String.valueOf((int)newRowIdKeyboard2),BaseColumns._ID));

        ContentValues user_account_values = new ContentValues();

        user_account_values.put(ItemReader.ItemEntry.COLUMN_NAME_USER_USERNAME,"Maciek");
        user_account_values.put(ItemReader.ItemEntry.COLUMN_NAME_USER_EMAIL,"maciek@mail.mail");
        user_account_values.put(ItemReader.ItemEntry.COLUMN_NAME_USER_PASSWORD,"Maciek123");
        user_account_values.put(ItemReader.ItemEntry.COLUMN_NAME_USER_PHONE_NUMBER,"5554");

        long newRowIdUserAccount = db_write.insert(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT,null,user_account_values);

        Log.v(CREATE_DATA_TAG,newRowIdUserAccount + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT,ItemReader.ItemEntry.COLUMN_NAME_USER_USERNAME,String.valueOf((int)newRowIdUserAccount),BaseColumns._ID) + " \n " + dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT,ItemReader.ItemEntry.COLUMN_NAME_USER_EMAIL,String.valueOf((int)newRowIdUserAccount),BaseColumns._ID) + " \n "+dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT,ItemReader.ItemEntry.COLUMN_NAME_USER_PASSWORD,String.valueOf((int)newRowIdUserAccount),BaseColumns._ID)+ " \n "+dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT,ItemReader.ItemEntry.COLUMN_NAME_USER_PHONE_NUMBER,String.valueOf((int)newRowIdUserAccount),BaseColumns._ID));

    }
}
