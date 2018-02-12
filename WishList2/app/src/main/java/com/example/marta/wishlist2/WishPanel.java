package com.example.marta.wishlist2;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.marta.wishlist2.BulletPoint;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class WishPanel implements Serializable, Parcelable {

    private String title;
    private long dateOfCreation;
    private ArrayList<BulletPoint> content;

    public WishPanel(String title, long dateOfCreation, ArrayList<BulletPoint> content) {
        this.title = title;
        this.dateOfCreation = dateOfCreation;
        this.content = content;
    }

    public WishPanel(Parcel parcel){
        title = parcel.readString();
        dateOfCreation = parcel.readLong();
        content = parcel.readArrayList(BulletPoint.class.getClassLoader());
    }

    public String getTitle() {
        return title;
    }
    public long getDateOfCreation(){
        return dateOfCreation;
    }
    public ArrayList<BulletPoint> getContent(){
        return content;
    }

    public void setTitle(String title1){
        title=title1;
    }
    public void setDateOfCreation(long dateOfCreation1){
        dateOfCreation=dateOfCreation1;
    }
    public void setContent(ArrayList<BulletPoint> content1){
        content=content1;
    }


    public String dateToString(Context contex){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",
                contex.getResources().getConfiguration().locale);

        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(new Date(dateOfCreation));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeLong(dateOfCreation);
        dest.writeList(content);
    }

    public static final Parcelable.Creator<WishPanel> CREATOR
            = new Parcelable.Creator<WishPanel>() {
        public WishPanel createFromParcel(Parcel in) {
            return new WishPanel(in);
        }

        public WishPanel[] newArray(int size) {
            return new WishPanel[size];
        }
    };

}
