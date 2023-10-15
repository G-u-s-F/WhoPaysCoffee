package ioc.codemugteam.whopayscoffee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import org.json.JSONException;
import org.json.JSONObject;

public class UserMainActivity extends AppCompatActivity {

    Toolbar toolbar;
    JSONObject jsonUser;
    String jsonMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        toolbar = findViewById(R.id.user_toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        jsonMsg = intent.getStringExtra("user");
        try {
            assert jsonMsg != null;
            jsonUser = new JSONObject(jsonMsg);
            toolbar.setTitle(jsonUser.getString("name"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu userMenu){
        getMenuInflater().inflate(R.menu.user_menu,userMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuOption){

        int id = menuOption.getItemId();
        if (id == R.id.user_gups_item){
            Toast.makeText(this,"Grups", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.user_estadistic_item){
            Toast.makeText(this,"Estadística", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.user_conf_item){
            Toast.makeText(this,"Configuració", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.user_logout_item){
            userLogout();
            Toast.makeText(UserMainActivity.this,"logout", Toast.LENGTH_SHORT).show();
        }

        return true;
        //return super.onOptionsItemSelected(menuOption);
    }

    private void userLogout(){

        Toast.makeText(UserMainActivity.this,"Logout", Toast.LENGTH_LONG).show();
    }
}