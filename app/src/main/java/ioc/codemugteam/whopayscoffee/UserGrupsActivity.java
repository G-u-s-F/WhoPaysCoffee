package ioc.codemugteam.whopayscoffee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class UserGrupsActivity extends AppCompatActivity {

    private final List<Grup> grups = new ArrayList<>();
    private Grup grup;
    private RecyclerView recyclerView;
    private UserGrupsAdapter userGrupsAdapter;
    Toolbar userGrupsToolbar;
    FloatingActionButton fab;
    Spinner userGrupsSpinner;
    String jsonMsg;
    JSONObject jsonUser, jsonGrup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_grups);

        recyclerView = findViewById(R.id.items_recyclerview);
        userGrupsToolbar = findViewById(R.id.user_grups_toolbar);
        fab = findViewById(R.id.userGroupFab);
        userGrupsSpinner = findViewById(R.id.userGroupSpinner);
        Intent intent = getIntent();
        jsonMsg = intent.getStringExtra("user");
        try {
            jsonUser = new JSONObject(jsonMsg);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        userGrupsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserGrupsActivity.this, NewUserGrupActivity.class);
                intent.putExtra("user", jsonMsg);
                startActivity(intent);
            }
        });

        userGrupsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        RequestQueue queue = Volley.newRequestQueue(UserGrupsActivity.this);
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
                    userGrupsAdapter = new UserGrupsAdapter(UserGrupsActivity.this, grups, jsonMsg);
                    recyclerView.setAdapter(userGrupsAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(UserGrupsActivity.this));
                } catch (Throwable e) {
                    Toast.makeText(UserGrupsActivity.this, "error", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserGrupsActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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