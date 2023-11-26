package ioc.codemugteam.whopayscoffee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AdminUsersActivity extends AppCompatActivity {

    private final List<Usuari> usuaris = new ArrayList<>();
    private Usuari user;
    private JSONObject jsonUsuaris, jsonAdmin;
    private RecyclerView recyclerView;
    private ItemsAdapter itemsAdapter;
    Toolbar toolbar;
    String jsonMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users);
        Intent intent = getIntent();
        jsonMsg = intent.getStringExtra("admin");
        recyclerView = findViewById(R.id.items_recyclerview);
        try {
            assert jsonMsg != null;
            jsonAdmin = new JSONObject(jsonMsg);
            getAllUsers(jsonAdmin);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        toolbar = findViewById(R.id.admin_users_toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Funció que envia al servidor la crida per obtenir tots els usuaris de l'aplicació
     * @param admin
     * @throws JSONException
     */
    private void getAllUsers(JSONObject admin) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.serverURL) + "/coffee/api/admin/r/get/all/users";
        String autoritzacio = admin.getString("head") + " " + admin.getString("token");

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    usuaris.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++){
                        jsonUsuaris = jsonArray.getJSONObject(i);
                        user = new Usuari(jsonUsuaris.getString("username"),jsonUsuaris.getString("email"));
                        usuaris.add(user);
                    }
                    itemsAdapter = new ItemsAdapter(AdminUsersActivity.this, usuaris);
                    recyclerView.setAdapter(itemsAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(AdminUsersActivity.this));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminUsersActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", autoritzacio);
                return headers;
            }
        };
        queue.add(request);
    }
}