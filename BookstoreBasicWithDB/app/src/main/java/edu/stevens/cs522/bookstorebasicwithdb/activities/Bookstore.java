package edu.stevens.cs522.bookstorebasicwithdb.activities;

import java.sql.SQLException;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


import edu.stevens.cs522.bookstorebasicwithdb.R;
import edu.stevens.cs522.bookstorebasicwithdb.contracts.Book;
import edu.stevens.cs522.bookstorebasicwithdb.contracts.BookContract;
import edu.stevens.cs522.bookstorebasicwithdb.database.CartDbAdapter;

public class Bookstore extends ListActivity {

    // Use this when logging errors and warnings.
    @SuppressWarnings("unused")
    private static final String TAG = Bookstore.class.getCanonicalName();

    // These are request codes for subactivity request calls
    static final private int ADD_REQUEST = 1;

    @SuppressWarnings("unused")
    static final private int CHECKOUT_REQUEST = ADD_REQUEST + 1;

    // There is a reason this must be an ArrayList instead of a List.
    @SuppressWarnings("unused")
    private Vector<Integer> deletePool = new Vector<Integer>();
    CartDbAdapter cartDb = new CartDbAdapter(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
    }
    @Override
    protected void onStart (){
        super.onStart();
        try {
            cartDb.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final Cursor allBooks = cartDb.fetchAllBooks();

        if (allBooks.moveToFirst()) {
            TextView emptyText = (TextView) findViewById(R.id.cart_empty_text);
            emptyText.setVisibility(View.GONE);
            String[] from = new String[]{BookContract.TITLE, BookContract.AUTHORS};
            int[] to = new int[]{R.id.cart_row_title, R.id.cart_row_author};
            final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.cart_row, allBooks, from, to);
            ListView listView = (ListView) findViewById(android.R.id.list);
            listView.setAdapter(adapter);
            listView.setLongClickable(true);
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position,
                                               long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("Sub Menu")
                            .setCancelable(false)
                            .setPositiveButton("More Info", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    TextView title = (TextView) view.findViewById(R.id.cart_row_title);
                                    Book longClicked = new Book(title.getText().toString(), "", "", "");
                                    try {
                                        cartDb.open();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    Cursor oneBook = cartDb.fetchAllBooks();
                                    try {
                                        oneBook = cartDb.fetchBook(longClicked);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    oneBook.moveToFirst();
                                    String text = "Title: " + oneBook.getString(1) + "\nAuthor: " + oneBook.getString(2) + "\nISBN: " + oneBook.getString(3) + "\nPrice: " + oneBook.getString(4);
                                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                                    cartDb.close();
                                }
                            })
                            .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    try {
                                        cartDb.open();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    TextView title = (TextView) view.findViewById(R.id.cart_row_title);
                                    Book deleteBook = new Book(title.getText().toString(), "", "", "");
                                    cartDb.deleteBook(deleteBook);
                                    adapter.changeCursor(cartDb.fetchAllBooks());
                                    cartDb.close();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    return true;
                }
            });
        }


        cartDb.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bookstore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.add:
                Intent addIntent = new Intent(this, AddBook.class);
                startActivityForResult(addIntent, ADD_REQUEST);
                return true;
            case R.id.checkout:
                Intent chkIntent = new Intent(this, Checkout.class);
                startActivityForResult(chkIntent, CHECKOUT_REQUEST);
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode) {
            case (ADD_REQUEST):
                if (resultCode == Activity.RESULT_OK) {
                    Bundle data = intent.getExtras();
                    Book newBook = (Book) data.getParcelable("book_result");
                    try {
                        cartDb.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    cartDb.persist(newBook.title, newBook.authors, newBook.isbn, newBook.price);
                    cartDb.close();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("User added a new book, and the title is " + newBook.title + ", the authors are " + newBook.authors)
                            .setCancelable(false)
                            .setTitle("Details")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(), "User canceled the \"SEARCH & ADD\" ACTION.", Toast.LENGTH_LONG).show();
                }
                break;
            case (CHECKOUT_REQUEST):
                if (resultCode == Activity.RESULT_OK) {
                    String orderInfo = intent.getStringExtra("orderInfo");
                    try {
                        cartDb.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    cartDb.deleteAll();
                    cartDb.close();
                    onStart();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(orderInfo)
                            .setCancelable(false)
                            .setTitle("Order Details")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    onCreate(null);

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(), "User canceled the \"CHECK OUT\" ACTION.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}