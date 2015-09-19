package com.sel2in.l1.lights2n;

import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    Camera camera;
    //TextView txtPos;
    private boolean isLighOn = false;
    private boolean isFlashOn;
    private boolean hasFlash;
    CheckBox letBacklightDim;
    Parameters params;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    PowerManager.WakeLock wl;
    boolean initDone = false;
    private boolean isFlashWasOn;
    long cnt = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public void flashTogggle(View v) {
        //showDialog("oChange ", "cnt " + cnt + " f " + isFlashOn );
        //txtPos = (TextView)findViewById(R.id.textView2);
        /*if(txtPos != null) {
            txtPos.setText(pos1 + " a");
        }else{
            showDialog("flashTogggle ", "cnt " + cnt + " f " + isFlashOn + ", "  + pos1);
        }*/
        cnt++;
        if (!isFlashOn) {
            turnOnFlash();
        } else {
            turnOffFlash();
        }
    }

    private void cameraFlashSetup(Context context) {
        //showDialog("cameraFlashSetup ", "a cnt " + cnt );
        PackageManager pm = context.getPackageManager();
        // First check if device is supporting flashlight or not
        hasFlash = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
            showDialog("No Flash", "Your device does not have a flash light; that I can get hold of.");
            return;
        }
        getCamera();

    }

    AlertDialog alert;

    private void showDialog(String ttle, String msg) {
        if (alert == null) {
            alert = new AlertDialog.Builder(MainActivity.this)
                    .create();
        }
        alert.setTitle(ttle);
        alert.setMessage(msg);
        alert.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alert.hide();
            }
        });
        alert.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFlashWasOn = false;
        if (isFlashOn) {
            isFlashWasOn = true;
            turnOffFlash();
        } else {
            isFlashWasOn = false;
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
            } catch (RuntimeException e) {
                Log.e("s2ner", "Camera Error. Failed to Open. Error: " + e, e);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!initDone) {
            initDone = true;
            letBacklightDim = (CheckBox) findViewById(R.id.letLightDim);
            try {
                try {
                   /* txtPos = (TextView)findViewById(R.id.textView2);
                    if(txtPos != null) {
                        txtPos.setText(pos1 + " b");
                    }*/
                    setContentView(R.layout.activity_main);
                    screenBackLightOnInitiate(100);
                    Log.e("s2ner", "init done 1 ");
                    //lshow1Dialog("init", "init done 1");
                    // Create the adapter that will return a fragment for each of the three
                    // primary sections of the activity.
                    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                    // Set up the ViewPager with the sections adapter.
                    mViewPager = (ViewPager) findViewById(R.id.pager);
                    mViewPager.setAdapter(mSectionsPagerAdapter);
                    Log.e("s2ner", "init done 2 ");
                    //show1Dialog("init", "init done 2");
                    Context context = this;
                    cameraFlashSetup(context);
                    Log.e("s2ner", "init done 3. ");
                    //show1Dialog("init", "init done 3");
                } catch (Exception e) {
                    Log.e("s2ner", "starting " + e, e);
                }
                letBacklightDim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            screenBackLightNormal();
                        } else {
                            screenBackLightOnInitiate(100);
                        }

                    }
                });
            } catch (Throwable e) {
                Log.e("s2ner", "init err " + e, e);
            }
        }
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

            // changing button/switch image
            //toggleButtonImage();
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

            // changing button/switch image
            //toggleButtonImage();
        }
    }

    int pos1 = -1;

    void positionAt(int position) {
        WindowManager.LayoutParams layout = getWindow().getAttributes();
        pos1 = position;
        // if(txtPos != null) {//DEBUG
        //txtPos.setText(pos1 + " a");
        // }

        if (letBacklightDim == null || (letBacklightDim != null && letBacklightDim.isChecked() == false)) {
            if (position == 2 || position == 3) {
                this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }
        if (position == 0 || position == 8) {
            layout.screenBrightness = 1F;
        } else if (position == 1 || position == 7) {
            layout.screenBrightness = 0.5F;
        } else if (position == 2 || position == 6) {
            layout.screenBrightness = 0.2F;
        } else if (position == 3) {
            layout.screenBrightness = 0.1F;
        } else if (position == 4) {
            layout.screenBrightness = 0.0F;
        } else if (position == 5) {
            layout.screenBrightness = -1F;
        }
        getWindow().setAttributes(layout);
    }

    private void screenBackLightNormal() {
        try {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            Log.e("s2ner", "screen backlight ini ");
            if (wl != null) {
                wl.release();
            }
        } catch (Exception e) {
            Log.e("s2ner", "screenBackLightOnInitiate " + e, e);
        }
    }

    private void screenBackLightOnInitiate(int mode) {
        try {
            if (mode == 100) {
                mode = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            }
            getWindow().addFlags(mode);
            Log.e("s2ner", "screen backlight ini ");
            PowerManager pwr = (PowerManager) getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pwr.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "Bright Screen Torch");
            wl.acquire();
        } catch (Exception e) {
            Log.e("s2ner", "screenBackLightOnInitiate " + e, e);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up btnFlash, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            positionAt(position);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            return "s2n " + position;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.e("s2ner", "stop ");
        try {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            if (wl != null) {
                try {
                    wl.release();
                } catch (Exception e) {
                    //Okay
                }
            }
            Log.e("s2ner", "stop 2. ");
            if (camera != null) {
                try {
                    camera.release();
                } catch (Exception e) {
                    //Okay
                }
            }
        } catch (Throwable e) {

        }
    }
}
