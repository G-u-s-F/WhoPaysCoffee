package ioc.codemugteam.whopayscoffee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserGrupsAdapter extends RecyclerView.Adapter<UserGrupsAdapter.ItemViewHolder>{

    private LayoutInflater mInflater;
    List<Grup> grups;

    public UserGrupsAdapter (Context context, List<Grup> grups){
        this.mInflater = LayoutInflater.from(context);
        this.grups = grups;
    }
    @NonNull
    @Override
    public UserGrupsAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item, viewGroup, false);
        return new UserGrupsAdapter.ItemViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull UserGrupsAdapter.ItemViewHolder viewHolder, int position) {
        viewHolder.itemView.setText(grups.get(position).getName());
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
                    String nombre = itemView.getText().toString();
                    itemView.setText("Deleted " + nombre);
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }

}
