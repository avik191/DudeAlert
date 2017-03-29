package com.example.hi.dudealert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Home extends AppCompatActivity {

    TextView head;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        head= (TextView) findViewById(R.id.head);
        SharedPreferences app_preferences= PreferenceManager.getDefaultSharedPreferences(Home.this);
        String name=app_preferences.getString("name","");

        head.setText("What do u want to do, "+name+"?");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.about) {
            Intent i=new Intent(Home.this,About.class);
            startActivity(i);
        }
        if (id == R.id.exit) {
            finish();
        }


        return super.onOptionsItemSelected(item);
    }


    public void startrating(View v)
    {
        ConnectivityManager check = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo info = check.getActiveNetworkInfo();			//for checking whether app is connected to a network or not..
        if(info!=null && info.isConnected()) {
            intent = new Intent(Home.this, MainActivity.class);
            String k = "http://dudealert.000webhostapp.com/dude/count.php";
            MyAsync task5 = new MyAsync();
            task5.execute(k);
        }
        else
            Toast.makeText(Home.this,"No Internet Connection",Toast.LENGTH_LONG).show();

    }

    public void leaderboard(View v)
    {
        ConnectivityManager check = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo info = check.getActiveNetworkInfo();			//for checking whether app is connected to a network or not..
        if(info!=null && info.isConnected()) {
            intent = new Intent(Home.this, Leader.class);
            startActivity(intent);
        }
        else
            Toast.makeText(Home.this,"No Internet Connection",Toast.LENGTH_LONG).show();

    }

    public void account(View v)
    {
        Intent i=new Intent(Home.this,Account.class);
        startActivity(i);

    }


    public class MyAsync extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {

            String data="",webPage="";
            try {
                URL url=new URL(strings[0]);
                //Toast.makeText(MainActivity.this, "async1", Toast.LENGTH_SHORT).show();

                try {
                    HttpURLConnection con=(HttpURLConnection)url.openConnection();// Opening browser and entering url
                    con.connect();						//going to url...
                    InputStream is = con.getInputStream();	//getting response from the page...
                    BufferedReader reader =new BufferedReader(new InputStreamReader(is, "UTF-8"));	//converting it into string..


                    while ((data = reader.readLine()) != null){
                        webPage += data;
                    }
                    //Toast.makeText(MainActivity.this, "async2", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    // webPage=e.toString();
                    return "error";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                //webPage=e.toString();
                return "error";
            }
            return webPage;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            s=s.trim();
            if(s.equals("error"))
                Toast.makeText(Home.this,"Cannot connect to server..Please try later",Toast.LENGTH_LONG).show();
            else
            {
                int n = Integer.parseInt(s);
                // Toast.makeText(Home.this,n+"",Toast.LENGTH_SHORT).show();
                Bundle b = new Bundle();
                b.putInt("max", n);
                intent.putExtras(b);
                startActivity(intent);
            }

        }
    }
}
