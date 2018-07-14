package com.example.q.soolsool;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Tab1 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1, container, false);
        final RecyclerView rec_view = view.findViewById(R.id.tab1_rec);

        final Tab1Adapter roomAdapter = new Tab1Adapter();
        rec_view.setAdapter(roomAdapter);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rec_view.setLayoutManager(lm);

        Volley.newRequestQueue(getContext()).add(new JsonArrayRequest("http://52.231.70.8:8080/room", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject json = (JSONObject) response.get(i);
                        Room room = new Room().setInterest(json.getString("category"))
                                .setTitle(json.getString("title"))
                                .setDescription(json.getString("content"))
                                .setMaxHold(json.getInt("maxHold"))
                                .setCurrentHold(json.getInt("currentHold"));

                        roomAdapter.addItem(room);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, null));

        return view;
    }

    class Tab1Adapter extends RecyclerView.Adapter<Tab1Adapter.Viewholder> {
        private ArrayList<Room> rooms = new ArrayList<>();

        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new Viewholder(((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.tab1_room, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {
            View view = viewholder.itemView;
            TextView title = view.findViewById(R.id.title);
            TextView content = view.findViewById(R.id.content);
//            TextView max = view.findViewById(R.id.maxhold);
//            TextView current = view.findViewById(R.id.currenthold);
            ImageView category = view.findViewById(R.id.category_view);
            try {
                title.setText(rooms.get(i).getTitle());
                content.setText(rooms.get(i).getDescription());
//                max.setText(rooms.get(i).getMaxHold());
//                current.setText(rooms.get(i).getCurrentHold());
                String strcat = rooms.get(i).getInterest();

                System.out.println(strcat);
//                if (strcat.equals("love")) {
//                    category.setImageResource(R.drawable.heart);
//                } else if (strcat.equals("life")) {
//                    category.setImageResource(R.drawable.puzzle);
//                } else if (strcat.equals("work")) {
//                    category.setImageResource(R.drawable.team);
//                } else if (strcat.equals("politics")) {
//                    category.setImageResource(R.drawable.group);
//                }
                switch (strcat) {
                    case "love":
                        category.setImageResource(R.drawable.heart);
                        return;
                    case "life":
                        category.setImageResource(R.drawable.puzzle);
                        return;
                    case "work":
                        category.setImageResource(R.drawable.team);
                        return;
                    case "politics":
                        category.setImageResource(R.drawable.group);
                        return;
                }return;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return rooms.size();
        }

        public void addItem(Room room) {
            rooms.add(room);
            notifyItemInserted(getItemCount() - 1);
        }

        class Viewholder extends RecyclerView.ViewHolder {

            public Viewholder(@NonNull View itemView) {
                super(itemView);

                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
