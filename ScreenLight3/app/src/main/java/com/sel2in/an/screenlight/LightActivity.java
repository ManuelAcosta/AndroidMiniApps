package com.sel2in.an.screenlight;

import android.content.Context;
import android.hardware.Camera;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class LightActivity extends AppCompatActivity {

    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        getCamera();
        screenBackLightOnInitiate();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("s2ner", "stop ");
        try {
            screenBackLightNormal();
            Log.e("s2ner", "stop 2. ");
            if (camera != null) {
                try {
                    camera.release();
                } catch (Throwable e) {
                    //Okay
                }
            }
        } catch (Throwable e) {

        }
    }

    public void flashTogggle(View v) {

    }
    // Get the camera
    private void getCamera() {
        Camera.Parameters params;
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (Throwable e) {
                Log.e("s2ner", "Camera Error. Failed to Open. Error: " + e, e);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_light, menu);
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

    private void showDialog(String ttle, String msg) {
        Toast.makeText(getApplicationContext(), ttle + " " + msg, Toast.LENGTH_LONG).show();
    }

    private void screenBackLightOnInitiate() {
        try {
            getWindow().addFlags( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            Log.e("s2ner", "screen backlight ini ");
//            if(wl == null) {
//                PowerManager pwr = (PowerManager) getSystemService(Context.POWER_SERVICE);
//                wl = pwr.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "Bright Screen Torch");
//                wl.setReferenceCounted(false);
//                wl.acquire();
//                Log.e("s2ner", " wl aq ");
//            }
        } catch (Throwable e) {
            Log.e("s2ner", "screenBackLightOnInitiate " + e, e);
        }
    }

    private void screenBackLightNormal() {
        try {
//            if (wl != null) {
//                wl.release();
//                Log.i("s2ner", " wl re ");
//            }
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            Log.i("s2ner", "screen backlight ini ");
        } catch (Throwable e) {
            Log.e("s2ner", "screenBackLightNormal " + e, e);
        }
        //wl = null;
    }
}
