package com.codestew.circles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.ViewHolder> {
    private final RequestManager glide;
    private List<ImagePost> imgPostList;
    private Context context;

    public ImgAdapter(List<ImagePost> imgPostList, Context context, RequestManager glide) {
        this.imgPostList = imgPostList;
        this.context = context;
        this.glide = glide;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ImagePost imgpost = imgPostList.get(i);

        viewHolder.title.setText(imgpost.getTitle());
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(imgpost.getImg());
        glide.load(ref).into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return imgPostList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.postTitle);
            image = (ImageView) itemView.findViewById(R.id.postImage);
        }
    }
}
