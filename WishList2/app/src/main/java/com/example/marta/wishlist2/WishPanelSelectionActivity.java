package com.example.marta.wishlist2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chootdev.recycleclick.RecycleClick;
import com.github.clans.fab.FloatingActionButton;
import com.goodiebag.pinview.Pinview;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;


public class WishPanelSelectionActivity extends AppCompatActivity {

    public static final int MOD_WISH_CODE = 0;
    public static final int ADD_WISH_CODE = 1;
    public static final int DELETE_WISH_RESULT_CODE=2;

    int index_to_mod = -1;

    RecyclerView recyclerView;
    FloatingActionButton addButton;
    ArrayList<WishPanel> content;
    WPAdapter wpAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR); getSupportActionBar().hide();
        setContentView(R.layout.activity_wish_panel_selection);

        recyclerView = (RecyclerView) findViewById(R.id.wishpanels_all_panels);
        addButton = (FloatingActionButton) findViewById(R.id.floating_button_add_new);
        final Context context = this;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(context, CathegoryActivity.class), ADD_WISH_CODE);
            }
        });

        recyclerView.setAdapter(null);
        content = Utilities.loadAllWishPanels(this);
        if (content == null) {
            content = new ArrayList<>();
            Toast.makeText(this, "You have no saved notes", Toast.LENGTH_SHORT).show();
        }
            wpAdapter = new WPAdapter();
            recyclerView.setAdapter(wpAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(wpAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            final ItemTouchHelper.Callback callback=new SimpleItemTouchHelper(wpAdapter);
            ItemTouchHelper itemTouchHelper=new ItemTouchHelper(callback);
            itemTouchHelper.attachToRecyclerView(recyclerView);

            RecycleClick.addTo(recyclerView).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, final int position, View v) {
                    index_to_mod = position;

                    final Intent viewWishListIntent= new Intent(getApplicationContext(),BulletListActivity.class);
                    viewWishListIntent.putExtra("CurrentWishPanel", (Parcelable)content.get(position));
                    final AlertDialog.Builder dialogBulider=new AlertDialog.Builder(WishPanelSelectionActivity.this);
                    View view=getLayoutInflater().inflate(R.layout.pop_up_enter_password,null);
                    final EditText password=view.findViewById(R.id.edit_text_enter_password);
                    Button setPassword=view.findViewById(R.id.button_enter_password);
                    Button forgotPassword=view.findViewById(R.id.button_forgot_password);

                    dialogBulider.setView(view);
                    final AlertDialog dialog=dialogBulider.create();

                    if(content.get(position).getSecret()) {
                        dialog.show();
                        setPassword.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(password.getText().toString().equals(content.get(position).getPassword())){
                                    dialog.dismiss();
                                    startActivityForResult(viewWishListIntent, MOD_WISH_CODE);
                                }
                                else if(!password.getText().toString().equals(content.get(position).getPassword())){
                                    Toast.makeText(getApplicationContext(),"Wrong password!",Toast.LENGTH_SHORT);
                                }
                            }
                        });

                        forgotPassword.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();

                            }
                        });
                    }
                    else {
                        //dialog.dismiss();
                        startActivityForResult(viewWishListIntent, MOD_WISH_CODE);
                    }
                }
            });
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null) {
            switch (requestCode) {
                case MOD_WISH_CODE: {
                    if (resultCode == Activity.RESULT_OK) {
                        WishPanel wishPanel = data.getParcelableExtra("Wish Panel");
                        content.set(index_to_mod, wishPanel);
                        wpAdapter.notifyItemChanged(index_to_mod);
                        //zapis do pliku, podmiana
                        //Utilities.saveAllBulletPoints(getApplicationContext(), wishPanel.getContent(), wishPanel);
                        if (Utilities.saveWishPanel(getApplicationContext(), wishPanel)) {
                            Toast.makeText(this, "Note is saved!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Cannot save the note, make sure you have enough space on your device!", Toast.LENGTH_LONG).show();
                        }
                    } else if (resultCode == DELETE_WISH_RESULT_CODE) {
                        WishPanel wishPanel = data.getParcelableExtra("Wish Panel");
                        Utilities.deleteWishPanel(getApplicationContext(), String.valueOf(wishPanel.getDateOfCreation()) + Utilities.WISH_PANEL_FILE_EXTENSION);
                        content.remove(index_to_mod);
                        wpAdapter.notifyItemRemoved(index_to_mod);
                    }
                    break;
                }
                case ADD_WISH_CODE: {
                    if (resultCode == Activity.RESULT_OK) {
                        WishPanel wishPanel = data.getParcelableExtra("Wish Panel");
                        content.add(wishPanel);
                        wpAdapter.notifyItemInserted(content.size() - 1);
                        //zapis do pliku, nowy

                        //Utilities.saveAllBulletPoints(getApplicationContext(), wishPanel.getContent(), wishPanel);
                        if (Utilities.saveWishPanel(getApplicationContext(), wishPanel)) {
                            Toast.makeText(this, "Note is saved!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Cannot save the note, make sure you have enough space on your device!", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                }
            }
        }
    }

    private void enterPin(){
        final AlertDialog.Builder dialogBulider=new AlertDialog.Builder(WishPanelSelectionActivity.this);
        View view=getLayoutInflater().inflate(R.layout.pop_up_enter_pin,null);
        dialogBulider.setView(view);
        final AlertDialog dialog=dialogBulider.create();
        final Pinview pinview=view.findViewById(R.id.pin_view);
        Button setNewPassword=view.findViewById(R.id.button_set_pin);
        dialog.show();
        setNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pinview.getValue().length()<4){
                    Toast.makeText(getApplicationContext(),"Please fill the spaces!",Toast.LENGTH_SHORT);
                }
                else if(!pinview.getValue().equals(Utilities.loadPin(getApplicationContext()))){

                    Toast.makeText(getApplicationContext(),"Wrong PIN number!",Toast.LENGTH_SHORT);
                }
                else if(pinview.getValue().equals(Utilities.loadPin(getApplicationContext()))){

                    dialog.dismiss();
                    changePassword();
                }
            }
        });
    }

    private void changePassword(){
        final AlertDialog.Builder dialogBulider=new AlertDialog.Builder(WishPanelSelectionActivity.this);
        View view=getLayoutInflater().inflate(R.layout.pop_up_window,null);
        dialogBulider.setView(view);
        final AlertDialog dialog=dialogBulider.create();



    }

    public class WPAdapter extends RecyclerView.Adapter<WishPanelSelectionActivity.ViewHolder> implements SimpleItemTouchInterface{

        LayoutInflater mLayoutInflater;

        public WPAdapter(){
            mLayoutInflater=getLayoutInflater();
        }

        @Override
        public WishPanelSelectionActivity.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=mLayoutInflater.inflate(R.layout.wishpanel_item,parent,false);
            WishPanelSelectionActivity.ViewHolder holder=new WishPanelSelectionActivity.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(WishPanelSelectionActivity.ViewHolder holder, int position) {

            holder.title.setText(content.get(position).getTitle());
            ImageLoader.getInstance().displayImage((String)Utilities.getInstance(getApplicationContext()).ImagesUri().get(content.get(position).getCategory()), holder.imageView);

            if(content.get(position).getSecret()) {
                holder.imageViewSecret.setImageResource(R.drawable.secret2);
            }
        }

        @Override
        public int getItemCount() {
            return content.size();
        }

        public WishPanel getItem(int position) {
            return content.get(position);
        }
        public void onItemDismiss(int position){
        }

        //tutaj metody usuń i drag and drop

        public void onItemMove(int positionSource, int positionTarget){
            Collections.swap(content,positionTarget,positionSource);
            notifyItemMoved(positionSource,positionTarget);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView imageView;
        ImageView imageViewSecret;

        public ViewHolder(View view){
            super(view);

            imageView=view.findViewById(R.id.imageview_secret);
            title=view.findViewById(R.id.wishpanel_title);
            imageViewSecret=view.findViewById(R.id.imageview_cat);
        }
    }
}