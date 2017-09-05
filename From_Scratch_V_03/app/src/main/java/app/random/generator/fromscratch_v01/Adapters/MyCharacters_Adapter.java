package app.random.generator.fromscratch_v01.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.random.generator.fromscratch_v01.Modelo.MyCharacters;
import app.random.generator.fromscratch_v01.R;

/**
 * Created by anali on 05/09/17.
 */

public class MyCharacters_Adapter extends RecyclerView.Adapter<MyCharacters_Adapter.MyCharactersViewHolder>
        implements ItemClickListener{


    private List<MyCharacters> items;
    private Context context;


    public MyCharacters_Adapter(List<MyCharacters> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public MyCharactersViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list_characters, viewGroup, false);
        return new MyCharactersViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(MyCharactersViewHolder viewHolder, int i) {
        viewHolder.name.setText(items.get(i).getName());
        viewHolder.race.setText(items.get(i).getDescription());
        viewHolder.gender.setText(items.get(i).getDescription_gender());
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    public static class MyCharactersViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        // Campos respectivos de un item
        public TextView name;
        public TextView race;
        public TextView gender;
        public ItemClickListener listener;

        public MyCharactersViewHolder(View v, ItemClickListener listener) {
            super(v);
            name = (TextView) v.findViewById(R.id.nameCharacter);
            race = (TextView) v.findViewById(R.id.categoria_01);
            gender = (TextView) v.findViewById(R.id.categoria_02);
            this.listener = listener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());
        }
    }
}

