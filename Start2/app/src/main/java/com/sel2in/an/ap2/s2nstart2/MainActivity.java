package com.sel2in.an.ap2.s2nstart2;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private boolean imgId = true;

    EditText txt2 = null;
    int rndi = 0;

    public void onClick(View v) {
        String s =  txt2.getText().toString();
        if(s.length() > 0) {
            try {
                int amt = Integer.parseInt(s);
                if(amt == rndi) {
                    s = "Got it!";
                }else if(amt > rndi){
                        s = "Your " + amt + " is too high" ;
                }else{
                    s = "Your " + amt + " is too low" ;
                }
                }catch(Throwable e){
                Log.i("warn", "er " + e);
                s ="bad number " + e;//in external/ production app dont show error to user
            }
        }else{
            s = "Enter a number to Check";
        }
        Toast.makeText(getApplicationContext(),  s + ", on " + new java.util.Date() + " .", Toast.LENGTH_LONG).show();
    }


    public void onClick1(View v) {
        String s =  txt2.getText().toString();
        if(s.length() > 0) {
            try {
                double amt = Double.parseDouble(s);
                s = "$" + amt + " in INR :" +  (amt * 60) + "  taking 60 as factor";
            }catch(Throwable e){
                Log.i("warn", "er " + e);
                s ="bad number " + e;//in external/ production app dont show error to user
            }
        }else{
            s = "Enter a number to convert";
        }
        Toast.makeText(getApplicationContext(), "Enter :" + txt2.getText().toString() + ". " + s + ", on " + new java.util.Date() + " .", Toast.LENGTH_LONG).show();
        Log.i("butn", " click " + v.getId() + "[" + txt2.getText().toString() + "]");
        ImageView img = (ImageView ) findViewById(R.id.imageView);
        try {
            if(imgId ) {
                img.setImageResource(R.drawable.f2);
            }else{
                img.setImageResource(R.drawable.f1);
            }
            imgId = !imgId;
        }catch(Throwable e){
            Log.i("warn", "er " + e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Random rnd = new Random();
        rndi = rnd.nextInt(30);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amainlayout);
        txt2 =(EditText) findViewById(R.id.txt2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_res, menu);
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
