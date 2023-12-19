/**
 * @author Gustavo Ferrario Barber
 * M13 DAM 2023-24 S1
 */

package ioc.codemugteam.whopayscoffee;

import android.content.Context;
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

public class MembrePagosDetallAdapter extends RecyclerView.Adapter<MembrePagosDetallAdapter.ItemViewHolder>{

    private LayoutInflater mInflater;
    List<Pago> pagos;
    Context context;
    String jsonMsg;

    public MembrePagosDetallAdapter (Context context, List<Pago> pagos, String jsonMsg){
        this.mInflater = LayoutInflater.from(context);
        this.pagos = pagos;
        this.context = context;
        this.jsonMsg = jsonMsg;
    }

    @NonNull
    @Override
    public MembrePagosDetallAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item, viewGroup, false);
        return new MembrePagosDetallAdapter.ItemViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MembrePagosDetallAdapter.ItemViewHolder viewHolder, int position) {
        String pagament = pagos.get(position).getPaymentDate().toString() + " --> " + pagos.get(position).getAmount() + " €";
        viewHolder.itemView.setText(pagament);

        viewHolder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int pos = viewHolder.getAbsoluteAdapterPosition();
                    deletePago(pagos.get(pos), new JSONObject(jsonMsg), pos, viewHolder);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return pagos.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemView;
        ImageView deleteIcon;
        MembrePagosDetallAdapter mAdapter;

        public ItemViewHolder(View view, MembrePagosDetallAdapter adapter) {
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

    public void deletePago(Pago pago, JSONObject user, int pos, MembrePagosDetallAdapter.ItemViewHolder viewHolder) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.serverURL) + "/coffee/api/payments/delete?groupId=" + pago.getGroupId() + "&paymentId=" + pago.getPaymentId();
        String autoritzacio = user.getString("head") + " " + user.getString("token");


        StringRequest request = new StringRequest(Request.Method.DELETE, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "Pagament eliminat", Toast.LENGTH_SHORT).show();
                pagos.remove(pos);
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
