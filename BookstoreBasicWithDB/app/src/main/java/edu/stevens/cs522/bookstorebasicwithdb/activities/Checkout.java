package edu.stevens.cs522.bookstorebasicwithdb.activities;

import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.widget.EditText;

        import edu.stevens.cs522.bookstorebasicwithdb.R;

public class Checkout extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // TODO display ORDER and CANCEL options.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.checkout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        // TODO

        // ORDER: display a toast message of how many books have been ordered and return

        // CANCEL: just return with REQUEST_CANCELED as the result code
        switch (item.getItemId()) {
            case R.id.chk_order:
                String info = order();
                Intent ok_data = new Intent();
                ok_data.putExtra("orderInfo", info);
                setResult(RESULT_OK, ok_data);
                finish();
                return true;
            case R.id.chk_cancel:
                setResult(RESULT_CANCELED, null);
                finish();
                return true;
        }
        return false;
    }

    public String order(){
		/*
		 * Search for the specified book.
		 */
        EditText billName = (EditText) findViewById(R.id.name);
        EditText billEmail = (EditText) findViewById(R.id.email);
        EditText billCredit = (EditText) findViewById(R.id.credit);
        EditText billAddress = (EditText) findViewById(R.id.address);
        String nameStr = billName.getText().toString();
        String emailStr = billEmail.getText().toString();
        String creditStr = billCredit.getText().toString();
        String addressStr = billAddress.getText().toString();

        String text = "Name: " + nameStr + "\n" + "Email: " + emailStr + "\n" + "CreditCard: " + creditStr + "\n" + "Address: " + addressStr;
        return text;
    }

}
