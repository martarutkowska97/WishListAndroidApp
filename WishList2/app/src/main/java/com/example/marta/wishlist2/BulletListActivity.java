package com.example.marta.wishlist2;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class BulletListActivity extends AppCompatActivity {

    EditText title;
    ArrayList<BulletPoint> content;
    RecyclerView recyclerView;

    WishPanel currentWishPanel;
    BPAdapter bpadapter;

    boolean plusClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bullet_list);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        title = (EditText)findViewById(R.id.bulletlist_title);
        recyclerView = (RecyclerView)findViewById(R.id.bulletlist_bullet_points);
        currentWishPanel = getIntent().getParcelableExtra("CurrentWishPanel");

        if(currentWishPanel == null)
        {
            currentWishPanel = new WishPanel("", System.currentTimeMillis(), new ArrayList<BulletPoint>());
        }
        else {
            title.setText(currentWishPanel.getTitle());
        }
        content = currentWishPanel.getContent();
        bpadapter = new BPAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bpadapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper.Callback callback=new SimpleItemTouchHelper(bpadapter);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        plusClicked=false;
    }//protected void onCreate(Bundle savedInstanceState)

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bullet_list_new,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_activity_save_bullet_list:
                if (title.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please, enter the title!", Toast.LENGTH_SHORT);
                } else {
                    updateArrayList();
                    currentWishPanel.setTitle(title.getText().toString());
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("Wish Panel", (Parcelable) currentWishPanel);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
                break;

            case R.id.action_activity_delete_bullet_list:
                AlertDialog.Builder deleteAlert= new AlertDialog.Builder(this)
                        .setTitle("Are You sure?")
                        .setMessage("You are about to delete "+title.getText().toString()+"!")
                        //we need to set positive and negative button
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("Wish Panel", (Parcelable) currentWishPanel);
                                setResult(WishPanelSelectionActivity.DELETE_WISH_RESULT_CODE, resultIntent);
                                finish();
                            }
                        })
                        //if click no does nothing
                        .setNegativeButton("NO", null)
                        //clicking anywhere else of the alertdialog does nothing
                        .setCancelable(false);
                deleteAlert.show();


                break;

            case R.id.action_activity_add_bullet_point:
                addNewBulletPoint();
                updateArrayList();
                plusClicked=true;
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder discardChanges= new AlertDialog.Builder(this)
                .setTitle("Do You want to discard changes?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("NO", null)
                .setCancelable(false);
        if(plusClicked) {
            discardChanges.show();
        }
        else{
            finish();
        }
    }

    private void updateArrayList(){
        for(int i=0;i<content.size();i++){
            BulletPoint bp=content.get(i);
            View view = recyclerView.getChildAt(content.indexOf(bp));
            if (view != null) {
                EditText text = view.findViewById(R.id.bulletpoint_title);
                CheckBox box = view.findViewById(R.id.bulletpoint_checkbox);
                bp.setTitle(text.getText().toString());
                bp.setChecked(box.isChecked());
            }
        }


    }
    private void updateRecyclerView(){
        for(int i=0;i<content.size();i++) {
            BulletPoint bp=content.get(i);
            View view = recyclerView.getChildAt(content.indexOf(bp));
            if (view != null) {
                EditText text = view.findViewById(R.id.bulletpoint_title);
                CheckBox box = view.findViewById(R.id.bulletpoint_checkbox);
                text.setText(bp.getTitle());
                box.setChecked(bp.isChecked());
            }
        }
    }

    private void addNewBulletPoint(){
        BulletPoint newBulletPoint= new BulletPoint("");
        content.add(newBulletPoint);
        bpadapter.notifyItemInserted(content.size()-1);
    }

    public class BPAdapter extends RecyclerView.Adapter<ViewHolder> implements SimpleItemTouchInterface{

        LayoutInflater mLayoutInflater;

        public BPAdapter(){
            mLayoutInflater=getLayoutInflater();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=mLayoutInflater.inflate(R.layout.bulletpoint_item,parent,false);
            ViewHolder holder=new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.checkBox.setChecked(content.get(position).isChecked());
            holder.editText.setText(content.get(position).getTitle());
        }

        @Override
        public int getItemCount() {
            return content.size();
        }

        //tutaj metody usuń i drag and drop

        public void onItemMove(int positionSource, int positionTarget){

            Collections.swap(content,positionSource,positionTarget);
            notifyItemMoved(positionSource,positionTarget);

        }
        public void onItemDismiss(int position){
            content.remove(position);
            notifyItemRemoved(position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;
        EditText editText;

        public ViewHolder(View view){
            super(view);
            checkBox=view.findViewById(R.id.bulletpoint_checkbox);
            editText=view.findViewById(R.id.bulletpoint_title);
        }
    }
}

