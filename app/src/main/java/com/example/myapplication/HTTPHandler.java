package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.util.List;

public class HTTPHandler extends RecyclerView.Adapter<HTTPHandler.ItemViewHolder> {
    private Context context;
    private List<ModelUserAdmin> mList;
//    private String mLoginToken = "";
    private boolean mBusy = false;
    private ViewCustomerAdmin mAdminUserActivity;

    public HTTPHandler(Context context, List<ModelUserAdmin> mList, Activity AdminUserActivity) {
        this.context = context;
        this.mList = mList;
        this.mAdminUserActivity = (ViewCustomerAdmin) AdminUserActivity;
    }

    @NonNull
    @Override
    public HTTPHandler.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_grid_customer, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HTTPHandler.ItemViewHolder holder, int i) {
        final ModelUserAdmin Amodel = mList.get(i);
        holder.bind(Amodel);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void clearData() {
        int size = this.mList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.mList.remove(0);
            }
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_nama, tv_phone;
        private Button delete;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nama = itemView.findViewById(R.id.tv_nama);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            delete = itemView.findViewById(R.id.hapusitem);
        }

        private void bind(final ModelUserAdmin Amodel) {
            tv_nama.setText(Amodel.getNama());
            tv_phone.setText(Amodel.getNohp());

            delete.setOnClickListener(new View.OnClickListener() {
                private void doNothing() {

                }

                @Override
                public void onClick(View view) {
                    if (mBusy) {
                        Toast.makeText(context, "Harap tunggu proses sebelumnya selesai...", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("Hapus data produk ?");
                    alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        private void doNothing() {

                        }
                        public void onClick(DialogInterface arg0, int arg1) {
                            deleteData(String.valueOf(Amodel.getId()));
                        }
                    });
                    alertDialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        private void doNothing() {

                        }

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();
                        }
                    });

                    //Showing the alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });
        }

        private void deleteData(String id) {
            if (mBusy) {
                Toast.makeText(context, "Harap tunggu proses sebelumnya selesai...", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d("AF", "uId:" + id);

            AndroidNetworking.post(Config.BASE_URL + "deletecustomer")
                    .addBodyParameter("id", id)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject jsonResponse) {
                            mBusy = false;


                            String message = jsonResponse.optString(Config.RESPONSE_MESSAGE_FIELD);
                            String status = jsonResponse.optString(Config.RESPONSE_STATUS_FIELD);

                            if (status != null && status.equalsIgnoreCase(Config.RESPONSE_STATUS_VALUE_SUCCESS)) {
                                mAdminUserActivity.getUserList();
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            mBusy = false;


                            Toast.makeText(context, Config.TOAST_AN_EROR, Toast.LENGTH_SHORT).show();
                            Log.d("RBA", "onError: " + anError.getErrorBody());
                            Log.d("RBA", "onError: " + anError.getLocalizedMessage());
                            Log.d("RBA", "onError: " + anError.getErrorDetail());
                            Log.d("RBA", "onError: " + anError.getResponse());
                            Log.d("RBA", "onError: " + anError.getErrorCode());
                        }
                    });

        }
    }
}