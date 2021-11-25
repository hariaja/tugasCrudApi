package com.aldiama.apilearning.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aldiama.apilearning.Adapter.AdapterData;
import com.aldiama.apilearning.Api.ApiRequestData;
import com.aldiama.apilearning.Api.RetrofitServer;
import com.aldiama.apilearning.Model.DataModel;
import com.aldiama.apilearning.Model.ResponseModel;
import com.aldiama.apilearning.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

  private RecyclerView rvData;
  private RecyclerView.Adapter adData;
  private RecyclerView.LayoutManager lmData;
  private List<DataModel> listMahasiswa = new ArrayList<>();
  private SwipeRefreshLayout srlData;
  private ProgressBar pbData;
  private FloatingActionButton fabTambah;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    fabTambah = findViewById(R.id.fab_tambah);
    srlData = findViewById(R.id.swl_data);
    pbData = findViewById(R.id.pb_data);
    rvData = findViewById(R.id.rv_data);
    lmData = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
    rvData.setLayoutManager(lmData);
    srlData.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        srlData.setRefreshing(true);
        retriveData();
        srlData.setRefreshing(false);
      }
    });

    fabTambah.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(MainActivity.this, TambahActivity.class));
      }
    });

  }

  @Override
  protected void onResume() {
    super.onResume();
    retriveData();
  }

  public void retriveData(){
    pbData.setVisibility(View.VISIBLE);
    ApiRequestData ardData = RetrofitServer
        .konekRetrofit().create(ApiRequestData.class);
    Call<ResponseModel> tampilData = ardData.ardRetriveData();
    tampilData.enqueue(new Callback<ResponseModel>() {
      @Override
      public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
        int kode = response.body().getKode();
        String message = response.body().getMessage();

//        Toast.makeText(MainActivity.this, "Kode : "+kode+" Pesan : "+message, Toast.LENGTH_SHORT).show();
        listMahasiswa = response.body().getData();
        adData = new AdapterData(MainActivity.this, listMahasiswa);
        rvData.setAdapter(adData);
        adData.notifyDataSetChanged();
        pbData.setVisibility(View.INVISIBLE);
      }

      @Override
      public void onFailure(Call<ResponseModel> call, Throwable t) {
        Toast.makeText(MainActivity.this, "Gagal Mengubungi Server : "+t.getMessage(), Toast.LENGTH_SHORT).show();
        pbData.setVisibility(View.INVISIBLE);
      }
    });
  }

}