package com.example.projekt.DatabaseManagement;

import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public final class ItemReader {
    public static final String SQL_TAG = "SQL";
    public ItemReader() {
    }

    public class ItemEntry implements BaseColumns {
        public static final String TABLE_NAME_COMPUTER = "computers";
        public static final String TABLE_NAME_MOUSE = "mouses";
        public static final String TABLE_NAME_KEYBOARD = "keyboards";
        public static final String TABLE_NAME_USER_ORDER = "user_order";
        public static final String TABLE_NAME_USER_ACCOUNT = "user_account";
        public static final String TABLE_NAME_SMS = "sms";
        //        public static final String COLUMN_NAME_TITLE = "title";
//        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
        public static final String COLUMN_NAME_COMPUTER = "computer";
        public static final String COLUMN_NAME_COMPUTER_PHOTO = "computer_photo";
        public static final String COLUMN_NAME_COMPUTER_PRICE = "computer_price";
        public static final String COLUMN_NAME_COMPUTER_ID = "computer_id";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_MOUSE = "mouse";
        public static final String COLUMN_NAME_MOUSE_PHOTO = "mouse_photo";
        public static final String COLUMN_NAME_MOUSE_PRICE = "mouse_price";
        public static final String COLUMN_NAME_MOUSE_ID = "mouse_id";
        public static final String COLUMN_NAME_KEYBOARD = "keyboard";
        public static final String COLUMN_NAME_KEYBOARD_PHOTO = "keyboard_photo";
        public static final String COLUMN_NAME_KEYBOARD_PRICE = "keyboard_price";
        public static final String COLUMN_NAME_KEYBOARD_ID = "keyboard_id";
        public static final String COLUMN_NAME_USER_USERNAME = "username";
        public static final String COLUMN_NAME_USER_PASSWORD = "password";
        public static final String COLUMN_NAME_USER_EMAIL = "email";
        public static final String COLUMN_NAME_USER_PHONE_NUMBER = "phone_number";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_SMS_MESSAGE = "sms_message";
        public static final String COLUMN_NAME_SMS_PHONE_NUMBER = "sms_phone_number";
        public static final String COLUMN_NAME_SMS_TYPE = "sms_type";
    }

    private static final String SQL_CREATE_ENTRIES_COMPUTER =
            "CREATE TABLE "
                    + ItemEntry.TABLE_NAME_COMPUTER + " ("
                    + ItemEntry._ID + " INTEGER PRIMARY KEY,"
                    + ItemEntry.COLUMN_NAME_COMPUTER + " TEXT,"
                    + ItemEntry.COLUMN_NAME_COMPUTER_PHOTO + " INT, "
                    + ItemEntry.COLUMN_NAME_COMPUTER_PRICE + " INT)";

    private static final String SQL_CREATE_ENTRIES_MOUSE =
            "CREATE TABLE "
                    + ItemEntry.TABLE_NAME_MOUSE + " ("
                    + ItemEntry._ID + " INTEGER PRIMARY KEY,"
                    + ItemEntry.COLUMN_NAME_MOUSE + " TEXT,"
                    + ItemEntry.COLUMN_NAME_MOUSE_PHOTO + " INT, "
                    + ItemEntry.COLUMN_NAME_MOUSE_PRICE + " INT)";

    private static final String SQL_CREATE_ENTRIES_KEYBOARD =
            "CREATE TABLE "
                    + ItemEntry.TABLE_NAME_KEYBOARD + " ("
                    + ItemEntry._ID + " INTEGER PRIMARY KEY,"
                    + ItemEntry.COLUMN_NAME_KEYBOARD + " TEXT,"
                    + ItemEntry.COLUMN_NAME_KEYBOARD_PHOTO + " INT, "
                    + ItemEntry.COLUMN_NAME_KEYBOARD_PRICE + " INT)";

    private static final String SQL_CREATE_ENTRIES_USER_ACCOUNT =
            "CREATE TABLE "
                    + ItemEntry.TABLE_NAME_USER_ACCOUNT + " ("
                    + ItemEntry._ID + " INTEGER PRIMARY KEY,"
                    + ItemEntry.COLUMN_NAME_USER_USERNAME + " TEXT,"
                    + ItemEntry.COLUMN_NAME_USER_PASSWORD + " TEXT, "
                    + ItemEntry.COLUMN_NAME_USER_EMAIL + " TEXT,"
                    + ItemEntry.COLUMN_NAME_USER_PHONE_NUMBER + " INT)";

    private static final String SQL_CREATE_ENTRIES_USER_ORDER =
            "CREATE TABLE "
                    + ItemEntry.TABLE_NAME_USER_ORDER + " ("
                    + ItemEntry._ID + " INTEGER PRIMARY KEY, "
                    + ItemEntry.COLUMN_NAME_USER_ID + " INT, "
                    + ItemEntry.COLUMN_NAME_AMOUNT + " INT, "
                    + ItemEntry.COLUMN_NAME_COMPUTER_ID + " INT ,"
                    + ItemEntry.COLUMN_NAME_MOUSE_ID + " INT ,"
                    + ItemEntry.COLUMN_NAME_KEYBOARD_ID + " INT ,"
                    + "FOREIGN KEY (" + ItemEntry.COLUMN_NAME_COMPUTER_ID + ") REFERENCES "+ItemEntry.TABLE_NAME_COMPUTER+" (" + ItemEntry._ID+"),"
                    + "FOREIGN KEY (" + ItemEntry.COLUMN_NAME_MOUSE_ID + ") REFERENCES "+ItemEntry.TABLE_NAME_MOUSE+" (" + ItemEntry._ID+"),"
                    + "FOREIGN KEY (" + ItemEntry.COLUMN_NAME_KEYBOARD_ID + ") REFERENCES "+ItemEntry.TABLE_NAME_KEYBOARD+" (" + ItemEntry._ID+"),"
                    + "FOREIGN KEY (" + ItemEntry.COLUMN_NAME_USER_ID + ") REFERENCES "+ItemEntry.TABLE_NAME_USER_ACCOUNT+" (" + ItemEntry._ID+"))";

    private static final String SQL_CREATE_ENTRIES_SMS =
            "CREATE TABLE "
                    + ItemEntry.TABLE_NAME_SMS + " ("
                    + ItemEntry._ID + " INTEGER PRIMARY KEY,"
                    + ItemEntry.COLUMN_NAME_SMS_MESSAGE + " TEXT,"
                    + ItemEntry.COLUMN_NAME_SMS_PHONE_NUMBER + " TEXT,"
                    + ItemEntry.COLUMN_NAME_SMS_TYPE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES_COMPUTER =
            "DROP TABLE IF EXISTS "
                    + ItemEntry.TABLE_NAME_COMPUTER;

    private static final String SQL_DELETE_ENTRIES_MOUSE =
            "DROP TABLE IF EXISTS "
                    + ItemEntry.TABLE_NAME_MOUSE;

    private static final String SQL_DELETE_ENTRIES_KEYBOARD =
            "DROP TABLE IF EXISTS "
                    + ItemEntry.TABLE_NAME_KEYBOARD;

    private static final String SQL_DELETE_ENTRIES_USER_ACCOUNT =
            "DROP TABLE IF EXISTS "
                    + ItemEntry.TABLE_NAME_USER_ACCOUNT;

    private static final String SQL_DELETE_ENTRIES_USER_ORDER =
            "DROP TABLE IF EXISTS "
                    + ItemEntry.TABLE_NAME_USER_ORDER;

    private static final String SQL_DELETE_ENTRIES_SMS =
            "DROP TABLE IF EXISTS "
                    + ItemEntry.TABLE_NAME_SMS;

    public static class ItemReaderDbHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 3;
        public static final String DATABASE_NAME = "ItemReader.db";

        public ItemReaderDbHelper(Context context){
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES_COMPUTER);
            sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES_KEYBOARD);
            sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES_MOUSE);
            sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES_USER_ACCOUNT);
            sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES_USER_ORDER);
            sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES_SMS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES_COMPUTER);
            sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES_KEYBOARD);
            sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES_MOUSE);
            sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES_USER_ACCOUNT);
            sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES_USER_ORDER);
            sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES_SMS);
            onCreate(sqLiteDatabase);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            super.onDowngrade(db, oldVersion, newVersion);
        }

        public List readData(String tableName,String columnNameToShow, String keyword, String columnNameToSelection){
            SQLiteDatabase db_read = getReadableDatabase();

            String[] projection;

            switch (tableName){
                case ItemEntry.TABLE_NAME_COMPUTER:
                    projection = new String[]{
                            BaseColumns._ID,
                            ItemReader.ItemEntry.COLUMN_NAME_COMPUTER,
                            ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_PHOTO,
                            ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_PRICE
                    };
                    break;
                case ItemEntry.TABLE_NAME_KEYBOARD:
                    projection = new String[]{
                            BaseColumns._ID,
                            ItemEntry.COLUMN_NAME_KEYBOARD,
                            ItemEntry.COLUMN_NAME_KEYBOARD_PHOTO,
                            ItemEntry.COLUMN_NAME_KEYBOARD_PRICE
                    };
                    break;
                case ItemEntry.TABLE_NAME_MOUSE:
                    projection = new String[]{
                            BaseColumns._ID,
                            ItemEntry.COLUMN_NAME_MOUSE,
                            ItemEntry.COLUMN_NAME_MOUSE_PHOTO,
                            ItemEntry.COLUMN_NAME_MOUSE_PRICE
                    };
                    break;
                case ItemEntry.TABLE_NAME_USER_ACCOUNT:
                    projection = new String[]{
                            BaseColumns._ID,
                            ItemEntry.COLUMN_NAME_USER_USERNAME,
                            ItemEntry.COLUMN_NAME_USER_EMAIL,
                            ItemEntry.COLUMN_NAME_USER_PASSWORD,
                            ItemEntry.COLUMN_NAME_USER_PHONE_NUMBER
                    };
                    break;
                case ItemEntry.TABLE_NAME_USER_ORDER:
                    projection = new String[]{
                            BaseColumns._ID,
                            ItemReader.ItemEntry.COLUMN_NAME_USER_ID,
                            ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_ID,
                            ItemReader.ItemEntry.COLUMN_NAME_MOUSE_ID,
                            ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD_ID,
                            ItemReader.ItemEntry.COLUMN_NAME_AMOUNT
                    };
                    break;
                case ItemEntry.TABLE_NAME_SMS:
                    projection = new String[]{
                            BaseColumns._ID,
                            ItemEntry.COLUMN_NAME_SMS_PHONE_NUMBER,
                            ItemEntry.COLUMN_NAME_SMS_MESSAGE,
                            ItemEntry.COLUMN_NAME_SMS_TYPE,
                    };
                    break;
                default:
                    projection = new String[]{};
            }

            String sortOrder =
                    BaseColumns._ID + " ASC";

            if(keyword != null){
                keyword = columnNameToSelection + " IN ("+keyword+")";
            }

            Cursor cursor = db_read.query(
                    tableName,
                    projection,
                    keyword,
                    null,
                    null,
                    null,
                    sortOrder
            );

            List result = new ArrayList<>();
            while(cursor.moveToNext()){
                if(columnNameToShow == BaseColumns._ID){
                    long item = cursor.getLong(
                            cursor.getColumnIndexOrThrow(ItemReader.ItemEntry._ID)
                    );
                    result.add(item);
                } else if(columnNameToShow == ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_PHOTO
                        || columnNameToShow == ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_PRICE
                        || columnNameToShow == ItemReader.ItemEntry.COLUMN_NAME_MOUSE_PHOTO
                        || columnNameToShow == ItemReader.ItemEntry.COLUMN_NAME_MOUSE_PRICE
                        || columnNameToShow == ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD_PHOTO
                        || columnNameToShow == ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD_PRICE
                        || columnNameToShow==ItemEntry.COLUMN_NAME_USER_PHONE_NUMBER
                        || columnNameToShow == ItemReader.ItemEntry.COLUMN_NAME_AMOUNT
                        || columnNameToShow == ItemReader.ItemEntry.COLUMN_NAME_COMPUTER_ID
                        || columnNameToShow == ItemReader.ItemEntry.COLUMN_NAME_KEYBOARD_ID
                        || columnNameToShow == ItemReader.ItemEntry.COLUMN_NAME_USER_ID
                        || columnNameToShow == ItemReader.ItemEntry.COLUMN_NAME_MOUSE_ID){
                    int item = cursor.getInt(
                            cursor.getColumnIndexOrThrow(columnNameToShow)
                    );
                    result.add(item);
                }
                else{
                    String item = cursor.getString(
                            cursor.getColumnIndexOrThrow(columnNameToShow)
                    );
                    result.add(item);
                }
            }
            cursor.close();
            Log.v(SQL_TAG,"--------------------------------"+result);
            return result;
        }

        public int deleteDataComputers(String like){
            SQLiteDatabase db_write = getWritableDatabase();
            String selection = ItemEntry.COLUMN_NAME_COMPUTER + " LIKE ?";
            String[] selectionArgs = {like};
            return db_write.delete(ItemEntry.TABLE_NAME_COMPUTER,selection,selectionArgs);
        }

        public int deleteDataMouses(String like){
            SQLiteDatabase db_write = getWritableDatabase();
            String selection = ItemEntry.COLUMN_NAME_MOUSE + " LIKE ?";
            String[] selectionArgs = {like};
            return db_write.delete(ItemEntry.TABLE_NAME_MOUSE,selection,selectionArgs);
        }

        public int deleteDataKeyboards(String like){
            SQLiteDatabase db_write = getWritableDatabase();
            String selection = ItemEntry.COLUMN_NAME_KEYBOARD + " LIKE ?";
            String[] selectionArgs = {like};
            return db_write.delete(ItemEntry.TABLE_NAME_KEYBOARD,selection,selectionArgs);
        }

        public int deleteDataUserAccount(String like){
            SQLiteDatabase db_write = getWritableDatabase();
            String selection = ItemEntry.COLUMN_NAME_USER_USERNAME + " LIKE ?";
            String[] selectionArgs = {like};
            return db_write.delete(ItemEntry.TABLE_NAME_USER_ACCOUNT,selection,selectionArgs);
        }

        public int deleteDataUserOrder(String like){
            SQLiteDatabase db_write = getWritableDatabase();
            String selection = BaseColumns._ID + " LIKE ?";
            String[] selectionArgs = {like};
            return db_write.delete(ItemEntry.TABLE_NAME_USER_ORDER,selection,selectionArgs);
        }

        public int deleteDataSms(String like){
            SQLiteDatabase db_write = getWritableDatabase();
            String selection = ItemEntry.COLUMN_NAME_SMS_PHONE_NUMBER + " LIKE ?";
            String[] selectionArgs = {like};
            return db_write.delete(ItemEntry.TABLE_NAME_SMS,selection,selectionArgs);
        }

        public void dropTable(String table){
            SQLiteDatabase db_write = getWritableDatabase();
            db_write.execSQL("DROP TABLE IF EXISTS "+table);
        }

        public void dropTables(){
            SQLiteDatabase db_write = getWritableDatabase();

            db_write.execSQL(SQL_DELETE_ENTRIES_COMPUTER);
            db_write.execSQL(SQL_DELETE_ENTRIES_MOUSE);
            db_write.execSQL(SQL_DELETE_ENTRIES_KEYBOARD);
            db_write.execSQL(SQL_DELETE_ENTRIES_USER_ACCOUNT);
            db_write.execSQL(SQL_DELETE_ENTRIES_USER_ORDER);
            db_write.execSQL(SQL_DELETE_ENTRIES_SMS);
        }

        public void truncateTable(String table){
            SQLiteDatabase db_write = getWritableDatabase();
            db_write.delete(table,null,null);
        }

        public void createTables(){
            SQLiteDatabase db_write = getWritableDatabase();

            db_write.execSQL(SQL_CREATE_ENTRIES_COMPUTER);
            db_write.execSQL(SQL_CREATE_ENTRIES_MOUSE);
            db_write.execSQL(SQL_CREATE_ENTRIES_KEYBOARD);
            db_write.execSQL(SQL_CREATE_ENTRIES_USER_ACCOUNT);
            db_write.execSQL(SQL_CREATE_ENTRIES_USER_ORDER);
            db_write.execSQL(SQL_CREATE_ENTRIES_SMS);
        }

        public void createTable(String table){
            SQLiteDatabase db_write = getWritableDatabase();

            switch (table){
                case ItemEntry.TABLE_NAME_COMPUTER:
                    db_write.execSQL(SQL_CREATE_ENTRIES_COMPUTER);
                    break;
                case ItemEntry.TABLE_NAME_MOUSE:
                    db_write.execSQL(SQL_CREATE_ENTRIES_MOUSE);
                    break;
                case ItemEntry.TABLE_NAME_KEYBOARD:
                    db_write.execSQL(SQL_CREATE_ENTRIES_KEYBOARD);
                    break;
                case ItemEntry.TABLE_NAME_USER_ACCOUNT:
                    db_write.execSQL(SQL_CREATE_ENTRIES_USER_ACCOUNT);
                    break;
                case ItemEntry.TABLE_NAME_USER_ORDER:
                    db_write.execSQL(SQL_CREATE_ENTRIES_USER_ORDER);
                    break;
                case ItemEntry.TABLE_NAME_SMS:
                    db_write.execSQL(SQL_CREATE_ENTRIES_SMS);
                    break;
            }
        }

        public boolean checkTable(String table){
            SQLiteDatabase db_read = getReadableDatabase();

            String[] projection = {
                    BaseColumns._ID
            };

            String id = BaseColumns._ID + " IN ("+1+")";

            Cursor cursor = db_read.query(
                    table,
                    projection,
                    id,
                    null,
                    null,
                    null,
                    null
            );


            List list = new ArrayList<>();
            while(cursor.moveToNext()){
                long item = cursor.getLong(cursor.getColumnIndexOrThrow(ItemReader.ItemEntry._ID));
                list.add(item);
            }
            boolean result = !list.isEmpty();
            cursor.close();
            Log.v(SQL_TAG,"--------------------------------"+list+"--------------------------------"+result);
            db_read.close();
            return result;
        }
    }

}
