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

import java.util.List;

public class EstadisticaAdapter extends RecyclerView.Adapter<EstadisticaAdapter.ItemViewHolder>{

    private LayoutInflater mInflater;
    List<Grup> grups;
    Context context;
    String jsonMsg;

    public EstadisticaAdapter (Context context, List<Grup> grups, String jsonMsg){

        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.grups = grups;
        this.jsonMsg = jsonMsg;
    }
    @NonNull
    @Override
    public EstadisticaAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item_estadistica, viewGroup, false);
        return new EstadisticaAdapter.ItemViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull EstadisticaAdapter.ItemViewHolder viewHolder, int position) {
        String grupName = grups.get(position).getName();
        int grupID = grups.get(position).getId();
        viewHolder.itemView.setText(grupName);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context ,GrupDetallEstadisticaActivity.class);
                intent.putExtra("grupID", grupID);
                intent.putExtra("grupName", grupName);
                intent.putExtra("user", jsonMsg);
                context.startActivity(intent);
            }
        });

        viewHolder.estadisticaIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context ,GrupDetallEstadisticaActivity.class);
                intent.putExtra("grupID", grupID);
                intent.putExtra("grupName", grupName);
                intent.putExtra("user", jsonMsg);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return grups.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemView;
        ImageView estadisticaIcon;
        EstadisticaAdapter mAdapter;

        public ItemViewHolder(View view, EstadisticaAdapter adapter) {
            super(view);
            this.itemView = view.findViewById(R.id.list_item_estadistica_textView);
            this.estadisticaIcon = view.findViewById(R.id.list_item_estadistica_quesito_icon);
            this.mAdapter = adapter;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
