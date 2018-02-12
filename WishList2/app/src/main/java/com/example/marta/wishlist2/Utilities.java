package com.example.marta.wishlist2;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Utilities {
    public static final String WISH_PANEL_FILE_EXTENSION  = ".bin";

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
}

