package com.example.q.soolsool;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab2, container, false);
        load();
        return view;
    }

    public void load() {
        if(!MainActivity.loggedIn)
            return;
        view.findViewById(R.id.tab2_sample_room).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.tab2_matched_rec).setVisibility(View.VISIBLE);
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
                        matchedRoomAdapter.addItem(new Room()
                                .setTitle(json.getString("title"))
                                .setDescription(json.getString("content"))
                                .setRegion(json.getString("region"))
                                .setDate(json.getString("date"))
                                .setTime(json.getString("time"))
                                .setInterest(json.getString("category"))
                                .setLeader(json.getString("leader"))
                                .setTargetHold(json.getInt("targetHold"))
                                .setRoomid((json.getString("_id"))));
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
    }

    private class Tab2MatchedRoomAdapter extends RecyclerView.Adapter<Tab2MatchedRoomAdapter.ViewHolder> {

        private ArrayList<Room> matchedRooms = new ArrayList<>();

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.tab2_matched_room, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
            View view = viewHolder.itemView;
            TextView room_loc = view.findViewById(R.id.room_loc);
            TextView room_title = view.findViewById(R.id.room_title);
            TextView room_date = view.findViewById(R.id.room_date);
            TextView room_time = view.findViewById(R.id.room_time);
            TextView room_content = view.findViewById(R.id.room_description);
            ImageView room_leader = view.findViewById(R.id.is_leader);
            ImageView room_type = view.findViewById(R.id.room_type);

            try {
                System.out.println("=============================");
                room_loc.setText(matchedRooms.get(i).getRegion());
                room_title.setText(matchedRooms.get(i).getTitle());
                room_date.setText(matchedRooms.get(i).getDate());
                room_time.setText(matchedRooms.get(i).getTime());
                room_content.setText(matchedRooms.get(i).getDescription());

                String strcat = matchedRooms.get(i).getInterest();

                switch (strcat) {
                    case "love": {
                        room_type.setImageResource(R.drawable.heart);
                        room_type.setAlpha(0.3f);
                        break;
                    }
                    case "life": {
                        room_type.setImageResource(R.drawable.puzzle);
                        room_type.setAlpha(0.3f);
                        break;
                    }
                    case "work": {
                        room_type.setImageResource(R.drawable.team);
                        room_type.setAlpha(0.3f);
                        break;
                    }
                    case "politics": {
                        room_type.setImageResource(R.drawable.group);
                        room_type.setAlpha(0.3f);
                        break;
                    }
                    default:
                        System.out.println("Error!!");
                }
                if (MainActivity.id.equals(matchedRooms.get(i).getLeader())) {
                    room_leader.setImageResource(R.drawable.lace);
                } else {
                    room_leader.setImageResource(0);
                }

            } catch (Exception e) {
                System.out.println("------------------------------");

                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            viewHolder.itemView.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), ChatActivity.class);
                            try{
                                intent.putExtra("room_id", matchedRooms.get(i).getRoomid());
                                intent.putExtra("title", matchedRooms.get(i).getTitle());
                                intent.putExtra("targetHold", Integer.toString(matchedRooms.get(i).getTargetHold()));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
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
            notifyItemInserted(getItemCount() - 1);
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }


}