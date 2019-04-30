package com.codestew.circles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        final ImagePost imgpost = imgPostList.get(i);

        viewHolder.title.setText(imgpost.getTitle());
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(imgpost.getImg());
        glide.load(ref).into(viewHolder.image);
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> data = new HashMap<>();
                data.put("Title",imgpost.getTitle());
                data.put("img", imgpost.getImg());
                FirebaseFirestore.getInstance().collection("/UserCircles/" + FirebaseAuth.getInstance().getCurrentUser().getEmail() +"/Posts")
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("MSG","SUCCESPROMPT");
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return imgPostList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView image;
        public Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.postTitle);
            image = (ImageView) itemView.findViewById(R.id.postImage);
            button = (Button) itemView.findViewById(R.id.postSaveButton);
        }
    }
}
