package ioc.codemugteam.whopayscoffee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
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

public class AdminGrupsActivity extends AppCompatActivity {


    private String[] grupsArray = {"grup 1", "grup 2", "grup 3", "grup 4"};
    private final List<Grup> grups = new ArrayList<>();
    private Grup grup;

    private RecyclerView recyclerView;
    private GroupsAdapter groupsAdapter;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_gups);

        for (int i = 0; i<grupsArray.length; i++){
            grup = new Grup(grupsArray[i]);
            grups.add(grup);
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

    private void getAllGroups(JSONObject admin) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.serverURL) + "/coffee/api/admin/r/getAllUsers";
        String autoritzacio = admin.getString("head") + " " + admin.getString("token");
        JSONObject data = new JSONObject();

        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                /*
                Toast.makeText((Context) AdminGrupsActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray = response.getJSONArray("user");
                    for (int i = 0; i < jsonArray.length(); i++){
                        jsonUsuaris = jsonArray.getJSONObject(i);
                        user = new Usuari(jsonUsuaris.getString("name"),jsonUsuaris.getString("email"));
                        usuaris.add(user);
                        itemsAdapter = new ItemsAdapter(AdminUsersActivity.this, usuaris);
                        recyclerView.setAdapter(itemsAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(AdminUsersActivity.this));
                    }
                    /*
                    String usuaris = jsonArray.toString();
                    Intent intent = new Intent(AdminUsersActivity.this, AdminUsersActivity.class);
                    intent.putExtra("usuaris", usuaris);
                    startActivity(intent);


                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminGrupsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", autoritzacio);
                return headers;
            }
        };
        queue.add(jsonRequest);
    }
}