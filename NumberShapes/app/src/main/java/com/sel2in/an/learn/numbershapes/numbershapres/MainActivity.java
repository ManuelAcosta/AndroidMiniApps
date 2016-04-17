package com.sel2in.an.learn.numbershapes.numbershapres;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void calc(View view){
        int errPos = 0;//not good
        String s = "N/a";
        try {
            EditText t = (EditText) findViewById(R.id.txtNo);
            TextView ans = (TextView) findViewById(R.id.ansTxt);

            s = t.getText().toString();
            if(s == null)s="";
            s = s.trim();
            if(s.length() == 0){
                Toast.makeText(getApplicationContext(), "Enter a number" , Toast.LENGTH_LONG).show();
                return;
            }
            errPos  = 1;
            double d = Double.parseDouble(s);
            errPos  = 2;
            NumberChecks nc = new NumberChecks(d);
            errPos  = 3;
            s += " is square " + nc.isSquare() + " and is triangular :" + nc.isTriangular();
            ans.setText(s);
        }catch (Throwable e){
            String es = null;
            if(errPos == 1){
                es = "Could not convert input to number :" + s ;
            }else{
                es = "Err " + e + ", code :" + errPos;
            }
            Toast.makeText(getApplicationContext(), es, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
