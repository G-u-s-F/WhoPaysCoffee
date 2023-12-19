/**
 * @author Gustavo Ferrario Barber
 * M13 DAM 2023-24 S1
 */

package ioc.codemugteam.whopayscoffee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GrupDetallEstadisticaActivity extends AppCompatActivity {
    private TextView fechaInicio, fechaFin;
    private ImageButton imgBtnRefresh;
    private Toolbar toolbar;
    private ArrayList<Integer> colors = new ArrayList<>();
    private ArrayList<Total> totales = new ArrayList<>();
    private ArrayList<PieEntry> pieEntry = new ArrayList<>();
    private PieChart pieChart;


    private Total total;
    private int any, mes, dia, grupId;
    private JSONObject jsonUser, jsonTotal;
    private String jsonMsg, dataInici, dataFi, grupName;
    PieDataSet pieDataSet;
    PieData pieData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grup_detall_estadistica);

        fechaInicio = findViewById(R.id.textViewDataInici);
        fechaFin = findViewById(R.id.textViewDataFi);
        imgBtnRefresh = findViewById(R.id.imageButtonRefreshEstadistica);

        pieChart = findViewById(R.id.pie_chart);

        toolbar = findViewById(R.id.user_grup_detall_estadistica_toolbar);
        Intent intent = getIntent();
        grupName = intent.getStringExtra("grupName");
        grupId = intent.getIntExtra("grupID", 0);
        jsonMsg = intent.getStringExtra("user");
        try {
            assert jsonMsg != null;
            jsonUser = new JSONObject(jsonMsg);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        toolbar.setTitle(grupName);
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
                mes = cal.get(Calendar.MONTH);
                dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpDialog = new DatePickerDialog(GrupDetallEstadisticaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String fecha = i2 + "/" + (i1+1) + "/" + i;
                        any = i;
                        mes = i1+1;
                        dia = i2;
                        dataInici = any + "-" + (mes<10?("0"+mes):(mes)) + "-" + (dia<10?("0"+dia):(dia));
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
                mes = cal.get(Calendar.MONTH);
                dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpDialog = new DatePickerDialog(GrupDetallEstadisticaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String fecha = i2 + "/" + (i1+1) + "/" + i;
                        any = i;
                        mes = i1+1;
                        dia = i2;
                        fechaFin.setText(fecha);
                        dataFi = any + "-" + (mes<10?("0"+mes):(mes)) + "-" + (dia<10?("0"+dia):(dia));
                    }
                }, any, mes, dia);
                dpDialog.show();
            }
        });

        imgBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getPagosTotalsMembres(jsonUser, grupId, dataInici, dataFi);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            getPagosTotalsMembres(jsonUser, grupId, dataInici, dataFi);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public int colorAleatorio() {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return color;
    }

    /**
     * Funció que envia al servidor la crida per obtenir el total de pagaments de cada membre d'un grup
     * @param user
     * @throws JSONException
     */
    private void getPagosTotalsMembres(JSONObject user, int groupId, String startDate, String endDate) throws JSONException {
        String url;
        RequestQueue queue = Volley.newRequestQueue(GrupDetallEstadisticaActivity.this);
        if (startDate == null || endDate == null){
            url = getString(R.string.serverURL) + "/coffee/api/payments/get/totals/by/group?groupId=" + groupId;
        } else {
            url = getString(R.string.serverURL) + "/coffee/api/payments/get/totals/by/group?groupId=" + groupId + "&initDate=" + startDate + "&endDate=" + endDate;
        }
        String autoritzacio = user.getString("head") + " " + user.getString("token");

        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    totales.clear();
                    pieEntry.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++){
                        jsonTotal = jsonArray.getJSONObject(i);
                        total = new Total(jsonTotal.getInt("memberId"),
                                jsonTotal.getString("nickname"),
                                Float.parseFloat(jsonTotal.getString("totalAmount")));
                        totales.add(total);
                        colors.add(colorAleatorio());
                        pieEntry.add(new PieEntry(total.getTotalAmount(),total.getNickname()));
                    }
                    pieDataSet = new PieDataSet(pieEntry, "");
                    pieDataSet.setValueTextColor(Color.BLACK);
                    pieData = new PieData(pieDataSet);
                    pieChart.setData(pieData);
                    pieChart.setCenterText(grupName);
                    Description desc = new Description();
                    desc.setText(grupName);
                    pieChart.setDescription(desc);
                    pieChart.animateY(2000);
                    pieDataSet.setColors(colors);
                    pieDataSet.setValueTextSize(15f);

                } catch (Throwable e) {
                    Toast.makeText(GrupDetallEstadisticaActivity.this, "error", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GrupDetallEstadisticaActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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