package com.example.lista3_bolanowski;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.Date;
import java.util.LinkedList;

public class DBHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "crimesDB_JAVA.db";

    public static final String TABLE_CRIMES = "crimes";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_SOLVED = "solved";
    public static final String COLUMN_IMAGE = "image";


    public DBHandler(Context context) {
        super(context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CRIMES_TABLE = "CREATE TABLE " + TABLE_CRIMES +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_TITLE + " TEXT," +
                COLUMN_DATE + " INTEGER," +
                COLUMN_SOLVED + " INTEGER," +
                COLUMN_IMAGE + " TEXT" + ")";
        db.execSQL(CREATE_CRIMES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRIMES);
        onCreate(db);
    }


    public LinkedList<Crime> getCrimesList() {
        LinkedList<Crime> crimeList = new LinkedList<>();

        crimeList.clear();
        String query = "SELECT * FROM " + TABLE_CRIMES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                Date date = new Date(cursor.getLong(2));
                boolean solved = (cursor.getInt(3) == 1);
                String image = cursor.getString(4);
                crimeList.add(new Crime(id, title, date, solved, image));
            }
        }
        cursor.close();
        db.close();
        return crimeList;
    }

    public Crime getCrime(int id){
        Crime crimeGetter = new Crime();
        String query = "SELECT * FROM " + TABLE_CRIMES +
                " WHERE " + COLUMN_ID +
                " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            crimeGetter = new Crime(cursor.getInt(0), cursor.getString(1), new Date(cursor.getLong(2)), (cursor.getInt(3) == 1), cursor.getString(4));
            cursor.close();
        }
        db.close();
        return crimeGetter;
    }

    public void addCrime(Crime crime){
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, crime.getTitle());
        values.put(COLUMN_DATE, crime.getDate().getTime());
        values.put(COLUMN_SOLVED, crime.isSolved());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_CRIMES, null, values);
        db.close();
    }

    public void deleteCrime(Crime crime) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CRIMES, COLUMN_ID + " = " + crime.getId(), null);
        db.close();
    }

    public void editCrime(Crime crime){
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, crime.getTitle());
        values.put(COLUMN_DATE, crime.getDate().getTime());
        values.put(COLUMN_SOLVED, crime.isSolved());
        values.put(COLUMN_IMAGE, crime.getImage());

        SQLiteDatabase db = this.getWritableDatabase();

        db.update(TABLE_CRIMES, values, COLUMN_ID + " = " + crime.getId(), null);
        db.close();
    }
}
