package com.example.appmahasiswa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.appmahasiswa.Model.Mahasiswa;
import com.example.appmahasiswa.Retrofit.ApiClient;
import com.example.appmahasiswa.Retrofit.ApiInterface;
import com.example.appmahasiswa.View.AdapterMahasiswa;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Button btnInsert;
    ApiInterface mApiInterface;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnInsert = (Button) findViewById(R.id.btnInsert);

        mRecyclerView = findViewById(R.id.rvMahasiswa);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        getMahasiswas();
    }

    public void getMahasiswas() {
       Call<List<Mahasiswa>> getMahasiswa = mApiInterface.getMahasiswas();
       getMahasiswa.enqueue(new Callback<List<Mahasiswa>>() {
           @Override
           public void onResponse(Call<List<Mahasiswa>> call, Response<List<Mahasiswa>> response) {
               List<Mahasiswa> mahasiswas = response.body();
               mAdapter = new AdapterMahasiswa(mahasiswas);
               mRecyclerView.setAdapter(mAdapter);

           }

           @Override
           public void onFailure(Call<List<Mahasiswa>> call, Throwable t) {
               Log.e("Retrofit Get", t.toString());
           }
       });
    }

}
