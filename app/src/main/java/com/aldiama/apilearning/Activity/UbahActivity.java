package com.aldiama.apilearning.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class UbahActivity extends AppCompatActivity {

  private int xId, xKodeJurusan;
  private String xNim, xNamaLengkap, xTempatLahir, xTanggalLahir, xNomorHp, xAlamat;

  private EditText etNim, etnama_lengkap, etTempatLahir, etTanggalLahir, etNomorHp, etAlamat;
  private DatePickerDialog datePickerDialog;
  private SimpleDateFormat dateFormatter;
  private Spinner spinJurusan;
  private RadioGroup rgGender;
  private Button btnUbah;

  private String yNim, yNama, yTempat, yTanggal, yNomor, yAlamat;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ubah);

    Intent intent = getIntent();
    xId = intent.getIntExtra("xId", -1);
    xNim = intent.getStringExtra("xNim");
    xKodeJurusan = intent.getIntExtra("xKodeJurusan", -1);
    xNamaLengkap = intent.getStringExtra("xNamaLengkap");
    xTempatLahir = intent.getStringExtra("xTempatLahir");
    xTanggalLahir = intent.getStringExtra("xTanggalLahir");
    xNomorHp = intent.getStringExtra("xNomorHp");
    xAlamat = intent.getStringExtra("xalamat");

    rgGender = findViewById(R.id.rg_gender);
    spinJurusan = findViewById(R.id.spin_jurusan);
    etNim = findViewById(R.id.et_nim);
    etnama_lengkap = findViewById(R.id.et_nama);
    etTanggalLahir = findViewById(R.id.tanggal_lahir);
    etTempatLahir = findViewById(R.id.et_tempat_lahir);
    etNomorHp = findViewById(R.id.et_nomor_hp);
    etAlamat = findViewById(R.id.et_alamat);
    btnUbah = findViewById(R.id.btn_ubah);

    etNim.setText(xNim);
    etnama_lengkap.setText(xNamaLengkap);
    etTempatLahir.setText(xTempatLahir);
    etTanggalLahir.setText(xTanggalLahir);
    etNomorHp.setText(xNomorHp);
    etAlamat.setText(xAlamat);

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

    btnUbah.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        yNim = etNim.getText().toString();
        yNama = etnama_lengkap.getText().toString();
        yAlamat = etAlamat.getText().toString();
        yTanggal = etTanggalLahir.getText().toString();
        yTempat = etTempatLahir.getText().toString();
        yNomor = etNomorHp.getText().toString();
        updateData();
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


  private void updateData() {

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
    Call<ResponseModel> ubahData = ardData.ardUpdateData(
        xId,
        yNim,
        String.valueOf(kode_jurusan),
        yNama,
        gender,
        yTempat,
        yTanggal,
        yNomor,
        yAlamat);

    ubahData.enqueue(new Callback<ResponseModel>() {
      @Override
      public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
        int kode = response.body().getKode();
        String message = response.body().getMessage();
        Toast.makeText(UbahActivity.this, "Kode : "+kode+" | Pesan : "+message, Toast.LENGTH_SHORT).show();
        finish();
      }
      @Override
      public void onFailure(Call<ResponseModel> call, Throwable t) {
        Toast.makeText(UbahActivity.this, "Error : "+t.toString(), Toast.LENGTH_SHORT).show();
      }
    });
  }
}