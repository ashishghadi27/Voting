package com.rootdevs.ashish.voting;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Details extends AppCompatActivity {
    private TextView name, party, details;
    private ImageView candiimg, elecimg;
    private Button vote;
    private String name_str, pname, canimg_link, elec_link, det_str, id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        name_str = intent.getStringExtra("name");
        pname = intent.getStringExtra("pname");
        canimg_link = intent.getStringExtra("candidate_pic");
        elec_link = intent.getStringExtra("elec_symbol");
        det_str = intent.getStringExtra("details");
        id = intent.getStringExtra("id");

        name = findViewById(R.id.candinamedet);
        party = findViewById(R.id.candipartydet);
        details = findViewById(R.id.detailscandi);
        candiimg = findViewById(R.id.detimg);
        elecimg = findViewById(R.id.elec_symboldet);
        vote = findViewById(R.id.vote);

        name.setText(name_str);
        party.setText(pname);
        details.setText(det_str);
        Glide.with(this).load(canimg_link).override(500, 200).placeholder(R.drawable.mtwoi).error(R.drawable.mtwoi).into(candiimg);
        Glide.with(this).load(elec_link).override(200, 200).placeholder(R.drawable.mtwoi).error(R.drawable.mtwoi).into(elecimg);

        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
                String mail = sharedPreferences.getString("email", "");
                String consti = sharedPreferences.getString("consti", "");
                Background_Worker background_worker = new Background_Worker(v.getContext());
                background_worker.execute("vote", consti, id, mail);
                

            }
        });

    }
}
