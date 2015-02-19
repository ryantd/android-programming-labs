package edu.stevens.cs522.chat.oneway.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class ClientName extends Activity {

    public void goNext(View view) {
        Intent intent = new Intent(this, ChatClient.class);
        EditText text1 = (EditText) findViewById(R.id.editText);
        EditText text2 = (EditText) findViewById(R.id.editText2);
        String name = text1.getText().toString();
        int port = Integer.parseInt(text2.getText().toString());
        intent.putExtra("client_name", name);
        intent.putExtra("client_port", port);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_name);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client_name, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
