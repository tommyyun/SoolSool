package com.example.q.soolsool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Tab1 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1, container, false);
        final RecyclerView all_view = view.findViewById(R.id.tab1_all_rooms);
        final RecyclerView my_view = view.findViewById(R.id.tab1_my_rooms);
        final Tab1Adapter roomAdapter = new Tab1Adapter();
        final Tab1Adapter myroomAdapter = new Tab1Adapter();
        final Spinner tab1_spinner = (Spinner) view.findViewById(R.id.tab1_spinner);

        String[] view_items = new String[]{"All Rooms", "My Rooms"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, view_items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tab1_spinner.setAdapter(adapter);
        tab1_spinner.setSelection(0);

        System.out.println(tab1_spinner.getSelectedItem().toString());

        tab1_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (tab1_spinner.getSelectedItem().toString().equals("My Rooms")) {
                    all_view.setVisibility(View.GONE);
                    my_view.setVisibility(View.VISIBLE);
                } else if (tab1_spinner.getSelectedItem().toString().equals("All Rooms")) {
                    System.out.println(tab1_spinner.getSelectedItem().toString());
                    all_view.setVisibility(View.VISIBLE);
                    my_view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        all_view.setAdapter(roomAdapter);
        my_view.setAdapter(myroomAdapter);
        LinearLayoutManager lm_all = new LinearLayoutManager(getContext());
        LinearLayoutManager lm_my = new LinearLayoutManager(getContext());
        lm_all.setOrientation(LinearLayoutManager.VERTICAL);
        lm_my.setOrientation(LinearLayoutManager.VERTICAL);
        all_view.setLayoutManager(lm_all);
        my_view.setLayoutManager(lm_my);

        FloatingActionButton create_room = (FloatingActionButton) view.findViewById(R.id.add_room);
        create_room.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), AddRoomActivity.class);
                        startActivity(intent);
                    }
                }
        );

        Volley.newRequestQueue(getContext()).add(new JsonArrayRequest("http://52.231.70.8:8080/room", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject json = (JSONObject) response.get(i);

                        //get String Array from JSON and put in Room info.
                        JSONArray participants = json.optJSONArray("participants");
                        final String[] arr = new String[participants.length()];
                        for (int j = 0; j < arr.length; j++)
                            arr[j] = participants.getString(j);

                        Room room = new Room().setLeader(json.getString("leader"))
                                .setParticipants(arr)
                                .setInterest(json.getString("category"))
                                .setTitle(json.getString("title"))
                                .setDescription(json.getString("content"))
                                .setMinHold(json.getInt("minHold"))
                                .setMaxHold(json.getInt("maxHold"))
                                .setCurrentHold(json.getInt("currentHold"))
                                .setRoomid(json.getString("_id"));

                        roomAdapter.addItem(room);
                        if (room.getLeader().equals(MainActivity.id) || Arrays.asList(room.getParticipants()).contains(MainActivity.id)) {
                            myroomAdapter.addItem(room);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage() + "12341234");
            }
        }));


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
                current.setText(rooms.get(i).getCurrentHold() + "");
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
                                    // 방장인 경우
                                    if (MainActivity.id.equals(rooms.get(i).getLeader())) {
                                        View popupView = getLayoutInflater().inflate(R.layout.tab1_dialog_delete, null);

                                        mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        //popupView 에서 (LinearLayout 을 사용) 레이아웃이 둘러싸고 있는 컨텐츠의 크기 만큼 팝업 크기를 지정
                                        mPopupWindow.setFocusable(true); // 외부 영역 선택히 PopUp 종료
                                        mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                                        // 닫기 버튼 구현
                                        Button cancel = (Button) popupView.findViewById(R.id.Cancel);
                                        cancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mPopupWindow.dismiss();
                                            }
                                        });

                                        // 삭제 버튼 구현
                                        Button delete = (Button) popupView.findViewById(R.id.delete);
                                        delete.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                try {
                                                    Volley.newRequestQueue(getContext()).add(new StringRequest(Request.Method.DELETE, "http://52.231.70.8:8080/delete_created/" + MainActivity.id + "/" + rooms.get(i).getRoomid(),
                                                            new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                    System.out.println(response);
                                                                    mPopupWindow.dismiss();
                                                                    Toast.makeText(getActivity(), "삭제되었습니다.", Toast.LENGTH_LONG).show();

                                                                }
                                                            },
                                                            new Response.ErrorListener() {
                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {
                                                                    System.out.println(error.getMessage());
                                                                }
                                                            }));
                                                } catch (Exception e) {
                                                    System.out.println(e.getMessage());
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                    // 방장이 아닌 경우
                                    else {
                                        // 방에 이미 참여한 경우
                                        if (Arrays.asList(rooms.get(i).getParticipants()).contains(MainActivity.id)) {
                                            View popupView = getLayoutInflater().inflate(R.layout.tab1_dialog_out, null);
                                            mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                            //popupView 에서 (LinearLayout 을 사용) 레이아웃이 둘러싸고 있는 컨텐츠의 크기 만큼 팝업 크기를 지정
                                            mPopupWindow.setFocusable(true); // 외부 영역 선택시 PopUp 종료
                                            mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                                            Button cancel = (Button) popupView.findViewById(R.id.Cancel);
                                            cancel.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    mPopupWindow.dismiss();
                                                }
                                            });

                                            Button join = (Button) popupView.findViewById(R.id.join);
                                            join.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    try {

                                                        Volley.newRequestQueue(getContext()).add(new StringRequest(Request.Method.DELETE, "http://52.231.70.8:8080/delete_joined/" + MainActivity.id + "/" + rooms.get(i).getRoomid(), new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                mPopupWindow.dismiss();
                                                                Toast.makeText(getActivity(), "방에서 나갔습니다.", Toast.LENGTH_LONG).show();
                                                            }
                                                        }, null));
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });

                                        }
                                        // 방에 참여하지 않은 경우
                                        else {
                                            View popupView = getLayoutInflater().inflate(R.layout.tab1_dialog_join, null);
                                            mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                            //popupView 에서 (LinearLayout 을 사용) 레이아웃이 둘러싸고 있는 컨텐츠의 크기 만큼 팝업 크기를 지정
                                            mPopupWindow.setFocusable(true); // 외부 영역 선택시 PopUp 종료
                                            mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                                            Button cancel = (Button) popupView.findViewById(R.id.Cancel);
                                            cancel.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    mPopupWindow.dismiss();
                                                }
                                            });

                                            Button join = (Button) popupView.findViewById(R.id.join);
                                            join.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    try {
                                                        Volley.newRequestQueue(getContext()).add(new StringRequest(Request.Method.GET, "http://52.231.70.8:8080/join/" + MainActivity.id + "/" + rooms.get(i).getRoomid(), new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                mPopupWindow.dismiss();
                                                                Toast.makeText(getActivity(), "참여하였습니다.", Toast.LENGTH_LONG).show();
                                                            }
                                                        }, null));
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }

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
