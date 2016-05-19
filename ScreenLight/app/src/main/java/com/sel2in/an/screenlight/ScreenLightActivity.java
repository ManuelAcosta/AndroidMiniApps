package com.sel2in.an.screenlight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.PowerManager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class ScreenLightActivity extends AppCompatActivity {

    private static boolean firstTime = true;
    private Camera camera;

    //TextView txtPos;
    private boolean isLighOn = false;
    private boolean isFlashOn;
    private boolean hasFlash;
    float bright = 1;
    String dimScreen = "On ";
    CheckBox letBacklightDim;
    float prevDragX;
    Camera.Parameters params;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    PowerManager.WakeLock wl;

    long cnt = -1;
    AlertDialog alert;
    int pos1 = 1;
    TextView txtStatus;
    float xStartDrag;

    private boolean isFlashWasOn;
    DecimalFormat numbrF = new DecimalFormat("0.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
    }

    private void crt(){
        try {
            screenBackLightOnInitiate(100);
            Log.i("s2ner", "onCreate 1 ");
            Context context = this;
            cameraFlashSetup(context);
            Log.e("s2ner", "onCreate 2 ");
            //show1Dialog("init", "init done 3");
            View v = findViewById(R.id.topLayout);
            v.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                        view.startDrag(null, shadowBuilder, view, 0);
                        view.setVisibility(View.INVISIBLE);

                    } else {
                        view.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
            });
            v.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    float x = event.getX();
                    if (x > 0) prevDragX = x;
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_ENTERED:
                            Log.i("s2ner", "ACTION_DRAG_ENTERED " + event);
                            xStartDrag = x;
                            break;
                        case DragEvent.ACTION_DRAG_ENDED:
                            Log.i("s2ner", "ACTION_DRAG_ENDED " + event);
                            Log.i("onDrag", "x " + x + ", start x" + xStartDrag + ", pre " + prevDragX);
                            if (prevDragX < xStartDrag) {
                                positionAt(1);

                            } else {
                                positionAt(-1);
                            }
                            break;
                    }
                    return true;
                }

            });

        } catch (Throwable e) {
            Log.e("s2ner", "onCreate " + e, e);
        }
    }

    public void flashTogggle(View v) {
        cnt++;
        try {
            if(firstTime){
                showDialog("a dia", "hi there" + new java.util.Date());
                try {
                    Thread.sleep(200);
                }catch (Exception e){

                }
                crt();
                positionAt(0);
                firstTime = false;
            }
            if (!isFlashOn) {
                turnOnFlash();
            } else {
                turnOffFlash();
            }
        }catch (Throwable e2){
            Log.e("btn", "err " + e2,e2);

        }
    }


    private void cameraFlashSetup(Context context) {


        //showDialog("cameraFlashSetup ", "a cnt " + cnt );
        PackageManager pm = context.getPackageManager();
        // First check if device supports flashlight
        hasFlash = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!hasFlash) {
            showDialog("No Flash", "Your device does not have a flash light, that I can get hold of.");
            return;
        }
        getCamera();
    }

    private void showDialog(String ttle, String msg) {
        Toast.makeText(getApplicationContext(), ttle + " " + msg, Toast.LENGTH_LONG).show();
//        if (alert == null) {
//            alert = new AlertDialog.Builder(ScreenLightActivity.this)
//                    .create();
//        }
//        alert.setTitle(ttle);
//        alert.setMessage(msg);
//        alert.setButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                alert.hide();
//            }
//        });
//        alert.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFlashWasOn = false;
        if (isFlashOn) {
            isFlashWasOn = true;
            turnOffFlash();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // on resume turn on the flash
        if (isFlashWasOn) {
            isFlashWasOn = false;
            turnOnFlash();
        }
    }

    // Get the camera
    private void getCamera() {
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
    protected void onStart() {
        super.onStart();

        Log.i("s2ner", "onStart ");
    }


    // Turning On flash
    private void turnOnFlash() {
        Log.e("s2ner", "fl on ");
        //showDialog("On", "flash on. Current :" + isFlashOn);
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            // play sound
            //playSound();
            try {
                params = camera.getParameters();
                params.setFlashMode(Parameters.FLASH_MODE_TORCH);
                camera.setParameters(params);
                camera.startPreview();
            } catch (Throwable e) {

            }
            isFlashOn = true;

        }

    }

    private void turnOffFlash() {
        Log.e("s2ner", "fl off ");
        //showDialog("off", "turnOffFlash isFlash " + isFlashOn + ", has " + this.hasFlash);
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            // play sound
            // playSound();
            try {
                params = camera.getParameters();
                params.setFlashMode(Parameters.FLASH_MODE_OFF);
                camera.setParameters(params);
                camera.stopPreview();
            } catch (Throwable e) {

            }
            isFlashOn = false;
        }
    }

    void positionAt(int position) {
        try {
            WindowManager.LayoutParams layout = getWindow().getAttributes();
            pos1 += position;
            if (pos1 < 1) {
                pos1 = 10;
            } else if (pos1 > 10) {
                pos1 = 1;
            }
            if (pos1 < 10) {
                layout.screenBrightness = 1f - (((float) pos1 - 1f) / 10f);
            } else {
                layout.screenBrightness = 0.1f;
            }
            bright = layout.screenBrightness;
            txtStatus.setText(dimScreen + pos1 + " " + numbrF.format(bright));
            getWindow().setAttributes(layout);
        }catch (Throwable e){
            Log.i("posAT", "err " + e + pos1 + dimScreen,e);
        }
    }

    public void dimToggle(View v) {
        letBacklightDim = (CheckBox) v;
        Log.i("dimToggle", " ch " + letBacklightDim.isChecked());
        if (letBacklightDim.isChecked()) {
            screenBackLightOnInitiate(100);
            dimScreen = "On ";
        } else {
            screenBackLightNormal();
            dimScreen = "N ";
        }
        txtStatus.setText(dimScreen + pos1 + " " + numbrF.format(bright));
    }


    private void screenBackLightNormal() {
        try {
            if (wl != null) {
                wl.release();
                Log.i("s2ner", " wl re ");
            }
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            Log.i("s2ner", "screen backlight ini ");
        } catch (Throwable e) {
            Log.e("s2ner", "screenBackLightNormal " + e, e);
        }
        wl = null;
    }

    private void screenBackLightOnInitiate(int mode) {
        try {
            if (mode == 100) {
                mode = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            }
            getWindow().addFlags(mode);
            Log.e("s2ner", "screen backlight ini ");
            if(wl == null) {
                PowerManager pwr = (PowerManager) getSystemService(Context.POWER_SERVICE);
                wl = pwr.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "Bright Screen Torch");
                wl.setReferenceCounted(false);
                wl.acquire();
                Log.e("s2ner", " wl aq ");
            }
        } catch (Throwable e) {
            Log.e("s2ner", "screenBackLightOnInitiate " + e, e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_screen_light, menu);
        return true;//true;//false no menu
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
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } catch (Throwable e) {

        }
    }
}
