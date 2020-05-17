package com.example.carcarcarcar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity  {

    private String url;
    private RequestQueue queue;
    private Boolean result;

    private String rentId, userId, carId,rentDate,returnDate;
    private JSONObject history, newDefects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        userId = getIntent().getStringExtra("user_id");

        /*
        Listview list = (ListView) findViewById(R.id.history);

        ArrayAdapter<String> adapter = new ArrayAdapter <String>(this, android.R.layout.simple_list_item_1, historyList);
        list.setAdapter(adapter);

        list.setOnItemClickListener (new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        */

        //서버
        queue = Volley.newRequestQueue(this);
        String url = "http://localhost:3000/viewHistory";



        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    result = response.getBoolean("result");
                    Toast.makeText(getApplicationContext(), "get response from server", Toast.LENGTH_LONG).show();
                    if (result) {
                        history = response.getJSONObject("history");
                        rentId = history.getString("rent_id");
                        userId = history.getString("user_id");
                        carId = history.getString("car_id");
                        rentDate = history.getString("rent_date");
                        returnDate = history.getString("return_date");
                        newDefects = history.getJSONObject("new_defects");

                    } else {
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userId);
                Toast.makeText(getApplicationContext(), "send request to server", Toast.LENGTH_LONG).show();
                return params;
            }
        };

        queue.add(jsonRequest);

    }

    public void onreturnButtonClicked(View v) {
        Intent intent = new Intent(this, BeforePastHistory.class);
        intent.putExtra("rent_id",rentId);
        intent.putExtra("return_date",returnDate);
        intent.putExtra("car_id",carId);
        intent.putExtra("rent_date",rentDate);
        //intent.putExtra("new_defects",newDefects);
        startActivity(intent);
    }
    public void onCameraButtonClicked(View v) {
        Intent intent2 = new Intent(HistoryActivity.this, CameraActivity.class);
        startActivity(intent2);

    }
    public void pasthistoryClicked(View v) {
        Intent intent3 = new Intent(HistoryActivity.this, AfterPastHistory.class);
        intent3.putExtra("rent_id",rentId);
        intent3.putExtra("return_date",returnDate);
        intent3.putExtra("car_id",carId);
        intent3.putExtra("rent_date",rentDate);
        startActivity(intent3);

    }
    public void completedhistory(View v) {
        Intent intent4 = new Intent(this, CompareActivity.class);
        intent4.putExtra("rent_id",rentId);
        startActivity(intent4);
    }


}
