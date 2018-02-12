package com.example.marta.wishlist2;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class BulletPoint implements Serializable, Parcelable{

    private String title;
    private long dateOfCreation;
    private boolean checked;

    public BulletPoint()
    {
        this.title = "";
        this.dateOfCreation =  0;
        checked=false;
    }

    public BulletPoint(String title){
        this.title=title;
        this.dateOfCreation=dateOfCreation;
        checked=false;
    }

    public BulletPoint(Parcel parcel)
    {
        title = parcel.readString();
        dateOfCreation = parcel.readLong();
        checked = (parcel.readInt() == 1);
    }

    public String getTitle(){
        return title;
    }
    public long getDateOfCreation(){
       return dateOfCreation;
    }
    public boolean isChecked(){return checked;}

    public void setTitle(String title1){
        title=title1;
    }
    public void setDateOfCreation(long dateOfCreation1){
        dateOfCreation=dateOfCreation1;
    }
    public  void setChecked(boolean checked1){checked=checked1;}

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
        dest.writeInt(checked ? 1 : 0);
    }

    public static final Parcelable.Creator<BulletPoint> CREATOR
            = new Parcelable.Creator<BulletPoint>() {
        public BulletPoint createFromParcel(Parcel in) {
            return new BulletPoint(in);
        }

        public BulletPoint[] newArray(int size) {
            return new BulletPoint[size];
        }
    };
    
}
