package com.example.assignment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SnackDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cinefast_snacks.db";
    private static final int DATABASE_VERSION = 1;

    // Table and columns
    private static final String TABLE_SNACKS = "snacks";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_PRICE = "price";
    private static final String COL_IMAGE = "image";

    private Context context;

    public SnackDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_SNACKS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_PRICE + " REAL NOT NULL, " +
                COL_IMAGE + " TEXT NOT NULL)";
        db.execSQL(createTable);

        // Insert initial snack data
        insertSnack(db, "Burger Combo", 14.99, "food1");
        insertSnack(db, "Pizza", 9.99, "food2");
        insertSnack(db, "Pina Colada", 6.99, "food3");
        insertSnack(db, "Nachos", 15.00, "food4");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SNACKS);
        onCreate(db);
    }

    private void insertSnack(SQLiteDatabase db, String name, double price, String image) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PRICE, price);
        values.put(COL_IMAGE, image);
        db.insert(TABLE_SNACKS, null, values);
    }

    public ArrayList<Snack> getAllSnacks() {
        ArrayList<Snack> snackList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SNACKS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE));
                String imageName = cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE));

                // Resolve drawable resource ID from name
                int imageResId = context.getResources().getIdentifier(
                        imageName, "drawable", context.getPackageName());

                Snack snack = new Snack(id, name, price, imageResId);
                snackList.add(snack);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return snackList;
    }
}
