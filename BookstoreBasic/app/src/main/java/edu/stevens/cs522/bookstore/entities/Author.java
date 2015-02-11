package edu.stevens.cs522.bookstore.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Author implements Parcelable {
	
	// TODO Modify this to implement the Parcelable interface.

	// NOTE: middleInitial may be NULL!
	
	public String firstName = null;
	
	public String middleInitial = null;
	
	public String lastName = null;

    public Author(String[] name){
        if(name.length == 1){
            this.lastName = name[0];
        }else if(name.length == 2){
            this.firstName = name[0];
            this.lastName = name[1];
        }else if(name.length >= 3){
            this.firstName = name[0];
            this.middleInitial = name[1];
            this.lastName = name[2];
        }else if(name.length == 0){
            //nothing needs to be done
        }
    }

    public Author(String firstName, String middleInitial, String lastName) {
        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.lastName = lastName;
    }
    public Author(Parcel in){
        String[] data = new String[3];
        in.readStringArray(data);
        this.firstName = data[0];
        this.middleInitial = data[1];
        this.lastName = data[2];
    }

    public static final Parcelable.Creator<Author> CREATOR = new Parcelable.Creator<Author>() {
        public Author createFromParcel(Parcel in) {
            return new Author(in);
        }
        public Author[] newArray(int size) {
            return new Author[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel pc, int flags) {
        String[] array = new String[3];
        array[0] = this.firstName;
        array[1] = this.middleInitial;
        array[2] = this.lastName;
        pc.writeStringArray(array);
    }

    @Override
    public String toString() {
        return firstName + middleInitial + lastName;
    }

}
