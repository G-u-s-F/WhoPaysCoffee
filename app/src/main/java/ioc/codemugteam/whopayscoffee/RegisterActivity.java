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

public class RegisterActivity extends AppCompatActivity {
    EditText userName, email, password;
    Button btnReg;
    User usuari;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = findViewById(R.id.edit_Text_Reg_Usuari);
        email = findViewById(R.id.edit_Text_Reg_Email);
        password = findViewById(R.id.edit_Text_Reg_Password);
        btnReg = findViewById(R.id.btn_Registra);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName.getText().toString().isEmpty() || email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Falta alguna dada", Toast.LENGTH_LONG).show();
                } else {
                    addNewUser(userName.getText().toString(), email.getText().toString(), password.getText().toString());
                }
            }
        });
    }

    private void addNewUser(String name, String email, String password){

        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        String url = "http://192.168.0.21:8080/coffee/api/auth/p/register";

        final JSONObject data = new JSONObject();
        try {
            data.put("name", name);
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
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this,"Error al registre", Toast.LENGTH_LONG).show();
                        Log.e("Error alta usuari", error.getMessage());
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