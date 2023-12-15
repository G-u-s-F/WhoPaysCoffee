/**
 * @author Gustavo Ferrario Barber
 * M13 DAM 2023-24 S1
 */

package ioc.codemugteam.whopayscoffee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class GrupDetallEstadisticaActivity extends AppCompatActivity {
    private TextView fechaInicio, fechaFin;
    private ImageButton imgBtnRefresh;
    private Toolbar toolbar;
    private int any, mes, dia;
    private JSONObject jsonUser;
    private String jsonMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grup_detall_estadistica);
        fechaInicio = findViewById(R.id.textViewDataInici);
        fechaFin = findViewById(R.id.textViewDataFi);
        imgBtnRefresh = findViewById(R.id.imageButtonRefreshEstadistica);
        toolbar = findViewById(R.id.user_grup_detall_estadistica_toolbar);
        Intent intent = getIntent();
        jsonMsg = intent.getStringExtra("user");
        toolbar.setTitle(intent.getStringExtra("grupName"));
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
                        String fecha = i2 + "/" + i1 + "/" + i;
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
                        String fecha = i2 + "/" + i1 + "/" + i;
                        fechaFin.setText(fecha);
                    }
                }, any, mes, dia);
                dpDialog.show();
            }
        });

        imgBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}