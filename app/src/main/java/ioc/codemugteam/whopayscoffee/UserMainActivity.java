package ioc.codemugteam.whopayscoffee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
        Intent intent = getIntent();
        jsonMsg = intent.getStringExtra("user");
        try {
            jsonUser = new JSONObject(jsonMsg);
            toolbar.setTitle(jsonUser.getString("name"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu userMenu){
        getMenuInflater().inflate(R.menu.menu_user,userMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuOption){
        int id = menuOption.getItemId();

        if (id==R.id.logout_user_item){

            return true;
        }

        return super.onOptionsItemSelected(menuOption);
    }
}