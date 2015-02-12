package edu.stevens.cs522.bookstorebasicwithdb.database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.stevens.cs522.bookstorebasicwithdb.R;
import edu.stevens.cs522.bookstorebasicwithdb.contracts.Book;

/**
 * Created by Xiaoyu on 2/2/2015.
 */
public class BooksAdapter extends ArrayAdapter<Book> {
    public BooksAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Book book = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cart_row, parent, false);
        }
        // Lookup view for data population
        TextView cartTitle = (TextView) convertView.findViewById(R.id.cart_row_title);
        TextView cartAuthor = (TextView) convertView.findViewById(R.id.cart_row_author);
        // Populate the data into the template view using the data object
        cartTitle.setText(book.title);
        cartAuthor.setText(book.authors[0].lastName + " " + book.authors[1].lastName + ", " + book.authors[2].lastName);
        // Return the completed view to render on screen
        return convertView;
    }
}
