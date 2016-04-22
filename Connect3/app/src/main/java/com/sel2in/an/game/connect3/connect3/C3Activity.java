package com.sel2in.an.game.connect3.connect3;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.util.Random;

public class C3Activity extends AppCompatActivity {

    public final int NEW_STATE = 0;
    public final int START_STATE = 1;
    public final int PROGRESS_STATE = 4;
    public final int END_STATE = 9;
    /**
     * game stats depends on the values of these 0,1,2
     */
    final int CLR_CELL = 0;
    final int RED_CELL = 1;
    final int Y_CELL = 2;
    final String[] Cell_Names = {"Draw", "Red", "Yellow"};
    /**
     * using 0 as cout for draw, 1 for red wins and 2 for yellow.
     * Usung constants CLR_CELL, RED_CELL and Y_CELL
     */
    int[] gameStats = new int[3];//draw, Red won or Yellow won.
    //status if the 9 squares
    int[] cells = new int[9];
    ImageView[] cellImgs = new ImageView[9];
    int turn = RED_CELL;
    TextView txtStatus;
    Button newBtn = null;
    boolean animOn = false;
    private int state = NEW_STATE;
    private boolean sureNew = false;
    private int moveCount;
    private Random rnd = new Random(343);

    public void newGame(View newgameActionUi) {
        Button btn = (Button) newgameActionUi;
        if (state == PROGRESS_STATE) {
            if (sureNew) {
                newGame();
            } else {
                sureNew = true;
                newBtn.setText("Sure New?");
                Toast.makeText(getApplicationContext(), "Tap again to confirm, drop this game after " + moveCount + " moves & new game? On " + new java.util.Date() + " .", Toast.LENGTH_LONG).show();
            }

        } else {
            newGame();
        }

    }

    void newGame() {
        findViewById(R.id.playAgainLayout).setVisibility(View.INVISIBLE);
        //findViewById(R.id.gridLayout).setVisibility(View.VISIBLE);
        moveCount = 0;
        sureNew = false;
        state = NEW_STATE;
        for (int i = 0; i < cells.length; i++) {
            cells[i] = CLR_CELL;
            cellImgs[i].setImageDrawable(null);
        }
        turn = RED_CELL;
        newBtn.setText("New game");
    }

    public void squareClick(View squareClickUi) {
        if (animOn) {
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
                Toast.makeText(getApplicationContext(), "This cell is already " + +(tag + 1) + " played!", Toast.LENGTH_SHORT).show();
                errTone(ToneGenerator.TONE_DTMF_S, 240);
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
        moveCount++;
        cells[tag] = turn;
        float xD = (rnd.nextInt(150) + 50f) * (1 - rnd.nextInt(3));
        float yD = (rnd.nextInt(100) + (50f)) * (1 - rnd.nextInt(3));
        ArcTranslateAnimation anim = new ArcTranslateAnimation(xD, 0, yD, 0);
        if (turn == RED_CELL) {
            cellImgs[tag].setImageResource(R.drawable.red);
        } else {
            cellImgs[tag].setImageResource(R.drawable.yellow);
        }
        anim.setDuration(1850);
        cellImgs[tag].startAnimation(anim);
        new CheckGameAfterAnimTask().execute();
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
            state = END_STATE;
            doWon(firstIndex, false);
            errTone(ToneGenerator.TONE_DTMF_A, 80);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void doWon(int i, boolean draw) {
        int whoWon = CLR_CELL;
        String colorWon = null;
        newBtn.setText("New game");
        String sd = null;
        if (draw) {
            errTone(ToneGenerator.TONE_DTMF_P, 160);
            sd = "Draw Game" + ", on " + new java.util.Date() + " .";
        } else {
            whoWon = cells[i];
            gameStats[whoWon]++;
            sd = Cell_Names[whoWon] + " won game after " + moveCount + " moves. On " + new java.util.Date() + ".";
        }
        Toast.makeText(getApplicationContext(), sd, Toast.LENGTH_LONG).show();
        this.txtStatus.setText(sd);
        Log.i("stats", "Draws " + gameStats[0] + ", " + Cell_Names[1] + " " + gameStats[1] + ", " + Cell_Names[2] + " " + gameStats[2]);
        findViewById(R.id.playAgainLayout).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c3);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        int i = 0;
        newBtn = (Button) findViewById(R.id.newBtn);
        cellImgs[i++] = (ImageView) findViewById(R.id.imageView1);
        cellImgs[i++] = (ImageView) findViewById(R.id.imageView2);
        cellImgs[i++] = (ImageView) findViewById(R.id.imageView3);
        cellImgs[i++] = (ImageView) findViewById(R.id.imageView4);
        cellImgs[i++] = (ImageView) findViewById(R.id.imageView5);
        cellImgs[i++] = (ImageView) findViewById(R.id.imageView6);
        cellImgs[i++] = (ImageView) findViewById(R.id.imageView7);
        cellImgs[i++] = (ImageView) findViewById(R.id.imageView8);
        cellImgs[i++] = (ImageView) findViewById(R.id.imageView9);
        newGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_c3, menu);
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

    private class CheckGameAfterAnimTask extends AsyncTask<Void, Void, String> {


        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Long result) {

        }




        @Override
        protected String doInBackground(Void... params) {
            animOn = true;
            try {
                Thread.sleep(1450);
            } catch (Exception e) {
            }
            checkGameStatus();
            animOn =false;
            return "done";
        }
    }
}
