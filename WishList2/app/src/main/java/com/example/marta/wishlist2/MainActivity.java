package com.example.marta.wishlist2;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.Image;
import android.media.VolumeShaper;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.goodiebag.pinview.Pinview;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    public Utilities utilities;
    public final String PREFERENCES="first_time_pin";
    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR); getSupportActionBar().hide();
         settings = getSharedPreferences(PREFERENCES, 0);

        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        if(!(ContextCompat.checkSelfPermission(this,

                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        }
        ImageView tapToStart=(ImageView)findViewById(R.id.imageView_tap);
        setAlphaAnimation(tapToStart);
        utilities = Utilities.getInstance(getApplicationContext());


        tapToStart();
    }

    public void tapToStart()
    {
        final Context context=this;
        RelativeLayout relativeLayout=(RelativeLayout) findViewById(R.id.main_menu_screen);


        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (settings.getBoolean("firstTime",true))
                {
                    final AlertDialog.Builder dialogBulider=new AlertDialog.Builder(MainActivity.this);
                    final View view2=getLayoutInflater().inflate(R.layout.pop_up_set_pin,null);

                    final Pinview myPinView=view2.findViewById(R.id.pin_view);

                   myPinView.setInputType(Pinview.InputType.NUMBER);

                    Button setPin=view2.findViewById(R.id.button_set_pin);

                    dialogBulider.setView(view2);
                    final AlertDialog dialog = dialogBulider.create();
                    dialog.setCancelable(false);
                    dialog.show();


                    setPin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            System.out.println("GUZIK");
                            if(myPinView.getValue().length()<4){
                                Toast.makeText(getApplicationContext(),"Fill the spaces!",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                System.out.println("OK PIN");
                                System.out.println("ON DATA ENTERED");
                                String pin = myPinView.getValue();

                                settings.edit().putBoolean("firstTime",false).apply();
                                Utilities.savePin(getApplicationContext(),pin);
                                dialog.dismiss();
                                System.out.println("KONIEC???");

                            }
                        }
                    });
                }
                else
                {
                    Intent panelIntent=new Intent(context,WishPanelSelectionActivity.class);
                    startActivity(panelIntent);
                }
            }
        });

    }
    public static void setAlphaAnimation(View v) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(v, "alpha",  1f, 0.1f);
        fadeOut.setDuration(2000);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(v, "alpha", 0.1f, 1f);
        fadeIn.setDuration(2000);

        final AnimatorSet mAnimationSet = new AnimatorSet();

        mAnimationSet.play(fadeIn).after(fadeOut);

        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimationSet.start();
            }
        });
        mAnimationSet.start();
    }
}
