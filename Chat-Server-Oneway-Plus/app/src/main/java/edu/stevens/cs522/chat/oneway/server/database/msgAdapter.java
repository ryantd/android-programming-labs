package edu.stevens.cs522.chat.oneway.server.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

import edu.stevens.cs522.chat.oneway.server.entity.Messages;
import edu.stevens.cs522.chat.oneway.server.entity.Peer;


/**
 * Created by Xiaoyu on 2/7/2015.
 */
public class msgAdapter{

    public static final String KEY_MSGID = "_id";
    public static final String KEY_TEXT = "content";
    public static final String KEY_SENDER = "sender";

    public static final String KEY_PEERID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_PORT = "port";

    private static final String TAG = "msgDbAdapter";

    private DatabaseHelper msgDbHelper;
    private SQLiteDatabase msgDb;
    private final Context msgContext;

    private static final String DATABASE_NAME = "msg.db";
    private static final String MSG_TABLE = "msg";
    private static final int DATABASE_VERSION = 1;
    private static final String PEER_TABLE = "peer";


    private static final String DATABASE_CREATE =
            "create table " + MSG_TABLE + " (" + KEY_MSGID + " integer primary key autoincrement, "
                    + "content text, sender text);";
    private static final String DATABASE_CREATE2 =
            "create table " + PEER_TABLE + " (" + KEY_PEERID + " integer primary key autoincrement, "
                    + "name text, address text, port text);";

    public msgAdapter(Context ctx) {
        this.msgContext = ctx;
        msgDbHelper = new DatabaseHelper(msgContext);
    }

    public msgAdapter open() throws SQLException {
        msgDb = msgDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        msgDbHelper.close();
    }

    public Cursor fetchAllMsgs() {
        return msgDb.query(MSG_TABLE, new String[] {KEY_MSGID, KEY_TEXT, KEY_SENDER}, null, null, null, null, null);
    }

    public Messages fetchMsg(long msgId) throws SQLException {
        Cursor mCursor =
                msgDb.query(true, MSG_TABLE, new String[] {KEY_MSGID, KEY_TEXT, KEY_SENDER}, KEY_MSGID + "=" + msgId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return new Messages(mCursor);
    }

    public Cursor fetchMsg(Messages msg) throws SQLException
    {
        Cursor mCursor =
                msgDb.query(true, MSG_TABLE, new String[] {KEY_MSGID, KEY_TEXT, KEY_SENDER}, KEY_TEXT + "='" + msg.text + "'", null,
                        null, null, null, null);
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchMsgbyName(Messages msg) throws SQLException
    {
        Cursor mCursor =
                msgDb.query(true, MSG_TABLE, new String[] {KEY_MSGID, KEY_TEXT, KEY_SENDER}, KEY_SENDER + "='" + msg.sender + "'", null,
                        null, null, null, null);
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void persist(Messages msg)throws SQLException
    {
        ContentValues initialValues = new ContentValues();
        msg.writeToProvider(initialValues);
        msgDb.insert(MSG_TABLE, null, initialValues);
    }

    public void persist(String text, String sender) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TEXT, text);
        initialValues.put(KEY_SENDER, sender);
        msgDb.insert(MSG_TABLE, null, initialValues);
    }

    public boolean deleteAll() {
        msgDb.delete(MSG_TABLE, null, null);
        return true;
    }

    public boolean deleteMsg(Messages msg)
    {
        return msgDb.delete(MSG_TABLE, KEY_TEXT + "='" + msg.text + "'", null) > 0;
    }

    public boolean deleteMsg(long id)
    {
        return msgDb.delete(MSG_TABLE, KEY_MSGID+ "=" + id, null) > 0;
    }



    public Cursor fetchAllPeers() {
        return msgDb.query(PEER_TABLE, new String[] {KEY_PEERID, KEY_NAME, KEY_ADDRESS, KEY_PORT}, null, null, null, null, null);
    }

    public Peer fetchPeer(long peerId) throws SQLException {
        Cursor mCursor =
                msgDb.query(true, PEER_TABLE, new String[] {KEY_PEERID, KEY_NAME, KEY_ADDRESS, KEY_PORT}, KEY_PEERID + "=" + peerId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return new Peer(mCursor);
    }

    public Cursor fetchPeer(Peer peer) throws SQLException
    {
        Cursor mCursor =
                msgDb.query(true, PEER_TABLE, new String[] {KEY_PEERID, KEY_NAME, KEY_ADDRESS, KEY_PORT}, KEY_NAME + "='" + peer.name + "'", null,
                        null, null, null, null);
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insertPeer(Peer peer)throws SQLException
    {
        ContentValues initialValues = new ContentValues();
        peer.writeToProvider(initialValues);
        msgDb.insert(PEER_TABLE, null, initialValues);
    }

    public void insertPeer(String name, String address, int port) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_ADDRESS, address);
        initialValues.put(KEY_PORT, port);
        msgDb.insert(PEER_TABLE, null, initialValues);
    }

    public boolean deleteAllPeers() {
        msgDb.delete(PEER_TABLE, null, null);
        return true;
    }

    public boolean deletePeer(Peer peer)
    {
        return msgDb.delete(PEER_TABLE, KEY_NAME + "='" + peer.name + "'", null) > 0;
    }

    public boolean deletePeer(long id)
    {
        return msgDb.delete(PEER_TABLE, KEY_PEERID + "=" + id, null) > 0;
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
            db.execSQL("DROP TABLE IF EXISTS msg");
            db.execSQL("DROP TABLE IF EXISTS peer");
            onCreate(db);
        }
    }
}