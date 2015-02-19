package edu.stevens.cs522.chat.oneway.server.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.InetAddress;

/**
 * Created by Xiaoyu on 2/7/2015.
 */
public class Messages implements Parcelable
{
    public long id;
    public String text;
    public String sender;

    public Messages(Cursor cursor)
    {
        this.id = MsgContract.getMsgid(cursor);
        this.text = MsgContract.getText(cursor);
        this.sender = MsgContract.getSender(cursor);
    }

    public Messages(String text, String sender) {
        this.text = text;
        this.sender = sender;
    }

    public Messages(Parcel in){
        this.id = in.readInt();
        String[] data = new String[2];
        in.readStringArray(data);
        this.text = data[0];
        this.sender = data[1];
    }

    public static final Creator<Messages> CREATOR = new Creator<Messages>() {
        public Messages createFromParcel(Parcel in) {
            return new Messages(in);
        }

        public Messages[] newArray(int size) {
            return new Messages[size];
        }
    };

    //@Override
    public int describeContents(){
        return 0;
    }

    //@Override
    public void writeToParcel(Parcel pc, int flags) {
        pc.writeLong(id);
        String[] data = new String[2];
        data[0] = text;
        data[1] = sender;
        pc.writeStringArray(data);
    }

    public void writeToProvider( ContentValues values)
    {
        MsgContract.putMsgid(values, id);
        MsgContract.putText(values, text);
        MsgContract.putSender(values, sender);

    }

    @Override
    public String toString() {
        return text;
    }
}