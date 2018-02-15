package com.example.marta.wishlist2;

/**
 * Created by marta on 2018-02-11.
 */

public interface SimpleItemTouchInterface {
    void onItemDismiss(int position);
    void onItemMove(int positionSource, int positionTarget);
}
