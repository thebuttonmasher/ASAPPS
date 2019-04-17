package com.codestew.circles;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeCircle extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button changeCir;
    private Button createCir;
    private EditText cirName;
    private String cirName2;
    private Spinner spinner;
    private ArrayList<String> circlesName;

    private void changeCircle()
    {

        Intent intent = new Intent(ChangeCircle.this,MainFeed.class);
        Bundle b = new Bundle();
        b.putString("Circle",cirName2);
        intent.putExtras(b);
        startActivity(intent);
    }
    private void createCircle()
    {
        Map<String,Object> data = new HashMap<>();
        data.put("Name",cirName.getText().toString());
        FirebaseFirestore.getInstance().collection("/Circles").document(cirName.getText().toString()).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(ChangeCircle.this,MainFeed.class);
                        Bundle b = new Bundle();
                        b.putString("Circle",cirName.getText().toString());
                        intent.putExtras(b);
                        startActivity(intent);

                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_circle);
        changeCir = (Button) findViewById(R.id.CHANGE_CIRCLE);
        createCir = (Button) findViewById(R.id.CREATE_CIRCLE);
        cirName = (EditText) findViewById(R.id.CIRCLE_NAME);
        spinner = (Spinner) findViewById(R.id.spin2win);
        circlesName = new ArrayList<>();
        createCir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCircle();
            }
        });
        changeCir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCircle();
            }
        });
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
                                    ChangeCircle.this,android.R.layout.simple_spinner_dropdown_item,circlesName);
                            spinner.setAdapter(arrayAdapter);

                        }
                    }
                });




    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        cirName2 = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
