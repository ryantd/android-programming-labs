package edu.stevens.cs522.chat.oneway.server.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by xiaoyuzhai on 2/15/15.
 */
public class Peer implements Parcelable
{
    public long id;
    public String name;
    public InetAddress address;
    public int port;

    public Peer(Cursor cursor)
    {
        this.id = MsgContract.getMsgid(cursor);
        this.name = MsgContract.getName(cursor);
        try {
            this.address = InetAddress.getByName(MsgContract.getAddress(cursor));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.port = MsgContract.getPort(cursor);
    }

    public Peer(String name, String address, int port) {
        this.name = name;
        try {
            this.address = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.port = port;
    }

    public Peer(Parcel in){
        int[] intdata = new int[2];
        in.readIntArray(intdata);
        this.id = intdata[0];
        this.port = intdata[1];

        String[] data = new String[2];
        in.readStringArray(data);
        this.name = data[0];
        try {
            this.address = InetAddress.getByName(data[1]);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static final Creator<Peer> CREATOR = new Creator<Peer>() {
        public Peer createFromParcel(Parcel in) {
            return new Peer(in);
        }

        public Peer[] newArray(int size) {
            return new Peer[size];
        }
    };

    //@Override
    public int describeContents(){
        return 0;
    }

    //@Override
    public void writeToParcel(Parcel pc, int flags) {
        pc.writeLong(id);
        pc.writeInt(port);
        String[] data = new String[2];
        data[0] = name;
        data[1] = address.getCanonicalHostName();
        pc.writeStringArray(data);
    }

    public void writeToProvider( ContentValues values)
    {
        MsgContract.putPeerid(values, id);
        MsgContract.putName(values, name);
        MsgContract.putAddress(values, address.getCanonicalHostName());
        MsgContract.putPort(values, port);

    }

    @Override
    public String toString() {
        return name;
    }
}
