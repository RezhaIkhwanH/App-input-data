package com.example.sertifikasi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class dataBase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE = "daftar.db";
    public static final String Table_name = "data";
    public static final String col_Id = "id";
    public static final String col_Name = "name";
    public static final String col_Alamat = "alamat";
    public static final String col_NOhp = "noHp";
    public static final String col_Location = "location";
    public static final String col_Gambar = "gambar";
    public static final String col_Kelamin = "kelamin";


    public dataBase(@Nullable Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY autoincrement,%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL,%s TEXT NOT NULL,%s TEXT NOT NULL ,%s TEXT NOT NULL,%s TEXT NOT NULL )",
                Table_name, col_Id, col_Name, col_Alamat, col_NOhp, col_Location, col_Gambar, col_Kelamin);
//        System.out.println(createTable);

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_name);
        onCreate(db);
    }

    public Cursor getAllData() {
        SQLiteDatabase sql = this.getWritableDatabase();
        return sql.rawQuery("SELECT * FROM " + Table_name, null);
    }

    public void addData(ContentValues values) {
        SQLiteDatabase sql = this.getWritableDatabase();
        sql.insert(Table_name, null, values);

    }
}
