package com.example.testerapp8;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class EditProjectActivity extends AppCompatActivity {

    EditText editProjectTitle, editProjectDesc;
    ImageButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project);

        editProjectTitle = findViewById(R.id.edit_project_title);
        editProjectDesc = findViewById(R.id.edit_project_desc);
        saveButton = findViewById(R.id.save_btn);

        String currentTitle = getIntent().getStringExtra("currentTitle");
        String currentDesc = getIntent().getStringExtra("currentDesc");

        editProjectTitle.setText(currentTitle);
        editProjectDesc.setText(currentDesc);

        saveButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("newTitle", editProjectTitle.getText().toString());
            resultIntent.putExtra("newDesc", editProjectDesc.getText().toString());
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}

