package com.example.q.soolsool;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AddRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1_add_room);

        final EditText title = (EditText) findViewById(R.id.input_title);
        final EditText content = (EditText) findViewById(R.id.input_content);
        final EditText minHold = (EditText) findViewById(R.id.input_minHold);
        final EditText maxHold = (EditText) findViewById(R.id.input_maxHold);
        final Spinner category = (Spinner) findViewById(R.id.spinner);
        Button create = (Button) findViewById(R.id.create);
        Button close = (Button) findViewById(R.id.cancel_create);

        String[] items = new String[]{"love", "work", "life", "politics"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddRoomActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);

        create.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Volley.newRequestQueue(getApplicationContext()).add(new StringRequest(Request.Method.POST, "http://52.231.70.8:8080/room/"+MainActivity.id ,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        System.out.println(response);
                                        finish();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        System.out.println(error.getMessage());
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("title", title.getText().toString());
                                params.put("content", content.getText().toString());
                                params.put("minHold", minHold.getText().toString());
                                params.put("maxHold", maxHold.getText().toString());
                                params.put("category", category.getSelectedItem().toString());

                                return params;
                            }
                        });
                    }
                }
        );

        close.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }
}
