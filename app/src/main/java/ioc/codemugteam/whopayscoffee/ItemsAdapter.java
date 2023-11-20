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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    //private final LinkedList<String> mItemsList;
    //private final LinkedList<Usuari> mUsuarisList;
    private LayoutInflater mInflater;
    List<Usuari> usuaris;

    public ItemsAdapter(Context context, List<Usuari> usuaris){
        this.mInflater = LayoutInflater.from(context);
        this.usuaris = usuaris;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View itemView = mInflater.inflate(R.layout.list_item, viewGroup, false);
        return new ItemViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder viewHolder, int position) {
        viewHolder.itemView.setText(usuaris.get(position).getName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return usuaris.size();
    }

    /*
    public ItemsAdapter(Context context, JSONArray itemsList) throws JSONException {
        mInflater = LayoutInflater.from(context);
        //this.mItemsList = itemsList;
        for (int i = 0; i < itemsList.length(); i++){
            JSONObject jsonUser = itemsList.getJSONObject(i);
            Usuari user = new Usuari(jsonUser.getString("name"), jsonUser.getString("email"));


        }
        this.mUsuarisList = itemsList;
    }
*/

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
/*
        @Override
        public void onClick(View view) {
            int mPosition = getLayoutPosition();
            //String element = mItemsList.get(mPosition);
            //mItemsList.set(mPosition, "Clicked! " + element);
            String element = String.valueOf(items.get(mPosition));
            items.set(mPosition, "Clicked! " + element);
            mAdapter.notifyDataSetChanged();
        }
*/
    }



    // Create new views (invoked by the layout manager)

/*

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ItemViewHolder viewHolder, int position) {
        String mCurrent = mItemsList.get(position);
        viewHolder.itemView.setText(mCurrent);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

 */
}
