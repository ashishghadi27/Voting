package com.rootdevs.ashish.voting;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

import Interface.ItemClickListener;
import Model.Candidate_list;

import static android.content.Context.MODE_PRIVATE;

public class Candidate_Adapter extends RecyclerView.Adapter<Candidate_Adapter.MyViewHolder>  {

    private List<Candidate_list> listItems;
    private Context context;
    public String id;
    String TAG = "Check Onclick";
    private ItemClickListener itemClickListener;







    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, pname, consti, id, imgholder, symbol_holder;
        ImageView imgbutton, elec_symbol;
        CardView cv;
        //LinearLayout desc;
        //Button vote;

        public MyViewHolder(View view) {
            super(view);
            context = view.getContext();
            name = view.findViewById(R.id.candiname);
            pname = view.findViewById(R.id.candiparty);
            consti = view.findViewById(R.id.candiconsti);
            imgbutton = view.findViewById(R.id.candiimage);
            elec_symbol = view.findViewById(R.id.elec_symbol);
            imgholder = view.findViewById(R.id.candiimgholder);
            symbol_holder = view.findViewById(R.id.symb_linkholder);
            id = view.findViewById(R.id.candiid);
            cv = view.findViewById(R.id.cv);
            //desc = view.findViewById(R.id.desclay);
           // vote = view.findViewById(R.id.vote);
            view.setOnClickListener(this);
            /*vote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context,consti.getText().toString(),Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = context.getSharedPreferences("User", MODE_PRIVATE);
                    String mail = sharedPreferences.getString("email", "");
                    String consti = sharedPreferences.getString("consti", "");
                    Background_Worker background_worker = new Background_Worker(v.getContext());
                    background_worker.execute("vote", consti, id.getText().toString(), mail);
                }
            });*/

        }




        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context, Details.class);
            intent.putExtra("name", name.getText().toString());
            intent.putExtra("pname", pname.getText().toString());
            intent.putExtra("details", consti.getText().toString());
            intent.putExtra("elec_symbol", symbol_holder.getText().toString());
            intent.putExtra("candidate_pic", imgholder.getText().toString());
            intent.putExtra("id", id.getText().toString());
            v.getContext().startActivity(intent);

        }
    }


    public Candidate_Adapter(List<Candidate_list> title, Context context) {
        this.listItems = title;
        this.context = context;


    }






    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.candidate_lay, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Candidate_list list = listItems.get(position);
        holder.name.setText(list.getName());
        holder.pname.setText(list.getPname());
        holder.consti.setText(list.getDetails());
        holder.id.setText(list.getId());
        holder.imgholder.setText(list.getPicture());
        holder.symbol_holder.setText(list.getElec_symbol());
        Glide.with(context).load(list.getPicture()).placeholder(R.drawable.mtwoi).error(R.drawable.mtwoi).into(holder.imgbutton);
        Glide.with(context).load(list.getElec_symbol()).placeholder(R.drawable.mtwoi).error(R.drawable.mtwoi).into(holder.imgbutton);
        YoYo.with(Techniques.Landing).playOn(holder.cv);



    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


}

