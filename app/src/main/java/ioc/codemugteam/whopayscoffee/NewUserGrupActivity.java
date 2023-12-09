package ioc.codemugteam.whopayscoffee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class NewUserGrupActivity extends AppCompatActivity {
    private Button afegir, cancel;
    private EditText nomGrup;
    private String jsonMsg;
    private JSONObject jsonUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_grup);

        afegir = findViewById(R.id.btnNewGrupOK);
        cancel = findViewById(R.id.btnNewGrupCancel);
        nomGrup = findViewById(R.id.editTextNewGrup);
        Intent intent = getIntent();
        jsonMsg = intent.getStringExtra("user");
        try {
            jsonUser = new JSONObject(jsonMsg);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        afegir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addNewGrup(jsonUser, nomGrup.getText().toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Funció que envia al servidor la crida per afegir un nou grup de l'usuari
     * @param user
     * @param nom
     * @throws JSONException
     */
    private void addNewGrup(JSONObject user, String nom) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(NewUserGrupActivity.this);
        String url = getString(R.string.serverURL) + "/coffee/api/groups/add/group";
        String autoritzacio = user.getString("head") + " " + user.getString("token");

        final JSONObject data = new JSONObject();
        try {
            data.put("memberName", user.getString("name"));
            data.put("groupName", nom);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(NewUserGrupActivity.this,"Grup afegit", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewUserGrupActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", autoritzacio);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }
}