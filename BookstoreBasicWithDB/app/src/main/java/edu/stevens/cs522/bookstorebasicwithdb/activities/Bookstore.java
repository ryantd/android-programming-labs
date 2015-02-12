package edu.stevens.cs522.bookstorebasicwithdb.activities;

import java.util.ArrayList;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import edu.stevens.cs522.bookstorebasicwithdb.R;
import edu.stevens.cs522.bookstorebasicwithdb.contracts.Book;
import edu.stevens.cs522.bookstorebasicwithdb.database.BooksAdapter;

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
    private ArrayList<Book> shoppingCart;
    private Vector<Integer> deletePool = new Vector<Integer>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO check if there is saved UI state, and if so, restore it (i.e. the cart contents)
        if (savedInstanceState != null) {
            ArrayList<Book> MyArrayList = savedInstanceState.getParcelableArrayList("MyArrayList");
            shoppingCart = MyArrayList;
        }
        else {
            shoppingCart = new ArrayList<Book>();
        }

        // TODO Set the layout (use cart.xml layout)

        setContentView(R.layout.cart);

        // TODO use an array adapter to display the cart contents.

    }
    @Override
    protected void onStart (){
        super.onStart();
        ArrayList<Book> arrayOfBooks = shoppingCart;
        if (!arrayOfBooks.isEmpty()){
            TextView emptyText = (TextView) findViewById(R.id.cart_empty_text);
            emptyText.setVisibility(View.GONE);
        }
        final BooksAdapter adapter = new BooksAdapter(this, arrayOfBooks);
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
                                String authorName = shoppingCart.get(position).authors[0].lastName + " " + shoppingCart.get(position).authors[1].lastName + ", " + shoppingCart.get(position).authors[2].lastName;
                                String text = "Title: " + shoppingCart.get(position).title + "\nAuthor: " + authorName + "\nISBN: " + shoppingCart.get(position).isbn + "\nPrice: " + shoppingCart.get(position).price;
                                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                shoppingCart.remove(position);
                                adapter.notifyDataSetChanged();
                                if (shoppingCart.isEmpty()) {
                                    TextView emptyText = (TextView) findViewById(R.id.cart_empty_text);
                                    emptyText.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                view.setSelected(true);
                deletePool.add(position);

                TextView title = (TextView) findViewById(R.id.cart_row_title);
                TextView author = (TextView) findViewById(R.id.cart_row_author);
                title.setTextColor(Color.RED);
                author.setTextColor(Color.RED);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // TODO provide ADD, DELETE and CHECKOUT options
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bookstore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        // TODO

        // ADD provide the UI for adding a book
        // Intent addIntent = new Intent(this, AddBookActivity.class);
        // startActivityForResult(addIntent, ADD_REQUEST);

        // DELETE delete the currently selected book

        // CHECKOUT provide the UI for checking out

        switch (item.getItemId()) {
            case R.id.add:
                Intent addIntent = new Intent(this, AddBook.class);
                startActivityForResult(addIntent, ADD_REQUEST);
                return true;
            case R.id.delete:
                for (int i = 0; i < deletePool.size(); i++){
                    int pos = deletePool.get(i);
                    shoppingCart.remove(pos);
                }
                deletePool.clear();
                onStart();
                if (shoppingCart.isEmpty()) {
                    TextView emptyText = (TextView) findViewById(R.id.cart_empty_text);
                    emptyText.setVisibility(View.VISIBLE);
                }
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
        // TODO Handle results from the Search and Checkout activities.

        // Use SEARCH_REQUEST and CHECKOUT_REQUEST codes to distinguish the cases.

        // SEARCH: add the book that is returned to the shopping cart.

        // CHECKOUT: empty the shopping cart.

        switch(requestCode) {
            case (ADD_REQUEST):
                if (resultCode == Activity.RESULT_OK) {
                    Bundle data = intent.getExtras();
                    Book newBook = (Book) data.getParcelable("book_result");
                    shoppingCart.add(newBook);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("User added a new book, and the title is " + newBook.title + ", the author is " + newBook.authors[0].lastName + " " + newBook.authors[1].lastName + ". " + newBook.authors[2].lastName)
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
                    shoppingCart.clear();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(orderInfo)
                            .setCancelable(false)
                            .setTitle("Order Details")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(getIntent());
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // TODO save the shopping cart contents (which should be a list of parcelables).
        savedInstanceState.putParcelableArrayList("MyArrayList", shoppingCart);
    }

}