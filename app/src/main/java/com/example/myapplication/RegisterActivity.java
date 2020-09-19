package com.example.myapplication;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.myapplication.RS;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.myapplication.Config;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText regEmail,regAlamat,regPassword,regNama,regHp,regKtp;
    Button btnRegister;
    private boolean mIsFormFilled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.btnRegister);
        regEmail = findViewById(R.id.regEmail);
        regAlamat = findViewById(R.id.regAlamat);
        regPassword = findViewById(R.id.regPassword);
        regNama = findViewById(R.id.regNama);
        regHp = findViewById(R.id.regHp);
        regKtp = findViewById(R.id.regKtp);

        btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View view) {
                final String email = regEmail.getText().toString();
                final String alamat = regAlamat.getText().toString();
                final String password = regPassword.getText().toString();
                final String nama = regNama.getText().toString();
                final String hp = regHp.getText().toString();
                final String ktp = regKtp.getText().toString();

                HashMap<String, String> body = new HashMap<>();
                body.put("email", email);
                body.put("password",password);
                body.put("nohp",hp);
                body.put("nama", nama);
                body.put("alamat", alamat);
                body.put("noktp", ktp);
                AndroidNetworking.post("http://192.168.6.233:8000/api/register")
                        .addBodyParameter(body)
                        .setPriority(Priority.MEDIUM)
                        .setOkHttpClient(((RS) getApplication()).getOkHttpClient())
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String message = response.optString(Config.RESPONSE_MESSAGE_FIELD);
                                if (message.equalsIgnoreCase(Config.RESPONSE_STATUS_VALUE_SUCCESS)) {
                                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);//CUSTOMER
                                    startActivity(intent);
                                    finish();
                                    finishAffinity();
                                }else {
                                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(RegisterActivity.this, Config.TOAST_AN_EROR, Toast.LENGTH_SHORT).show();
                                Log.d("HBB", "onError: " + anError.getErrorBody());
                                Log.d("HBB", "onError: " + anError.getLocalizedMessage());
                                Log.d("HBB", "onError: " + anError.getErrorDetail());
                                Log.d("HBB", "onError: " + anError.getResponse());
                                Log.d("HBB", "onError: " + anError.getErrorCode());
                            }
                        });
            }
        });
    }
    public void orLogin(View view){
        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(intent);
    }


}