/**
 * @author Gustavo Ferrario Barber
 * M13 DAM 2023-24 S1
 */

package ioc.codemugteam.whopayscoffee;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    JSONObject jsonUser;
    String jsonMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        toolbar = findViewById(R.id.user_toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        jsonMsg = intent.getStringExtra("user");
        try {
            assert jsonMsg != null;
            jsonUser = new JSONObject(jsonMsg);
            toolbar.setTitle(jsonUser.getString("name"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
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
            Toast.makeText(this,"Grups", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.user_estadistic_item){
            Toast.makeText(this,"Estadística", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.user_conf_item){
            Toast.makeText(this,"Configuració", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.user_logout_item){
            Toast.makeText(UserMainActivity.this,"Desconectant usuari", Toast.LENGTH_SHORT).show();
            try {
                userLogout(jsonUser);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        return true;
    }

    private void userLogout(JSONObject user) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.0.21:8080/coffee/api/auth/logout";
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