/**
 * @author Gustavo Ferrario Barber
 * M13 DAM 2023-24 S1
 */

package ioc.codemugteam.whopayscoffee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminGrupsActivity extends AppCompatActivity {

    private final List<Grup> grups = new ArrayList<>();
    private Grup grup;
    private String jsonMsg;
    JSONObject jsonAdmin, jsonUser, jsonGrup;
    private RecyclerView recyclerView;
    private GroupsAdapter groupsAdapter;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_gups);

        Intent intent = getIntent();
        jsonMsg = intent.getStringExtra("admin");
        try {
            assert jsonMsg != null;
            jsonAdmin = new JSONObject(jsonMsg);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        recyclerView = findViewById(R.id.items_recyclerview);
        groupsAdapter = new GroupsAdapter(this, grups);
        recyclerView.setAdapter(groupsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = findViewById(R.id.admin_grups_toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            getAllGrups(jsonAdmin);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Funció que envia al servidor la crida per mostrar tots els grups donats d'alta al sistema
     * @param admin
     * @throws JSONException
     */
    private void getAllGrups(JSONObject admin) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.serverURL) + "/coffee/api/admin/r/get/all/groups";
        String autoritzacio = admin.getString("head") + " " + admin.getString("token");
        JSONObject data = new JSONObject();

        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    grups.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++){
                        jsonGrup = jsonArray.getJSONObject(i);
                        grup = new Grup(jsonGrup.getString("groupName"), jsonGrup.getInt("numMembers"));
                        grups.add(grup);
                    }
                    groupsAdapter = new GroupsAdapter(AdminGrupsActivity.this, grups);
                    recyclerView.setAdapter(groupsAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(AdminGrupsActivity.this));
                } catch (Throwable e) {
                    Toast.makeText(AdminGrupsActivity.this, "error", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminGrupsActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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