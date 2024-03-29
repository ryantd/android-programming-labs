package edu.stevens.cs522.bookstorebasicwithdb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

import edu.stevens.cs522.bookstorebasicwithdb.contracts.Book;

/**
 * Created by xiaoyuzhai on 2/11/15.
 */
public class CartDbAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_AUTHORS = "authors";
    public static final String KEY_ISBN = "isbn";
    public static final String KEY_PRICE = "price";

    public static final String KEY_AID = "_id";
    public static final String KEY_AUTHORNAME = "author";
    public static final String KEY_BOOKTITLE = "book";

    private static final String TAG = "CartDbAdapter";

    private DatabaseHelper cartDbHelper;
    private SQLiteDatabase cartDb;
    private final Context cartContext;

    private static final String DATABASE_NAME = "bookcart.db";
    private static final String BOOK_TABLE = "cart";
    private static final String AUTHOR_TABLE = "author";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "create table " + BOOK_TABLE + " (" + KEY_ROWID + " integer primary key autoincrement, "
                    + "title text, authors text, isbn text, price text);";
    private static final String DATABASE_CREATE2 =
            "create table " + AUTHOR_TABLE + " (" + KEY_AID + " integer primary key autoincrement, "
                    + "author text, book text);";

    public CartDbAdapter(Context ctx) {
        this.cartContext = ctx;
        cartDbHelper = new DatabaseHelper(cartContext);
    }

    public CartDbAdapter open() throws SQLException {
        cartDb = cartDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        cartDbHelper.close();
    }

    public Cursor fetchAllBooks() {
        return cartDb.query(BOOK_TABLE, new String[] {KEY_ROWID, KEY_TITLE, KEY_AUTHORS,
                KEY_ISBN, KEY_PRICE}, null, null, null, null, null);
    }

    public Book fetchBook(long rowId) throws SQLException {
        Cursor mCursor =
                cartDb.query(true, BOOK_TABLE, new String[] {KEY_ROWID,
                                KEY_TITLE, KEY_AUTHORS, KEY_ISBN, KEY_PRICE}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return new Book(mCursor);
    }

    public Cursor fetchBook(Book book) throws SQLException
    {
        Cursor mCursor =
                cartDb.query(true, BOOK_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
                                KEY_AUTHORS, KEY_ISBN, KEY_PRICE}, KEY_TITLE + "='" + book.title + "'", null,
                        null, null, null, null);
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void persist(Book book)throws SQLException
    {
        ContentValues initialValues = new ContentValues();
        book.writeToProvider(initialValues);
        cartDb.insert(BOOK_TABLE, null, initialValues);
    }

    public void persist(String title, String authors, String isbn, String price) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_AUTHORS, authors);
        initialValues.put(KEY_ISBN, isbn);
        initialValues.put(KEY_PRICE, price);
        cartDb.insert(BOOK_TABLE, null, initialValues);
    }

    public boolean deleteAll() {
        cartDb.delete(BOOK_TABLE, null, null);
        return true;
    }

    public boolean deleteBook(Book book)
    {
        return cartDb.delete(BOOK_TABLE, KEY_TITLE + "='" + book.title + "'", null) > 0;
    }

    public boolean deleteBook(long id)
    {
        return cartDb.delete(BOOK_TABLE, KEY_ROWID+ "=" + id, null) > 0;
    }

    public Cursor fetchAllAuthors() {
        return cartDb.query(AUTHOR_TABLE, new String[] {KEY_AID, KEY_AUTHORNAME, KEY_BOOKTITLE}, null, null, null, null, null);
    }

    public Book fetchAuthor(long rowId) throws SQLException {
        Cursor mCursor =
                cartDb.query(true, AUTHOR_TABLE, new String[] {KEY_AID, KEY_AUTHORNAME, KEY_BOOKTITLE}, KEY_AID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return new Book(mCursor);
    }

    public Cursor fetchAuthorBook(String name) throws SQLException
    {
        Cursor mCursor =
                cartDb.query(true, AUTHOR_TABLE, new String[] {KEY_AID, KEY_AUTHORNAME, KEY_BOOKTITLE}, KEY_AUTHORNAME + "='" + name + "'", null,
                        null, null, null, null);
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void addAuthor(String name, String book) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_AUTHORNAME, name);
        initialValues.put(KEY_BOOKTITLE, book);
        cartDb.insert(BOOK_TABLE, null, initialValues);
    }

    public boolean deleteAllAuthor() {
        cartDb.delete(AUTHOR_TABLE, null, null);
        return true;
    }

    public boolean deleteAuthor(String name)
    {
        return cartDb.delete(AUTHOR_TABLE, KEY_AUTHORNAME + "='" + name + "'", null) > 0;
    }

    public boolean deleteAuthor(long id)
    {
        return cartDb.delete(AUTHOR_TABLE, KEY_AID+ "=" + id, null) > 0;
    }




    public static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE2);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS cart");
            db.execSQL("DROP TABLE IF EXISTS author");
            db.execSQL("PRAGMA foreign_keys=ON;");
            onCreate(db);
        }
    }
}