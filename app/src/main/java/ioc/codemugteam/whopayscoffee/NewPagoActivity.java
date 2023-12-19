/**
 * @author Gustavo Ferrario Barber
 * M13 DAM 2023-24 S1
 */

package ioc.codemugteam.whopayscoffee;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewPagoActivity extends AppCompatActivity {

    private TextView textView, txtData;
    private EditText editText;
    private Button okButton, cancelButton;
    private String jsonMsg, data;
    private JSONObject jsonUser;
    private Calendar cal;
    private int any, mes, dia;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pago);

        textView = findViewById(R.id.txtNewPago);
        txtData = findViewById(R.id.txtNewPagoData);
        editText = findViewById(R.id.editTextNewPago);
        okButton = findViewById(R.id.btnNewPagoOK);
        cancelButton = findViewById(R.id.btnNewPagoCancel);
        intent = getIntent();
        jsonMsg = intent.getStringExtra("user");
        try {
            assert jsonMsg != null;
            jsonUser = new JSONObject(jsonMsg);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        cal = Calendar.getInstance();
        any = cal.get(Calendar.YEAR);
        mes = cal.get(Calendar.MONTH)+1;
        dia = cal.get(Calendar.DAY_OF_MONTH);
        data = dia + "/" + mes + "/" + any;
        txtData.setText(data);

        txtData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpDialog = new DatePickerDialog(NewPagoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        data = i2 + "/" + (i1+1) + "/" + i;
                        any = i;
                        mes = i1+1;
                        dia = i2;
                        txtData.setText(data);
                    }
                }, any, mes, dia);
                dpDialog.show();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addNewPago(jsonUser);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Funció que envia al servidor la crida per afegir un nou grup de l'usuari
     * @param user
     * @throws JSONException
     */
    private void addNewPago(JSONObject user) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(NewPagoActivity.this);
        String url = getString(R.string.serverURL) + "/coffee/api/payments/add";
        String autoritzacio = user.getString("head") + " " + user.getString("token");

        final JSONObject data = new JSONObject();
        try {
            data.put("amount", editText.getText().toString());
            data.put("paymentDate", any + "-" + (mes<10?("0"+mes):(mes)) + "-" + (dia<10?("0"+dia):(dia)));
            data.put("groupId", String.valueOf(intent.getIntExtra("grupId", 0)));
            data.put("memberId", String.valueOf(intent.getIntExtra("membreId", 0)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(NewPagoActivity.this,"Pagament afegit", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewPagoActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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