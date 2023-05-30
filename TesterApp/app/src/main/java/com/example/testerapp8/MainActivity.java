package com.example.testerapp8;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addProjectBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;
    ProjectAdapter projectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addProjectBtn = findViewById(R.id.add_project_btn);
        recyclerView = findViewById(R.id.recycler_view);
        menuBtn = findViewById(R.id.menu_btn);

        addProjectBtn.setOnClickListener((v) -> startActivity(new Intent(MainActivity.this, ProjectDetailsActivity.class)));
        menuBtn.setOnClickListener((v)-> showMenu());
        setupRecyclerViewProject();

    }

    void showMenu(){
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, menuBtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle() == "Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    return true;
                }
                return false;
            }

        });
    }



    private String getDevicePhoneNumber() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
            return null;
        }
        return telephonyManager.getLine1Number();
    }






    void setupRecyclerViewProject(){
        Query query = Utility.getCollectionReferenceForProjects().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Project> options = new FirestoreRecyclerOptions.Builder<Project>()
                .setQuery(query, Project.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        projectAdapter = new ProjectAdapter(options,this);
        recyclerView.setAdapter(projectAdapter);
    }




    @Override
    protected void onStart() {
        super.onStart();
        projectAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        projectAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        projectAdapter.notifyDataSetChanged();
    }
}