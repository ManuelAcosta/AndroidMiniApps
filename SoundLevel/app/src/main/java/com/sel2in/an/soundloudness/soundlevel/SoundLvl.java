package com.sel2in.an.soundloudness.soundlevel;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class SoundLvl extends AppCompatActivity {

    int dbgCnt = 0;
    DecimalFormat df = new DecimalFormat("#.00");
    TextView statusText;
    TextView dt;
    TextView ampl;
    MediaRecorder mRecorder;
    ProgressBar levelBar;
    CheckBox enabled;
    //Thread runner;
    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;
    ScheduledExecutorService scheduler = null;
    boolean running = false;

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            updateSoundlevel();
        }
    };

    public void recordChange(View view) {
        statusText.setText("Chng " + enabled.isChecked());
        if (enabled.isChecked()) {
            startRecorder();
        } else {
            stopRecorder();
        }

    }

    void debug(String s) {
        if (dbgCnt < 9) {
            dt.setText(dt.getText() + "\n" + s);
            dbgCnt++;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_lvl);
        //detectSoundCheck

        levelBar = (ProgressBar) findViewById(R.id.levelBar);
        statusText = (TextView) findViewById(R.id.statusText);
        ampl= (TextView) findViewById(R.id.txtAmpl);
        enabled = (CheckBox) findViewById(R.id.detectSoundCheck);
        levelBar.setMax(150);
        levelBar.setProgress(20);

        statusText.setText("Detecting");
        dt = (TextView) findViewById(R.id.dbg);

    }


    public void onResume() {
        super.onResume();
        if (enabled.isChecked()) {
            try {
                startRecorder();
            } catch (Throwable e) {
                Log.e("r", " Err " + e, e);
            }
        }

    }

    public void onPause() {
        super.onPause();
        try {
            stopRecorder();

        } catch (Throwable e) {
            Log.e("r", " Err " + e, e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sound_lvl, menu);
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


    public void startRecorder() {
        if (running) {
            statusText.setText("Already runing");
            return;
        }
        try {
            if (mRecorder == null) {
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mRecorder.setOutputFile("/dev/null");
                try {
                    mRecorder.prepare();
                    mRecorder.start();
                    running = true;
                    statusText.setText("Runing");
                    debug("started 1");
                    scheduler = Executors.newSingleThreadScheduledExecutor();
                    scheduler.scheduleAtFixedRate
                            (new Runnable() {
                                public void run() {
                                    mHandler.obtainMessage(1).sendToTarget();
                                }
                            }, 0, 700, TimeUnit.MILLISECONDS);
                } catch (java.lang.Throwable e) {
                    android.util.Log.e("[startRecorder]", "SecurityException: " +
                            android.util.Log.getStackTraceString(e));
                    statusText.setText("Err rec start 1 :" + e);
                }

                //mEMA = 0.0;
            }
        } catch (Throwable e2) {
            Log.e("Start", " Err " + e2, e2);
            statusText.setText("Err s2:" + e2);
        }


    }

    public void stopRecorder() {

        try {
            scheduler.shutdown();
            if (mRecorder != null) {
                try {
                    mRecorder.stop();
                    mRecorder.release();
                } catch (Throwable e) {
                    Log.e("stop", "Err " + e, e);
                }
                mRecorder = null;

            }
        } catch (Throwable e2) {
            Log.e("Stop", " Err " + e2, e2);
        }

        running = false;
    }

    boolean first = true;
    public void updateSoundlevel() {
        //statusText.setText(Double.toString((getAmplitudeEMA())) + " dB");
        String d = "up ";
        try {


            double dd = getAmplitudeEMA();
            d = d + dd;
            int l = (int) dd;
            int iampl = 32767;
            try {
                iampl = Integer.parseInt(ampl.getText().toString());
            }catch (Exception e){}
            int d3 =  (int)soundDbF(iampl);
            statusText.setText(d3 + " dB " + df.format(dd) + " am " + iampl);
            levelBar.setProgress(d3);
            if(first){
                first = false;
                debug("progress bar " + iampl + " " +  levelBar.isIndeterminate());
            }
        } catch (Throwable e2) {
            Log.e("updateSoundlevel", " Err " + e2, e2);
            d = d + " " + e2;
            debug(d);
        }

    }


    public double soundDbF(double ampl) {
        return 20 * Math.log10(getAmplitudeEMA() / ampl);
    }

    public double soundDb(double ampl) {
        return 20 * Math.log10(getAmplitudeEMA() / ampl);
    }

    public double getAmplitude() {
        if (mRecorder != null)
            return (mRecorder.getMaxAmplitude());
        else
            return 0;

    }

    public double getAmplitudeEMA() {
        double amp = getAmplitude();
        //mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return amp;
    }


}
