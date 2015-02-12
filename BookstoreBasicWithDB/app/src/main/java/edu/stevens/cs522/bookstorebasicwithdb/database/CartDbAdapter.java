package edu.stevens.cs522.bookstorebasicwithdb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by xiaoyuzhai on 2/11/15.
 */
public class CartDbAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_ISBN = "isbn";
    public static final String KEY_PRICE = "price";

    private static final String TAG = "BookStoreDbAdapter";

    private DatabaseHelper cartDbHelper;
    private SQLiteDatabase cartDb;

    private static final String DATABASE_NAME = "bookcart.db";
    private static final String DATABASE_TABLE = "cart";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " (_id integer primary key autoincrement, "
                    + "title text, author text, isbn text, price text);";

    private final Context cartContext;

    public CartDbAdapter(Context ctx) {
        this.cartContext = ctx;
    }

    public CartDbAdapter open() throws SQLException {
        cartDbHelper = new DatabaseHelper(cartContext);
        cartDb = cartDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        cartDbHelper.close();
    }

    public Cursor fetchAllItems() {
        return cartDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE, KEY_AUTHOR,
                KEY_ISBN, KEY_PRICE}, null, null, null, null, null);
    }

    public Cursor fetchItem(long rowId) throws SQLException {

        Cursor mCursor =
                cartDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_TITLE, KEY_AUTHOR, KEY_ISBN, KEY_PRICE}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public long createItem(String title, String author, String isbn, String price) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_AUTHOR, author);
        initialValues.put(KEY_ISBN, isbn);
        initialValues.put(KEY_PRICE, price);

        return cartDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteAll() {
        // TODO: Delete all records in the shopping cart.
        cartDb.delete(DATABASE_TABLE, null, null);
        return true;
    }

    public boolean deleteItem(long rowId) {
        String[] whereArgs = {""+rowId};
        return cartDb.delete(DATABASE_TABLE, KEY_ROWID+ "=?", whereArgs) > 0;
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS cart");
            onCreate(db);
        }
    }
}