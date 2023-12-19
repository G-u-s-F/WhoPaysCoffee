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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrupDetallPagosAdapter extends RecyclerView.Adapter<GrupDetallPagosAdapter.ItemViewHolder>{

    private LayoutInflater mInflater;
    List<Membre> members;
    Context context;
    String jsonMsg;

    public GrupDetallPagosAdapter (Context context, List<Membre> members, String jsonMsg){
        this.mInflater = LayoutInflater.from(context);
        this.members = members;
        this.context = context;
        this.jsonMsg = jsonMsg;
    }

    @NonNull
    @Override
    public GrupDetallPagosAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item_pagos, viewGroup, false);
        return new GrupDetallPagosAdapter.ItemViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull GrupDetallPagosAdapter.ItemViewHolder viewHolder, int position) {
        String nickName = members.get(position).getNickName();
        String userName = members.get(position).getUserName();
        int membreId = members.get(position).getUserID();
        int grupId = members.get(position).getGrupID();
        boolean isAdmin = members.get(position).getAdmin();
        viewHolder.itemView.setText(nickName);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context ,MembrePagosDetallActivity.class);
                intent.putExtra("grupId", String.valueOf(grupId));
                intent.putExtra("nickName", nickName);
                intent.putExtra("membreId", String.valueOf(membreId));
                intent.putExtra("user", jsonMsg);
                context.startActivity(intent);
            }
        });

        viewHolder.euroIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context ,NewPagoActivity.class);
                intent.putExtra("grupId", grupId);
                intent.putExtra("nickName", nickName);
                intent.putExtra("membreId", membreId);
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
        ImageView deleteIcon, euroIcon;
        GrupDetallPagosAdapter mAdapter;

        public ItemViewHolder(View view, GrupDetallPagosAdapter adapter) {
            super(view);
            this.itemView = view.findViewById(R.id.list_item_pagos_textView);
            this.deleteIcon = view.findViewById(R.id.list_item_pagos_delete_icon);
            this.euroIcon = view.findViewById(R.id.list_item_pagos_euro_icon);
            this.mAdapter = adapter;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public void deleteMembre(Membre membre, JSONObject user, int pos, GrupDetallPagosAdapter.ItemViewHolder viewHolder) throws JSONException {
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
