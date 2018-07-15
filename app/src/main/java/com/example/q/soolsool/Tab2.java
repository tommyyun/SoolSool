package com.example.q.soolsool;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class Tab2 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2, container, false);
        RecyclerView rec_view = view.findViewById(R.id.tab2_matched_rec);
        Tab2MatchedRoomAdapter matchedRoomAdapter = new Tab2MatchedRoomAdapter();
        matchedRoomAdapter.addItem(new MatchedRoom());
        matchedRoomAdapter.addItem(new MatchedRoom());
        matchedRoomAdapter.addItem(new MatchedRoom());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rec_view.setAdapter(matchedRoomAdapter);
        rec_view.setLayoutManager(layoutManager);
        return view;
    }

    private class Tab2MatchedRoomAdapter extends  RecyclerView.Adapter<Tab2MatchedRoomAdapter.ViewHolder> {

        private ArrayList<MatchedRoom> matchedRooms = new ArrayList<>();

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.tab2_matched_room, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            return;
        }

        @Override
        public int getItemCount() {
            return matchedRooms.size();
        }

        public void addItem(MatchedRoom matchedRoom) {
            matchedRooms.add(matchedRoom);
            notifyItemInserted(getItemCount()-1);
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }


}
