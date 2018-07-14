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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Tab1 extends Fragment {
    static JSONObject json;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1, container, false);
        final RecyclerView rec_view = view.findViewById(R.id.tab1_rec);

        Volley.newRequestQueue(getActivity()).add(new StringRequest(Request.Method.GET, "http://52.231.70.8:8080/room", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            json = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Tab1Adapter roomAdapter = new Tab1Adapter();
                        rec_view.setAdapter(roomAdapter);
                        LinearLayoutManager lm = new LinearLayoutManager(getContext());
                        lm.setOrientation(LinearLayoutManager.VERTICAL);
                        rec_view.setLayoutManager(lm);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }));


        return view;
    }

    class Tab1Adapter extends RecyclerView.Adapter<Tab1Adapter.Viewholder>{
        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new Viewholder(((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.tab1_room, viewGroup, false ));
        }

        @Override
        public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {
            return;
        }

        @Override
        public int getItemCount() {
            return 7;
        }

        class Viewholder extends RecyclerView.ViewHolder{

            public Viewholder(@NonNull View itemView) {
                super(itemView);

                try {
                    ((TextView) itemView.findViewById(R.id.title)).setText(json.getString("title"));
                    ((TextView) itemView.findViewById(R.id.content)).setText(json.getString("content"));
                }catch(Exception e) {
                    e.printStackTrace();
                }


            }
        }
    }
}
