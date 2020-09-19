package com.example.myapplication;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.androidnetworking.interfaces.JSONObjectRequestListener;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;

import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private EditText logEmail,logPassword;
    private Button logBtnLog;
    private boolean isForm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_main);

        logEmail = findViewById(R.id.logEmail);
        logPassword = findViewById(R.id.logPassword);
        logBtnLog = findViewById(R.id.logBtnLog);

        logBtnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isForm = true;
                final String email = logEmail.getText().toString();
                final String password = logPassword.getText().toString();

                if (isForm) {
                HashMap<String, String> body = new HashMap<>();
                body.put("email", email);
                body.put("password", password);
                    AndroidNetworking.post(Config.BASE_URL + "login")
                            .addBodyParameter(body)
                            .setPriority(Priority.MEDIUM)
                            .setOkHttpClient(((RS) getApplication()).getOkHttpClient())
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    String message = response.optString(Config.RESPONSE_MESSAGE_FIELD);
                                    if (message.equalsIgnoreCase(Config.RESPONSE_STATUS_VALUE_SUCCESS)) {
                                        JSONObject payload = response.optJSONObject("data");
                                        String role = payload.optString("role_user");
                                        int userid = payload.optInt("id");
                                        Log.e("AF", "id: "+userid);
                                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                        intent.putExtra("userid",userid);
                                        startActivity(intent);
                                        if (role.equalsIgnoreCase("2"))
                                            intent = new Intent(MainActivity.this, ViewCustomerAdmin.class);
                                        startActivity(intent);
                                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                        finish();
                                        finishAffinity();
                                    }else {
                                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }

                                    }


                                @Override
                                public void onError(ANError anError) {
//                                    mProgress.dismiss();
                                    Toast.makeText(MainActivity.this, Config.TOAST_AN_EROR, Toast.LENGTH_SHORT).show();
                                    Log.d("HBB", "onError: " + anError.getErrorBody());
                                    Log.d("HBB", "onError: " + anError.getLocalizedMessage());
                                    Log.d("HBB", "onError: " + anError.getErrorDetail());
                                    Log.d("HBB", "onError: " + anError.getResponse());
                                    Log.d("HBB", "onError: " + anError.getErrorCode());
                                }
                            });
                }
            }
        });
    }

    public void register(View view){
        Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    public void home(View view){
        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
        startActivity(intent);
    }



}