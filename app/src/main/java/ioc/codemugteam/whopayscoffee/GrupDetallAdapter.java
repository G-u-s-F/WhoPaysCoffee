/**
 * @author Gustavo Ferrario Barber
 * M13 DAM 2023-24 S1
 */

package ioc.codemugteam.whopayscoffee;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrupDetallAdapter extends RecyclerView.Adapter<GrupDetallAdapter.ItemViewHolder>{

    private LayoutInflater mInflater;
    List<Membre> members;
    Context context;
    String jsonMsg;

    public GrupDetallAdapter (Context context, List<Membre> members, String jsonMsg){
        this.mInflater = LayoutInflater.from(context);
        this.members = members;
        this.context = context;
        this.jsonMsg = jsonMsg;
    }
    @NonNull
    @Override
    public GrupDetallAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item, viewGroup, false);
        return new GrupDetallAdapter.ItemViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull GrupDetallAdapter.ItemViewHolder viewHolder, int position) {
        String nickName = members.get(position).getNickName();
        String userName = members.get(position).getUserName();
        int grupId = members.get(position).getGrupID();
        boolean isAdmin = members.get(position).getAdmin();
        viewHolder.itemView.setText(nickName);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context ,MembreDetallActivity.class);
                intent.putExtra("grupId", grupId);
                intent.putExtra("nickName", nickName);
                intent.putExtra("userName", userName);
                intent.putExtra("isAdmin", isAdmin);
                intent.putExtra("user", jsonMsg);
                context.startActivity(intent);
            }
        });

        viewHolder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int pos = viewHolder.getAbsoluteAdapterPosition();
                    deleteMembre(members.get(pos), new JSONObject(jsonMsg), pos, viewHolder);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemView;
        ImageView deleteIcon;
        GrupDetallAdapter mAdapter;

        public ItemViewHolder(View view, GrupDetallAdapter adapter) {
            super(view);
            this.itemView = view.findViewById(R.id.list_item_textView);
            this.deleteIcon = view.findViewById(R.id.list_item_delete_icon);
            this.mAdapter = adapter;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public void deleteMembre(Membre membre, JSONObject user, int pos, ItemViewHolder viewHolder) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.serverURL) + "/coffee/api/groups/delete/member/from/group/" + membre.getUserID() + "/" + membre.getGrupID();
        String autoritzacio = user.getString("head") + " " + user.getString("token");


        StringRequest request = new StringRequest(Request.Method.DELETE, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                members.remove(pos);
                viewHolder.mAdapter.notifyDataSetChanged();
            }
        }, error -> Toast.makeText(context, "Fail to get response = " + error, Toast.LENGTH_SHORT).show()) {
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
