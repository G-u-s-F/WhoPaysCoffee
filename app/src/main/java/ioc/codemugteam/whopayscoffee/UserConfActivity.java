/**
 * @author Gustavo Ferrario Barber
 * M13 DAM 2023-24 S1
 */

package ioc.codemugteam.whopayscoffee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
                    userDelete(jsonUser);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void userDelete(JSONObject user) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(UserConfActivity.this);
        String url = getString(R.string.serverIP) + "/coffee/api/auth/delete";
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

}