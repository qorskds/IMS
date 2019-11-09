package com.example.ims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class record extends AppCompatActivity {
    RecyclerView  recyclerView;
    FirebaseDatabase database;
    List<Infu> minfu = new ArrayList<>();
    FirebaseUser use;
    madapter mad;
    Spinner yearSpinner;
    Spinner monthSpinner;
    SimpleDateFormat year;
    SimpleDateFormat month;
    String styear;
    TextView x;
    Object tmp;
    Date td = new Date(System.currentTimeMillis());
    SimpleDateFormat tds = new SimpleDateFormat("MM");
    String p;

    String s;

    SimpleDateFormat monthsimple = new SimpleDateFormat("MM");
    String mon;



    int flag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        database= FirebaseDatabase.getInstance();
        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        year = new SimpleDateFormat("yyyy년");
        month = new SimpleDateFormat("M월");

        p = tds.format(td).toString();
        mon = monthsimple.format(td).toString();



        mad = new madapter();
        recyclerView.setAdapter(mad);
        use = FirebaseAuth.getInstance().getCurrentUser();
        yearSpinner = (Spinner)findViewById(R.id.spinnerYear);
        monthSpinner = (Spinner)findViewById(R.id.spinnerMonth);



        //년도 눌렀을때 리스너
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, View view, int i, long l) {
                tmp = adapterView.getItemAtPosition(i);
                s = tmp.toString().substring(0,4);
                database.getReference().child(use.getDisplayName()).child(s).child(mon).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        minfu.clear();
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            Date gototime = snapshot.child("gototime").getValue(Date.class);
                            Date offtime = snapshot.child("offtime").getValue(Date.class);



                            Infu infu =new Infu(gototime,offtime);




                            if(tmp.toString().equals(year.format(infu.gototime))
                                    && monthSpinner.getSelectedItem().toString().equals(month.format(infu.gototime))){

                                minfu.add(infu);

                            }
                        }
                        mad.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //달 눌렀을때 리스너
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, View view, int i, long l) {
                tmp = adapterView.getItemAtPosition(i);
                database.getReference().child(use.getDisplayName()).child(s).child(mon).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        minfu.clear();
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            Date gototime = snapshot.child("gototime").getValue(Date.class);
                            Date offtime = snapshot.child("offtime").getValue(Date.class);


                            Infu infu =new Infu(gototime,offtime);


                            if(tmp.toString().equals(month.format(infu.gototime))
                                    && yearSpinner.getSelectedItem().toString().equals(year.format(infu.gototime))){

                                minfu.add(infu);

                            }

                        }
                        mad.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    class madapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            SimpleDateFormat t = new SimpleDateFormat("yyyy년MM월dd일 hh시 mm분");


            ((CustomViewHolder)holder).textView.setText(use.getDisplayName());
            ((CustomViewHolder)holder).textView2.setText(t.format(minfu.get(position).gototime));
            if(minfu.get(position).offtime == null){
                ((CustomViewHolder)holder).textView3.setText(" ");
            }else{
                ((CustomViewHolder)holder).textView3.setText( t.format(minfu.get(position).offtime));

            }
        }

        @Override
        public int getItemCount() {
            return minfu.size();
        }
        private class  CustomViewHolder extends RecyclerView.ViewHolder{
            TextView textView;
            TextView textView2;
            TextView textView3;
            public CustomViewHolder(View view){
                super(view);
                textView =view.findViewById(R.id.item1);
                textView2 =view.findViewById(R.id.item2);
                textView3= view.findViewById(R.id.item3);
            }
        }
    }

}