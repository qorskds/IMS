package com.example.ims;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Button menu_commute = (Button)findViewById(R.id.menu_commute);
        Button menu_record = (Button)findViewById(R.id.menu_record);


        menu_commute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),commute.class);

                startActivity(intent);
            }
        });//클릭시 출퇴근 하기 창 뜨기


        menu_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),record.class);

                startActivity(intent);
            }
        });//클릭시 출퇴근 기록 창 뜨기

    }
}
