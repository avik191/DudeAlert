package com.example.hi.dudealert;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {

    ImageView img1,img2;
    String s="",u="",h="",k="",t="";
    ProgressDialog dialog;
    int x,y;
    TextView tv;
    int max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        img1= (ImageView) findViewById(R.id.img1);
        img2= (ImageView) findViewById(R.id.img2);
        tv= (TextView) findViewById(R.id.note);
        s="http://dudealert.000webhostapp.com/dude/";
        u="http://dudealert.000webhostapp.com/dude/updaterating.php?id=";
        h="http://dudealert.000webhostapp.com/dude/update.php?id=";

        Bundle b=getIntent().getExtras();
        max=b.getInt("max");
        //Toast.makeText(MainActivity.this, t, Toast.LENGTH_SHORT).show();

        x=getRandom();
        y=getRandom();

        while(x==y)
            y=getRandom();

        String p=h+x;
        MyAsync task = new MyAsync();
        task.execute(p);

        String w=h+y;
        MyAsync task2 = new MyAsync();
        task2.execute(w);

        String img_one=s+x+".jpg";
        String img_two=s+y+".jpg";
        Picasso.with(this).load(img_one).into(img1);
        Picasso.with(this).load(img_two).into(img2);



    }

    public void change(View v)
    {
        switch(v.getId())
        {
            case R.id.img1 :    String p=u+x;
                                MyAsync task = new MyAsync();
                                task.execute(p);
                                //Toast.makeText(MainActivity.this, p+" switch", Toast.LENGTH_SHORT).show();
                                break;

            case R.id.img2 :    String j=u+y;
                                MyAsync task2 = new MyAsync();
                                task2.execute(j);
                                break;
        }
        dialog=ProgressDialog.show(MainActivity.this,"","Loading images..");
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
        x=getRandom();
        y=getRandom();

        while(x==y)
            y=getRandom();

        String p=h+x;
        MyAsync task3 = new MyAsync();
        task3.execute(p);

        String w=h+x;
        MyAsync task4 = new MyAsync();
        task4.execute(w);

        String img_one=s+x+".jpg";
        String img_two=s+y+".jpg";
        Picasso.with(this).load(img_one).into(img1);
        Picasso.with(this).load(img_two).into(img2);


    }

    public int getRandom()
    {
        return (int)((Math.random()*(max-1+1))+1);
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
                        webPage += data + "\n";
                    }
                    //Toast.makeText(MainActivity.this, "async2", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                   // webPage=e.toString();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                //webPage=e.toString();
            }
            return webPage;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            //tv.setText(s);
            t=s;
        }
    }
}
