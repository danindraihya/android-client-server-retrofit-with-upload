package com.example.appmahasiswa;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appmahasiswa.Model.Mahasiswa;
import com.example.appmahasiswa.Retrofit.ApiClient;
import com.example.appmahasiswa.Retrofit.ApiInterface;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;
    EditText editNama, editAlamat;
    Button btnUpdate, btnDelete, btnChangeImage;
    ApiInterface mApiInterface;
    Integer id;
    String nama, alamat, foto;
    ImageView ivDetailFoto, ivChangeFoto;
    Bundle bundle;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        editNama = (EditText) findViewById(R.id.editNama);
        editAlamat = (EditText) findViewById(R.id.editAlamat);
        btnUpdate = (Button) findViewById(R.id.Update);
        btnDelete = (Button) findViewById(R.id.Delete);
        ivDetailFoto = findViewById(R.id.ivEditFoto);
        btnChangeImage = findViewById(R.id.btnChangeFoto);
        ivChangeFoto = findViewById(R.id.ivChangeFoto);

        btnChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        nama = bundle.getString("nama");
        alamat = bundle.getString("alamat");
        foto = bundle.getString("foto");

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        getMahasiswa();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nama = editNama.getText().toString();
                alamat = editAlamat.getText().toString();
                foto = bundle.getString("foto");
                updateMahasiswa();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMahasiswa();
            }
        });

    }

    private void getMahasiswa() {
        Call<Mahasiswa> mahasiswa = mApiInterface.getMahasiswa(id);
        mahasiswa.enqueue(new Callback<Mahasiswa>() {
            @Override
            public void onResponse(Call<Mahasiswa> call, Response<Mahasiswa> response) {

                Mahasiswa mahasiswa = response.body();

                editNama.setText(mahasiswa.getNama());
                editAlamat.setText(mahasiswa.getAlamat());
                Picasso.get().load(mahasiswa.getFoto()).into(ivDetailFoto);
//                editNama.setText(mahasiswa.getNama());
//                editAlamat.setText(mahasiswa.getAlamat());
            }

            @Override
            public void onFailure(Call<Mahasiswa> call, Throwable t) {

            }
        });
    }

    private void updateMahasiswa() {

        if (uri == null) {

            Call<Mahasiswa> mahasiswa = mApiInterface.updateMahasiswa(id, nama, alamat);
            mahasiswa.enqueue(new Callback<Mahasiswa>() {
                @Override
                public void onResponse(Call<Mahasiswa> call, Response<Mahasiswa> response) {
                    Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<Mahasiswa> call, Throwable t) {

                }
            });

        } else {

            File file = FileUtils.getFile(this, uri);
            System.out.println(uri);
            RequestBody nama = RequestBody.create(MediaType.parse("text/plain"), editNama.getText().toString());
            RequestBody alamat = RequestBody.create(MediaType.parse("text/plain"), editAlamat.getText().toString());
            RequestBody photoBody = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), file);
            MultipartBody.Part foto = MultipartBody.Part.createFormData("foto", file.getName(), photoBody);

            Call<Mahasiswa> mahasiswa = mApiInterface.editMahasiswa(id, nama, alamat, foto);
            mahasiswa.enqueue(new Callback<Mahasiswa>() {
                @Override
                public void onResponse(Call<Mahasiswa> call, Response<Mahasiswa> response) {
                    Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<Mahasiswa> call, Throwable t) {

                }
            });
        }
    }

    private void deleteMahasiswa() {
        Call<Void> mahasiswa = mApiInterface.deleteMahasiswa(id);
        mahasiswa.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    // UPDATE FOTO

    public void chooseImage() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_STORAGE);
        } else {
            openGallery();
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK ) {
            if(data != null) {
                //data.getData return the content URI for the selected Image
                uri = data.getData();
                System.out.println(uri);

                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    ivChangeFoto.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
