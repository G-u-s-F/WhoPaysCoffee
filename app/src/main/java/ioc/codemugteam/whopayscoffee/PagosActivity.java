/**
 * @author Gustavo Ferrario Barber
 * M13 DAM 2023-24 S1
 */

package ioc.codemugteam.whopayscoffee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PagosActivity extends AppCompatActivity {

    private final List<Grup> grups = new ArrayList<>();
    private Grup grup;
    private RecyclerView recyclerView;
    private PagosAdapter pagosAdapter;
    Toolbar pagosToolbar;
    FloatingActionButton fab;
    Spinner pagosSpinner;
    String jsonMsg;
    JSONObject jsonUser, jsonGrup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagos);

        recyclerView = findViewById(R.id.items_recyclerview);
        pagosToolbar = findViewById(R.id.pagos_toolbar);
        pagosSpinner = findViewById(R.id.pagosSpinner);
        Intent intent = getIntent();
        jsonMsg = intent.getStringExtra("user");
        try {
            assert jsonMsg != null;
            jsonUser = new JSONObject(jsonMsg);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        pagosToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pagosSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    switch(i) {
                        case 0:
                            getAllUserGrups(jsonUser, "all");
                            break;
                        case 1:
                            getAllUserGrups(jsonUser, "admin");
                            break;
                        case 2:
                            getAllUserGrups(jsonUser, "member");
                            break;
                        default:
                            // code block
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            getAllUserGrups(jsonUser, "all");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Funció que envia al servidor la crida per tots els grups de l'usuari
     * @param user
     * @param type
     * @throws JSONException
     */
    private void getAllUserGrups(JSONObject user, String type) throws JSONException{

        String parametre = type;
        RequestQueue queue = Volley.newRequestQueue(PagosActivity.this);
        String url = getString(R.string.serverURL) + "/coffee/api/groups/get/groups?type=" + parametre;
        String autoritzacio = user.getString("head") + " " + user.getString("token");

        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    grups.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++){
                        jsonGrup = jsonArray.getJSONObject(i);
                        grup = new Grup(jsonGrup.getString("name"), Integer.parseInt(jsonGrup.getString("id")));
                        grups.add(grup);
                    }
                    pagosAdapter = new PagosAdapter(PagosActivity.this, grups, jsonMsg);
                    recyclerView.setAdapter(pagosAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PagosActivity.this));
                } catch (Throwable e) {
                    Toast.makeText(PagosActivity.this, "error", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PagosActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", autoritzacio);
                return headers;
            }
        };
        queue.add(request);
    }
}