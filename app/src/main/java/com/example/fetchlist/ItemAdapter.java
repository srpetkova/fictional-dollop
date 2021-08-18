package com.example.fetchlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private ArrayList<Item> iList;
    private MainActivity mainActivity;

    ItemAdapter(ArrayList<Item> list, MainActivity mainActivity) {
        iList = list;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        Item selectedItem = iList.get(position);

        holder.id.setText(Integer.toString(selectedItem.getId()));
        holder.listId.setText(Integer.toString(selectedItem.getListId()));
        holder.name.setText(selectedItem.getName());

    }

    @Override
    public int getItemCount() {
        return iList.size();
    }
}
