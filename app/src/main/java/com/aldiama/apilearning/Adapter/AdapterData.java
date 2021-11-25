package com.aldiama.apilearning.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.aldiama.apilearning.Activity.MainActivity;
import com.aldiama.apilearning.Activity.UbahActivity;
import com.aldiama.apilearning.Api.ApiRequestData;
import com.aldiama.apilearning.Api.RetrofitServer;
import com.aldiama.apilearning.Model.DataModel;
import com.aldiama.apilearning.Model.ResponseModel;
import com.aldiama.apilearning.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData>{

  private Context mContext;
  private List<DataModel> listMahasiswa;
  private List<DataModel> detailDataMahasiswa;
  private int idMahasiswa;

  public AdapterData(Context mContext, List<DataModel> listMahasiswa) {
    this.mContext = mContext;
    this.listMahasiswa = listMahasiswa;
  }

  @NonNull
  @Override
  public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
    HolderData holder = new HolderData(layout);
    return holder;
  }

  @Override
  public void onBindViewHolder(@NonNull HolderData holder, int position) {
    DataModel semuaMahasiswa = listMahasiswa.get(position);
    holder.tvId.setText(String.valueOf(semuaMahasiswa.getId()));
    holder.tvNim.setText(semuaMahasiswa.getNim());
    holder.tvNama.setText(semuaMahasiswa.getNama_lengkap());
    holder.tvGender.setText(semuaMahasiswa.getGender());
    holder.tvTelepon.setText(semuaMahasiswa.getNomor_hp());
    holder.tvAlamat.setText(semuaMahasiswa.getAlamat());
    holder.tvNamaJurusan.setText(semuaMahasiswa.getNama_jurusan());
  }

  @Override
  public int getItemCount() {
    return listMahasiswa.size();
  }

  public class HolderData extends RecyclerView.ViewHolder {
    TextView tvId, tvNim, tvNama, tvGender, tvTelepon, tvAlamat, tvNamaJurusan;

    public HolderData(@NonNull View itemView) {
      super(itemView);

      tvId = itemView.findViewById(R.id.tv_id);
      tvNim = itemView.findViewById(R.id.tv_nim);
      tvNama = itemView.findViewById(R.id.tv_nama);
      tvGender = itemView.findViewById(R.id.tv_jk);
      tvTelepon = itemView.findViewById(R.id.tv_telepon);
      tvAlamat = itemView.findViewById(R.id.tv_alamat);
      tvNamaJurusan = itemView.findViewById(R.id.tv_nama_jurusan);

      itemView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          AlertDialog.Builder dialogPesan = new AlertDialog.Builder(mContext);
          dialogPesan.setMessage("Pilih Operasi Yang Akan Dilakukan");
          dialogPesan.setTitle("Perhatian");
          dialogPesan.setIcon(R.drawable.ic_info);
          dialogPesan.setCancelable(true);

          idMahasiswa = Integer.parseInt(tvId.getText().toString());

          dialogPesan.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              deleteData();
              dialog.dismiss();
              Handler handler = new Handler();
              handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                  ((MainActivity) mContext).retriveData();
                }
              }, 1000);
            }
          });

          dialogPesan.setNegativeButton("Ubah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              detailData();
              dialog.dismiss();
            }
          });
          dialogPesan.show();
          return false;
        }
      });
    }

    private void deleteData() {
      ApiRequestData ardData = RetrofitServer.konekRetrofit().create(ApiRequestData.class);
      Call<ResponseModel> hapusData = ardData.deleteData(idMahasiswa);
      hapusData.enqueue(new Callback<ResponseModel>() {
        @Override
        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
          int kode = response.body().getKode();
          String message = response.body().getMessage();
          Toast.makeText(mContext, "Kode : "+kode+" | Pesan :"+message, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onFailure(Call<ResponseModel> call, Throwable t) {
          Toast.makeText(mContext, "Gagal Menghubungi Server "+ t.getMessage(), Toast.LENGTH_SHORT).show();
        }
      });
    }

    private void detailData() {
      ApiRequestData ardData = RetrofitServer.konekRetrofit().create(ApiRequestData.class);
      Call<ResponseModel> detailData = ardData.ardDetailData(idMahasiswa);
      detailData.enqueue(new Callback<ResponseModel>() {
        @Override
        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
          int kode = response.body().getKode();
          String message = response.body().getMessage();
          detailDataMahasiswa = response.body().getData();

          int id = detailDataMahasiswa.get(0).getId();
          String nim = detailDataMahasiswa.get(0).getNim();
          int kode_jurusan = detailDataMahasiswa.get(0).getKode_jurusan();
          String nama_lengkap = detailDataMahasiswa.get(0).getNama_lengkap();
          String gender = detailDataMahasiswa.get(0).getGender();
          String tempat_lahir = detailDataMahasiswa.get(0).getTempat_lahir();
          String tanggal_lahir = detailDataMahasiswa.get(0).getTanggal_lahir();
          String nomor_hp = detailDataMahasiswa.get(0).getNomor_hp();
          String alamat = detailDataMahasiswa.get(0).getAlamat();

          Intent intent = new Intent(mContext, UbahActivity.class);
          intent.putExtra("xId", id);
          intent.putExtra("xNim", nim);
          intent.putExtra("xKodeJurusan", kode_jurusan);
          intent.putExtra("xNamaLengkap", nama_lengkap);
          intent.putExtra("xGender", gender);
          intent.putExtra("xTempatLahir", tempat_lahir);
          intent.putExtra("xTanggalLahir", tanggal_lahir);
          intent.putExtra("xNomorHp", nomor_hp);
          intent.putExtra("xalamat", alamat);
          mContext.startActivity(intent);

        }
        @Override
        public void onFailure(Call<ResponseModel> call, Throwable t) {
          Toast.makeText(mContext, "Gagal Menghubungi Server "+ t.getMessage(), Toast.LENGTH_SHORT).show();
        }
      });
    }

  }
}
