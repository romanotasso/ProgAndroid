package com.example.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Login.db";
    private static final String TABELLA_UTENTE = "utente";
    private static final String TABELLA_CITTA = "citta";


    public DatabaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME, null,2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABELLA_UTENTE + "(email text PRIMARY KEY, password text NOT NULL, nome text NOT NULL, cognome text NOT NULL, citta text NOT NULL, sesso text NOT NULL)");
        db.execSQL("CREATE TABLE " + TABELLA_CITTA + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, nome text NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELLA_UTENTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABELLA_CITTA);
        onCreate(db);
    }

    //inserimento dati nella tabella utente
    public boolean inserisci(String email, String password, String nome, String cognome, String citta, String sesso){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email",email);
        contentValues.put("password",password);
        contentValues.put("nome",nome);
        contentValues.put("cognome",cognome);
        contentValues.put("citta",citta);
        contentValues.put("sesso",sesso);
        long ins = db.insert(TABELLA_UTENTE, null, contentValues);
        if(ins==-1) return  false;
        else return true;
    }

    public boolean inserisciCitta(String nome){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("nome",nome);
        long ins = db.insert(TABELLA_CITTA, null, contentValues1);
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

    public Cursor getAllData(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABELLA_UTENTE, null);
        return res;
    }

    public boolean updateDati(String nome, String cognome, String citta, String sesso){
         SQLiteDatabase db = getWritableDatabase();
         ContentValues contentValues = new ContentValues();
        contentValues.put("nome",nome);
        contentValues.put("cognome",cognome);
        contentValues.put("citta",citta);
        contentValues.put("sesso",sesso);
        db.update(TABELLA_UTENTE,contentValues, null, null);
        return true;
    }

    public Integer deleteDati(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(TABELLA_UTENTE, "email = ?", new String[]{email});
    }

    public Cursor getAllDataCitta(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABELLA_CITTA, null);
        return res;
    }

}
