package com.example.marta.wishlist2;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Utilities {

    private static Utilities instance=null;//singleton

    public static final String WISH_PANEL_FILE_EXTENSION  = "w.bin";
    public static final String PIN_FILE_EXTENSION="p.bin";


    //zapisujemy pin do pliku!
    public String appPin;//int

    public static final int BEAUTY=0;
    public static final int BOOKS=1;
    public static final int CLOTHES=2;
    public static final int ELECTRONICS=3;
    public static final int ESSENTIALS=4;
    public static final int FOOD=5;
    public static final int HOME=6;
    public static final int OTHERS=7;
    public static final int PRESENT=8;
    public static final int REPAIR=9;
    public static final int SERVICES=10;

    static ArrayList<String> images_uri;
    static ArrayList <String> names;


    DisplayImageOptions defaultOptions;
    ImageLoaderConfiguration config;
    protected Utilities(Context context){

        defaultOptions=new DisplayImageOptions.Builder().cacheInMemory(true).build();
        config= new ImageLoaderConfiguration.Builder(context).defaultDisplayImageOptions(defaultOptions).build();

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        images_uri=new ArrayList();
        names=new ArrayList<>();

        //czterocyfrowy pin
        appPin="";

        images_uri.add("assets://beauty.png");
        images_uri.add("assets://books.png");
        images_uri.add("assets://clothes.png");
        images_uri.add("assets://electronics.png");
        images_uri.add("assets://essentials.png");
        images_uri.add("assets://food.png");
        images_uri.add("assets://home.png");
        images_uri.add("assets://others.png");
        images_uri.add("assets://present.png");
        images_uri.add("assets://repair.png");
        images_uri.add("assets://services.png");

        for(String s:images_uri){
            imageLoader.loadImageSync(s);
        }

        names.add("Beauty");
        names.add("Books");
        names.add("Clothes");
        names.add("Electronics");
        names.add("Essentials");
        names.add("Food");
        names.add("Home");
        names.add("Others");
        names.add("Gifts");
        names.add("Repair");
        names.add("Services");
        //names.add("dupa");
/*
        images_id.add(R.drawable.beauty);
        images_id.add(R.drawable.books);
        images_id.add(R.drawable.clothes);
        images_id.add(R.drawable.electronics);
        images_id.add(R.drawable.essentials);
        images_id.add(R.drawable.food);
        images_id.add(R.drawable.home);
        images_id.add(R.drawable.others);
        images_id.add(R.drawable.present);
        images_id.add(R.drawable.repair);
        images_id.add(R.drawable.services);
        */
    }

    public static Utilities getInstance(Context context){
        if(instance==null){
            instance=new Utilities(context);
        }

        return instance;
    }

    public void setAppPin(String string){
        appPin=string;
    }

    public String getAppPin(){
        return appPin;
    }

    public static boolean savePin(Context context,String pin){
        String fileName= PIN_FILE_EXTENSION;
        FileOutputStream fos;
        ObjectOutputStream oos;
        try
        {
            fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(pin);
            oos.close();
            fos.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public static String loadPin(Context context){
        String pin=null;
        File fileDir=context.getFilesDir();
        String pinFile=null;

        for(String file: fileDir.list())
        {
            if(file.endsWith(PIN_FILE_EXTENSION)){
                pinFile=file;
            }
        }

        FileInputStream fis;
        ObjectInputStream ois;

        try{
                fis=context.openFileInput(pinFile);
                ois= new ObjectInputStream(fis);
                pin=(String) ois.readObject();
                fis.close();
                ois.close();
            }
            catch(IOException | ClassNotFoundException e){
                e.printStackTrace();
                return null;
            }

        return pin;
    }

    public static boolean saveWishPanel(Context context, WishPanel wishPanel){
        String fileName= String.valueOf(wishPanel.getDateOfCreation())+WISH_PANEL_FILE_EXTENSION;
        FileOutputStream fos;
        ObjectOutputStream oos;
        try
        {
            fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(wishPanel);
            oos.writeObject(wishPanel.getContent());
            oos.close();
            fos.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static ArrayList<WishPanel> loadAllWishPanels(Context context){
        ArrayList<WishPanel> wishpanels=new ArrayList<>();
        File fileDir=context.getFilesDir();
        ArrayList<String> wishpanelsFiles=new ArrayList<>();

        for(String file: fileDir.list())
        {
            if(file.endsWith(WISH_PANEL_FILE_EXTENSION)){
                wishpanelsFiles.add(file);
            }
        }

        FileInputStream fis;
        ObjectInputStream ois;

        for(int i=0;i<wishpanelsFiles.size();i++){
            try{
                fis=context.openFileInput(wishpanelsFiles.get(i));
                ois= new ObjectInputStream(fis);
                System.out.println(fis.toString());
                System.out.println(ois.toString());
                WishPanel wp = (WishPanel)(ois.readObject());
                wishpanels.add(wp);

                fis.close();
                ois.close();
            }
            catch(IOException | ClassNotFoundException e){
                e.printStackTrace();
                return null;
            }
        }
        return wishpanels;
    }

    public static void deleteWishPanel(Context context, String fileName) {

        File dir=context.getFilesDir();
        File file=new File(dir,fileName);
        if(file.exists()){
            file.delete();
        }
    }

    public ArrayList ImagesUri(){
        return images_uri;
    }
    public ArrayList Names(){
        return names;
    }
}

