package com.example.ims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class commute extends AppCompatActivity {
    NfcAdapter nfcAdapter;
    String tmp;

    byte[] b;
    int t;

    Ndef ndef;
    NdefMessage ndefmssage;
    NdefRecord[] ndefrecord;


    Date td = new Date(System.currentTimeMillis());
    SimpleDateFormat yearsimple = new SimpleDateFormat("yyyy");

    SimpleDateFormat monthsimple = new SimpleDateFormat("MM");

    SimpleDateFormat datesimple = new SimpleDateFormat("dd");
    String year;
    String month;
    String date;








    Intent intent;
    PendingIntent mPendingIntent;

    FirebaseUser use;
    String today;
    String time;
    SimpleDateFormat tformat;
    String k ;
    DatabaseReference mdatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commute);


        //데이터베이스 연결
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        mdatabase = database.getReference();


        intent = new Intent(this, getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntent = (PendingIntent) PendingIntent.getActivity(this, 0, intent, 0);

        t= 0;

        year = yearsimple.format(td).toString();
        month = monthsimple.format(td).toString();
        date = datesimple.format(td).toString();



        //유저 알아내기
        use = FirebaseAuth.getInstance().getCurrentUser();
        //nfc어텝더 가져오기
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);



        //정보를 가져올때 날짜를 초기화 해야되기 때문에
        tformat = new SimpleDateFormat("yyyy MM dd");
        today = tformat.format(new Date(System.currentTimeMillis()));






        //데이터 베이스에 카운터 하기 출근 기록을 적을지 퇴근 기록을 적을지를 위한 카운터

        mdatabase.child(use.getDisplayName()).child(year).child(month).child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                t=0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    t++;

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    protected void onPause() {

        if (nfcAdapter != null) {

            nfcAdapter.disableForegroundDispatch(this);

        }

        super.onPause();

    }


    @Override
    protected void onResume() {

        super.onResume();

        if (nfcAdapter != null) {

            nfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);

        }
    }


    @Override

    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);


        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        ndef = Ndef.get(tag);
        if (ndef != null) {
            ndefmssage = ndef.getCachedNdefMessage();
            if (ndefmssage != null) {
                ndefrecord = ndefmssage.getRecords();
                for (NdefRecord record : ndefrecord) {
                    if (record.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(record.getType(), NdefRecord.RTD_TEXT)) {
                        try {
                            b = record.getPayload();
                        } catch (Exception e) {

                        }
                    }
                }
            }
        }
        if (tag != null) {
            tmp = new String(b);
            Infu x = new Infu();


            if (t==0){
                x.gototime= new Date(System.currentTimeMillis());

                if (!tmp.equals("enIMS")) {
                    Toast.makeText(this, "출근 되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();

                }
                //데이터 베이스 출근시간 기록
                mdatabase.child(use.getDisplayName()).child(year).child(month).child(date).child("gototime").setValue(x.gototime);




            }else if(t==2){
                Toast.makeText(this, "오늘의 출퇴근은 이미 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }else if(t==1){

                if (!tmp.equals("enIMS")) {
                    Toast.makeText(this, "퇴근 되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();


                }
                //데이터 베이스 퇴근시간 기록
                x.offtime =new Date(System.currentTimeMillis());
                mdatabase.child(use.getDisplayName()).child(year).child(month).child(date).child("offtime").setValue(x.offtime);

            }
        }

    }

}

