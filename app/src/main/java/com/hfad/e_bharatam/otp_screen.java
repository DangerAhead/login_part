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

public class otp_screen extends AppCompatActivity {


    EditText otp;
    Button verify;
    String phone_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screen);

        Intent intent = getIntent();
        phone_num = intent.getStringExtra(confirm.EXTRA_TEXT);


        otp = findViewById(R.id.phone);
        verify = findViewById(R.id.verify);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify_otp();
            }
        });


    }


    public void verify_otp()
    {
        String num = otp.getText().toString().trim();
        if(num.length()!=4)
        {
            Toast.makeText(this,"OTP must be of 4 digits",Toast.LENGTH_LONG).show();
            return;
        }

        else
        {

            JSONObject jsonObject = new JSONObject();
            String url = "http://648959ac1e85.ngrok.io/users/loginotp/verify";
            //Toast.makeText(this,"yo",Toast.LENGTH_LONG).show();
            try {
                jsonObject.put("mobileNo", phone_num);
                jsonObject.put("otp", num);

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
                        otp_screen.this.runOnUiThread(new Runnable() {
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
                       otp_screen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                wrong();
                            }
                        });
                    }

                    else
                    {
                        otp_screen.this.runOnUiThread(new Runnable() {
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
        Toast.makeText(this,"OTP verified successfully",Toast.LENGTH_LONG).show();
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
