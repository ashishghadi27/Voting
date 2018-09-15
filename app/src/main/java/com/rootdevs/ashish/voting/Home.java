package com.rootdevs.ashish.voting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Model.Candidate_list;

public class Home extends AppCompatActivity {
    private String constituency="", jsonurl="";
    private RecyclerView recyclerView;
    private List<Candidate_list> listItems;
    private Candidate_Adapter adapter;
    String TAG = "Check ADAPTER", id;
    SwipeRefreshLayout mSwipeRefresh;
    TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        constituency = sharedPreferences.getString("consti", "");
        Log.v("OUT", constituency);
        /*try{
            Intent intent = getIntent();
            constituency = intent.getStringExtra("consti");
            if(constituency.equals("")||constituency.equals(null)){
                constituency = sharedPreferences.getString("consti", "");
                Log.v("IN", constituency);
            }
        }catch (Exception e){
            constituency = sharedPreferences.getString("consti", "");
            Log.v("Catch", constituency);
        }*/

        mSwipeRefresh = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        msg = (TextView)findViewById(R.id.message);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);




        listItems = new ArrayList<>();





        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listItems.clear();
                loaddata();
                mSwipeRefresh.setRefreshing(false);

            }
        });

        adapter = new Candidate_Adapter(listItems, getApplicationContext());
        recyclerView.setAdapter(adapter);
        loaddata();




    }

    public void loaddata(){
        Background_Worker background_worker = new Background_Worker(Home.this);
        background_worker.execute("candidate", constituency);

        SharedPreferences sharedPreferences11 = getSharedPreferences("Candidate",MODE_PRIVATE);
        jsonurl = sharedPreferences11.getString("data","[]");
        try {
            JSONArray jsonArray = new JSONArray(jsonurl);
            for(int i=0; i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String details = jsonObject.getString("details");
                String picture = jsonObject.getString("picture");
                String elec_symbol = jsonObject.getString("elec_symbol");
                String pname = jsonObject.getString("pname");
                Candidate_list list = new Candidate_list(id,name,details,picture,elec_symbol,pname);
                listItems.add(list);
                msg.setVisibility(View.GONE);
                adapter = new Candidate_Adapter(listItems, getApplicationContext());
                recyclerView.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
            }
                adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
