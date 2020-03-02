package com.example.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context,"Login.db", null,2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE utente (email text PRIMARY KEY, password text NOT NULL, nome text NOT NULL, cognome text NOT NULL, citta text NOT NULL, sesso text NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS utente");
    }

    //inserimento dati nel database
    public boolean inserisci(String email, String password, String nome, String cognome, String citta, String sesso){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email",email);
        contentValues.put("password",password);
        contentValues.put("nome",nome);
        contentValues.put("cognome",cognome);
        contentValues.put("citta",citta);
        contentValues.put("sesso",sesso);
        long ins = db.insert("utente", null, contentValues);
        if(ins==-1) return  false;
        else return true;
    }

    //controllo della mail esistente
    public Boolean checkEmail(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT email FROM utente WHERE email=?", new String[]{email});
        if(cursor.getCount()>0) return false;
        else return true;
    }

    public Boolean checkEmailPassword (String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM utente WHERE email =? and password=?", new String[]{email,password});
        if(cursor.getCount()>0) return true;
        else return false;
    }
}
