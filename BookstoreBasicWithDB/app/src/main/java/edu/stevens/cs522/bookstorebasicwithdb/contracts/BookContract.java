package edu.stevens.cs522.bookstorebasicwithdb.contracts;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by xiaoyuzhai on 2/11/15.
 */
public class BookContract {

    public static final String ROWID = "_id";
    public static final String TITLE = "title";
    public static final String AUTHORS = "authors";
    public static final String ISBN = "isbn";
    public static final String PRICE = "price";

    public static int getRowid(Cursor cursor) {
        return Integer.getInteger(cursor.getString(cursor.getColumnIndexOrThrow(ROWID)));
    }
    public static String getTitle(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
    }
    public static String getAuthors(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(AUTHORS));
    }
    public static String getIsbn(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(ISBN));
    }
    public static String getPrice(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(PRICE));
    }

    public static void putRowid( ContentValues values, int id)
    {
        values.put(ROWID, id);
    }
    public static void putTitle( ContentValues values, String title)
    {
        values.put(TITLE, title);
    }
    public static void putAuthors( ContentValues values, String authors)
    {
        values.put(AUTHORS, authors);
    }
    public static void putIsbn( ContentValues values, String isbn)
    {
        values.put(ISBN, isbn);
    }
    public static void putPrice( ContentValues values, String price)
    {
        values.put(PRICE, price);
    }
}
