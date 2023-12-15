/**
 * @author Gustavo Ferrario Barber
 * M13 DAM 2023-24 S1
 */

package ioc.codemugteam.whopayscoffee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MembreDetallActivity extends AppCompatActivity {

    private String grupId, nickName, userName, oldNickName, jsonMsg;
    JSONObject jsonUser;
    private Button tornar;
    private ImageButton edit, save, editUserName, saveUserName;
    private EditText editTextNickName, editTextUserName;
    private CheckBox isAdmin;
    private androidx.appcompat.widget.Toolbar membreDetallToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membre_detall);

        membreDetallToolbar = findViewById(R.id.membre_detall_toolbar);
        tornar = findViewById(R.id.btn_membre_detall_tornar);
        edit = findViewById(R.id.imageButton_membre_detall_edit);
        save = findViewById(R.id.imageButton_membre_detall_save);
        editUserName = findViewById(R.id.imageButton_membre_detall_edit_userName);
        saveUserName = findViewById(R.id.imageButton_membre_detall_save_userName);
        editTextNickName = findViewById(R.id.editText_membre_detall_nickName);
        editTextUserName = findViewById(R.id.editText_membre_detall_userName);
        isAdmin = findViewById(R.id.checkBox_membre_detall_isAdmin);
        Intent intent = getIntent();
        grupId = String.valueOf(intent.getIntExtra("grupId", 0));
        nickName = intent.getStringExtra("nickName");
        userName = intent.getStringExtra("userName");
        jsonMsg = intent.getStringExtra("user");
        try {
            assert jsonMsg != null;
            jsonUser = new JSONObject(jsonMsg);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        isAdmin.setChecked(intent.getBooleanExtra("isAdmin", false));

        editTextNickName.setText(nickName);
        editTextUserName.setText(userName);

        tornar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        membreDetallToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldNickName = editTextNickName.getText().toString();
                save.setVisibility(view.VISIBLE);
                editTextNickName.setEnabled(true);
                editTextNickName.requestFocus();
                //editTextNickName.selectAll();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    canviNickName(jsonUser);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        editUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserName.setVisibility(view.VISIBLE);
                editTextUserName.setEnabled(true);
                editTextUserName.requestFocus();
            }
        });

        saveUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    canviUserName(jsonUser);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Funció que envia al servidor la crida per canviar el nickName d'un membre
     * @param user
     * @throws JSONException
     */
    private void canviNickName(JSONObject user) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(MembreDetallActivity.this);
        String url = getString(R.string.serverURL) + "/coffee/api/groups/update/nickname/group";
        String autoritzacio = user.getString("head") + " " + user.getString("token");

        final JSONObject data = new JSONObject();
        try {
            data.put("groupId", grupId);
            data.put("oldNickname", oldNickName);
            data.put("newNickname", editTextNickName.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(Request.Method.PUT, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MembreDetallActivity.this, response, Toast.LENGTH_SHORT).show();
                save.setVisibility(View.INVISIBLE);
                editTextNickName.setEnabled(false);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MembreDetallActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
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

    /**
     * Funció que envia al servidor la crida per afegir o canviar el userName associat a un membre
     * @param user
     * @throws JSONException
     */
    private void canviUserName(JSONObject user) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(MembreDetallActivity.this);
        String url = getString(R.string.serverURL) + "/coffee/api/groups/add/reguser/member/from/group";
        String autoritzacio = user.getString("head") + " " + user.getString("token");

        final JSONObject data = new JSONObject();
        try {
            data.put("groupId", grupId);
            data.put("nickname", editTextNickName.getText().toString());
            data.put("username", editTextUserName.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MembreDetallActivity.this, response, Toast.LENGTH_SHORT).show();
                saveUserName.setVisibility(View.INVISIBLE);
                editTextUserName.setEnabled(false);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MembreDetallActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
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