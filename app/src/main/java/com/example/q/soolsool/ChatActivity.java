package com.example.q.soolsool;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private String room_id = "5b4dfef4737d77c7a5ec94eb";

    public int messageStart = -1;
    public int messageEnd = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Identify recyclerview and set adapter and layout manager.
        final RecyclerView chat_rec_view = findViewById(R.id.chat_rec_view);
        final ChatRecViewAdapter chatRecViewAdapter = new ChatRecViewAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);





        /*findViewById(R.id.send_button).setOnClickListener(new View.OnClickListener() {
            int i = 0;

            @Override
            public void onClick(View view) {
                chatRecViewAdapter.addItem(new SingleMessage("Person "+ ++i, i+"th message"));
            }
        });*/



        chat_rec_view.setAdapter(chatRecViewAdapter);
        chat_rec_view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                // When layout changes and adapter has one or more items, set focus to the bottom
                if(chat_rec_view.getAdapter().getItemCount()>0)
                    chat_rec_view.smoothScrollToPosition(chat_rec_view.getAdapter().getItemCount()-1);
            }
        });
        chat_rec_view.setLayoutManager(layoutManager);


        Volley.newRequestQueue(this).add(new JsonArrayRequest(JsonArrayRequest.Method.GET, "http://52.231.70.8:9090/receive/initial/"+room_id, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response);
            }
        }, null));
    }

    class SingleMessage {
        String person;
        String text;
        boolean onMe=false;
        SingleMessage(String _person, String _text) {
            person = _person;
            text = _text;
        }
        SingleMessage(String _person, String _text, boolean _onMe) {
            person = _person;
            text = _text;
            onMe = _onMe;
        }
    }

    class ChatRecViewAdapter extends RecyclerView.Adapter<ChatRecViewAdapter.ViewHolder> {

        ArrayList<SingleMessage> messages = new ArrayList<>();

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            return new ViewHolder(((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_chat_single_message, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.setMessage(messages.get(i).text);
            viewHolder.setPerson(messages.get(i).person);
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        public void addItem(SingleMessage singleMessage) {
            messages.add(singleMessage);
            notifyItemInserted(getItemCount()-1);
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            public void setMessage(String message) {
                ((TextView)itemView.findViewById(R.id.single_message_message)).setText(message);
            }

            public void setPerson(String person) {
                ((TextView)itemView.findViewById(R.id.single_message_person)).setText(person);
            }
        }
    }
}
