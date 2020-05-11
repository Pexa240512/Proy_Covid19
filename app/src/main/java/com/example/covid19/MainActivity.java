package com.example.covid19;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.covid19.Interface.JsonTipoDocumentoApi;
import com.example.covid19.Model.TipoDocumentos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerTipDoc;
    private Spinner spinnerNac;
    private EditText NumCel;
    private EditText NumDoc;
    //String [] TipDocOpc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerTipDoc= (Spinner)findViewById(R.id.spTipDoc);
        spinnerNac= (Spinner)findViewById(R.id.spNac);
        NumCel= (EditText)findViewById(R.id.txtCel);
        NumDoc= (EditText)findViewById(R.id.txtNumDoc);

        //GetTipDocRetrofit();
        String [] TipDocOpc = getTipDocument();
        String [] NacOpc = getNacionalidad();

        //String [] NacOpc = {"DNI","CE"};

        ArrayAdapter <String> adapterTipDoc = new ArrayAdapter<String>( this, android.R.layout.simple_dropdown_item_1line,TipDocOpc);
        spinnerTipDoc.setAdapter(adapterTipDoc);

        ArrayAdapter <String> adapterNac = new ArrayAdapter<String>( this, android.R.layout.simple_dropdown_item_1line,NacOpc);
        spinnerNac.setAdapter(adapterNac);

        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor objeditor = preferences.edit();

        if (preferences.getString("NumCel","") != "") {
            Intent ps = new Intent(this, AccesosActivity.class);
            startActivity(ps);
        }

        //NumCel.setText(preferences.getString("Cel",""));
        //NumDoc.setText(preferences.getString("Doc",""));

    }

    //Siguiente
    public void Siguiente(View view){

        String NumCel1  = NumCel.getText().toString();
        String NumDoc1  = NumDoc.getText().toString();

        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor objeditor = preferences.edit();
        objeditor.putString("NumCel", NumCel1);
        objeditor.putString("NumDoc", NumDoc1);
        objeditor.commit();

        Intent ps = new Intent(this, NextActivity.class);
        startActivity(ps);
    }

    public String [] getTipDocument(){

        String [] sReturn = new String [0];

        String sql = "http://edinsonaldaz-001-site1.htempurl.com/api/TiposDocumentoIdentidad";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URL url = null;
        HttpURLConnection conn;
        try {
            url= new URL(sql);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            String json = "";
            while ((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            json = response.toString();
            JSONArray jsonArr = null;
            jsonArr = new JSONArray(json);

            sReturn= new String [jsonArr.length()];

            for (int i = 0; i<jsonArr.length();i++){
                JSONObject jsonObject = jsonArr.getJSONObject(i);
                String dat = jsonObject.optString("descripcionCorta");
                sReturn[i]=dat;
                //Log.d("Sal",dat);
                //Toast.makeText(this,dat,Toast.LENGTH_SHORT).show();
            }

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return sReturn;
    }
    public String [] getNacionalidad(){

        String [] sReturn = new String [0];

        String sql = "http://edinsonaldaz-001-site1.htempurl.com/api/Paises";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URL url = null;
        HttpURLConnection conn;
        try {
            url= new URL(sql);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            String json = "";
            while ((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            json = response.toString();
            JSONArray jsonArr = null;
            jsonArr = new JSONArray(json);

            sReturn= new String [jsonArr.length()];

            for (int i = 0; i<jsonArr.length();i++){
                JSONObject jsonObject = jsonArr.getJSONObject(i);
                String dat = jsonObject.optString("nombre");
                sReturn[i]=dat;
                //Log.d("Sal",dat);
                //Toast.makeText(this,dat,Toast.LENGTH_SHORT).show();
            }

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return sReturn;
    }

    public void  GetTipDocRetrofit(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://edinsonaldaz-001-site1.htempurl.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonTipoDocumentoApi jsonApi = retrofit.create(JsonTipoDocumentoApi.class);

        retrofit2.Call<List<TipoDocumentos>> call = jsonApi.getTipoDocuemtos();

        call.enqueue(new Callback<List<TipoDocumentos>>() {
            @Override
            public void onResponse(retrofit2.Call<List<TipoDocumentos>> call, Response<List<TipoDocumentos>> response) {
                if (!response.isSuccessful()){
                    //response.code();
                    return;
                }
                List<TipoDocumentos> objTD = response.body();
                String [] sReturn= new String [objTD.size()];
                int count = 0;

                for (TipoDocumentos td: objTD){
                    String dat = td.getDescripcionCorta();
                    sReturn[count]=dat;
                    count++;
                }
                //TipDocOpc=sReturn;
            }

            @Override
            public void onFailure(retrofit2.Call<List<TipoDocumentos>> call, Throwable t) {
                //t.getMessage();
            }
        });

    }

}
