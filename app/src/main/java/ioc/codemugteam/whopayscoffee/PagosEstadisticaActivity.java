/**
 * @author Gustavo Ferrario Barber
 * M13 DAM 2023-24 S1
 */

package ioc.codemugteam.whopayscoffee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class PagosEstadisticaActivity extends AppCompatActivity {

    private ImageView imgPagos, imgEstadistica;
    private Toolbar pagosEstadisticaToolbar;
    String jsonMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagos_estadistica);

        imgPagos = findViewById(R.id.imgPagos);
        imgEstadistica = findViewById(R.id.imgEstadistica);
        pagosEstadisticaToolbar = findViewById(R.id.pagos_estadistica_toolbar);
        Intent intent = getIntent();
        jsonMsg = intent.getStringExtra("user");

        pagosEstadisticaToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgPagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PagosEstadisticaActivity.this, PagosActivity.class);
                intent.putExtra("user", jsonMsg);
                startActivity(intent);
            }
        });

        imgEstadistica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PagosEstadisticaActivity.this, EstadisticaActivity.class);
                intent.putExtra("user", jsonMsg);
                startActivity(intent);
            }
        });
    }
}