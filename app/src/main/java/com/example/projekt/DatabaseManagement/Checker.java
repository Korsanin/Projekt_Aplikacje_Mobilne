package com.example.projekt.DatabaseManagement;

import android.provider.BaseColumns;

public class Checker {
    public static boolean check(ItemReader.ItemReaderDbHelper dbHelper){
        try{
            dbHelper.checkTable(ItemReader.ItemEntry.TABLE_NAME_COMPUTER);
        } catch (Exception e){
            dbHelper.createTable(ItemReader.ItemEntry.TABLE_NAME_COMPUTER);
        }
        try{
            dbHelper.checkTable(ItemReader.ItemEntry.TABLE_NAME_MOUSE);
        } catch (Exception e){
            dbHelper.createTable(ItemReader.ItemEntry.TABLE_NAME_MOUSE);
        }
        try{
            dbHelper.checkTable(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD);
        } catch (Exception e){
            dbHelper.createTable(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD);
        }
        try{
            dbHelper.checkTable(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT);
        } catch (Exception e){
            dbHelper.createTable(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT);
        }
        try{
            dbHelper.checkTable(ItemReader.ItemEntry.TABLE_NAME_USER_ORDER);
        } catch (Exception e){
            dbHelper.createTable(ItemReader.ItemEntry.TABLE_NAME_USER_ORDER);
        }
        try{
            dbHelper.checkTable(ItemReader.ItemEntry.TABLE_NAME_SMS);
        } catch (Exception e){
            dbHelper.createTable(ItemReader.ItemEntry.TABLE_NAME_SMS);
        }
        try{
            if (dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_COMPUTER,ItemReader.ItemEntry.COLUMN_NAME_COMPUTER,"1", BaseColumns._ID).get(0) == ""
            && dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_MOUSE,ItemReader.ItemEntry.COLUMN_NAME_MOUSE,"1", BaseColumns._ID).get(0) == ""
            && dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_KEYBOARD,ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD,"1", BaseColumns._ID).get(0) == ""
            && dbHelper.readData(ItemReader.ItemEntry.TABLE_NAME_USER_ACCOUNT,ItemReader.ItemEntry.COLUMN_NAME_USER_USERNAME,"1", BaseColumns._ID).get(0) == ""){
                int[] x = {};
                int y = x[10];
            }
        }catch (Exception e){
            CreateData.CreateDatabaseData(dbHelper);
        }
        return true;
    }
}
