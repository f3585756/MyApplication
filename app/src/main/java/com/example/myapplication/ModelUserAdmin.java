package com.example.myapplication;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.List;

public class ModelUserAdmin implements Parcelable {
    private int id;
    private String email;
    private String nama;
    private String nohp;
    private String alamat;
    private String noktp;
    private String roleuser;

    protected ModelUserAdmin(Parcel in) {
        id = in.readInt();
        email = in.readString();
        nama = in.readString();
        nohp = in.readString();
        alamat = in.readString();
        noktp = in.readString();
        roleuser = in.readString();
    }

    public static final Creator<ModelUserAdmin> CREATOR = new Creator<ModelUserAdmin>() {
        @Override
        public ModelUserAdmin createFromParcel(Parcel in) {
            return new ModelUserAdmin(in);
        }

        @Override
        public ModelUserAdmin[] newArray(int size) {
            return new ModelUserAdmin[size];
        }
    };

    public ModelUserAdmin(JSONObject dataUser) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoktp() {
        return noktp;
    }

    public void setNoktp(String noktp) {
        this.noktp = noktp;
    }

    public String getRoleuser() {
        return roleuser;
    }

    public void setRoleuser(String roleuser) {
        this.roleuser = roleuser;
    }

    public static Creator<ModelUserAdmin> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(email);
        parcel.writeString(nama);
        parcel.writeString(nohp);
        parcel.writeString(alamat);
        parcel.writeString(noktp);
        parcel.writeString(roleuser);
    }
}
