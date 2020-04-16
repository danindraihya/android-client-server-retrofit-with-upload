package com.example.appmahasiswa.Retrofit;

import com.example.appmahasiswa.Model.Mahasiswa;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("mahasiswa")
    Call<List<Mahasiswa>> getMahasiswas();

    @GET("mahasiswa/{id}")
    Call<Mahasiswa> getMahasiswa(@Path("id") int id);

    @Multipart
    @POST("mahasiswa")
    Call<Mahasiswa> formMahasiswa(
            @Part("nama") RequestBody nama,
            @Part("alamat") RequestBody alamat,
            @Part MultipartBody.Part foto
    );

    @Multipart
    @PUT("mahasiswa/{id}")
    Call<Mahasiswa> editMahasiswa(
            @Path("id") int id,
            @Part("nama") RequestBody nama,
            @Part("alamat") RequestBody alamat,
            @Part MultipartBody.Part foto
    );

    @FormUrlEncoded
    @PUT("mahasiswa/{id}")
    Call<Mahasiswa> updateMahasiswa(
            @Path("id") int id,
            @Field("nama") String nama,
            @Field("alamat") String alamat
    );

    @DELETE("mahasiswa/{id}")
    Call<Void> deleteMahasiswa(@Path("id") int id);
}
