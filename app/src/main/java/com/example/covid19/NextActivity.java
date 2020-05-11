package com.example.covid19;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
    }
    public void Siguiente(View view){
        Intent ps = new Intent(this, AccesosActivity.class);
        startActivity(ps);
    }
}
