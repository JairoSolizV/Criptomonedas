package com.example.criptomonedas.api;

import com.example.criptomonedas.model.CriptoResponse;
import com.example.criptomonedas.model.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitService {
    @GET("api/criptomonedas.json")
    Call<CriptoResponse> getCriptomonedas();
}
