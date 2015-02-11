package edu.stevens.cs522.bookstore.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

public class Book implements Parcelable{
	
	// TODO Modify this to implement the Parcelable interface.
	
	// TODO redefine toString() to display book title and price (why?).
	
	public int id;
	
	public String title;
	
	public Author[] authors;

	public String isbn;
	
	public String price;

	public Book(int id, String title, Author[] author, String isbn, String price) {
		this.id = id;
		this.title = title;
        this.authors = new Author[author.length];
        for(int i = 0; i < author.length; i++){
            authors[i] = author[i];
        }
		this.isbn = isbn;
		this.price = price;
	}

    public Book(Parcel in){
        this.id = in.readInt();
        String[] data = new String[3];
        in.readStringArray(data);
        this.title = data[0];
        this.isbn = data[1];
        this.price = data[2];

        Parcelable[] authorsInfo =  in.readParcelableArray(Author.class.getClassLoader());
        this.authors = new Author[authorsInfo.length];
        for(int i = 0; i < authorsInfo.length; i++){
            authors[i] = (Author)authorsInfo[i];
        }
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel pc, int flags) {
        pc.writeInt(id);
        String[] data = new String[3];
        data[0] = title;
        data[1] = isbn;
        data[2] = price;
        pc.writeStringArray(data);
        if(authors == null){
            authors = new Author[1];
        }
        pc.writeParcelableArray(authors, flags);
    }

    @Override
    public String toString() {
        return isbn;
    }
}