package com.studio.neopanda.diabetesmeganotes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "MeganotesReader.db";

    private static final String SQL_CREATE_ENTRIES_CREDENTIALS =
            "CREATE TABLE " + SQliteDatabase.Credentials.TABLE_NAME + " (" +
                    SQliteDatabase.Credentials._ID + " INTEGER PRIMARY KEY," +
                    SQliteDatabase.Credentials.COLUMN_NAME_PASSWORD + " TEXT," +
                    SQliteDatabase.Credentials.COLUMN_NAME_USERNAME + " TEXT)";

    private static final String SQL_DELETE_ENTRIES_CREDENTIALS =
            "DROP TABLE IF EXISTS " + SQliteDatabase.Credentials.TABLE_NAME;

    private static final String SQL_CREATE_ENTRIES_GLYCEMIES =
            "CREATE TABLE " + SQliteDatabase.Glycemies.TABLE_NAME + " (" +
                    SQliteDatabase.Glycemies._ID + " INTEGER PRIMARY KEY," +
                    SQliteDatabase.Glycemies.COLUMN_NAME_GLYCEMY + " TEXT," +
                    SQliteDatabase.Glycemies.COLUMN_NAME_EXTRA_INFOS + " TEXT," +
                    SQliteDatabase.Glycemies.COLUMN_NAME_DATE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES_GLYCEMIES =
            "DROP TABLE IF EXISTS " + SQliteDatabase.Glycemies.TABLE_NAME;

    private static final String SQL_CREATE_ENTRIES_INSULIN =
            "CREATE TABLE " + SQliteDatabase.InsulinUnits.TABLE_NAME + " (" +
                    SQliteDatabase.InsulinUnits._ID + " INTEGER PRIMARY KEY," +
                    SQliteDatabase.InsulinUnits.COLUMN_NAME_UNITS + " INTEGER," +
                    SQliteDatabase.InsulinUnits.COLUMN_NAME_EXTRA_INFOS + " TEXT," +
                    SQliteDatabase.InsulinUnits.COLUMN_NAME_DATE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES_INSULIN =
            "DROP TABLE IF EXISTS " + SQliteDatabase.InsulinUnits.TABLE_NAME;

    private static final String SQL_CREATE_ENTRIES_NOTE =
            "CREATE TABLE " + SQliteDatabase.Note.TABLE_NAME + " (" +
                    SQliteDatabase.Note._ID + " INTEGER PRIMARY KEY," +
                    SQliteDatabase.Note.COLUMN_NAME_TEXT + " TEXT)";

    private static final String SQL_DELETE_ENTRIES_NOTE =
            "DROP TABLE IF EXISTS " + SQliteDatabase.Note.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_CREDENTIALS);
        db.execSQL(SQL_CREATE_ENTRIES_GLYCEMIES);
        db.execSQL(SQL_CREATE_ENTRIES_INSULIN);
        db.execSQL(SQL_CREATE_ENTRIES_NOTE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES_CREDENTIALS);
        db.execSQL(SQL_DELETE_ENTRIES_GLYCEMIES);
        db.execSQL(SQL_DELETE_ENTRIES_INSULIN);
        db.execSQL(SQL_DELETE_ENTRIES_NOTE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public List<String> getGlycemiesInTimePeriod(String today, String target) {
        List<String> text = new ArrayList<>();
        String selectQuery = "SELECT * FROM Glycemies WHERE Date >= '" + target + "' AND Date <= '" + today + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    text.add(cursor.getString(3));
                }
                while (cursor.moveToNext());
            } else
                return null;
            db.close();
            cursor.close();
        } catch (Exception e) {
            System.out.println("Exception throw in SQLiteDBHandler" + e);
        }
        return text;
    }

    public List<String> getAverageGlycemies(String today, String target) {
        int i = 0;
        List<String> text = new ArrayList<>();
        String selectQuery = "SELECT * FROM Glycemies WHERE Date >= '" + target + "' AND Date <= '" + today + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    text.add(cursor.getString(1));
                }
                while (cursor.moveToNext());
            } else
                return null;
            db.close();
            cursor.close();
        } catch (Exception e) {
            System.out.println("Exception throw in SQLiteDBHandler" + e);
        }
        return text;
    }

    public String getNote() {
        String note;

        String selectQuery = "SELECT * FROM Note";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();
            note = cursor.getString(1);
            cursor.close();
            return note;
        } else {
            return "Vous pouvez ajouter une note en cliquant ici :)";
        }
    }
}
