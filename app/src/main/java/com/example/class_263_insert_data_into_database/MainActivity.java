package com.example.class_263_insert_data_into_database;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText edName, edNumber, edEmail;
    ProgressBar progressBar;
    Button buttonInsert;
    ListView listView;

    HashMap<String, String>hashMap;
    ArrayList< HashMap<String,String> >arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edName = findViewById(R.id.edName);
        edNumber = findViewById(R.id.edNumber);
        edEmail = findViewById(R.id.edEmail);
        progressBar = findViewById(R.id.progressBar);
        buttonInsert = findViewById(R.id.buttonInsert);
        listView = findViewById(R.id.listView);

        loadData();

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edName.getText().toString();
                String mobile = edNumber.getText().toString();
                String email = edEmail.getText().toString();


                //String url = "https://masterbari69.000webhostapp.com/apps/data.php?n=" +name + "&m=" +mobile + "&e=" +email;

//                String url = "http://192.168.0.117/Apps/data.php?n=" +name + "&m=" +mobile + "&e=" +email;
                String url = "http://localhost/Apps/data.php?n=" +name + "&m=" +mobile + "&e=" +email;

                progressBar.setVisibility(View.VISIBLE);

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        new AlertDialog.Builder(MainActivity.this).setTitle("Server Response")
                                .setMessage(response)
                                .show();

                        loadData();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                requestQueue.add(stringRequest);

//                if(name.length()>0 && mobile.length()>0 && email.length()>0){
//                    requestQueue.add(stringRequest);
//                }
//                else{
//                    edName.setError("Intur your name");
//                    edNumber.setError("Input your Number");
//                    edEmail.setError("Input your Email");
//                }

            }
        });
    }

//    -------------------
    public class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
        return arrayList.size();
    }

        @Override
        public Object getItem(int position) {
        return null;
    }

        @Override
        public long getItemId(int position) {
        return 0;
    }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View myView = layoutInflater.inflate(R.layout.item, null);

            TextView tvId = myView.findViewById(R.id.tvId);
            TextView tvName = myView.findViewById(R.id.tvName);
            TextView tvMobile = myView.findViewById(R.id.tvMobile);
            TextView tvEmail = myView.findViewById(R.id.tvEmail);
            Button buttonUpdate = myView.findViewById(R.id.buttonUpdate);
            Button buttonDelete = myView.findViewById(R.id.buttonDelete);

            hashMap = arrayList.get(position);
            String id = hashMap.get("id");
            String name = hashMap.get("name");
            String mobile = hashMap.get("mobile");
            String email = hashMap.get("email");

            tvId.setText(id);
            tvName.setText(name);
            tvMobile.setText(mobile);
            tvEmail.setText(email);

//            buttonUpdate.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });

            return myView;
        }
    }

    //-----------------------------------
    private void loadData(){

        arrayList = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        progressBar.setVisibility(View.VISIBLE);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://localhost/Apps/view.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                progressBar.setVisibility(View.GONE);

                for(int x=0; x<response.length(); x++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(x);

                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String mobile = jsonObject.getString("mobile");
                        String email = jsonObject.getString("email");

                        hashMap = new HashMap<>();
                        hashMap.put("id", id);
                        hashMap.put("name", name);
                        hashMap.put("mobile", mobile);
                        hashMap.put("email", email);

                        arrayList.add(hashMap);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                if(arrayList.size()>0){
                    MyAdapter myAdapter = new MyAdapter();
                    listView.setAdapter(myAdapter);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ServerRes", error.toString());

            }
        });

        requestQueue.add(jsonArrayRequest);

    }

}

