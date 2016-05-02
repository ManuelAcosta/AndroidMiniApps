package com.sel2in.an.test.dtraw.draw1;

import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import java.util.Random;

public class C3Activity extends AppCompatActivity {

    public static final String PREFS_NAME = "ticTacToePrefs";


    public static final int NEW_STATE = 0;
    public static final int START_STATE = 1;
    public static final int PROGRESS_STATE = 4;
    public static final int END_STATE = 9;

    int w3 = 300, h3 = 300, imgH = 100, imgW = 100, h2 = 300, w2 = 280;
    /**
     * game stats depends on the values of these 0,1,2
     */
    final static int CLR_CELL = 0;
    final static int RED_CELL = 1;
    final static int Y_CELL = 2;
    final static String[] Cell_Names = {"Draw", "Red", "Yellow"};
    /**
     * using 0 as cout for draw, 1 for red wins and 2 for yellow.
     * Usung constants CLR_CELL, RED_CELL and Y_CELL
     */
    static int[] gameStats = new int[3];//draw, Red won or Yellow won.
    static int[] gameAllStats = new int[3];//draw, Red won or Yellow won.
    //status if the 9 squares
    int[] cells = new int[9];
    Intent intentAbout = null;
    ImageView[] cellImgs = new ImageView[9];
    int turn = RED_CELL;
    TextView txtStatus;
    private int state = NEW_STATE;
    //private boolean sureNew = false;
    private int moveCount;
    private Random rnd = new Random(343);

    private volatile boolean animOn = false;

   /* public void newGame(View newgameActionUi) {
        Button btn = (Button) newgameActionUi;
        if (state == PROGRESS_STATE) {
            if (sureNew) {
                newGame();
            } else {
                sureNew = true;
                Toast.makeText(getApplicationContext(), "Tap again to confirm, drop this game after " + moveCount + " moves & new game? On " + new java.util.Date() + " .", Toast.LENGTH_LONG).show();
            }

        } else {

            newGame();
        }

    }*/

    void newGame() {
        findViewById(R.id.playAgainLayout).setVisibility(View.INVISIBLE);
        //findViewById(R.id.gridLayout).setVisibility(View.VISIBLE);
        this.txtStatus.setVisibility(View.INVISIBLE);
        moveCount = 0;
        //sureNew = false;
        state = PROGRESS_STATE;
        for (int i = 0; i < cells.length; i++) {
            cells[i] = CLR_CELL;
            cellImgs[i].setImageDrawable(null);
            //cellImgs[i].setBackground(R.drawable.border);
            cellImgs[i].setBackgroundResource(R.drawable.border);
        }
        turn = RED_CELL;
        //newBtn.setText("New game");
    }

    public void squareClick(View squareClickUi) {
        if (animOn || state != PROGRESS_STATE) {
            errTone(ToneGenerator.TONE_DTMF_D, 75);
            return;
        }
        //img.setImageResource(R.drawable.f2);
        int tag = -1;
        try {
            tag = Integer.parseInt(squareClickUi.getTag().toString());
            if (tag < 0 || tag > (cells.length - 1)) {
                Toast.makeText(getApplicationContext(), "squareClick Err tag (0 based) :" + tag, Toast.LENGTH_LONG).show();
                Log.w("squareClick", "squareClick err bad tag " + tag);
            }
            if (cells[tag] == CLR_CELL) {
                play(tag, turn);
                if (turn == RED_CELL) {
                    turn = Y_CELL;
                } else {
                    turn = RED_CELL;
                }

            } else {
                Toast.makeText(getApplicationContext(), "Cell " + (tag + 1) + " already played!", Toast.LENGTH_SHORT).show();
                errTone(ToneGenerator.TONE_DTMF_S, 340);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "squareClick Err " + e, Toast.LENGTH_LONG).show();
            Log.w("squareClick", "squareClick err " + e + ", tag " + tag, e);
        }

    }

    private void errTone(int toneId, int duration) {
        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        toneG.startTone(toneId, duration);
    }

