package edu.stevens.cs522.chat.oneway.server.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.sql.SQLException;

import edu.stevens.cs522.chat.oneway.server.activity.R;
import edu.stevens.cs522.chat.oneway.server.database.msgAdapter;
import edu.stevens.cs522.chat.oneway.server.entity.Messages;
import edu.stevens.cs522.chat.oneway.server.entity.MsgContract;
import edu.stevens.cs522.chat.oneway.server.entity.Peer;

public class PeerInfo extends Activity {

    msgAdapter msgDb = new msgAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peerinfo);

        try {
            msgDb.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        String name = intent.getStringExtra("NAMEDATA");
        Messages clickedPeer = new Messages("", name);

        Cursor onePeer = msgDb.fetchAllPeers();
        try {
            onePeer = msgDb.fetchMsgbyName(clickedPeer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (onePeer.moveToFirst()) {
            do{
                String[] from = new String[]{MsgContract.TEXT};
                int to[] = new int[]{R.id.info_list_content};
                final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.info, onePeer, from, to);
                ListView listView = (ListView) findViewById(android.R.id.list);
                listView.setAdapter(adapter);
            }while (onePeer.moveToNext());
        }
        msgDb.close();
    }

}
