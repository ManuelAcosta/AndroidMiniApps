package com.sel2in.an.learn.animate.animate1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;

public class DoggyAnimActivity2 extends Activity {

    Timer tmr = null;
    ImageView i1;
    public void anim(View view){
        i1.setAlpha(1f);
        i1.setImageResource(R.drawable.beagle2);//beagle2
        //wait for 300 ms
        //i1.animate().alpha(0.2f).setDuration(2400);
        //        /.setImageResource(R.drawable.f2).alpha(1f).set

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.anim_fade);
        i1.startAnimation(fadeIn);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                i1.setImageResource(R.drawable.puppy_beagle);//beagle2
                Animation fadeOut = AnimationUtils.loadAnimation(DoggyAnimActivity2.this, R.anim.anim_back);
                i1.startAnimation(fadeOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_puppy);
        tmr = new Timer("anim_dog", true);
        i1 = (ImageView) findViewById(R.id.aniImg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_puppy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
