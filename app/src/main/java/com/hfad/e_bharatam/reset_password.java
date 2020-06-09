package com.hfad.e_bharatam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class reset_password extends AppCompatActivity {

    EditText otp;
    EditText new_pass;
    Button done;
    String phone_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Intent intent = getIntent();
        phone_num = intent.getStringExtra(enter_number.EXTRA_TEXT);

        otp = findViewById(R.id.phone);
        new_pass = findViewById(R.id.new_pass);
        done = findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });






    }


    public void reset()
    {
        String num = otp.getText().toString().trim();
        String pass = new_pass.getText().toString().trim();

        if(num.length()!=4)
        {
            Toast.makeText(this,"OTP must be of 4 digits",Toast.LENGTH_LONG).show();
        }



        else
        {

            JSONObject jsonObject = new JSONObject();
            String url = "http://4c3b5f005da5.ngrok.io/users/forgotpw";
            //Toast.makeText(this,"yo",Toast.LENGTH_LONG).show();
            try {
                jsonObject.put("mobileNo", phone_num);
                jsonObject.put("otp", num);
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
                        reset_password.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                verified();
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
                        reset_password.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                wrong();
                            }
                        });
                    }

                    else
                    {
                        reset_password.this.runOnUiThread(new Runnable() {
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


    public void verified()
    {
        Toast.makeText(this,"password reset successfully",Toast.LENGTH_LONG).show();
    }

    public void wrong()
    {
        Toast.makeText(this,"OTP entered is wrong",Toast.LENGTH_LONG).show();
    }

    public void error()
    {
        Toast.makeText(this,"server error!",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,confirm.class);
        startActivity(intent);
    }







}
