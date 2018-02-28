package com.example.marta.wishlist2;
import android.app.Activity;
import android.app.Dialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
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

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR); getSupportActionBar().hide();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);


        setContentView(R.layout.activity_bullet_list);
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        title = (EditText)findViewById(R.id.bulletlist_title);
        recyclerView = (RecyclerView)findViewById(R.id.bulletlist_bullet_points);
        currentWishPanel = getIntent().getParcelableExtra("CurrentWishPanel");

        if(currentWishPanel == null)
        {
            Bundle bundle = getIntent().getExtras();
            if(bundle == null)
                Log.d("BUG", "BUNDLE IS NULL O CO CHODZI");
            else
            {
                int category = bundle.getInt("Category", 0);
                currentWishPanel = new WishPanel("", System.currentTimeMillis(), new ArrayList<BulletPoint>());
                currentWishPanel.setCategory(category);
            }
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
        menuOptions();
    }//protected void onCreate(Bundle savedInstanceState)


    public void menuOptions(){
         FloatingActionButton add=(FloatingActionButton) findViewById(R.id.fb_button_add);
         FloatingActionButton delete=(FloatingActionButton) findViewById(R.id.fb_button_delete);
         FloatingActionButton save=(FloatingActionButton) findViewById(R.id.fb_button_save);
         final FloatingActionButton secret=(FloatingActionButton) findViewById(R.id.fb_button_secret);

         if(!currentWishPanel.getSecret()){
             secret.setLabelText("Secret");
         }
         else{
             secret.setLabelText("Unsecret");
         }

        final AlertDialog.Builder deleteAlert= new AlertDialog.Builder(this)
                        .setTitle("Are You sure?")
                .setMessage("You are about to delete "+title.getText().toString()+"!")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("Wish Panel", (Parcelable) currentWishPanel);
                        setResult(WishPanelSelectionActivity.DELETE_WISH_RESULT_CODE, resultIntent);
                        finish();
                    }
                })
                .setNegativeButton("NO", null)
                .setCancelable(false);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewBulletPoint();
                updateArrayList();
                plusClicked=true;
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteAlert.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (title.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please, enter the title!", Toast.LENGTH_SHORT).show();
                } else {
                    updateArrayList();
                    currentWishPanel.setTitle(title.getText().toString());
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("Wish Panel", (Parcelable) currentWishPanel);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }

            }
        });

        secret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder dialogBulider=new AlertDialog.Builder(BulletListActivity.this);
                final View viewSecret=getLayoutInflater().inflate(R.layout.pop_up_window,null);

                final EditText password=viewSecret.findViewById(R.id.edit_text_set_password);
                final EditText repeatPassword=viewSecret.findViewById(R.id.edit_text_repeat_password);

                Button setPassword=viewSecret.findViewById(R.id.button_set_password);


                if(currentWishPanel.getSecret()){
                    currentWishPanel.setPassword("");
                    currentWishPanel.setSecret(false);
                    secret.setLabelText("Secret");
                }

                else {
                    dialogBulider.setView(viewSecret);
                    final AlertDialog dialog = dialogBulider.create();
                    dialog.show();

                    setPassword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (password.getText().toString().isEmpty() && repeatPassword.getText().toString().isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Please fill the spaces!", Toast.LENGTH_SHORT).show();
                            } else if (password.getText().toString().isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Please enter the password!", Toast.LENGTH_SHORT).show();
                            } else if (repeatPassword.getText().toString().isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Please repeat the password!", Toast.LENGTH_SHORT).show();
                            } else if (!repeatPassword.getText().toString().equals(password.getText().toString())) {
                                Toast.makeText(getApplicationContext(), "Different passwords!", Toast.LENGTH_LONG).show();
                            } else {
                                currentWishPanel.setPassword(repeatPassword.getText().toString());
                                currentWishPanel.setSecret(true);

                                secret.setLabelText("Unsecret");
                                dialog.dismiss();
                            }
                        }

                    });
                }
            }
        });


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
            setResult(Activity.RESULT_CANCELED);
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

