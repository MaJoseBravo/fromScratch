package app.random.generator.fromscratch_v01.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.random.generator.fromscratch_v01.Modelo.MyLocations;
import app.random.generator.fromscratch_v01.R;

/**
 * Created by Maria Jose Bravo on 06/09/2017.
 */

public class MyLocations_Adapter extends RecyclerView.Adapter<MyLocations_Adapter.MyLocationsViewHolder>
        implements ItemClickListener {

    private List<MyLocations> items;
    private Context context;


    public MyLocations_Adapter(List<MyLocations> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list_locations, viewGroup, false);
    }

    @Override
        viewHolder.name.setText(items.get(i).getName());
        viewHolder.description.setText(items.get(i).getDescription());
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    public static class MyLocationsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        // Campos respectivos de un item
        public TextView name;
        public TextView description;
        public ItemClickListener listener;

        public MyLocationsViewHolder(View v, ItemClickListener listener) {
            super(v);
            description = (TextView) v.findViewById(R.id.categoria_01);
            this.listener = listener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());
        }
    }
}
