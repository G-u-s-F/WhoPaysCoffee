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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ItemViewHolder> {

    private LayoutInflater mInflater;
    List<Grup> grups;
    Context context;


    public GroupsAdapter(Context context, List<Grup> grups) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.grups = grups;
    }

    @NonNull
    @Override
    public GroupsAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item, viewGroup, false);
        return new GroupsAdapter.ItemViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder viewHolder, int position) {
        String grupName = grups.get(position).getName();
        String numMembres = String.valueOf(grups.get(position).getId());
        viewHolder.itemView.setText(grupName + " (" + numMembres + " membres");
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


    }
}
