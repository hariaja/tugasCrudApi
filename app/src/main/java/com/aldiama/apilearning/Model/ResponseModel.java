package com.aldiama.apilearning.Model;

import java.util.List;

public class ResponseModel {

  private int kode;
  private String message;
  private List<DataModel> data;

  public ResponseModel(int kode, String message, List<DataModel> data) {
    this.kode = kode;
    this.message = message;
    this.data = data;
  }

  public int getKode() {
    return kode;
  }

  public void setKode(int kode) {
    this.kode = kode;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<DataModel> getData() {
    return data;
  }

  public void setData(List<DataModel> data) {
    this.data = data;
  }
}
