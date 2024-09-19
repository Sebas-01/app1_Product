package com.example.app1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class clsDB extends SQLiteOpenHelper {
    String tblProduct = "Create Table product(reference text, description text, price integer, typeref integer)";
    String tblUser = "Create Table user(username text, fullname text, password text)";
    public clsDB(Context context, String name,  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla producto a través de la variable tblProduct
        db.execSQL(tblProduct);
        db.execSQL(tblUser);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("Drop table product");
        db.execSQL(tblProduct);
        db.execSQL("Drop table user");
        db.execSQL(tblUser);
    }
}
