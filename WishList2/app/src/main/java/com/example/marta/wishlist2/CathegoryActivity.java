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

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class CathegoryActivity extends AppCompatActivity {

    public static final int CATEGORY_RESULT_CODE = 0;

    GridView gridView;
    static ArrayList<String> images_uri= new ArrayList();
    static ArrayList <String> names=new ArrayList();
    CathegoryAdapter cathegoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cathegory);
        gridView=(GridView)findViewById(R.id.grid_view_cathegory);

        names=Utilities.getInstance(getApplicationContext()).Names();
        images_uri=Utilities.getInstance(getApplicationContext()).ImagesUri();

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
            return images_uri.size();
        }

        @Override
        public Object getItem(int position) {
            return images_uri.get(position);
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

            ImageLoader.getInstance().displayImage((String)Utilities.getInstance(getApplicationContext()).ImagesUri().get(position),imageView);
           // imageView.setImageResource((int)images_id.get(position));

            nameTextView.setText(names.get(position));
            return convertView;
        }
    }

}
