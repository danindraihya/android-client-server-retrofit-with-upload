package com.example.appmahasiswa;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.appmahasiswa.Model.Mahasiswa;
import com.example.appmahasiswa.Retrofit.ApiClient;
import com.example.appmahasiswa.Retrofit.ApiInterface;

import java.io.File;
import java.io.FileNotFoundException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;
    Button btnChooseImage;
    Button btnCreateMahasiswa;
    EditText dataNama, dataAlamat;
    ApiInterface mApiInterface;
    String nama, alamat;
    ImageView ivFoto;
    String mediaPath;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        btnChooseImage = (Button) findViewById(R.id.btnChooseImage);

        dataNama = findViewById(R.id.dataNama);
        dataAlamat = findViewById(R.id.dataAlamat);
        ivFoto = findViewById(R.id.ivFoto);

        btnCreateMahasiswa = findViewById(R.id.btnCreateMahasiswa);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        btnCreateMahasiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nama = dataNama.getText().toString();
                alamat = dataAlamat.getText().toString();
                createMahasiswa();
            }
        });

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    openGallery();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

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

                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    ivFoto.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void createMahasiswa(){
        File file = FileUtils.getFile(this, uri);
        RequestBody nama = RequestBody.create(MediaType.parse("text/plain"), dataNama.getText().toString());
        RequestBody alamat = RequestBody.create(MediaType.parse("text/plain"), dataAlamat.getText().toString());
        RequestBody photoBody = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), file);
        MultipartBody.Part foto = MultipartBody.Part.createFormData("foto", file.getName(), photoBody);

        Call<Mahasiswa> mahasiswa = mApiInterface.formMahasiswa(nama, alamat, foto);
        mahasiswa.enqueue(new Callback<Mahasiswa>() {
            @Override
            public void onResponse(Call<Mahasiswa> call, Response<Mahasiswa> response) {
                Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Mahasiswa> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

}
