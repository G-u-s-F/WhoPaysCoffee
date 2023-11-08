/**
 * @author Gustavo Ferrario Barber
 * M13 DAM 2023-24 S1
 */

package ioc.codemugteam.whopayscoffee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText userName, password;
    TextView alta;
    Button btnLogin;
    JSONObject userJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.edit_Text_Usuari);
        password = findViewById(R.id.edit_Text_Password);
        alta = findViewById(R.id.txt_alta_User);
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName.getText().toString().contains("@")) {
                    loginUser(userName.getText().toString(), password.getText().toString());
                } else {
                    loginAdmin(userName.getText().toString(), password.getText().toString());
                }
            }
        });

        alta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        userName.setText("");
        userName.requestFocus();
        password.setText("");
    }

    /**
     * Funció que envia al servidor la crida per connectar un usuari
     * @param userName
     * @param pass
     */
    private void loginUser(String userName, String pass){
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        String url = getString(R.string.serverURL) + "/coffee/api/auth/p/login";

        JSONObject data = new JSONObject();
        try {
            data.put("email", userName);
            data.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                        intent.putExtra("user", response.toString());
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,"Usuari desconegut", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    /**
     * Funció que envia al servidor la crida per connectar un administrador
     * @param userName
     * @param pass
     */
    private void loginAdmin(String userName, String pass){
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        String url = getString(R.string.serverURL) + "/coffee/api/admin/p/login";

        JSONObject data = new JSONObject();
        try {
            data.put("username", userName);
            data.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                        intent.putExtra("admin", response.toString());
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,"Administrador desconegut", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }
}