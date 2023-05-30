package lostandfound.maps.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import lostandfound.maps.model.Item;
import lostandfound.maps.util.Util;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_ITEM_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "(" + Util.ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Util.ITEM_LOST + " TEXT, " + Util.ITEM_NAME + " TEXT, " + Util.ITEM_DESCRIPTION + " TEXT, "
                + Util.ITEM_LOCATION + " TEXT, " + Util.PHONE + " TEXT, "+ Util.DATE + " TEXT, " + Util.LATITUDE + " TEXT, " + Util.LONGITUDE + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_ITEM_TABLE = "DROP TABLE IF EXISTS";
        sqLiteDatabase.execSQL(DROP_ITEM_TABLE, new String[] {Util.TABLE_NAME});

        onCreate(sqLiteDatabase);
    }


    public long insertItem (Item item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.ITEM_NAME, item.getName());
        contentValues.put(Util.ITEM_LOST, item.getLost());
        contentValues.put(Util.ITEM_DESCRIPTION, item.getDescription());
        contentValues.put(Util.ITEM_LOCATION, item.getLocation());
        contentValues.put(Util.PHONE, item.getPhone());
        contentValues.put(Util.LATITUDE, item.getLatitude());
        contentValues.put(Util.LONGITUDE, item.getLongitude());
        long newRowId = db.insert(Util.TABLE_NAME, null, contentValues);
        db.close();
        return newRowId;
    }

    public void deleteItem(int itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Define 'where' part of query.
        String selection = Util.ITEM_ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(itemId) };
        // Issue SQL statement.
        db.delete(Util.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public List<Item> getAllItems() {
        List<Item> itemList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Util.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setLost(cursor.getString(1));
                item.setName(cursor.getString(2));
                item.setDescription(cursor.getString(3));
                item.setLocation(cursor.getString(4));
                item.setPhone(cursor.getString(5));
                item.setDate(cursor.getString(6));
                item.setLatitude(cursor.getString(7));
                item.setLongitude(cursor.getString(8));
                // Adding item to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        // return item list
        return itemList;
    }



}
