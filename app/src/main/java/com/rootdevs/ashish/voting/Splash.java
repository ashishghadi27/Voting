package com.rootdevs.ashish.voting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        int secondsDelayed = 1;

        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                        SharedPreferences sharedPreferences = getSharedPreferences("Signup", MODE_PRIVATE);
                        if(sharedPreferences.getString("signed", "0").equals("1")){
                            Intent intent = new Intent(Splash.this, Home.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Intent intent = new Intent(Splash.this, Signup.class);
                            startActivity(intent);
                            finish();
                        }
                }catch (Exception e)
                {
                    Toast.makeText(Splash.this,"App crashed", Toast.LENGTH_SHORT).show();
                }


                // close this activity

            }
        }, secondsDelayed * 1000);
    }

}
