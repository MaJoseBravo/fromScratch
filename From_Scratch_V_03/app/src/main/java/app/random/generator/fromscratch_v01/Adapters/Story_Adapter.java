package app.random.generator.fromscratch_v01.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import app.random.generator.fromscratch_v01.Story_Overview;

/**
 * Created by anali on 12/08/17.
 */

public class Story_Adapter extends RecyclerView.Adapter<Story_Adapter.StoryViewHolder> {

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
    public Story_Adapter.StoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            return new Story_Adapter.StoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Story_Adapter.StoryViewHolder viewHolder, final int position) {

            final Story story = items.get(position);

            viewHolder.name.setText(story.getName());
            viewHolder.synopsis.setText(story.getSynopsis());
            viewHolder.description.setText(story.getDescription());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Story_Overview myFragment = new Story_Overview();
                    //Create a bundle to pass data, add data, set the bundle to your fragment and:

                    Bundle bundle = new Bundle();
                    bundle.putString("id", String.valueOf(story.getId()));
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.Contenedor, myFragment).addToBackStack(null).commit();
                }
            });
    }


    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView name;
        public TextView synopsis;
        public TextView description;

        public StoryViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.title_story);
            synopsis = (TextView) v.findViewById(R.id.synopis_story);
            description = (TextView) v.findViewById(R.id.genre_story);
            //v.setOnClickListener(this);
        }

    }

}


