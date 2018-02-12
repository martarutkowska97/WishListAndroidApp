package com.example.marta.wishlist2;

/**
 * Created by marta on 2018-02-11.
 */

public interface SimpleItemTouchInterface {
    public void onItemDismiss(int position);

    public void onItemMove(int positionSource, int positionTarget);
}
