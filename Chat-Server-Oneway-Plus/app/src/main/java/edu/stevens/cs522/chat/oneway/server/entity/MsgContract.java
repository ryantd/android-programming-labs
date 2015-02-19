package edu.stevens.cs522.chat.oneway.server.entity;

import android.content.ContentValues;
import android.database.Cursor;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by xiaoyuzhai on 2/15/15.
 */
public class MsgContract {
    public static final String MSGID = "_id";
    public static final String TEXT = "content";
    public static final String SENDER = "sender";
    public static final String PEERID = "_id";
    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String PORT = "port";

    public static int getMsgid(Cursor cursor) {
        return Integer.getInteger(cursor.getString(cursor.getColumnIndexOrThrow(MSGID)));
    }
    public static String getText(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(TEXT));
    }
    public static String getSender(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(SENDER));
    }
    public static int getPeerid(Cursor cursor) {
        return Integer.getInteger(cursor.getString(cursor.getColumnIndexOrThrow(PEERID)));
    }
    public static String getName(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(NAME));
    }
    public static String getAddress(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(ADDRESS));
    }
    public static int getPort(Cursor cursor) {
        return Integer.getInteger(cursor.getString(cursor.getColumnIndexOrThrow(PORT)));
    }

    public static void putMsgid( ContentValues values, long id)
    {
        values.put(MSGID, id);
    }
    public static void putText( ContentValues values, String text)
    {
        values.put(TEXT, text);
    }
    public static void putSender( ContentValues values, String sender)
    {
        values.put(SENDER, sender);
    }
    public static void putPeerid( ContentValues values, long id)
    {
        values.put(PEERID, id);
    }
    public static void putName( ContentValues values, String name)
    {
        values.put(NAME, name);
    }
    public static void putAddress( ContentValues values, String address)
    {
        values.put(ADDRESS, address);
    }
    public static void putPort( ContentValues values, int port)
    {
        values.put(PORT, port);
    }
}
