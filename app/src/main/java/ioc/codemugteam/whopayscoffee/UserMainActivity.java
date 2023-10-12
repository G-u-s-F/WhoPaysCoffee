package ioc.codemugteam.whopayscoffee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class UserMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
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