    //current turn set img & animate
    private void play(int tag, int turn) {
        animOn = true;
        moveCount++;
        cells[tag] = turn;
        int signX = 1;
        int signY = 1;
        if (rnd.nextInt(2) == 1) signX = -1;
        if (rnd.nextInt(2) == 1) signY = -1;

        int minX = (int) (imgW + 0.5 * imgW);
        int minY = (int) (imgH + 0.5 * imgH);
        float xD = (rnd.nextInt(w2) + minX) * signX;
        float yD = (rnd.nextInt(h2) + minY) * signY;
        Log.i("play sign", "x sign " + signX + ", sign Y " + signY + ", xD :" + xD + ", y D :" + yD);
        cellImgs[tag].setBackgroundResource(R.drawable.border_none);
        if (turn == RED_CELL) {
            cellImgs[tag].setImageResource(R.drawable.red);
        } else {
            cellImgs[tag].setImageResource(R.drawable.yellow);
        }
        boolean arcAnim = true;
        if (arcAnim) {
            ArcTranslateAnimation anim = new ArcTranslateAnimation(xD, 0, yD, 0);
            anim.setDuration(1100);
            cellImgs[tag].startAnimation(anim);
        } else {
            cellImgs[tag].setX(cellImgs[tag].getX() + xD);
            cellImgs[tag].setY(cellImgs[tag].getY() + yD);
            cellImgs[tag].animate().xBy(-xD).yBy(-yD).setDuration(1100).start();
        }
        new CheckGameAfterAnimTask().execute();
        //new CheckGameAfterAnimTask().doInBackground(null);
    }

    /**
     * 0, 1, 2,
     * 3, 4, 5,
     * 6, 7, 8
     */
    private void checkGameStatus() {
        try {
            int i = 0;
            int j = 0;
            int n = 0, n2 = 0;
            for (i = 0; i < 9; i += 3) {
                n = i + 1;
                n2 = n + 1;
                checkWon(i, n, n2);
                if (state == END_STATE) return;
            }

            for (i = 0; i < 3; i++) {
                n = i + 3;
                n2 = n + 3;
                checkWon(i, n, n2);
                if (state == END_STATE) return;
            }
            //inclines via formula if it was a bigger grid
            checkWon(0, 4, 8);
            if (state == END_STATE) return;
            checkWon(2, 4, 6);
            if (state == END_STATE) return;
            //any cell can be played on?
            for (i = 0; i < 9; i++) {
                if (cells[i] == CLR_CELL) {
                    return;//not draw as yet
                }
            }
            doWon(i, true);//draw as not a win and no square clear
        } catch (Throwable e) {
            Log.e("checkStatus", "err " + e, e);
        }
    }

