package com.hfad.e_bharatam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    EditText phone;
    EditText password;
    Button log_in;
    Button log_in_otp;
    Button forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        log_in = findViewById(R.id.log__in);
        log_in_otp = findViewById(R.id.log_in_otp);
        forgot_password  =findViewById(R.id.forgot_password);

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });

        log_in_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_confirm();
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_reset();
            }
        });



    }

    public void check()
    {
        String num = phone.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if(num.length()==0)
        {
            Toast.makeText(this,"Phone No. field is empty",Toast.LENGTH_LONG).show();
            return;
        }

        if(pass.length()==0)
        {
            Toast.makeText(this,"Password field is empty",Toast.LENGTH_LONG).show();
            return;
        }

        int flag=0;
        for(int i=0;i<num.length();i++)
        {
            if(!(num.charAt(i)<='9' && num.charAt(i)>='0'))
            {
                flag=1;
            }
        }
        if(flag==1)
        {
            Toast.makeText(this,"Enter a valid Phone No.!",Toast.LENGTH_LONG).show();
            return;
        }

        else
        {

            JSONObject jsonObject = new JSONObject();
            String url = "http://bd21ab2296c2.ngrok.io/users/loginpw";
            //Toast.makeText(this,"yo",Toast.LENGTH_LONG).show();
            try {
                jsonObject.put("mobileNo", num);
                jsonObject.put("password", pass);

            } catch (JSONException e) {
                Toast.makeText(this,"1st",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


            final OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            // put your json here
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            final Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();


            //Toast.makeText(this,"success",Toast.LENGTH_LONG).show();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                        int responseCode = response.code();
                        if(responseCode==200)
                        {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    success();
                                }
                            });

                            String token;
                            try {
                                JSONObject reader = new JSONObject(response.body().string());
                                token = reader.getString("token");

                            } catch (JSONException e) {

                            }

                        }

                        else if(responseCode==401)
                        {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    failed();
                                }
                            });
                        }

                        else
                        {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    error();
                                }
                            });
                        }

                }

            });

        }
    }


    public void failed()
    {
        Toast.makeText(this,"Authentication failed!",Toast.LENGTH_LONG).show();
    }

    public void success()
    {
        Toast.makeText(this,"Login successful!",Toast.LENGTH_LONG).show();
    }

    public void error()
    {
        Toast.makeText(this,"Server Error!",Toast.LENGTH_LONG).show();
    }

    public void open_confirm()
    {
        Intent intent = new Intent(this,confirm.class);
        startActivity(intent);
    }

    public void open_reset()
    {
        Intent intent = new Intent(this,enter_number.class);
        startActivity(intent);
    }




}
