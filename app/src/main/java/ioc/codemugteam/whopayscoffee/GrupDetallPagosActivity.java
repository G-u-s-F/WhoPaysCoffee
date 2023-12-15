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

public class GrupDetallPagosActivity extends AppCompatActivity {

    private final List<Membre> membres = new ArrayList<>();
    private Membre membre;
    private Grup grup;
    private RecyclerView recyclerView;
    private GrupDetallPagosAdapter grupDetallPagosAdapter;
    Toolbar grupDetallPagosToolbar;
    FloatingActionButton fab;
    String jsonMsg, grupID, grupName;
    JSONObject jsonUser, jsonMembre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grup_detall_pagos);

        recyclerView = findViewById(R.id.items_recyclerview_pagos);
        grupDetallPagosToolbar = findViewById(R.id.user_grup_detall_pagos_toolbar);
        Intent intent = getIntent();
        grupID = intent.getStringExtra("grupID");
        grupName = intent.getStringExtra("grupName");
        jsonMsg = intent.getStringExtra("user");
        try {
            assert jsonMsg != null;
            jsonUser = new JSONObject(jsonMsg);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        grupDetallPagosToolbar.setTitle(grupName);
        grupDetallPagosToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    protected void onStart() {
        super.onStart();
        try {
            getGrupMembres(jsonUser);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Funció que envia al servidor la crida per tots els membres d'un grup
     * @param user
     * @throws JSONException
     */
    private void getGrupMembres(JSONObject user) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(GrupDetallPagosActivity.this);
        String url = getString(R.string.serverURL) + "/coffee/api/groups/get/members/group/" + grupID;
        String autoritzacio = user.getString("head") + " " + user.getString("token");

        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    membres.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++){
                        jsonMembre = jsonArray.getJSONObject(i);
                        membre = new Membre(jsonMembre.getInt("groupId"),
                                jsonMembre.getInt("userId"),
                                jsonMembre.getString("nickname"),
                                jsonMembre.getString("username"),
                                jsonMembre.getBoolean("isAdmin"));
                        membres.add(membre);
                    }
                    grupDetallPagosAdapter = new GrupDetallPagosAdapter(GrupDetallPagosActivity.this, membres, jsonMsg);
                    recyclerView.setAdapter(grupDetallPagosAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(GrupDetallPagosActivity.this));
                } catch (Throwable e) {
                    Toast.makeText(GrupDetallPagosActivity.this, "error", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GrupDetallPagosActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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