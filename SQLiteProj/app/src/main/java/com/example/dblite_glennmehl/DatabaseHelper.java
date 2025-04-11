package com.example.dblite_glennmehl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE = "Accounts.db";
    private static final String TABLE = "accounts";
    private static final String C_SITE = "site";
    private static final String C_USER = "user";
    private static final String C_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
db.execSQL("CREATE TABLE " + TABLE + " (" + C_SITE + " TEXT PRIMARY KEY, " + C_USER + " TEXT, " + C_PASSWORD + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public boolean insertAccount(String site, String user, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(C_SITE, site);
        contentValues.put(C_USER, user);
        contentValues.put(C_PASSWORD, password);

        long result = db.insertWithOnConflict(TABLE, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

        return result != -1;
    }

    public Cursor retrieveAccount(String site) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE + " WHERE " + C_SITE + "=?", new String[]{site});
    }
}
