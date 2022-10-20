package es.ifp.gastec.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import es.ifp.gastec.R;

public class DataBaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "transactions.database";
    private static int DATABASE_VERSION = 1;
    protected SQLiteDatabase database;

    public DataBaseHelper (@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE table controlTransactions(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, title TEXT, amount FLOAT, type TEXT, tag TEXT, date DATETIME, note TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS controlTransactions");
        this.onCreate(database);
    }

    public void addTransaction(String title, float amount, String type, String tag, String date ,String note) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("title", title);
        cv.put("amount", amount);
        cv.put("type", type);
        cv.put("tag", tag);
        cv.put("date", date);
        cv.put("note", note);

        long result = database.insert("controlTransactions", null, cv);
        if (result == -1) {
            Toast.makeText(context, R.string.toast1_add_transaction, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.toast2_add_transaction, Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllData() {
        String query = "SELECT * FROM controlTransactions";
        SQLiteDatabase dataBase = this.getReadableDatabase();

        Cursor cursor = null;
        if (dataBase != null) {
            cursor = dataBase.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateData(String row_id, String title, float amount, String type, String tag, String date, String note) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("amount", amount);
        cv.put("type", type);
        cv.put("tag", tag);
        cv.put("date", date);
        cv.put("note", note);

        long result = database.update("controlTransactions",cv,"id=?", new String[] {row_id});
        if (result == -1) {
            Toast.makeText(context, R.string.toast1_update_transaction, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.toast2_update_transaction, Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOneRow(String row_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        long result = database.delete("controlTransactions","id=?",new String[] {row_id});

        if (result == -1) {
            Toast.makeText(context, R.string.toast1_delete_transaction, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.toast2_delete_transaction, Toast.LENGTH_SHORT).show();
        }
    }

    public void close() {
        database.close();
    }
}
