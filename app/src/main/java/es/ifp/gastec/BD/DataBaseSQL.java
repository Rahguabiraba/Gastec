package es.ifp.gastec.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseSQL extends SQLiteOpenHelper {

    protected SQLiteDatabase db;

    public DataBaseSQL(Context context) {
        super(context, "login", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE table users(username TEXT primary key, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        this.onCreate(db);
    }

    // ---- Los métodos abajo han sido utilizados con el sistema CRUD ----

    //Métodos para el login con usuario y contrasena
    public Boolean insertData(String username, String password) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put("username", username);
        values.put("password", password);

        long result = db.insert("users", null, values);
        if(result==-1) {
            return false;
        }
        else{
            return true;
        }
    }

    public Boolean checkusername(String username) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username=?", new String[] {username});
        if(cursor.getCount()>0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkusernamepassword(String username, String password) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?", new String[] {username, password});
        if(cursor.getCount()>0) {
            return true;
        } else {
            return false;
        }
    }

    public void close() {
        db.close();
    }

}
