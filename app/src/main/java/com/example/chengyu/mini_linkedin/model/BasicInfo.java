package com.example.chengyu.mini_linkedin.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;

/**
 * Created by Chengyu on 8/24/2016.
 */
public class BasicInfo implements Parcelable{
    public static final String user_picture = "USER_PICTURE";
    public String name;

    public String email;

    public Uri imageUri;

    //public String path = "";//test

    public BasicInfo() {}

    protected BasicInfo(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        this.imageUri = in.readParcelable(Uri.class.getClassLoader());
        //this.path = in.readString();//test
    }

    public static final Creator<BasicInfo> CREATOR = new Creator<BasicInfo>() {
        @Override
        public BasicInfo createFromParcel(Parcel in) {
            return new BasicInfo(in);
        }

        @Override
        public BasicInfo[] newArray(int size) {
            return new BasicInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeParcelable(imageUri, i);
        //parcel.writeString(path);//test
    }
}
