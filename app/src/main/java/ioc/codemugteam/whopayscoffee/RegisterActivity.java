/**
 * @author Gustavo Ferrario Barber
 * M13 DAM 2023-24 S1
 */

package ioc.codemugteam.whopayscoffee;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    EditText userName, email, password;
    Button btnReg, btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = findViewById(R.id.edit_Text_Reg_Usuari);
        email = findViewById(R.id.edit_Text_Reg_Email);
        password = findViewById(R.id.edit_Text_Reg_Password);
        btnReg = findViewById(R.id.btn_Registra);
        btnCancel = findViewById(R.id.btn_reg_cancel);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userName.getText().toString().isEmpty() || email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Falta alguna dada", Toast.LENGTH_LONG).show();
                } else {
                    addNewUser(userName.getText().toString(), email.getText().toString(), password.getText().toString());
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Funció que envia al servidor la crida per registrar un nou usuari
     * @param name
     * @param email
     * @param password
     */
    private void addNewUser(String name, String email, String password){

        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        String url = getString(R.string.serverURL) + "/coffee/api/auth/p/register";

        final JSONObject data = new JSONObject();
        try {
            data.put("username", name);
            data.put("email", email);
            data.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(RegisterActivity.this,"Usuari registrat correctament", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this, UserMainActivity.class);
                        intent.putExtra("user",response.toString());
                        finish();
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Error alta usuari", Objects.requireNonNull(error.getMessage()));
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