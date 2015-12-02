package com.erikmafo.dailyselfie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.Collection;

/**
 * Created by erikmafo on 18.11.15.
 */
public class SelfieDatabaseHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Selfie.db";

    public SelfieDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SelfieEntry.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SelfieEntry.SQL_DELETE_ENTRIES);
        db.execSQL(SelfieEntry.SQL_CREATE_ENTRIES);
    }

    public void add(Selfie selfie) {
        ContentValues values = new ContentValues();
        values.put(SelfieEntry.COLUMN_NAME_PATH, selfie.getPath());
        values.put(SelfieEntry.COLUMN_NAME_TIMESTAMP, selfie.getTimeStamp());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(SelfieEntry.TABLE_NAME, null, values);
    }

    public void delete(Selfie selfie) {
        SQLiteDatabase db  = getWritableDatabase();
        db.delete(SelfieEntry.TABLE_NAME,
                SelfieEntry.COLUMN_NAME_TIMESTAMP + "=" + selfie.getTimeStamp(), null);
    }

    public void delete(Collection<Selfie> selfies) {
        for (Selfie selfie : selfies) {
            delete(selfie);
        }
    }

    public Selfie[] getSelfies() {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(SelfieEntry.TABLE_NAME,
                new String[] {SelfieEntry.COLUMN_NAME_PATH, SelfieEntry.COLUMN_NAME_TIMESTAMP},
                null, null, null, null, SelfieEntry.COLUMN_NAME_TIMESTAMP, null);

        Selfie[] selfies = new Selfie[cursor.getCount()];

        for (int i=0; i<cursor.getCount(); i++) {
            cursor.moveToNext();
            String path = cursor.getString(0);
            Long timestamp = cursor.getLong(1);
            selfies[i] = new Selfie(path, timestamp);
        }

        return selfies;
    }



}

abstract class SelfieEntry implements BaseColumns {

    private SelfieEntry(){}

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ", ";
    //private static final String UNIQUE = " UNIQUE";
    private static final String NOT_NULL = " NOT NULL";

    public static final String TABLE_NAME = "selfies";
    public static final String COLUMN_NAME_PATH = "path";
    public static final String COLUMN_NAME_TIMESTAMP = "timestamp";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    SelfieEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    SelfieEntry.COLUMN_NAME_PATH + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    SelfieEntry.COLUMN_NAME_TIMESTAMP + INTEGER_TYPE + NOT_NULL +
                    " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SelfieEntry.TABLE_NAME;

}
