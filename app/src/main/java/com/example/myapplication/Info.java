package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class Info extends AppCompatActivity {
    private TextView txt_status;
    private Button on_btn;
    private Button off_btn;
    private Button status_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        //ON Button
        on_btn = findViewById(R.id.on);
        //OFF Button
        off_btn = findViewById(R.id.off);
        //Status Button
        status_btn = findViewById(R.id.status);
        //Status Text View
        txt_status = findViewById(R.id.txt_status_view);

        //INTENT
        Intent intent = getIntent();
        final String text = intent.getStringExtra(MainActivity.EXTRA_NAME);
        Log.d("MSG",text);
        TextView txt = (TextView)findViewById(R.id.device);
        txt.setText(text);
        //On BTN is clicked
        on_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txt_status.setText("");
                // set the field1 option as 1
                String url ;
                if (text.equals("Fan!"))
                {
                    url = "https://api.thingspeak.com/update?api_key=1JSF260WRN3GQTYG&field1=1";
                }else
                {
                  url =  "https://api.thingspeak.com/update?api_key=1JSF260WRN3GQTYG&field2=1";
                }
                set_field(url);

                btns_delay();

                make_toast(text+" is On!");

            }

        });

        //OFF BTN is clicked
        off_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_status.setText("");
                //set the field1 option as 0

                String url = "";
                if (text.equals("Fan!"))
                {
                    url = "https://api.thingspeak.com/update?api_key=1JSF260WRN3GQTYG&field1=0";
                }else
                {
                    url =  "https://api.thingspeak.com/update?api_key=1JSF260WRN3GQTYG&field2=0";
                }
                set_field(url);
                btns_delay();
                make_toast(text+" is Off!");
            }
        });
        //Status button is clicked
        status_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting the data from APi given
                txt_status.setText("");
                String url = "https://api.thingspeak.com/channels/1304816/feeds.json?api_key=6QEUU4P71VR12CPZ";
                get_field(url,text);

            }
        });

    }
    public void get_field(String url, final String help)
    {
        RequestQueue queue = Volley.newRequestQueue(Info.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray feeds  = obj.getJSONArray("feeds");
                            String field="";
                            for (int i = feeds.length()-1;i>=0;i--)
                            {
                                JSONObject obj2 = feeds.getJSONObject(i);
                                Log.d("Field1"+help,obj2.getString("field1"));
                                Log.d("Field2"+help,obj2.getString("field2"));
                                if(help.equals("Fan!"))
                                {

                                    field  = obj2.getString("field1");
                                    if(field.length()==1)
                                    {
                                        break;
                                    }

                                }else
                                {
                                    field  = obj2.getString("field2");
                                    if(field.length()==1)
                                        break;
                                }
                            }

                            Log.d("Field",field);
                            if (field.equals("1"))//Device is OFF!
                            {
                                txt_status.setText(help +" is ON!");

                            }else//Device is ON!
                            {
                                txt_status.setText(help + " is OFF!");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }}, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                make_toast("Error");
            }
        });
        queue.add(stringRequest);
    }
    public void set_field(String url)
    {
        RequestQueue queue = Volley.newRequestQueue(Info.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //We hit our Api with Get Method
                    }}, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                make_toast("Error");
            }
        });
        queue.add(stringRequest);
    }
    public void make_toast(String resp)
    {
        Toast.makeText(this,resp,Toast.LENGTH_LONG).show();
    }
    public void btns_delay()
    {
        on_btn.setEnabled(false);
        off_btn.setEnabled(false);
        status_btn.setEnabled(false);
        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        on_btn.setEnabled(true);
                        off_btn.setEnabled(true);
                        status_btn.setEnabled(true);
                    }
                });
            }
        }, 15000);

    }
}