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

public class PagosAdapter extends RecyclerView.Adapter<PagosAdapter.ItemViewHolder>{

    private LayoutInflater mInflater;
    List<Grup> grups;
    Context context;
    String jsonMsg;

    public PagosAdapter (Context context, List<Grup> grups, String jsonMsg){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.grups = grups;
        this.jsonMsg = jsonMsg;
    }
    @NonNull
    @Override
    public PagosAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item, viewGroup, false);
        return new PagosAdapter.ItemViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PagosAdapter.ItemViewHolder viewHolder, int position) {
        String grupName = grups.get(position).getName();
        String grupID = String.valueOf(grups.get(position).getId());
        viewHolder.itemView.setText(grupName);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context ,GrupDetallPagosActivity.class);
                intent.putExtra("grupID", grupID);
                intent.putExtra("grupName", grupName);
                intent.putExtra("user", jsonMsg);
                context.startActivity(intent);
            }
        });

        viewHolder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int pos = viewHolder.getAbsoluteAdapterPosition();
                    deleteGrup(grups.get(pos), new JSONObject(jsonMsg), pos, viewHolder);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return grups.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemView;
        ImageView deleteIcon;
        PagosAdapter mAdapter;

        public ItemViewHolder(View view, PagosAdapter adapter) {
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

    /**
     * Funció que envia al servidor la crida per eliminar el grup
     * @param user
     * @param grup
     * @param pos
     * @param viewHolder
     * @throws JSONException
     */
    public void deleteGrup(Grup grup, JSONObject user, int pos, PagosAdapter.ItemViewHolder viewHolder) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.serverURL) + "/coffee/api/groups/delete/group/" + grup.getId();
        String autoritzacio = user.getString("head") + " " + user.getString("token");


        StringRequest request = new StringRequest(Request.Method.DELETE, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                grups.remove(pos);
                viewHolder.mAdapter.notifyDataSetChanged();
            }
        }, error -> Toast.makeText(context, "Només el propietari pot eliminar el grup", Toast.LENGTH_SHORT).show()) {
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
