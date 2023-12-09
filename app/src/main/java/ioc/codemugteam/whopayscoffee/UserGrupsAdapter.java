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

public class UserGrupsAdapter extends RecyclerView.Adapter<UserGrupsAdapter.ItemViewHolder>{

    private LayoutInflater mInflater;
    List<Grup> grups;
    Context context;
    String jsonMsg;

    public UserGrupsAdapter (Context context, List<Grup> grups, String jsonMsg){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.grups = grups;
        this.jsonMsg = jsonMsg;
    }
    @NonNull
    @Override
    public UserGrupsAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item, viewGroup, false);
        return new UserGrupsAdapter.ItemViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull UserGrupsAdapter.ItemViewHolder viewHolder, int position) {
        String grupName = grups.get(position).getName();
        String grupID = String.valueOf(grups.get(position).getId());
        viewHolder.itemView.setText(grupName);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context ,GrupDetallActivity.class);
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
        ImageView deleteIcon;
        UserGrupsAdapter mAdapter;

        public ItemViewHolder(View view, UserGrupsAdapter adapter) {
            super(view);
            this.itemView = view.findViewById(R.id.list_item_textView);
            this.deleteIcon = view.findViewById(R.id.list_item_delete_icon);
            this.mAdapter = adapter;
            view.setOnClickListener(this);



            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Grup grup = grups.get(getAbsoluteAdapterPosition());
                    Toast.makeText(view.getContext(), "Borrando grupo " + grup.getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }

}
