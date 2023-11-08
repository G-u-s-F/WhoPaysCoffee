/**
 * @author Gustavo Ferrario Barber
 * M13 DAM 2023-24 S1
 */

package ioc.codemugteam.whopayscoffee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserConfActivity extends AppCompatActivity {

    Toolbar confToolbar;
    EditText userName, email;
    TextView canviPass;
    ImageView imgElimina;
    String jsonMsg;
    JSONObject jsonUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_conf);
        confToolbar = findViewById(R.id.conf_toolbar);
        userName = findViewById(R.id.editText_conf_username);
        email = findViewById(R.id.editText_conf_email);
        canviPass = findViewById(R.id.textView_conf_password);
        imgElimina = findViewById(R.id.img_delete);
        Intent intent = getIntent();
        jsonMsg = intent.getStringExtra("user");
        try {
            assert jsonMsg != null;
            jsonUser = new JSONObject(jsonMsg);
            userName.setHint(jsonUser.getString("name"));
            email.setHint(jsonUser.getString("email"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        confToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgElimina.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    Toast.makeText(UserConfActivity.this,"Eliminant usuari", Toast.LENGTH_SHORT).show();
                    deleteUser(jsonUser);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        canviPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    changePass(jsonUser, "654321");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Funció que envia al servidor la crida per eliminar un usuari
     * @param user
     * @throws JSONException
     */
    private void deleteUser(JSONObject user) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(UserConfActivity.this);
        String url = getString(R.string.serverURL) + "/coffee/api/auth/delete";
        String autoritzacio = user.getString("head") + " " + user.getString("token");


        StringRequest request = new StringRequest(Request.Method.DELETE, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Intent intent=new Intent(UserConfActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserConfActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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

    /**
     * Funció que envia al servidor la crida per canviar la contrasenya de l'usuari
     * @param newPass
     * @return result
     */
    public void changePass (JSONObject user, String newPass) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(UserConfActivity.this);
        String url = getString(R.string.serverURL) + "/coffee/api/auth/modPassword";
        String autoritzacio = user.getString("head") + " " + user.getString("token");
        final JSONObject body = new JSONObject();
        try {
            body.put("password", newPass);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(UserConfActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserConfActivity.this, "error = " + error, Toast.LENGTH_SHORT).show();
            }
        }){
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", autoritzacio);
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return body.toString().getBytes();
            }

        };
        queue.add(request);
/*
        final JSONObject data = new JSONObject();
        try {
            data.put("password", newPass);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (Request.Method.PUT, url, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(UserConfActivity.this, response.toString(), Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserConfActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                //Log.e("Error alta usuari", Objects.requireNonNull(error.getMessage()));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", autoritzacio);
                return headers;
            }
            public param
        };
        queue.add(jsonObjectRequest);

        RequestQueue queue = Volley.newRequestQueue(UserConfActivity.this);
        String url = getString(R.string.serverURL) + "/coffee/api/auth/modPass";
        String autoritzacio = user.getString("head") + " " + user.getString("token");

        HashMap<String, Object> params = new HashMap<>();
        params.put("password", newPass);
        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Toast.makeText(UserConfActivity.this, "Contrasenya canviada", Toast.LENGTH_SHORT).show();
                Toast.makeText(UserConfActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserConfActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", autoritzacio);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        queue.add(request);*/
    }
}