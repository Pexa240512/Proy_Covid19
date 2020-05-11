package com.example.covid19.Interface;

import com.example.covid19.Model.TipoDocumentos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonTipoDocumentoApi {

    @GET("TiposDocumentoIdentidad")
    Call<List<TipoDocumentos>> getTipoDocuemtos();
}
