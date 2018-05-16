package com.example.jose.gastosapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jose.gastosapp.services.gastosService;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = findViewById(R.id.btn_ListaGastos);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListaGastosActivity.class);
                startActivity(intent);
//                Context context = getApplicationContext();
//                CharSequence text = "Hola!";
//                int duration = Toast.LENGTH_LONG;
//                Toast.makeText(context, text, duration).show();
            }
        });

        final Button buttonApi = findViewById(R.id.btn_GastosMes);
        buttonApi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, GastosMesActivity.class);
                startActivity(intent);
            }
        });
    }
}
