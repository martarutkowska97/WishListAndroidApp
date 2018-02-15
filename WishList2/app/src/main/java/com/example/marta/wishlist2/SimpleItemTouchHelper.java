package com.example.marta.wishlist2;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by marta on 2018-02-05.
 */

public class SimpleItemTouchHelper extends ItemTouchHelper.Callback {

        private final SimpleItemTouchInterface mAdapter;
        public SimpleItemTouchHelper(SimpleItemTouchInterface adapter) {
            mAdapter = adapter;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int  dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags=0;
            if(mAdapter instanceof BulletListActivity.BPAdapter){
                swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            }

            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        }
}
