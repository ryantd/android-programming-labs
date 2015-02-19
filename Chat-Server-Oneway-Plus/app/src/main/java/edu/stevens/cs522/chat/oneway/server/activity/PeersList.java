package edu.stevens.cs522.chat.oneway.server.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.sql.SQLException;

import edu.stevens.cs522.chat.oneway.server.activity.R;
import edu.stevens.cs522.chat.oneway.server.database.msgAdapter;
import edu.stevens.cs522.chat.oneway.server.entity.MsgContract;
import edu.stevens.cs522.chat.oneway.server.entity.Peer;

public class PeersList extends Activity {


    msgAdapter msgDb = new msgAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peerslist);

        try {
            msgDb.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cursor allPeers = msgDb.fetchAllPeers();

        if (allPeers.moveToFirst()) {
            String[] from = new String[]{MsgContract.NAME, MsgContract.ADDRESS};
            int[] to = new int[]{R.id.peer_list_name, R.id.peer_list_content};
            final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.peer, allPeers, from, to);
            ListView listView = (ListView) findViewById(android.R.id.list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    TextView peerName = (TextView) view.findViewById(R.id.peer_list_name);
                    Intent intent = new Intent(getBaseContext(), PeerInfo.class);
                    intent.putExtra("NAMEDATA", peerName.getText().toString());
                    startActivity(intent);
                }
            });
        }

        msgDb.close();


    }

}
