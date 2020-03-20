package com.example.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Login.db";
    private static final String TABELLA_UTENTE = "utente";
    private static final String TABELLA_CITTA = "citta";
    private static final String TABELLA_MONUMENTI = "monumenti";
    private static final String TABELLA_GASTRONOMIA = "gastronomia";
    private static final String TABELLA_HOTELEBB = "hotelebb";


    public DatabaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME, null,2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABELLA_UTENTE + "(email text PRIMARY KEY, password text NOT NULL, nome text NOT NULL, cognome text NOT NULL, citta text NOT NULL, sesso text NOT NULL,dataNascita date NOT NULL)");
        db.execSQL("CREATE TABLE " + TABELLA_CITTA + "(nome text PRIMARY KEY)");
        db.execSQL("CREATE TABLE " + TABELLA_MONUMENTI + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, nome text NOT NULL, citta TEXT, FOREIGN KEY (citta) REFERENCES  " + TABELLA_CITTA + " (nome))");
        db.execSQL("CREATE TABLE " + TABELLA_GASTRONOMIA + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, nome text NOT NULL, citta TEXT, FOREIGN KEY (citta) REFERENCES " + TABELLA_CITTA + " (nome))");
        db.execSQL("CREATE TABLE " + TABELLA_HOTELEBB + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, nome text NOT NULL, citta TEXT, FOREIGN KEY (citta) REFERENCES  " + TABELLA_CITTA + " (nome))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELLA_UTENTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABELLA_CITTA);
        db.execSQL("DROP TABLE IF EXISTS " + TABELLA_HOTELEBB);
        db.execSQL("DROP TABLE IF EXISTS " + TABELLA_MONUMENTI);
        db.execSQL("DROP TABLE IF EXISTS " + TABELLA_GASTRONOMIA);
        onCreate(db);
    }

    /*SEZIONE UTENTE*/
    public boolean inserisciUtente(String email, String password, String nome, String cognome, String citta, String sesso, Date dataNascita){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentUtente = new ContentValues();
        contentUtente.put("email",email);
        contentUtente.put("password",password);
        contentUtente.put("nome",nome);
        contentUtente.put("cognome",cognome);
        contentUtente.put("citta",citta);
        contentUtente.put("sesso",sesso);
        contentUtente.put("dataNascita", String.valueOf(dataNascita));
        long ins = db.insert(TABELLA_UTENTE, null, contentUtente);
        if(ins==-1) return  false;
        else return true;
    }

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

    public Integer deleteDati(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(TABELLA_UTENTE, "email = ?", new String[]{email});
    }

    /*SEZIONE CITTA*/
    public boolean inserisciCitta(String nome){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentCitta = new ContentValues();
        contentCitta.put("nome", nome);
        long ins = db.insert(TABELLA_CITTA, null, contentCitta);
        if(ins==-1) return  false;
        else return true;
    }

    public Cursor getAllDataCitta(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM citta ORDER BY nome ASC",null);
        return res;
    }

    public Boolean checkCitta(String citta){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nome FROM citta WHERE nome=?", new String[]{citta});
        if(cursor.getCount()>0) return false;
        else return true;
    }

    public Integer deleteCitta(String nome){
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(TABELLA_CITTA, "nome = ?", new String[]{nome});
    }

    /*SEZIONE MONUMENTI*/
    public boolean inserisciMonumento(String nome,String nome_citta){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentMonumento = new ContentValues();
        contentMonumento.put("nome", nome);
        contentMonumento.put("citta", nome_citta);
        long ins = db.insert(TABELLA_MONUMENTI, null, contentMonumento);
        if(ins==-1) return  false;
        else return true;
    }

    public Cursor getAllDataMonumenti(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABELLA_MONUMENTI, null);
        return res;
    }

    public Boolean checkMonumento(String monumento){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nome FROM monumenti WHERE nome=?", new String[]{monumento});
        if(cursor.getCount()>0) return false;
        else return true;
    }

    public Integer deleteMonumento(String nome){
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(TABELLA_MONUMENTI, "nome = ?", new String[]{nome});
    }

    /*SEZIONE GASTRONOMIA*/
    public boolean inserisciGastronomia(String nome,String nome_citta){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentGastronomia = new ContentValues();
        contentGastronomia.put("nome",nome);
        contentGastronomia.put("citta", nome_citta);
        long ins = db.insert(TABELLA_GASTRONOMIA, null, contentGastronomia);
        if(ins==-1) return  false;
        else return true;
    }

    public Cursor getAllDataGastronomia(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABELLA_GASTRONOMIA, null);
        return res;
    }

    public Boolean checkGastronomia(String nome){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nome FROM gastronomia WHERE nome=?", new String[]{nome});
        if(cursor.getCount()>0) return false;
        else return true;
    }

    public Integer deleteGastronomia(String nome){
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(TABELLA_GASTRONOMIA, "nome = ?", new String[]{nome});
    }

    /*SEZIONE HOTEL&BB*/
    public boolean inserisciHotelBB(String nome,String nome_citta){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentHotel = new ContentValues();
        contentHotel.put("nome",nome);
        contentHotel.put("citta", nome_citta);
       // contentValues1.put("citta_ID", i);
        long ins = db.insert(TABELLA_HOTELEBB, null, contentHotel);
        if(ins==-1) return  false;
        else return true;
    }

    public Cursor getAllDataHotelBB(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABELLA_HOTELEBB, null);
        return res;
    }

    public Boolean checkHotelBB(String nome){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nome FROM hotelebb WHERE nome=?", new String[]{nome});
        if(cursor.getCount()>0) return false;
        else return true;
    }

    public Integer deleteHotelBB(String nome){
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(TABELLA_HOTELEBB, "nome = ?", new String[]{nome});
    }

}
