package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewCustomerAdmin extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rv;


    private ArrayList<ModelUserAdmin> mList = new ArrayList<>();
    private HTTPHandler mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer);
       Button btndelete = findViewById(R.id.hapusitem);

        rv = findViewById(R.id.rv_user);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                getUserList();
            }
        });
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

    }

    public void getUserList() {
        swipeRefresh.setRefreshing(true);
        AndroidNetworking.get(Config.BASE_URL + "getcustomer")
//                .addBodyParameter(body)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(((RS) getApplication()).getOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        swipeRefresh.setRefreshing(false);
                        if (mAdapter != null) {
                            mAdapter.clearData();
                            mAdapter.notifyDataSetChanged();
                        }
                        if (mList != null) mList.clear();
                        Log.d("RBA", "res" + response);

                        String status = response.optString(Config.RESPONSE_STATUS_FIELD);
                        String message = response.optString(Config.RESPONSE_MESSAGE_FIELD);
                        if (message.trim().equalsIgnoreCase(Config.RESPONSE_STATUS_VALUE_SUCCESS)) {
                            JSONArray payload = response.optJSONArray(Config.RESPONSE_PAYLOAD_FIELD);


                            if (payload == null) {
                                Toast.makeText(ViewCustomerAdmin.this, "Tidak ada user", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            for (int i = 0; i < payload.length(); i++) {
                                JSONObject dataUser = payload.optJSONObject(i);
                                ModelUserAdmin item = new ModelUserAdmin(dataUser);
                                item.setId(dataUser.optInt("id"));
                                item.setNama(dataUser.optString("nama"));
                                item.setAlamat(dataUser.optString("alamat"));
                                item.setNoktp(dataUser.optString("noktp"));
                                item.setEmail(dataUser.optString("email"));
                                item.setNohp(dataUser.optString("nohp"));
                                item.setRoleuser(dataUser.optString("role_user"));
                                mList.add(item);
                            }

                            mAdapter = new HTTPHandler(ViewCustomerAdmin.this, mList, ViewCustomerAdmin.this);
                            rv.setAdapter(mAdapter);
                        } else {
                            Toast.makeText(ViewCustomerAdmin.this, message, Toast.LENGTH_SHORT).show();
                            JSONObject payload = response.optJSONObject(Config.RESPONSE_PAYLOAD_FIELD);
                            if (payload != null && payload.optString("API_ACTION").equalsIgnoreCase("LOGOUT"))
                                Config.forceLogout(ViewCustomerAdmin.this);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        swipeRefresh.setRefreshing(false);
                        Toast.makeText(ViewCustomerAdmin.this, Config.TOAST_AN_EROR, Toast.LENGTH_SHORT).show();
                        Log.d("RBA", "onError: " + anError.getErrorBody());
                        Log.d("RBA", "onError: " + anError.getLocalizedMessage());
                        Log.d("RBA", "onError: " + anError.getErrorDetail());
                        Log.d("RBA", "onError: " + anError.getResponse());
                        Log.d("RBA", "onError: " + anError.getErrorCode());
                    }
                });


    }

    @Override
    public void onRefresh () {
        getUserList();
    }
    @Override

    public void onBackPressed() {
        finish();
        Intent intent = new Intent(ViewCustomerAdmin.this, MainActivity.class);
        startActivity(intent);
    }
}