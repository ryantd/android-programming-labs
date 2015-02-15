package edu.stevens.cs522.bookstorebasicwithdb.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable{

	public int id;
	
	public String title;
	
	public String authors;

	public String isbn;
	
	public String price;

    public Book(Cursor cursor)
    {
        this.id = BookContract.getRowid(cursor);
        this.title = BookContract.getTitle(cursor);
        this.authors = BookContract.getAuthors(cursor);
        this.isbn = BookContract.getIsbn(cursor);
        this.price = BookContract.getPrice(cursor);
    }

	public Book(String title, String authors, String isbn, String price) {
		this.title = title;
        this.authors = authors;
		this.isbn = isbn;
		this.price = price;
	}

    public Book(Parcel in){
        this.id = in.readInt();
        String[] data = new String[4];
        in.readStringArray(data);
        this.title = data[0];
        this.authors = data[1];
        this.isbn = data[2];
        this.price = data[3];
/*
        Parcelable[] authorsInfo =  in.readParcelableArray(Author.class.getClassLoader());
        this.authors = new Author[authorsInfo.length];
        for(int i = 0; i < authorsInfo.length; i++){
            authors[i] = (Author)authorsInfo[i];
        }*/
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
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
        String[] data = new String[4];
        data[0] = title;
        data[1] = authors;
        data[2] = isbn;
        data[3] = price;
        pc.writeStringArray(data);
/*        if(authors == null){
            authors = new Author[1];
        }
        pc.writeParcelableArray(authors, flags);*/
    }

    public void writeToProvider( ContentValues values)
    {
        BookContract.putRowid(values, id);
        BookContract.putTitle(values, title);
        BookContract.putAuthors(values, authors);
        BookContract.putIsbn(values, isbn);
        BookContract.putPrice(values, price);

    }

    @Override
    public String toString() {
        return isbn;
    }
}