package com.example.marta.wishlist2;
import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Utilities {
    public static final String WISH_PANEL_FILE_EXTENSION  = ".bin";

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

    static ArrayList images_id= new ArrayList();//int
    static ArrayList <String> names=new ArrayList();




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

    public static ArrayList Images(){
        if(images_id.size()==0) {
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
        }
        return images_id;
    }
    public static ArrayList Names(){
        if(names.size()==0) {
            names.add("Beauty");
            names.add("Books");
            names.add("Clothes");
            names.add("Electronics");
            names.add("Essentials");
            names.add("Food");
            names.add("Home");
            names.add("Others");
            names.add("Present");
            names.add("Repair");
            names.add("Services");
        }
        return names;
    }
}

