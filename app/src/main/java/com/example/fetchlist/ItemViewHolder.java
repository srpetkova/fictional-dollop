package com.example.fetchlist;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

class ItemViewHolder extends RecyclerView.ViewHolder {

    TextView listId;
    TextView id;
    TextView name;

    ItemViewHolder(View view) {
        super(view);
        listId = view.findViewById(R.id.tv2);
        id = view.findViewById(R.id.tv1);
        name = view.findViewById(R.id.tv3);
    }
}
