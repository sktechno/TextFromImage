package com.sk.doubtnut;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sk on 20/07/17.
 */

public class Adapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<DataModel> list;

    public Adapter(Context context, ArrayList<DataModel> list) {

        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v1 = inflater.inflate(R.layout.listing_item, parent, false);
        viewHolder = new viewHolder(v1);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {

        final viewHolder holder  = (viewHolder) holder1;

        if (position==0){

            holder.id.setText("Id");
            holder.text.setText("Text");
            holder.tag.setText("Tags");
            holder.path.setText("Image Path");
            holder.id.setTypeface(Typeface.DEFAULT_BOLD);
            holder.path.setTypeface(Typeface.DEFAULT_BOLD);
            holder.text.setTypeface(Typeface.DEFAULT_BOLD);
            holder.tag.setTypeface(Typeface.DEFAULT_BOLD);

        }else {
            DataModel data = list.get(position-1);
            holder.id.setText(data.getId());
            holder.text.setText(data.getImageText());
            holder.tag.setText(data.getTag());
            holder.path.setText(data.getImagePath());
        }


    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    private class viewHolder extends RecyclerView.ViewHolder {

        TextView id, tag, text, path;

        public viewHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.list_id);
            tag = (TextView) itemView.findViewById(R.id.list_tag);
            text = (TextView) itemView.findViewById(R.id.list_text);
            path = (TextView) itemView.findViewById(R.id.list_path);

        }
    }
}
