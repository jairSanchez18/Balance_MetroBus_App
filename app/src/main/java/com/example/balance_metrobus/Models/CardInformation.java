package com.example.balance_metrobus.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.balance_metrobus.Adapter.AdapterCard;
import com.example.balance_metrobus.BDHelper.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class CardInformation {
    private String cardnumber;
    private String cardbalance;
    private String cardstatus;
    private String carddate;
    private String cardtype;
    private String cardnumberupdate;
    private String cardname;

    public String getCardnumberupdate() {
        return cardnumberupdate;
    }

    public void setCardnumberupdate(String cardnumberupdate) {
        this.cardnumberupdate = cardnumberupdate;
    }

    public CardInformation(String cardnumber, String cardbalance, String cardstatus, String carddate, String cardtype, String cardname) {
        this.cardnumber = cardnumber;
        this.cardbalance = cardbalance;
        this.cardstatus = cardstatus;
        this.carddate = carddate;
        this.cardtype = cardtype;
        this.cardname = cardname;
    }

    public CardInformation() {

    }

    public CardInformation(String cardnumberupdate) {
        this.cardnumberupdate = cardnumberupdate;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public String getCardbalance() {
        return cardbalance;
    }

    public void setCardbalance(String cardbalance) {
        this.cardbalance = cardbalance;
    }

    public String getCardstatus() {
        return cardstatus;
    }

    public void setCardstatus(String cardstatus) {
        this.cardstatus = cardstatus;
    }

    public String getCarddate() {
        return carddate;
    }

    public void setCarddate(String carddate) {
        this.carddate = carddate;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public boolean insert(Context context) {
        try {
            //Validacion de campos en nulo o vacio
            DbHelper dbHelper = new DbHelper(context, "CardDB");
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            if (db != null) {
                ContentValues content = new ContentValues();

                content.put("numero", this.getCardnumber());
                content.put("saldo", this.getCardbalance());
                content.put("estado", this.getCardstatus());
                content.put("fecha", this.getCarddate());
                content.put("tipo", this.getCardtype());
                content.put("nombre", this.getCardname());
                db.insert("cards", null, content);
                return true;
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error" + e.getMessage(), Toast.LENGTH_SHORT);
        }
        return false;
    }

    public List<CardInformation> GetCards(Context context) {
        List<CardInformation> Cards = new ArrayList<>();
        try {
            //Validacion de campos en nulo o vacio
            DbHelper dbHelper = new DbHelper(context, "CardDB");
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            if (db != null) {
                String[] campos = new String[]{"numero", "saldo", "estado", "fecha", "tipo", "nombre"};
                Cursor cursor = db.query("cards", campos, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        Cards.add(new CardInformation(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                                cursor.getString(3), cursor.getString(4), cursor.getString(5)));

                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error al cargar tarjetas", Toast.LENGTH_SHORT);
        }
        return Cards;
    }

    public void DeleteCards(Context context, String number) {
        try {
            DbHelper dbHelper = new DbHelper(context, "CardDB");
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            db.execSQL("DELETE FROM cards WHERE numero = '" + number + "'");

        } catch (Exception e) {
            Toast.makeText(context, "Error al eliminar tarjetas", Toast.LENGTH_SHORT);
        }
    }

    public Boolean ValidateCards(Context context, String cardnumber) {
        List<String> Cards = new ArrayList<>();
        try {
            //Validacion de campos en nulo o vacio
            DbHelper dbHelper = new DbHelper(context, "CardDB");
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            if (db != null) {
                Cursor cursor = db.query("cards", new String[]{"numero"}, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        if (cursor.getString(0).equals(cardnumber)) {
                            return true;
                        }
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error al cargar tarjetas", Toast.LENGTH_SHORT);
        }
        return false;
    }

    public void Update(Context context, String balance) {
        try {
            DbHelper dbHelper = new DbHelper(context, "CardDB");
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            db.execSQL("UPDATE cards SET saldo='" + balance + "' WHERE numero = '" + cardnumberupdate + "'");

            Toast.makeText(context, "Tarjeta Actualizada: " + cardnumberupdate, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(context, "Error al eliminar tarjetas", Toast.LENGTH_SHORT);
        }
    }
}
