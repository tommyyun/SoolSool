package com.example.q.soolsool;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Tab2 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2, container, false);
        RecyclerView rec_view = view.findViewById(R.id.tab2_matched_rec);
        final Tab2MatchedRoomAdapter matchedRoomAdapter = new Tab2MatchedRoomAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rec_view.setAdapter(matchedRoomAdapter);
        rec_view.setLayoutManager(layoutManager);
        Volley.newRequestQueue(getContext()).add(new StringRequest(StringRequest.Method.GET, "http://52.231.70.8:8080/room/matched/" + MainActivity.id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    JSONArray jsonArr = new JSONArray(response);
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject json = jsonArr.getJSONObject(i);
                        matchedRoomAdapter.addItem(new Room().setTitle(json.getString("title")).setDescription(json.getString("content")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        }));
        return view;
    }

    private class Tab2MatchedRoomAdapter extends  RecyclerView.Adapter<Tab2MatchedRoomAdapter.ViewHolder> {

        private ArrayList<Room> matchedRooms = new ArrayList<>();

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.tab2_matched_room, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            try {
                ((TextView) viewHolder.itemView.findViewById(R.id.room_title)).setText(matchedRooms.get(i).getTitle());
                ((TextView) viewHolder.itemView.findViewById(R.id.room_description)).setText(matchedRooms.get(i).getDescription());
            }catch(Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            viewHolder.itemView.setOnClickListener(
                    new View.OnClickListener(){
                        public void onClick(View v){
                            Intent intent = new Intent(getContext(), ChatActivity.class);
                            startActivity(intent);
                        }
                    }
            );
            return;
        }

        @Override
        public int getItemCount() {
            return matchedRooms.size();
        }

        public void addItem(Room matchedRoom) {
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
