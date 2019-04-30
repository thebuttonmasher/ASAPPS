package com.codestew.circles;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadPost extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button upPhoto;
    private Uri imageUri;
    private EditText titl;
    private Button actualUpload;
    private ImageView imgv;
    public Spinner spinner;
    public String cirOption;
    public  ArrayList<String> circlesName = new ArrayList<>();
    private final static int PICK_IMAGE = 100;
    private String createRef(String x)
    {
        String ref;
        ref = "gs://circles-23b26.appspot.com/" + x;
        return ref;
    }
    private void openGallery()
    {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE)
        {
            imageUri = data.getData();
            imgv.setImageURI(imageUri);

        }
    }

    //"/Circles/"+ cirName2 +"/Posts"
    private void upload()
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference();
        StorageReference imgRef = ref.child(imageUri.getLastPathSegment());
        UploadTask uploadTask = imgRef.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Map<String,Object> data = new HashMap<>();
                String img = createRef(imageUri.getLastPathSegment());
                data.put("Title", titl.getText().toString());
                data.put("img",img);
                FirebaseFirestore.getInstance().collection("/Circles/" + cirOption + "/Posts")
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("TAG", "MSG7 : DocumentSnapshot written with ID: " + documentReference.getId());
                                Intent intent = new Intent(UploadPost.this,MainFeed.class);
                                Bundle b = new Bundle();
                                b.putString("Circle","/Circles/" + cirOption + "/Posts");
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        });

            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);

        titl = (EditText) findViewById(R.id.EDIT_TITLE);
        spinner = (Spinner) findViewById(R.id.circleChoice);

        FirebaseFirestore.getInstance()
                .collection("/Circles")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();

                            for( int i = 0; i < myListOfDocuments.size(); i++){

                                circlesName.add(myListOfDocuments.get(i).getString("Name"));
                                Log.d("TAG","MSG " + Integer.toString(myListOfDocuments.size()));




                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                    UploadPost.this,android.R.layout.simple_spinner_dropdown_item,circlesName);
                            spinner.setAdapter(arrayAdapter);

                        }
                    }
                });



        upPhoto = (Button) findViewById(R.id.UP_PHOTO);
        imgv = (ImageView) findViewById(R.id.imageView2);
        actualUpload = (Button) findViewById(R.id.UPLOAD_POST);
        actualUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        upPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        cirOption = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
