package ioc.codemugteam.whopayscoffee;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class passwordChangeActivity extends AppCompatActivity {

    String jsonMsg;
    JSONObject jsonUser;
    EditText newPassword, verificaNewPassword;
    Button canvia, cancela;
    CheckBox checkBoxViewPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        Intent intent = getIntent();
        jsonMsg = intent.getStringExtra("user");
        try {
            jsonUser = new JSONObject(jsonMsg);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        newPassword = findViewById(R.id.editTextNewPassword1);
        verificaNewPassword = findViewById(R.id.editTextNewPassword2);
        canvia = findViewById(R.id.btnNewPasswordOK);
        cancela = findViewById(R.id.btnNewPasswordCancel);
        checkBoxViewPass = findViewById(R.id.checkBoxMostraPassword);

        checkBoxViewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxViewPass.isChecked()){
                    newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    verificaNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    verificaNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        canvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newPassword.getText().toString().equals(verificaNewPassword.getText().toString())){
                    try {
                        changePass(jsonUser, newPassword.getText().toString());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(passwordChangeActivity.this,"La contrasenya no coincideix", Toast.LENGTH_SHORT).show();
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
     * Funció que envia al servidor la crida per canviar la contrasenya de l'usuari
     * @param user
     * @param newPass
     */
    public void changePass (JSONObject user, String newPass) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(passwordChangeActivity.this);
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
                Toast.makeText(passwordChangeActivity.this, response, Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(passwordChangeActivity.this, "error = " + error, Toast.LENGTH_SHORT).show();
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
    }
}