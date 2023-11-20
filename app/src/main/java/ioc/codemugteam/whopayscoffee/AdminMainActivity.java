/**
 * @author Gustavo Ferrario Barber
 * M13 DAM 2023-24 S1
 */

package ioc.codemugteam.whopayscoffee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.util.HashMap;
import java.util.Map;

public class AdminMainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView txtAdmin;
    JSONObject jsonAdmin;
    String jsonMsg;
    ImageView imgUsers, imgGrups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        toolbar = findViewById(R.id.admin_toolbar);
        txtAdmin = findViewById(R.id.textView_adminMain);
        imgUsers = findViewById(R.id.imgUsersAdmin);
        imgGrups = findViewById(R.id.imgGrupsAdmin);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        jsonMsg = intent.getStringExtra("admin");
        try {
            assert jsonMsg != null;
            jsonAdmin = new JSONObject(jsonMsg);
            txtAdmin.setText(jsonAdmin.getString("username"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        imgUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminUsers();
            }
        });

        imgGrups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    adminGroups();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu adminMenu){
        getMenuInflater().inflate(R.menu.admin_menu,adminMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuOption){

        int id = menuOption.getItemId();
        if (id == R.id.admin_users_item){
            adminUsers();
        }

        if (id == R.id.admin_grups_item){
            try {
                adminGroups();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        if (id == R.id.admin_logout_item){
            //Toast.makeText(AdminMainActivity.this,"Desconectant administrador", Toast.LENGTH_SHORT).show();
            try {
                logoutAdmin(jsonAdmin);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    private void adminUsers(){
        Toast.makeText(this,"Usuaris", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AdminMainActivity.this, AdminUsersActivity.class);
        //intent.putExtra("admin", response.toString());
        startActivity(intent);
    }

    private void adminGroups() throws JSONException {
        Toast.makeText(this,"Grups", Toast.LENGTH_SHORT).show();
        getAllUsers(jsonAdmin);
        Intent intent = new Intent(AdminMainActivity.this, AdminGrupsActivity.class);
        //intent.putExtra("admin", response.toString());
        startActivity(intent);
    }

    private void getAllUsers(JSONObject admin) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.serverURL) + "/coffee/api/admin/r/getAllUsers";
        String autoritzacio = admin.getString("head") + " " + admin.getString("token");
        JSONObject data = new JSONObject();

        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("user");
                    String usuaris = jsonArray.toString();
                    Intent intent = new Intent(AdminMainActivity.this, AdminUsersActivity.class);
                    intent.putExtra("usuaris", usuaris);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

    /**
     * Funció que envia al servidor la crida per desconnectar l'administrador
     * @param admin
     * @throws JSONException
     */
    private void logoutAdmin(JSONObject admin) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.serverURL) + "/coffee/api/admin/r/logout";
        String autoritzacio = admin.getString("head") + " " + admin.getString("token");

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Toast.makeText(AdminMainActivity.this, response, Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminMainActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }){
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