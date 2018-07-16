package com.example.q.soolsool;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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

                        //get String Array from JSON and put in Room info.
                        JSONArray participants = json.optJSONArray("participants");
                        String[] arr = new String[participants.length()];
                        for (int j = 0; j < arr.length; j++)
                            arr[j] = participants.getString(j);

                        Room room = new Room().setLeader(json.getString("leader"))
                                .setParticipants(arr)
                                .setInterest(json.getString("category"))
                                .setTitle(json.getString("title"))
                                .setDescription(json.getString("content"))
                                .setMinHold(json.getInt("minHold"))
                                .setMaxHold(json.getInt("maxHold"));

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
        private PopupWindow mPopupWindow;
        private ArrayList<Room> rooms = new ArrayList<>();

        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new Viewholder(((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.tab1_room, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull Viewholder viewholder, final int i) {
            View view = viewholder.itemView;
            TextView title = view.findViewById(R.id.title);
            TextView content = view.findViewById(R.id.content);
            TextView max = view.findViewById(R.id.maxhold);
            TextView current = view.findViewById(R.id.currenthold);
            ImageView leader = view.findViewById(R.id.check_leader);
            ImageView category = view.findViewById(R.id.category_view);

            try {
                title.setText(rooms.get(i).getTitle());
                content.setText(rooms.get(i).getDescription());
                max.setText(" / " + rooms.get(i).getMaxHold() + "");
                current.setText(rooms.get(i).getParticipants().length + "");
                String strcat = rooms.get(i).getInterest();

                switch (strcat) {
                    case "love": {
                        category.setImageResource(R.drawable.heart);
                        break;
                    }
                    case "life": {
                        category.setImageResource(R.drawable.puzzle);
                        break;
                    }
                    case "work": {
                        category.setImageResource(R.drawable.team);
                        break;
                    }
                    case "politics": {
                        category.setImageResource(R.drawable.group);
                        break;
                    }
                    default:
                        System.out.println("Error!!");
                }

                if (MainActivity.id.equals(rooms.get(i).getLeader())) {
                    leader.setImageResource(R.drawable.lace);
                } else {
                    leader.setImageResource(0);
                }
            } catch (Exception e) {
                e.getMessage();
                e.printStackTrace();
            }

            //로그인 상황 확인
            if (!MainActivity.loggedIn) {
                viewholder.itemView.setOnClickListener(
                        new View.OnClickListener() {
                            public void onClick(View v) {
                                Toast.makeText(getActivity(), "로그인이 필요합니다.", Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                //로그인 되어 있을 때, 방장 여부에 따른 Dialog 화면 출력
                viewholder.itemView.setOnClickListener(
                        new View.OnClickListener() {
                            public void onClick(View v) {
                                try {
                                    if (MainActivity.id.equals(rooms.get(i).getLeader())) {
                                        View popupView = getLayoutInflater().inflate(R.layout.tab1_dialog_delete, null);
                                        mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        //popupView 에서 (LinearLayout 을 사용) 레이아웃이 둘러싸고 있는 컨텐츠의 크기 만큼 팝업 크기를 지정
                                        mPopupWindow.setFocusable(true); // 외부 영역 선택히 PopUp 종료
                                        mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                                        Button cancel = (Button) popupView.findViewById(R.id.Cancel);
                                        cancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mPopupWindow.dismiss();
                                            }
                                        });

                                        Button delete = (Button) popupView.findViewById(R.id.delete);
                                        delete.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Toast.makeText(getActivity(), "삭제 기능을 곧 추가할 예정입니다.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        View popupView = getLayoutInflater().inflate(R.layout.tab1_dialog_join, null);
                                        mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        //popupView 에서 (LinearLayout 을 사용) 레이아웃이 둘러싸고 있는 컨텐츠의 크기 만큼 팝업 크기를 지정
                                        mPopupWindow.setFocusable(true); // 외부 영역 선택히 PopUp 종료
                                        mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                                        Button cancel = (Button) popupView.findViewById(R.id.Cancel);
                                        cancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mPopupWindow.dismiss();
                                            }
                                        });

                                        Button delete = (Button) popupView.findViewById(R.id.join);
                                        delete.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Toast.makeText(getActivity(), "참여 기능을 곧 추가할 예정입니다.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                    e.printStackTrace();
                                }
                            }
                        }
                );
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
