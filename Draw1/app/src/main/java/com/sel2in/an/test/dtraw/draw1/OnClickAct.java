package com.sel2in.an.test.dtraw.draw1;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * Created by t on 23-04-2016.
 */
public class OnClickAct implements View.OnTouchListener, View.OnClickListener {
    C3Activity act;
    public OnClickAct(C3Activity act){
        this.act= act;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (act.getAnimOn() || act.getState() != C3Activity.PROGRESS_STATE){
            return true;
        }

        if(v instanceof ImageView){
            act.squareClick(v);
            return true;
        }
        float x = event.getX();
        float y = event.getY();
        if(x > act.w3 || y > act.h3){
            Log.i("touch", "Outside area x " + x + ", y " + y + ", w3 " + act.w3 + ", h3 " + act.h3);
            return false;
        }
        int xx = (int)(x / act.imgW);
        int yy = (int)(y / act.imgH);
        int which = xx  + (yy * 3);//0-8
        Log.i("touch", "Outside area x " + x + ", y " + y + ", w3 " + act.w3 + ", h3 " + act.h3);
        Log.i("touch", "at xx " + xx + ", yy " + yy + ", which " + which + ". ");
        act.squareClick(act.cellImgs[which]);
        return true;
    }

    @Override
    public void onClick(View v) {
        act.squareClick(v);
    }
}
