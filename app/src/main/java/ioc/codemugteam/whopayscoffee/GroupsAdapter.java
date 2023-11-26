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

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ItemViewHolder> {

    private LayoutInflater mInflater;
    List<Grup> grups;

    public GroupsAdapter(Context context, List<Grup> grups) {
        this.mInflater = LayoutInflater.from(context);
        this.grups = grups;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item, viewGroup, false);
        return new ItemViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder viewHolder, int position) {
        viewHolder.itemView.setText(grups.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return grups.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemView;
        ImageView deleteIcon;
        GroupsAdapter mAdapter;

        public ItemViewHolder(View view, GroupsAdapter adapter) {
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
/*
    @Override
    public GroupsAdapter.ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item, viewGroup, false);
        return new GroupsAdapter.(itemView, this);
    }

    @Override
    public void onBindViewHolder(GroupsAdapter.ItemViewHolder viewHolder, int position) {
        viewHolder.itemView.setText(grups.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return grups.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView itemView;
        ImageView deleteIcon;
        ItemsAdapter mAdapter;

        public GroupsAdapter(View view, ItemsAdapter adapter) {
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

 */
    }
}
