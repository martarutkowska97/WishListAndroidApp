package com.example.marta.wishlist2;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CathegoryActivity extends AppCompatActivity {

    public static final int CATEGORY_RESULT_CODE = 0;

    GridView gridView;
    static ArrayList images_id= new ArrayList();//int
    static ArrayList <String> names=new ArrayList();
    CathegoryAdapter cathegoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cathegory);
        gridView=(GridView)findViewById(R.id.grid_view_cathegory);

        names=Utilities.Names();
        images_id=Utilities.Images();

        cathegoryAdapter=new CathegoryAdapter();
        gridView.setAdapter(cathegoryAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivityForResult(new Intent(getApplicationContext(), BulletListActivity.class).putExtra("Category", position), CATEGORY_RESULT_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case CATEGORY_RESULT_CODE:
            {
                setResult(Activity.RESULT_OK, data);
                finish();
                break;
            }
        }

    }

    public class CathegoryAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return images_id.size();
        }

        @Override
        public Object getItem(int position) {
            return images_id.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                final LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
                convertView = layoutInflater.inflate(R.layout.cathegory, null);
            }

            final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview_cathegory);
            final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_cathegory);

            imageView.setImageResource((int)images_id.get(position));
            System.out.println(names.size());
            nameTextView.setText(names.get(position));
            return convertView;
        }
    }

}
