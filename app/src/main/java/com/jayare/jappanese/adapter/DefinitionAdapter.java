package com.jayare.jappanese.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jayare.jappanese.definition.Definition;
import com.jayare.jappanese.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DefinitionAdapter extends RecyclerView.Adapter<DefinitionAdapter.DefintionHolder> {

    private List<Definition> definitions = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public DefintionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.definition_item, parent, false);

        return new DefintionHolder(itemView);
    }

    //take create getting data from single definition java objects into views of definitionholder
    @Override
    public void onBindViewHolder(@NonNull DefintionHolder holder, int position) {
        Definition currentDefinition = definitions.get(position);
        holder.textEnglish.setText(currentDefinition.getEnglish());
        holder.textFurigana.setText(currentDefinition.getFurigana());
        holder.textJapanese.setText(currentDefinition.getJapanese());
    }

    @Override
    public int getItemCount() {
        return definitions.size();
    }

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
        notifyDataSetChanged();
    }

    public Definition getDefinitionAt(int position) {
        return definitions.get(position);
    }


    class DefintionHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtEnglish)
        TextView textEnglish;
        @BindView(R.id.txtFurigana)
        TextView textFurigana;
        @BindView(R.id.txtJapanese)
        TextView textJapanese;

        public DefintionHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(definitions.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Definition definition);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
