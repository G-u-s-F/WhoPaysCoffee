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

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private LayoutInflater mInflater;
    List<Usuari> usuaris;

    public ItemsAdapter(Context context, List<Usuari> usuaris){
        this.mInflater = LayoutInflater.from(context);
        this.usuaris = usuaris;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item, viewGroup, false);
        return new ItemViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder viewHolder, int position) {
        viewHolder.itemView.setText(usuaris.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return usuaris.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView itemView;
        ImageView deleteIcon;
        ItemsAdapter mAdapter;

        public ItemViewHolder(View view, ItemsAdapter adapter) {
            super(view);
            // Define click listener for the ViewHolder's View
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
