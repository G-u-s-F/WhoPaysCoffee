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

import java.util.List;

public class GrupDetallAdapter extends RecyclerView.Adapter<GrupDetallAdapter.ItemViewHolder>{

    private LayoutInflater mInflater;
    List<Membre> members;

    public GrupDetallAdapter (Context context, List<Membre> members){
        this.mInflater = LayoutInflater.from(context);
        this.members = members;
    }
    @NonNull
    @Override
    public GrupDetallAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item, viewGroup, false);
        return new GrupDetallAdapter.ItemViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull GrupDetallAdapter.ItemViewHolder viewHolder, int position) {
        viewHolder.itemView.setText(members.get(position).getNickName());
        viewHolder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMembre(members.get(viewHolder.getAbsoluteAdapterPosition()));
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

    public void deleteMembre(Membre membre){

    }
}
