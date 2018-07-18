package com.example.q.soolsool;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {

    private String room_id;
    private Socket socket;

    public int messageStart = -1;
    public int messageEnd = -1;

    @Override
    protected void onResume() {
        super.onResume();
        socket.connect();
        socket.emit("id", MainActivity.id);
    }

    @Override
    protected void onPause() {
        super.onPause();
        socket.disconnect();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Get room id of this chatting room
        room_id = getIntent().getStringExtra("room_id");
        ((TextView)findViewById(R.id.title)).setText(getIntent().getStringExtra("title"));
        ((TextView)findViewById(R.id.num_people)).setText(" ("+getIntent().getStringExtra("targetHold")+")");

        // Connect websocket
        try {
            socket = IO.socket("http://52.231.70.8:7070");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        // Identify recyclerview and set adapter and layout manager
        final RecyclerView chat_rec_view = findViewById(R.id.chat_rec_view);
        final ChatRecViewAdapter chatRecViewAdapter = new ChatRecViewAdapter();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chat_rec_view.setAdapter(chatRecViewAdapter);
        chat_rec_view.setLayoutManager(layoutManager);


        // When the send_button is clicked, the message is sent
        findViewById(R.id.send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (socket.connected()) {
                    try {
                        String message = ((EditText) findViewById(R.id.message_field)).getText().toString();
                        System.out.println("text : "+message);
                        if(message==null || message.equals("")) {
                            Toast.makeText(ChatActivity.this, "Empty message!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        socket.emit("chat", new JSONObject("{\"room_id\" : " + "\"" + room_id + "\"" + ", \"user_id\" : " + "\"" + MainActivity.id + "\"" + ", \"message\" : " + "\"" + message + "\"" + "}"));
                        ((EditText) findViewById(R.id.message_field)).setText("");
                    } catch (JSONException e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                }
                else
                    Toast.makeText(ChatActivity.this, "Internet connection failed", Toast.LENGTH_SHORT).show();
            }
        });

        // Receive messages stored in the db
        Volley.newRequestQueue(this).add(new JsonArrayRequest(JsonArrayRequest.Method.GET, "http://52.231.70.8:9090/receive/initial/" + room_id, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject single_message_object = response.getJSONObject(i);
                        if (i == 0)
                            messageStart = single_message_object.getInt("index");
                        if (i == response.length() - 1)
                            messageEnd = single_message_object.getInt("index");
                        chatRecViewAdapter.addItem(new SingleMessage(single_message_object.getString("creator"), single_message_object.getString("message"), (single_message_object.getString("creator").equals(MainActivity.id)) ? true : false));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                }

                //Scroll to the bottom after initialization
                if (chat_rec_view.getAdapter().getItemCount() > 0)
                    chat_rec_view.smoothScrollToPosition(chat_rec_view.getAdapter().getItemCount() - 1);
            }
        }, null));


        // Deal with cases where the soft keyboard is either shown or faded
        chat_rec_view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {

                //When layout changes and adapter has one or more items, set focus to the bottom

                //This catches SoftKeyboard show/hide
                if (i3 != i7) {
                    if (chat_rec_view.getAdapter().getItemCount() > 0)
                        //This checks whether the user is looking on the last item.
                        if (chat_rec_view.computeVerticalScrollRange() - (chat_rec_view.computeVerticalScrollOffset() + i7 - i5) < 100)
                            chat_rec_view.smoothScrollToPosition(chat_rec_view.getAdapter().getItemCount() - 1);
                }
            }
        });

        socket.on("chat", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Websocket : \"chat\" received.");
                System.out.println(args[0]);
                if (args[0].equals(room_id))
                    Volley.newRequestQueue(ChatActivity.this).add(new JsonArrayRequest("http://52.231.70.8:9090/receive/update/" + room_id + "/" + messageEnd, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            synchronized (chatRecViewAdapter) {
                                System.out.println(response.toString());
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        JSONObject single_message_object = response.getJSONObject(i);
                                        if(single_message_object.getInt("index")==messageEnd+1) {
                                            chatRecViewAdapter.addItem(new SingleMessage(single_message_object.getString("creator"), single_message_object.getString("message"), (single_message_object.getString("creator").equals(MainActivity.id)) ? true : false));
                                            messageEnd++;
                                        }
                                        //Scroll to the bottom after initialization
                                        chat_rec_view.smoothScrollToPosition(chat_rec_view.getAdapter().getItemCount() - 1);
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Error : " + error.toString());
                        }
                    }));
            }
        });
    }

    class SingleMessage {
        String person;
        String text;
        boolean onMe = false;

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
            return new ViewHolder(((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_chat_single_message, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.setMessage(messages.get(i).text);
            viewHolder.setPerson(messages.get(i).person);
            if (messages.get(i).onMe) {
                ((LinearLayout) viewHolder.itemView).setGravity(Gravity.RIGHT);
            } else {
                ((LinearLayout) viewHolder.itemView).setGravity(Gravity.LEFT);
            }
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        public void addItem(SingleMessage singleMessage) {
            messages.add(singleMessage);
            notifyItemInserted(getItemCount() - 1);
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            public void setMessage(String message) {
                ((TextView) itemView.findViewById(R.id.single_message_message)).setText(message);
            }

            public void setPerson(String person) {
                ((TextView) itemView.findViewById(R.id.single_message_person)).setText(person);
            }
        }
    }
}