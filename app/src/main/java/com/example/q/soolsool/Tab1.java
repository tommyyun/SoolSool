package com.example.q.soolsool;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Tab1 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1, container, false);
        RecyclerView rec_view = view.findViewById(R.id.tab1_rec);






    }

    class Tab1Adapter extends RecyclerView.Adapter<Tab1Adapter.Viewholder>{
        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class Viewholder extends RecyclerView.ViewHolder{

            public Viewholder(@NonNull View itemView) {
                super(itemView);

            }
        }



    }
}
