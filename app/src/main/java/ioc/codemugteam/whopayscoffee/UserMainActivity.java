/**
 * @author Gustavo Ferrario Barber
 * M13 DAM 2023-24 S1
 */

package ioc.codemugteam.whopayscoffee;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class UserMainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView txtUser;
    JSONObject jsonUser;
    String jsonMsg;
    ImageView imgGrups, imgStats, imgConf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        toolbar = findViewById(R.id.user_toolbar);
        txtUser = findViewById(R.id.textView_userMain);
        imgGrups = findViewById(R.id.imgGrups);
        imgStats = findViewById(R.id.imgStats);
        imgConf = findViewById(R.id.imgConf);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        jsonMsg = intent.getStringExtra("user");
        try {
            assert jsonMsg != null;
            jsonUser = new JSONObject(jsonMsg);
            txtUser.setText(jsonUser.getString("name"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        imgGrups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUserGrups(jsonMsg);
            }
        });

        imgStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStats(jsonMsg);
            }
        });
        imgConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConf(jsonMsg);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu userMenu){
        getMenuInflater().inflate(R.menu.user_menu,userMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuOption){

        int id = menuOption.getItemId();
        if (id == R.id.user_gups_item){
            openUserGrups(jsonMsg);
        }

        if (id == R.id.user_estadistic_item){
            openStats(jsonMsg);
        }

        if (id == R.id.user_conf_item){
            openConf(jsonMsg);
        }

        if (id == R.id.user_logout_item){
            Toast.makeText(UserMainActivity.this,"Desconectant usuari", Toast.LENGTH_SHORT).show();
            try {
                logoutUser(jsonUser);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    private void openUserGrups(String jsonMsg){
        Intent intent = new Intent(UserMainActivity.this, UserGrupsActivity.class);
        intent.putExtra("user", jsonMsg);
        startActivity(intent);
    }

    private void openStats(String jsonMsg){
        Intent intent = new Intent(UserMainActivity.this, PagosEstadisticaActivity.class);
        intent.putExtra("user", jsonMsg);
        startActivity(intent);
    }

    private void openConf(String jsonMsg){
        Intent intent = new Intent(UserMainActivity.this, UserConfActivity.class);
        intent.putExtra("user", jsonMsg);
        startActivity(intent);
    }

    /**
     * Funció que envia al servidor la crida per desconnectar l'usuari
     * @param user
     * @throws JSONException
     */
    private void logoutUser(JSONObject user) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.serverURL) + "/coffee/api/auth/logout";
        String autoritzacio = user.getString("head") + " " + user.getString("token");

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    finish();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserMainActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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