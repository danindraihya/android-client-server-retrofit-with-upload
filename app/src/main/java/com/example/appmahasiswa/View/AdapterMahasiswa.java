package com.example.appmahasiswa.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmahasiswa.DetailActivity;
import com.example.appmahasiswa.Model.Mahasiswa;
import com.example.appmahasiswa.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AdapterMahasiswa extends RecyclerView.Adapter<AdapterMahasiswa.MahasiswaViewHolder> {
    private Context context;
    private List<Mahasiswa> mListMahasiswa;
    Mahasiswa currentItem;

    public static class MahasiswaViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView;

        public MahasiswaViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.ivMahasiswa);
            mTextView = itemView.findViewById(R.id.tvNamaMahasiswa);
        }

    }

    public AdapterMahasiswa(List<Mahasiswa> listMahasiswa) {
        mListMahasiswa = listMahasiswa;
    }

    @NonNull
    @Override
    public MahasiswaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mahasiswa_view, parent, false);
        MahasiswaViewHolder mvh = new MahasiswaViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MahasiswaViewHolder holder, int position) {
        currentItem = mListMahasiswa.get(position);

        Picasso.get().load(currentItem.getFoto()).into(holder.mImageView);
        holder.mTextView.setText(currentItem.getNama());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                System.out.println("ASSSSSSSSSSSSSSSSSSSSSSSS");
                bundle.putInt("id", mListMahasiswa.get(position).getPk());
                bundle.putString("nama", mListMahasiswa.get(position).getNama());
                bundle.putString("alamat", mListMahasiswa.get(position).getAlamat());
                bundle.putString("foto", mListMahasiswa.get(position).getFoto());
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListMahasiswa.size();
    }
}
