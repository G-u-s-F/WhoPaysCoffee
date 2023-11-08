package ioc.codemugteam.whopayscoffee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private final LinkedList<String> mItemsList;
    private LayoutInflater mInflater;

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView itemView;
        final ItemsAdapter mAdapter;

        public ItemViewHolder(View view, ItemsAdapter adapter) {
            super(view);
            // Define click listener for the ViewHolder's View
            itemView = view.findViewById(R.id.list_item_textView);
            this.mAdapter = adapter;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int mPosition = getLayoutPosition();
            String element = mItemsList.get(mPosition);
            mItemsList.set(mPosition, "Clicked! " + element);
            mAdapter.notifyDataSetChanged();
        }

    }

    public ItemsAdapter(Context context, LinkedList<String> itemsList) {
        mInflater = LayoutInflater.from(context);
        this.mItemsList = itemsList;
    }

    // Create new views (invoked by the layout manager)

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View itemView = mInflater.inflate(R.layout.list_item,
                viewGroup, false);

        return new ItemViewHolder(itemView, this);
    }

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
}
