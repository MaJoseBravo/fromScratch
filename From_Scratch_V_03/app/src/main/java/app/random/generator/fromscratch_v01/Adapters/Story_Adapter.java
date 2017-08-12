package app.random.generator.fromscratch_v01.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import app.random.generator.fromscratch_v01.MainActivity;
import app.random.generator.fromscratch_v01.Modelo.Story;
import app.random.generator.fromscratch_v01.R;
import app.random.generator.fromscratch_v01.Splash_Menu;

/**
 * Created by anali on 12/08/17.
 */

public class Story_Adapter extends RecyclerView.Adapter<Story_Adapter.StoryViewHolder>
        implements ItemClickListener {

    private List<Story> items;

    private Context context;

    public Story_Adapter(List<Story> items, Context context) {
        this.context = context;
        this.items = items;
    }


    @Override
    public int getItemCount() {
            return items.size();
            }

    @Override
    public StoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.item_list, viewGroup, false);
            return new StoryViewHolder(v, this);
            }

    @Override
    public void onBindViewHolder(StoryViewHolder viewHolder, int i) {
            viewHolder.name.setText(items.get(i).getName());
            viewHolder.synopsis.setText(items.get(i).getSynopsis());
    }

    /**
     * Sobrescritura del método de la interfaz {@link ItemClickListener}
     *
     * @param view     item actual
     * @param position posición del item actual
     */

    @Override
    public void onItemClick(View view, int position) {
          /* MainActivity.launch(
            (Activity) context, items.get(position).getIdStory());*/
    }


    public static class StoryViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        // Campos respectivos de un item
        public TextView name;
        public TextView synopsis;
        public ItemClickListener listener;

        public StoryViewHolder(View v, ItemClickListener listener) {
            super(v);
            name = (TextView) v.findViewById(R.id.title_story);
            synopsis = (TextView) v.findViewById(R.id.synopis_story);
            this.listener = listener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());
        }
    }
}


    interface ItemClickListener {
        void onItemClick(View view, int position);
    }
