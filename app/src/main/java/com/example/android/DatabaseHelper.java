package com.example.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Login.db";
    private static final String TABELLA_UTENTE = "utente";
    private static final String TABELLA_CITTA = "citta";
    private static final String TABELLA_MONUMENTI = "monumenti";
    private static final String TABELLA_GASTRONOMIA = "gastronomia";
    private static final String TABELLA_HOTELEBB = "hotelebb";
    private static final String TABELLA_VIAGGI = "viaggi";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABELLA_UTENTE + "(email text PRIMARY KEY, nome text NOT NULL, cognome text NOT NULL, citta text NOT NULL, sesso text NOT NULL, dataNascita date NOT NULL, coupon text NOT NULL, codiceFoto text NOT NULL)");
        db.execSQL("CREATE TABLE " + TABELLA_CITTA + "(nome text PRIMARY KEY)");
        db.execSQL("CREATE TABLE " + TABELLA_MONUMENTI + "( nome text PRIMARY KEY, citta TEXT,categoria text NOT NULL,FOREIGN KEY (citta) REFERENCES  " + TABELLA_CITTA + " (nome))");
        db.execSQL("CREATE TABLE " + TABELLA_GASTRONOMIA + "( nome text PRIMARY KEY, citta TEXT,categoria text NOT NULL,FOREIGN KEY (citta) REFERENCES " + TABELLA_CITTA + " (nome))");
        db.execSQL("CREATE TABLE " + TABELLA_HOTELEBB + "(nome text PRIMARY KEY,citta TEXT,categoria text NOT NULL, FOREIGN KEY (citta) REFERENCES  " + TABELLA_CITTA + " (nome))");
        db.execSQL("CREATE TABLE " + TABELLA_VIAGGI + "(email text, citta TEXT, nome TEXT, tipologia TEXT,rating TEXT, PRIMARY KEY(email, citta, nome), FOREIGN KEY (citta) REFERENCES  " + TABELLA_CITTA + " (nome), FOREIGN KEY (email) REFERENCES  " + TABELLA_UTENTE + " (email))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELLA_UTENTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABELLA_CITTA);
        db.execSQL("DROP TABLE IF EXISTS " + TABELLA_HOTELEBB);
        db.execSQL("DROP TABLE IF EXISTS " + TABELLA_MONUMENTI);
        db.execSQL("DROP TABLE IF EXISTS " + TABELLA_GASTRONOMIA);
        db.execSQL("DROP TABLE IF EXISTS " + TABELLA_VIAGGI);
        onCreate(db);
    }
    /*SEZIONE UTENTE*/
    public boolean inserisciUtente(String email, String nome, String cognome, String citta, String sesso, String dataNascita, String coupon, String codiceFoto){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentUtente = new ContentValues();
        contentUtente.put("email",email);
        contentUtente.put("nome",nome);
        contentUtente.put("cognome",cognome);
        contentUtente.put("citta",citta);
        contentUtente.put("sesso",sesso);
        contentUtente.put("dataNascita", dataNascita);
        contentUtente.put("coupon", coupon);
        contentUtente.put("codiceFoto", codiceFoto);
        long ins = db.insert(TABELLA_UTENTE, null, contentUtente);
        if(ins==-1) return  false;
        else return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABELLA_UTENTE + " ORDER BY email", null);
        return res;
    }

    public Cursor getAllDataUtente(String email) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABELLA_UTENTE + " WHERE email = ?", new String[]{email});
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

    public boolean checkEmail (String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT email FROM utente WHERE email = ?", new String[]{email});
        int count = res.getCount();

        if (count > 0){
            return false;
        } else {
            return true;
        }
    }

    public boolean checkCitta (String nome) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT nome FROM citta WHERE nome = ?", new String[]{nome});
        int count = res.getCount();

        if (count > 0){
            return false;
        } else {
            return true;
        }
    }

    public boolean checkMonumento (String nome,String citta) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT nome FROM monumenti WHERE nome = ? AND citta = ?", new String[]{nome,citta});
        int count = res.getCount();

        if (count > 0){
            return false;
        } else {
            return true;
        }
    }

    public boolean checkGastronomia (String nome,String citta) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT nome FROM gastronomia WHERE nome = ? AND citta = ?", new String[]{nome,citta});
        int count = res.getCount();

        if (count > 0){
            return false;
        } else {
            return true;
        }
    }

    public boolean checkHotel (String nome,String citta) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT nome FROM hotelebb WHERE nome = ?  AND citta = ?", new String[]{nome,citta});
        int count = res.getCount();

        if (count > 0){
            return false;
        } else {
            return true;
        }
    }

    public Integer deleteCitta(String nome){
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(TABELLA_CITTA, "nome = ?", new String[]{nome});
    }

    /*SEZIONE MONUMENTI*/
    public boolean inserisciMonumento(String nome,String nome_citta,String categoria){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentMonumento = new ContentValues();
        contentMonumento.put("nome", nome);
        contentMonumento.put("citta", nome_citta);
        contentMonumento.put("categoria", categoria);
        long ins = db.insert(TABELLA_MONUMENTI, null, contentMonumento);
        if(ins==-1) return  false;
        else return true;
    }

    public Cursor getAllDataMonumenti(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABELLA_MONUMENTI + " ORDER BY nome", null);
        return res;
    }

    public Cursor getAllDataMonumentiCitta(String citta){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT nome,categoria FROM " + TABELLA_MONUMENTI + " WHERE citta = ? ORDER BY nome", new String[]{citta});
        return res;
    }

    public Integer deleteMonumento(String nome){
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(TABELLA_MONUMENTI, "nome = ?", new String[]{nome});
    }
    /*SEZIONE GASTRONOMIA*/
    public boolean inserisciGastronomia(String nome,String nome_citta,String categoria){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentGastronomia = new ContentValues();
        contentGastronomia.put("nome",nome);
        contentGastronomia.put("citta", nome_citta);
        contentGastronomia.put("categoria",categoria);
        long ins = db.insert(TABELLA_GASTRONOMIA, null, contentGastronomia);
        if(ins==-1) return  false;
        else return true;
    }

    public Cursor getAllDataGastronomia(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABELLA_GASTRONOMIA + " ORDER BY nome", null);
        return res;
    }

    public Cursor getAllDataRistorantiCitta(String citta){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT nome,categoria FROM " + TABELLA_GASTRONOMIA + " WHERE citta = ? ORDER BY nome", new String[]{citta});
        return res;
    }

    public Integer deleteGastronomia(String nome){
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(TABELLA_GASTRONOMIA, "nome = ?", new String[]{nome});
    }

    /*SEZIONE HOTEL&BB*/
    public boolean inserisciHotelBB(String nome,String nome_citta,String categoria){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentHotel = new ContentValues();
        contentHotel.put("nome",nome);
        contentHotel.put("citta", nome_citta);
        contentHotel.put("categoria",categoria);
        // contentValues1.put("citta_ID", i);
        long ins = db.insert(TABELLA_HOTELEBB, null, contentHotel);
        if(ins==-1) return  false;
        else return true;
    }

    public Cursor getAllDataHotelBB(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABELLA_HOTELEBB + " ORDER BY nome", null);
        return res;
    }

    public Cursor getAllDataHotelBBCitta(String citta){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT nome,categoria FROM " + TABELLA_HOTELEBB + " WHERE citta = ? ORDER BY nome", new String[]{citta});
        return res;
    }

    public Integer deleteHotelBB(String nome){
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(TABELLA_HOTELEBB, "nome = ?", new String[]{nome});
    }

    public void updateDatiUtente(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" DELETE  FROM " +  TABELLA_UTENTE);
    }

    public void updateDatiCitta(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" DELETE  FROM " +  TABELLA_CITTA);

    }

    public void updateDatiHotel(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" DELETE  FROM " +  TABELLA_HOTELEBB);
    }

    public void updateDatiMonumenti(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" DELETE  FROM " +  TABELLA_MONUMENTI);

    }

    public void updateDatiGastronomia(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" DELETE  FROM " +  TABELLA_GASTRONOMIA);
    }

    public void updateDatiViaggio(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" DELETE  FROM " +  TABELLA_VIAGGI);
    }

    public String getNome(String email){

        String nome ="";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT nome FROM " + TABELLA_UTENTE + " WHERE email = ?", new String[]{email});
        if(res!=null && res.getCount()>0){
            res.moveToFirst();
            nome = res.getString(0);
            return nome;
        }else{
            return nome;
        }
    }

    public String getCognome(String email){

        String cognome ="";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT cognome FROM " + TABELLA_UTENTE + " WHERE email = ?", new String[]{email});
        if(res!=null && res.getCount()>0){
            res.moveToFirst();
            cognome = res.getString(0);
            return cognome;
        }else{
            return cognome;
        }
    }

    /*Sezione Viaggi*/
    public boolean inserisciViaggio(String email,String nome_citta, String nome, String tipologia,String rating){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentViaggio = new ContentValues();
        contentViaggio.put("email", email);
        contentViaggio.put("citta", nome_citta);
        contentViaggio.put("nome", nome);
        contentViaggio.put("tipologia", tipologia);
        contentViaggio.put("rating", rating);
        long ins = db.insert(TABELLA_VIAGGI, null, contentViaggio);
        if(ins==-1) return  false;
        else return true;
    }

    public Cursor getAllDataViaggi(String email){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT DISTINCT citta FROM " + TABELLA_VIAGGI + " WHERE email = ? ORDER BY citta" , new String[]{email});
        return res;
    }

    public Cursor getAllViaggiMonumento(String citta, String email, String tipologia){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT nome,rating FROM " + TABELLA_VIAGGI + " WHERE citta = ? AND email = ? AND tipologia = ? ORDER BY nome" , new String[]{citta, email, tipologia});
        return res;
    }

    public Cursor getAllViaggiGastronomia(String citta, String email, String tipologia){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT nome,rating FROM " + TABELLA_VIAGGI + " WHERE citta = ? AND email = ? AND tipologia = ? ORDER BY nome" , new String[]{citta, email, tipologia});
        return res;
    }

    public Cursor getAllViaggiHotel(String citta, String email, String tipologia){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT nome,rating FROM " + TABELLA_VIAGGI + " WHERE citta = ? AND email = ? AND tipologia = ? ORDER BY nome" , new String[]{citta, email, tipologia});
        return res;
    }

    public Integer deleteViaggio (String citta, String email, String nome, String tipologia) {
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(TABELLA_VIAGGI, "nome = ? and citta = ? and email = ? and tipologia = ?", new String[]{nome, citta, email, tipologia});
    }

    /*Sezione coupon*/

    public Cursor getCouponUtente (String email) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT coupon FROM " + TABELLA_UTENTE + " WHERE email = ?", new String[]{email});
        return  res;
    }
    public Cursor getDataCouponMonumento (String citta) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT nome FROM " + TABELLA_MONUMENTI + " WHERE citta = ? ORDER BY RANDOM() LIMIT 2", new String[]{citta});
        return  res;
    }

    public Cursor getDataCouponGastronomia (String citta) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT nome FROM " + TABELLA_GASTRONOMIA + " WHERE citta = ? ORDER BY RANDOM() LIMIT 2", new String[]{citta});
        return  res;
    }

    public Cursor getDataCouponHotelBB (String citta) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT nome FROM " + TABELLA_HOTELEBB + " WHERE citta = ? ORDER BY RANDOM() LIMIT 2", new String[]{citta});
        return  res;
    }

    public String getCodiceFoto(String email){

        String codice ="";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT codiceFoto FROM " + TABELLA_UTENTE + " WHERE email = ?", new String[]{email});
        if(res!=null && res.getCount()>0){
            res.moveToFirst();
            codice = res.getString(0);
            return codice;
        }else{
            return codice;
        }
    }

}
