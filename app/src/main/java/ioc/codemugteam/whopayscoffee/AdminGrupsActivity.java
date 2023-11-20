package ioc.codemugteam.whopayscoffee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AdminGrupsActivity extends AppCompatActivity {


    private String[] grups = {"grup 1", "grup 2", "grup 3", "grup 4"};
    private final List<String> list = Arrays.asList(grups);
    private final List<String> itemsList = new LinkedList<String>(list);

    private RecyclerView recyclerView;
    private ItemsAdapter itemsAdapter;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_gups);

        recyclerView = findViewById(R.id.items_recyclerview);
        //itemsAdapter = new ItemsAdapter(this, itemsList);
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = findViewById(R.id.admin_grups_toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}