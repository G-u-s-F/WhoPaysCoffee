/**
 * @author Gustavo Ferrario Barber
 * M13 DAM 2023-24 S1
 */

package ioc.codemugteam.whopayscoffee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MembrePagosDetallActivity extends AppCompatActivity {

    private final List<Pago> pagos = new ArrayList<>();
    private Pago pago;
    private TextView fechaInicio, fechaFin, amountTotal;
    private ImageButton imgBtnRefresh;
    private int any, mes, dia;
    private RecyclerView recyclerView;
    private MembrePagosDetallAdapter membrePagosDetallAdapter;
    Toolbar toolbar;
    String jsonMsg, grupID, memberId, nickName, dataInici, dataFi;
    JSONObject jsonUser, jsonPago;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membre_pagos_detall);

        recyclerView = findViewById(R.id.items_recyclerview);
        toolbar = findViewById(R.id.membre_pagos_detall_toolbar);
        fechaInicio = findViewById(R.id.textViewMembrePagoDataInici);
        fechaFin = findViewById(R.id.textViewMembrePagosDataFi);
        amountTotal = findViewById(R.id.amountTotalPagosDetall);
        imgBtnRefresh = findViewById(R.id.imageButtonRefreshMembrePago);

        Intent intent = getIntent();
        memberId = intent.getStringExtra("membreId");
        grupID = intent.getStringExtra("grupId");
        nickName = intent.getStringExtra("nickName");
        jsonMsg = intent.getStringExtra("user");
        try {
            assert jsonMsg != null;
            jsonUser = new JSONObject(jsonMsg);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        toolbar.setTitle("Pagaments de " + nickName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                any = cal.get(Calendar.YEAR);
                mes = cal.get(Calendar.MONTH)+1;
                dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpDialog = new DatePickerDialog(MembrePagosDetallActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String fecha = i2 + "/" + i1 + "/" + i;
                        dataInici = i + "-" + (i1+1) + "-" + i2;
                        fechaInicio.setText(fecha);
                    }
                }, any, mes, dia);
                dpDialog.show();
            }
        });

        fechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                any = cal.get(Calendar.YEAR);
                mes = cal.get(Calendar.MONTH)+1;
                dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpDialog = new DatePickerDialog(MembrePagosDetallActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String fecha = i2 + "/" + i1 + "/" + i;
                        dataFi = i + "-" + (i1+1) + "-" + i2;
                        fechaFin.setText(fecha);
                    }
                }, any, mes, dia);
                dpDialog.show();
            }
        });

        imgBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getMembrePagosDetall(jsonUser, memberId, grupID, dataInici, dataFi);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    protected void onStart() {
        super.onStart();
        try {
            getMembrePagosDetall(jsonUser, memberId, grupID, dataInici, dataFi);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Funció que envia al servidor la crida per obtenir tots els pagaments d'un membre
     * @param user
     * @throws JSONException
     */
    private void getMembrePagosDetall(JSONObject user, String memberId, String grupId, String dataInici, String dataFi) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(MembrePagosDetallActivity.this);
        String url;
        if (dataInici == null || dataFi == null){
            url = getString(R.string.serverURL) + "/coffee/api/payments/get/by/member?memberId=" + memberId + "&groupId=" + grupId;
        } else {
            url = getString(R.string.serverURL) + "/coffee/api/payments/get/by/member?memberId=" + memberId + "&groupId=" + grupId + "&initDate=" + dataInici + "&endDate=" + dataFi;
        }
        String autoritzacio = user.getString("head") + " " + user.getString("token");

        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pagos.clear();
                    float total = 0.0f;
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("paymentData");
                    for (int i = 0; i < jsonArray.length(); i++){
                        jsonPago = jsonArray.getJSONObject(i);
                        pago = new Pago(jsonPago.getInt("paymentId"),
                                Integer.parseInt(memberId),
                                Integer.parseInt(grupId),
                                Float.parseFloat(jsonPago.getString("amount")),
                                jsonObject.getString("nickname"),
                                Date.valueOf(jsonPago.getString("paymentDate")));
                        pagos.add(pago);
                        total = total + pago.getAmount();
                    }
                    String txtTotal = "Total: " + Float.toString(total);
                    amountTotal.setText(txtTotal);
                    membrePagosDetallAdapter = new MembrePagosDetallAdapter(MembrePagosDetallActivity.this, pagos, jsonMsg);
                    recyclerView.setAdapter(membrePagosDetallAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MembrePagosDetallActivity.this));
                } catch (Throwable e) {
                    Toast.makeText(MembrePagosDetallActivity.this, "error", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MembrePagosDetallActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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