package edu.stevens.cs522.bookstore.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.entities.Author;
import edu.stevens.cs522.bookstore.entities.Book;

public class AddBookActivity extends Activity {
	
	// Use this as the key to return the book details as a Parcelable extra in the result intent.
	public static final String BOOK_RESULT_KEY = "book_result";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_book);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// TODO provide SEARCH and CANCEL options
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		// TODO
		
		// SEARCH: return the book details to the BookStore activity
		
		// CANCEL: cancel the search request
        switch (item.getItemId()) {
            case R.id.search_add:
                Book newBook = searchBook();
                Intent ok_data = new Intent();
                ok_data.putExtra(BOOK_RESULT_KEY, newBook);
                setResult(RESULT_OK, ok_data);
                finish();
                return true;
            case R.id.search_cancel:
                setResult(RESULT_CANCELED, null);
                finish();
                return true;
        }
		return false;
	}
	
	public Book searchBook(){
		/*
		 * Search for the specified book.
		 */
		// TODO Just build a Book object with the search criteria and return that.

        EditText bookTitle = (EditText) findViewById(R.id.search_title);
        EditText bookAuthor = (EditText) findViewById(R.id.search_author);
        EditText bookIsbn = (EditText) findViewById(R.id.search_isbn);

        String thisTitle = bookTitle.getText().toString();
        String thisAuthor = bookAuthor.getText().toString();
        String[] authors = thisAuthor.split(", ");
        Author[] authorsArray = new Author[authors.length];
        for(int i = 0; i < authors.length; i++){
            authorsArray[i] = new Author(authors[i].split(" "));
        }
        String thisIsbn = bookIsbn.getText().toString();
        Book newBook = new Book(100, thisTitle, authorsArray, thisIsbn, "10");
        return newBook;
	}

}