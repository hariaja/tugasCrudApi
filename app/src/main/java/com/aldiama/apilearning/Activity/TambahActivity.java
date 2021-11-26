package com.aldiama.apilearning.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.aldiama.apilearning.Api.ApiRequestData;
import com.aldiama.apilearning.Api.RetrofitServer;
import com.aldiama.apilearning.Model.ResponseModel;
import com.aldiama.apilearning.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahActivity extends AppCompatActivity {

  private DatePickerDialog datePickerDialog;
  private SimpleDateFormat dateFormatter;
  private Spinner spinJurusan;
  private RadioGroup rgGender;
  private Button btnAdd;
  private EditText etNim, etnama_lengkap, etTempatLahir, etTanggalLahir, etNomorHp, etAlamat;
  private String nim, nama_lengkap, tempat_lahir, tanggal_lahir, alamat, nomor_hp;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tambah);

    rgGender = findViewById(R.id.rg_gender);
    spinJurusan = findViewById(R.id.spin_jurusan);
    etTanggalLahir = findViewById(R.id.tanggal_lahir);
    btnAdd = findViewById(R.id.btn_add);
    etNim = findViewById(R.id.et_nim);
    etnama_lengkap = findViewById(R.id.et_nama);
    etTempatLahir = findViewById(R.id.et_tempat_lahir);
    etNomorHp = findViewById(R.id.et_nomor_hp);
    etAlamat = findViewById(R.id.et_alamat);
    btnAdd = findViewById(R.id.btn_add);

    dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.jurusan, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinJurusan.setAdapter(adapter);

    etTanggalLahir.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showDateDialog();
      }
    });

    btnAdd.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        nim = etNim.getText().toString();
        nama_lengkap = etnama_lengkap.getText().toString();
        alamat = etAlamat.getText().toString();
        tanggal_lahir = etTanggalLahir.getText().toString();
        tempat_lahir = etTempatLahir.getText().toString();
        nomor_hp = etNomorHp.getText().toString();

        if (nim.trim().equals("")) {
          etNim.setError("Tidak Boleh Kosong");
        } else if (nama_lengkap.trim().equals("")) {
          etnama_lengkap.setError("Tidak Boleh Kosong");
        } else if (alamat.trim().equals("")) {
          etAlamat.setError("Tidak Boleh Kosong");
        } else if (tanggal_lahir.trim().equals("")) {
          etTanggalLahir.setError("Tidak Boleh Kosong");
        } else if (tempat_lahir.trim().equals("")) {
          etTempatLahir.setError("Tidak Boleh Kosong");
        } else if (nomor_hp.trim().equals("")) {
          etNomorHp.setError("Tidak Boleh Kosong");
        } else {
          requestSimpanData();
        }
      }
    });
  }

  private void showDateDialog() {
    Calendar nCalendar = Calendar.getInstance();
    datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar nDate = Calendar.getInstance();
        nDate.set(year, month, dayOfMonth);
        etTanggalLahir.setText(dateFormatter.format(nDate.getTime()));
      }
    }, nCalendar.get(Calendar.YEAR),
        nCalendar.get(Calendar.MONTH),
        nCalendar.get(Calendar.DAY_OF_MONTH));
    datePickerDialog.show();
  }

  private void requestSimpanData() {

    int selectedId = rgGender.getCheckedRadioButtonId();
    RadioButton radioButton = findViewById(selectedId);
    String gender = radioButton.getText().toString();
    String jurusan = spinJurusan.getSelectedItem().toString();

    Integer kode_jurusan = 0;

    if (jurusan.equalsIgnoreCase("Teknik Komputer")) {
      kode_jurusan = 1;
    } else if (jurusan.equalsIgnoreCase("Teknik Sipil")) {
      kode_jurusan = 2;
    } else if (jurusan.equalsIgnoreCase("Teknik Mesin")) {
      kode_jurusan = 3;
    } else {
      jurusan.equalsIgnoreCase("Administrasi Bisnis");
      kode_jurusan = 4;
    }

    ApiRequestData ardData = RetrofitServer.konekRetrofit().create(ApiRequestData.class);
    Call<ResponseModel> simpanData = ardData.ardCreateData(
        nim,
        String.valueOf(kode_jurusan),
        nama_lengkap,
        gender,
        tempat_lahir, 
        tanggal_lahir,
        nomor_hp,
        alamat);

    simpanData.enqueue(new Callback<ResponseModel>() {
      @Override
      public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
        int kode = response.body().getKode();
        String message = response.body().getMessage();
        Toast.makeText(TambahActivity.this, message, Toast.LENGTH_SHORT).show();
        finish();
      }
      @Override
      public void onFailure(Call<ResponseModel> call, Throwable t) {
        Toast.makeText(TambahActivity.this, "Error : "+t.toString(), Toast.LENGTH_SHORT).show();
      }
    });
  }
}