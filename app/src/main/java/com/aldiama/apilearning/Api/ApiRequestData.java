package com.aldiama.apilearning.Api;

import com.aldiama.apilearning.Model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiRequestData {

  @GET("retrive_data.php")
  Call<ResponseModel> ardRetriveData();

  @FormUrlEncoded
  @POST("create_data.php")
  Call<ResponseModel> ardCreateData(
      @Field("nim") String nim,
      @Field("kode_jurusan") String kode_jurusan,
      @Field("nama_lengkap") String nama_lengkap,
      @Field("gender") String gender,
      @Field("tempat_lahir") String tempat_lahir,
      @Field("tanggal_lahir") String tanggal_lahir,
      @Field("nomor_hp") String nomor_hp,
      @Field("alamat") String alamat
  );

  @FormUrlEncoded
  @POST("delete_data.php")
  Call<ResponseModel> deleteData(@Field("id") int id);

  @FormUrlEncoded
  @POST("detail_data.php")
  Call<ResponseModel> ardDetailData(@Field("id") int id);

  @FormUrlEncoded
  @POST("update_data.php")
  Call<ResponseModel> ardUpdateData(
      @Field("id") int id,
      @Field("nim") String nim,
      @Field("kode_jurusan") String kode_jurusan,
      @Field("nama_lengkap") String nama_lengkap,
      @Field("gender") String gender,
      @Field("tempat_lahir") String tempat_lahir,
      @Field("tanggal_lahir") String tanggal_lahir,
      @Field("nomor_hp") String nomor_hp,
      @Field("alamat") String alamat
  );
}
