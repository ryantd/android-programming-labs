/*********************************************************************

 Chat server: accept chat messages from clients.

 Sender name and GPS coordinates are encoded
 in the messages, and stripped off upon receipt.

 Copyright (c) 2012 Stevens Institute of Technology

 **********************************************************************/
package edu.stevens.cs522.chat.oneway.server.activity;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.SQLException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import edu.stevens.cs522.chat.oneway.server.activity.R;
import edu.stevens.cs522.chat.oneway.server.database.msgAdapter;
import edu.stevens.cs522.chat.oneway.server.entity.Messages;
import edu.stevens.cs522.chat.oneway.server.entity.MsgContract;
import edu.stevens.cs522.chat.oneway.server.entity.Peer;

public class ChatServer extends Activity implements OnClickListener {

    final static public String TAG = ChatServer.class.getCanonicalName();

    /*
     * Socket used both for sending and receiving
     */
    private DatagramSocket serverSocket;

    /*
     * True as long as we don't get socket errors
     */
    private boolean socketOK = true;

	/*
     * TODO: Declare UI.
	 */
    Button nextButton;
	/*
	 * End Todo
	 */
    msgAdapter msgDb = new msgAdapter(this);
    /*
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        /**
         * Let's be clear, this is a HACK to allow you to do network communication on the main thread.
         * This WILL cause an ANR, and is only provided to simplify the pedagogy.  We will see how to do
         * this right in a future assignment (using a Service managing background threads).
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
			/*
			 * Get port information from the resources.
			 */
            int port = Integer.parseInt(this.getString(R.string.app_port));
            serverSocket = new DatagramSocket(port);
        } catch (Exception e) {
            Log.e(TAG, "Cannot open socket" + e.getMessage());
            return;
        }


        nextButton = (Button) findViewById(R.id.next);


    }

    public void onClick(View v) {

        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        try {

            serverSocket.receive(receivePacket);
            Log.i(TAG, "Received a packet");

            InetAddress sourceIPAddress = receivePacket.getAddress();
            Log.i(TAG, "Source IP Address: " + sourceIPAddress);
			
			/*
			 * TODO: Extract sender and receiver from message and display.
			 */
            String msgContent[] = new String(receivePacket.getData(), 0, receivePacket.getLength()).split(":");
			String clientName = msgContent[0];
            String content = msgContent[1];
            InetAddress clientHost = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            try {
                msgDb.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Messages newMsg =  new Messages(content, clientName);
            Peer newPeer = new Peer(clientName, clientHost.getCanonicalHostName(), clientPort);
            msgDb.persist(newMsg.text, newMsg.sender);

            Peer chk = new Peer(newPeer.name, "", 0);
            Cursor chkPeer = msgDb.fetchPeer(chk);
            if (!chkPeer.moveToFirst())
                msgDb.insertPeer(newPeer.name, newPeer.address.getCanonicalHostName(), newPeer.port);

            Cursor allMsgs = msgDb.fetchAllMsgs();

            if (allMsgs.moveToFirst()) {
                String[] from = new String[]{MsgContract.SENDER, MsgContract.TEXT};
                int[] to = new int[]{R.id.msg_list_name, R.id.msg_list_content};
                final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.message, allMsgs, from, to);
                ListView listView = (ListView) findViewById(android.R.id.list);
                listView.setAdapter(adapter);
            }
			msgDb.close();

        } catch (Exception e) {

            Log.e(TAG, "Problems receiving packet: " + e.getMessage());
            socketOK = false;
        }

    }

    /*
     * Close the socket before exiting application
     */
    public void closeSocket() {
        serverSocket.close();
    }

    /*
     * If the socket is OK, then it's running
     */
    boolean socketIsOK() {
        return socketOK;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.peer_list:
                Intent peerslist = new Intent(this, PeersList.class);
                startActivity(peerslist);
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_peers, menu);
        return true;
    }

}