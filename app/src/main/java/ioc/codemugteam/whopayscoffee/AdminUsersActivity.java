package ioc.codemugteam.whopayscoffee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AdminUsersActivity extends AppCompatActivity {

    private String[] users = {"Gus", "Hugo", "Sandra", "Elora"};
    private final List<String> list = Arrays.asList(users);
    private final List<Usuari> usuaris = new ArrayList<>();
    private Usuari user;
    private final LinkedList<String> itemsList = new LinkedList<String>(list);
    private JSONObject jsonUsuaris;
    private JSONArray jsonArray;

    private RecyclerView recyclerView;
    private ItemsAdapter itemsAdapter;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users);

        try {
            jsonUsuaris = new JSONObject(getIntent().getStringExtra("usuaris"));
            jsonArray = jsonUsuaris.getJSONArray("usuari");
            for (int i = 0; i < jsonArray.length(); i++){
                jsonUsuaris = jsonArray.getJSONObject(i);
                user = new Usuari(jsonUsuaris.getString("name"),jsonUsuaris.getString("email"));
                usuaris.add(user);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        recyclerView = findViewById(R.id.items_recyclerview);
        itemsAdapter = new ItemsAdapter(this, usuaris);
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = findViewById(R.id.admin_users_toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}