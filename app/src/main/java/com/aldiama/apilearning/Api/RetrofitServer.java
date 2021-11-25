package com.aldiama.apilearning.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServer {

  private static final String baseURL = "http://192.168.1.3/api-mahasiswa/";
  private static Retrofit retro;
  public static Retrofit konekRetrofit() {
    if (retro == null) {
      retro = new Retrofit.Builder()
          .baseUrl(baseURL)
          .addConverterFactory(GsonConverterFactory.create())
          .build();
    }
    return retro;
  }

}
