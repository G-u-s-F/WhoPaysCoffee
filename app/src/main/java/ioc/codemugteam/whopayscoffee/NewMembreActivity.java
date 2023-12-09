package ioc.codemugteam.whopayscoffee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class NewMembreActivity extends AppCompatActivity {

    String jsonMsg, grupID;
    JSONObject jsonUser;
    EditText nickName, userName;
    TextView txtEsUsuari;
    Button afegir, cancela;
    CheckBox checkBoxEsUsuari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_membre);

        Intent intent = getIntent();
        grupID = intent.getStringExtra("groupId");
        jsonMsg = intent.getStringExtra("user");
        try {
            jsonUser = new JSONObject(jsonMsg);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        nickName = findViewById(R.id.editTextNewMembreNickName);
        userName = findViewById(R.id.editTextNewMembreUserName);
        txtEsUsuari = findViewById(R.id.txtNewMembreUserName);
        afegir = findViewById(R.id.btnNewMembreOK);
        cancela = findViewById(R.id.btnNewMembreCancel);
        checkBoxEsUsuari = findViewById(R.id.checkBoxEsUsuari);

        checkBoxEsUsuari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxEsUsuari.isChecked()){
                    txtEsUsuari.setVisibility(View.VISIBLE);
                    userName.setVisibility(View.VISIBLE);
                } else {
                    txtEsUsuari.setVisibility(View.INVISIBLE);
                    userName.setVisibility(View.INVISIBLE);
                }
            }
        });

        afegir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (nickName.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(NewMembreActivity.this,"No hi ha nickName", Toast.LENGTH_SHORT).show();
                    } else {
                        newMembre(jsonUser, nickName.getText().toString(), userName.getText().toString(), grupID);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        cancela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Funció que envia al servidor la crida per afegir un nou membre a un grup
     * @param user
     * @param nickName
     * @param userName
     */
    public void newMembre (JSONObject user, String nickName, String userName, String grupId) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(NewMembreActivity.this);
        String url = getString(R.string.serverURL) + "/coffee/api/groups/add/member";
        String autoritzacio = user.getString("head") + " " + user.getString("token");

        final JSONObject data = new JSONObject();
        try {
            data.put("groupId", grupId);
            data.put("nickname", nickName);
            data.put("username", userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(NewMembreActivity.this,"Membre afegit", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewMembreActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", autoritzacio);
                return headers;
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                return data.toString().getBytes();
            }

            public String getBodyContentType() {
                return "application/json";
            }

        };
        queue.add(request);
    }
}