    private void checkWon(int firstIndex, int i2, int i3) {
        if (cells[firstIndex] == cells[i2] && cells[i2] == cells[i3] && cells[firstIndex] > CLR_CELL) {
            doWon(firstIndex, false);
            errTone(ToneGenerator.TONE_DTMF_A, 80);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void doWon(int i, boolean draw) {
        state = END_STATE;
        int whoWon = CLR_CELL;
        String colorWon = null;
        //newBtn.setText("New game");
        String sd = null;
        if (draw) {
            errTone(ToneGenerator.TONE_DTMF_P, 160);
            sd = "Draw Game" + ", on " + new java.util.Date() + " .";
        } else {
            whoWon = cells[i];
            sd = Cell_Names[whoWon] + " won game after " + moveCount + " moves. On " + new java.util.Date() + ".";
        }
        gameStats[whoWon]++;
        gameAllStats[whoWon]++;
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("WINS_" + Cell_Names[whoWon], gameAllStats[whoWon]);
        editor.commit();

        sd += getStatsFormatted();
        Toast.makeText(getApplicationContext(), sd, Toast.LENGTH_LONG).show();
        this.txtStatus.setText(sd);
        Log.i("stats", "Draws " + gameStats[0] + ", " + Cell_Names[1] + " " + gameStats[1] + ", " + Cell_Names[2] + " " + gameStats[2]);
        findViewById(R.id.playAgainLayout).setVisibility(View.VISIBLE);
    }

    @NonNull
    public static String getStatsFormatted() {
        return "\nThis session : " + gameStats[0] + " " + Cell_Names[0] + ", " + gameStats[1] + " " + Cell_Names[1] + " and "
                + gameStats[2] + " " + Cell_Names[2] + ".";
    }

    public static String getStatsAllFormatted() {
        return "\nAll games: " + gameAllStats[0] + " " + Cell_Names[0] + ", " + gameAllStats[1] + " " + Cell_Names[1] + " and "
                + gameAllStats[2] + " " + Cell_Names[2] + ".";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //gameAllStats;
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            gameAllStats[RED_CELL] = settings.getInt("WINS_" + Cell_Names[RED_CELL], 0);
            gameAllStats[Y_CELL] = settings.getInt("WINS_" + Cell_Names[Y_CELL], 0);
            gameAllStats[CLR_CELL] = settings.getInt("WINS_" + Cell_Names[CLR_CELL], 0);

            setContentView(R.layout.activity_c3);
            txtStatus = (TextView) findViewById(R.id.txtStatus);
            int i = 0;
            cellImgs[0] = (ImageView) findViewById(R.id.imageView1);
            ViewGroup parent = (ViewGroup) cellImgs[0].getParent();
            Display display = getWindowManager().getDefaultDisplay();
            Point screenSize = new Point();
            display.getSize(screenSize);
            w3 = (int) (screenSize.x / 3);
            int margin = 25;
            if (w3 > 500) w3 = 500 + (int) (w3 * .1);//10% of real + 500
            //ViewGroup.LayoutParams params = new ViewGroup.LayoutParams ;

            h3 = (int) (screenSize.y / 3);
            if (h3 > 500) h3 = 500 + (int) (h3 * .1);//10% of real + 500
            float fact = h3 / w3;
            if (fact > 2) {
                h3 = 2 * w3;
            } else if (fact < 0.5) {
                h3 = (int) (0.5 * w3);
            }

            android.widget.LinearLayout.LayoutParams paramsL = null;
            if (imgW < 40) margin = 10;
            imgH = h3 - (margin * 2);
            imgW = w3 - (margin * 2);
            if (imgW > 100) {
                imgW = (int) (0.9 * imgW);
            }
            if (imgH > 100) {
                imgH = (int) (0.9 * imgH);
            }
            float hwRatio = imgH / imgW;
            if (hwRatio > 1.25f) {
                imgH = (int) (1.25f * imgW);
            } else if (hwRatio < 0.75f) {
                imgW = (int) (1.25f * imgH);
            }
            OnClickAct clickAct = new OnClickAct(this);
            parent.setOnTouchListener(clickAct);
            h2 = h3 - imgH;
            w2 = w3 - imgW;
            Log.i("positions", "img W " + imgW + ", img H " + imgH + ", margin " + margin + ", h3 " + h3 + ", w3 " + w3);

            for (i = 0; i < cellImgs.length; i++) {
                if (i > 0) {
                    cellImgs[i] = new ImageView(this);
                    parent.addView(cellImgs[i]);
                }

                cellImgs[i].setTag(i);
                int xpos = (int) margin + (imgW * (i % 3));
                int ypos = margin + (int) (imgH * Math.floor(i / 3));
                Log.i("positions", " xpos :" + xpos + ", y pos :" + ypos + "img W " + imgW + ", img H " + imgH + ", margin " + margin + ", h3 " + h3 + ", w3 " + w3);
                cellImgs[i].setLeft(xpos);
                cellImgs[i].setTop(ypos);
                parent.bringChildToFront(cellImgs[i]);
                cellImgs[i].setOnTouchListener(clickAct);
                paramsL = new android.widget.LinearLayout.LayoutParams(imgW, imgW);
                paramsL.setMargins(xpos, ypos, margin, margin);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(paramsL);
                cellImgs[i].setLayoutParams(params);
                cellImgs[i].setOnClickListener(clickAct);
            }
            View playAgain = findViewById(R.id.playAgainLayout);
            playAgain.setOnTouchListener(clickAct);
            parent.bringChildToFront(playAgain);
            parent.invalidate();
        } catch (Exception e) {
            Log.i("init", " Err create " + e, e);
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
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new) {
            newGame();
            return true;
        }
        if (intentAbout == null) {
            intentAbout = new Intent(this, AboutActivity.class);
        }
        //intentAbout.init();
        startActivity(intentAbout);

        return super.onOptionsItemSelected(item);
    }

    private class CheckGameAfterAnimTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            checkGameStatus();
            animOn = false;
        }

        @Override
        protected String doInBackground(Void... params) {
            animOn = true;
            sleep(1150);
            return "done";
        }
    }

    void sleep(long m) {
        try {
            Thread.sleep(m);
        } catch (Exception e) {
        }
    }

    public boolean getAnimOn() {
        return animOn;
    }

    public int getState() {
        return state;
    }

    class Log1 implements Runnable {
        public void run() {
            for (int i = 1; i < cellImgs.length; i++) {
                Log.i("postInit", "i " + i + " left " + cellImgs[i].getLeft() + " top " + cellImgs[i].getTop());
            }
        }

    }
}
