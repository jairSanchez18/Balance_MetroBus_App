package com.example.balance_metrobus.BDHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    String sqlCreate = "CREATE TABLE cards (numero TEXT, saldo TEXT, estado TEXT, fecha TEXT, tipo TEXT, nombre TEXT)";

    public DbHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
