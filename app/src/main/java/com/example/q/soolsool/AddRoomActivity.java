package com.example.q.soolsool;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
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
        final EditText region = (EditText) findViewById(R.id.region);
        final EditText day = (EditText) findViewById(R.id.date);
        final EditText time = (EditText) findViewById(R.id.time);

        Button create = (Button) findViewById(R.id.create);
        Button close = (Button) findViewById(R.id.cancel_create);

        String[] items = new String[]{"love", "work", "life", "politics"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddRoomActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myCalendar.set(Calendar.YEAR, i);
                myCalendar.set(Calendar.MONTH, i1);
                myCalendar.set(Calendar.DAY_OF_MONTH, i2);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

                day.setText(sdf.format(myCalendar.getTime()));
            }
        };
        day.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddRoomActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddRoomActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        create.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Volley.newRequestQueue(getApplicationContext()).add(new StringRequest(Request.Method.POST, "http://52.231.70.8:8080/room/" + MainActivity.id,
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
                                params.put("date", day.getText().toString());
                                params.put("time", time.getText().toString());
                                params.put("region", region.getText().toString());

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
