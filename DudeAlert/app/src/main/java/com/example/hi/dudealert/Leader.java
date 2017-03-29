package com.example.hi.dudealert;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.Thread.sleep;

public class Leader extends AppCompatActivity {

    ImageView leaderimg;
    TextView rating,selected,viewed;
    String s="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader);

        leaderimg= (ImageView) findViewById(R.id.leaderimg);

        rating= (TextView) findViewById(R.id.rating);
        selected= (TextView) findViewById(R.id.selected);
        viewed= (TextView) findViewById(R.id.viewed);

        s="http://dudealert.000webhostapp.com/dude/leader.php";
        MyAsync task5 = new MyAsync();
        task5.execute(s);

        final ProgressDialog dialog= ProgressDialog.show(Leader.this,"","Getting Leader..");
        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    dialog.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();

    }

    public class MyAsync extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {

            String data="",webPage="";
            try {
                URL url=new URL(strings[0]);

                try {
                    HttpURLConnection con=(HttpURLConnection)url.openConnection();// Opening browser and entering url
                    con.connect();						//going to url...
                    InputStream is = con.getInputStream();	//getting response from the page...
                    BufferedReader reader =new BufferedReader(new InputStreamReader(is, "UTF-8"));	//converting it into string..


                    while ((data = reader.readLine()) != null){
                        webPage += data + "\n";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return "error";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "error";
            }
            return webPage;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            s=s.trim();
            if(s.equals("error"))
                Toast.makeText(Leader.this,"Cannot connect to server..Please try later",Toast.LENGTH_LONG).show();
            else
            {
                String temp[] = s.split(",");
                String image = "http://dudealert.000webhostapp.com/dude/";
                image = image + temp[0] + ".jpg";
                Picasso.with(Leader.this).load(image).into(leaderimg);

                rating.setText("Rating : " + temp[3] + "%");
                selected.setText("Selected : " + temp[2]);
                viewed.setText("Viewed : " + temp[1]);
            }
        }
    }
}
